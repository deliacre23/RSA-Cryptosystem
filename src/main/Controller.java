package main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    RSA rsa;

    public Controller(RSA rsa) {
        this.rsa = rsa;
    }

    @FXML
    private TextField encryptTextField;

    @FXML
    private TextField decryptTextField;

    @FXML
    private Label encryptLabel;

    @FXML
    private Label decryptLabel;

    @FXML
    private Label validationLabel;

    @FXML
    void encryptButton(Event event) {
        String plainText = this.encryptTextField.getText();
        if (plainText.matches("[a-zA-Z]+") || plainText.contains(" "))
        {
            validationLabel.setText("");
            plainText = plainText.toLowerCase();
            System.out.println(plainText);
            String encrypted = this.rsa.encryptText(plainText);
            this.encryptLabel.setText(encrypted);
            System.out.println(encrypted);
        }
        else
        {
            validationLabel.setText("Please give a valid text");
        }

    }

    @FXML
    void decryptButton(Event event) {
        String encryptText=this.decryptTextField.getText();
        if (encryptText.matches("[a-zA-Z]+") || encryptText.contains(" "))
        {
            validationLabel.setText("");
            encryptText=encryptText.toLowerCase();
            String decrypted=this.rsa.decryptText(encryptText);
            this.decryptLabel.setText(decrypted);
            System.out.println(decrypted);
        }
        else
        {
            validationLabel.setText("Please give a valid text");
        }

    }

}

