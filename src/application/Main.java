package application;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		EncryDecryAlgorithm algorithm = new EncryDecryAlgorithm();

		primaryStage.setTitle("Home | Welcome");

		HomePage homePage = new HomePage(primaryStage);

		Scene scene = new Scene(homePage, 550, 345);

		primaryStage.setScene(scene);
		primaryStage.show();

		// create and save the master key to the file when start the application
		File fileToCheck = new File("users", "masterkey.txt");
		if (!fileToCheck.exists()) {
			String fileName = "users/masterkey.txt";
			algorithm.saveMasterKeyFile(fileName);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
