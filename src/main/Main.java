package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        RSA rsa = new RSA(2,3); //3,4
        String plainText = "algebra";
        String encrypted = rsa.encryptText(plainText);
        System.out.println("ENCRYPTED TEXT IS "+encrypted);
        String decrypted = rsa.decryptText(encrypted);
        System.out.println("DECRYPTED TEXT IS "+decrypted);

        Controller ctrl=new Controller(rsa);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setController(ctrl);
        Parent root = (Parent) loader.load();

        primaryStage.setTitle("Criptography lab");
        primaryStage.setScene(new Scene(root, 410, 250));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
