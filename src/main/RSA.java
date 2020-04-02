package main;

import java.math.BigInteger;
import java.util.Random;

import static java.lang.Character.getNumericValue;
import static java.lang.Math.pow;

public class RSA {
    int k;
    int l;
    BigInteger p;
    BigInteger q;
    BigInteger n;
    BigInteger phi;
    BigInteger e;
    BigInteger d;
    String[] alphabet = {" ","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};


    public RSA(int k, int l)
    {
        this.k = k;
        this.l = l;

        int length = 6;
        // create a random object
        Random random = new Random();
         boolean ok = false;
         while(!ok)
        {
            p=BigInteger.probablePrime(length,random);  //p gets the value of a random prime number that is less than 2^length
            System.out.println("p = " + p);
            q = BigInteger.probablePrime(length,random);    //q gets the value of a random prime number that is less than 2^length
            System.out.println("q = " + q);
            n = p.multiply(q.multiply(BigInteger.valueOf(2)));
            System.out.println("n = " + n);
            if(BigInteger.valueOf((long) pow(27,k)).compareTo(n) < 0 &&
                    BigInteger.valueOf((long) pow(27,l)).compareTo(n) > 0)
                ok=true;   // 27^k < n < 27^l
        }

        phi=p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1))); //ϕ(n) = (p-1)(q-1)
        System.out.println("phi = " + phi);

        e = BigInteger.probablePrime(length/2,random);  //e gets the value of a random prime number that is less than 2^length/2

        //Computes d = e^−1 mod ϕ(n)
        while (e.gcd(phi).compareTo(BigInteger.valueOf(1)) != 0 && e.compareTo(phi) < 0)
            //gcd returns a BigInteger whose value is the greatest common divisor of abs(this) and abs(that)
            //here we need gcd(phi,e) = 1
            //compareTo returns -1 if this < that, 0 if this = that and 1 if this > that; we need e < phi
        {
            e = e.add(BigInteger.valueOf(1));
        }
        System.out.println("e = " + e);
        d = e.modInverse(phi); // modInverse returns a BigInteger object whose value is ((this)^(-1) mod m)
        System.out.println("d = " + d);

    }

    String getLetter(int pos)
    {
        return alphabet[pos];
    }

    int getPosition(String letter)
    {
        int i=0;
        for(String s: alphabet)
        {
            if (s.equals(letter))
                return i;
            i++;
        }
        return 0;
    }

    static  long repeatedSquaringModularExponentiation( long base,  long exponent,  long n)
    {
        //computes base^exponent (modulo n) for large numbers
        long result=1;
        String bin = Long.toBinaryString(exponent);  //convert exponent to binary
        long seq[]=new long[100];
        char binary[] = new char[100];
        binary = bin.toCharArray(); //transforms a binary number in a sequence representing each digit of this same number
        // (!!) binary number is written as 2^k + 2^(k-1) + ... + 2^0 but we need it in reverse order
        seq[0]=base%n;

        for(int i=1;i<binary.length;i++)
        {
            seq[i]=(seq[i-1]*seq[i-1])%n;  //constructs a sequence of numbers of the form base^(2^k)
        }

        for(int i=0;i<binary.length;i++)
        {
            //we only take numbers of the form base^(2^k) from where the binary digit is 1
            if(getNumericValue((binary[binary.length-i-1]))==1) // (!!) solved by taking the number from end to beginning
            {
                result=(result*seq[i])%n;
            }
        }

        if(result==n-1) result = -1; //if the rest is n-1 we write the modulo result as -1


        return result;
    }

    String encryptText(String plainText)
    {
        //Plaintext message units are blocks of k letters
        // The plaintext is completed with blanks, when necessary.
        while (plainText.length()%k!=0)
            plainText = plainText+" ";
        String[] encryptedText = new String[plainText.length()/k*l]; //reserve space for the encrypted text
        int len = 0;
        //split the plaintext
        String[] splitPlainText = plainText.split("");
        for(int i = 0; i < splitPlainText.length-k +1; i++)
           {
               int numericalEquiv = 0;
               //compute the numerical equivalent by taking blocks of k letters
               for(int j = i; j < i + k; j++)
            {
                numericalEquiv = (int) (numericalEquiv + getPosition(splitPlainText[j]) * pow(27, i-j+k-1));
            }
               System.out.println("numerical equiv is " + numericalEquiv);
               //encrypt each block of k letters
               int encryptedNumericalEquiv = (int) repeatedSquaringModularExponentiation(numericalEquiv,e.longValue(),n.longValue());
                //find the literal equivalents (now l letters)
               for(int h = l-1; h >= 0; h--)
               {
                   encryptedText[len] = getLetter((int)(encryptedNumericalEquiv/(int)pow(27,h)));
                   len++;
                   encryptedNumericalEquiv = encryptedNumericalEquiv - (int) (encryptedNumericalEquiv/pow(27,h))*(int)pow(27,h);
               }
              i=i+l-2;
           }
        String encrypted = new String();
        //built the ciphertext
        for(int i = 0; i<encryptedText.length;i++)
            encrypted = encrypted + encryptedText[i];

        return encrypted;


    }

    String decryptText(String cypherText)
    {
        //Ciphertext message units are blocks of l letters.
        String[] decryptedText = new String[cypherText.length()/l*k]; ///
        int len = 0;
        //split the ciphertext
        String[] splitCypherText = cypherText.split("");
        for(int i = 0; i < splitCypherText.length-l +1; i++)
        {
            int numericalEquiv = 0;
            //compute the numerical equivalent
            for(int j = i; j < i + l; j++)
            {
                numericalEquiv = (int) (numericalEquiv + getPosition(splitCypherText[j]) * pow(27, i-j+l-1));
            }

            //decrypt
            int decryptedNumericalEquiv = (int) repeatedSquaringModularExponentiation(numericalEquiv,d.longValue(),n.longValue());
            if(decryptedNumericalEquiv > pow(27,k))
                decryptedNumericalEquiv = (int) (decryptedNumericalEquiv % pow(27,k));

            //find the literal equivalents
            for(int h = k-1; h >= 0; h--)
            {
                decryptedText[len] = getLetter((int)(decryptedNumericalEquiv/(int)pow(27,h)));
                len++;
                decryptedNumericalEquiv = decryptedNumericalEquiv - (int) (decryptedNumericalEquiv/pow(27,h))*(int)pow(27,h);
            }
            i=i+k;
        }
        String decrypted = new String();
        for(int i = 0; i<decryptedText.length;i++)
            decrypted = decrypted + decryptedText[i];

        return decrypted;


    }



}
