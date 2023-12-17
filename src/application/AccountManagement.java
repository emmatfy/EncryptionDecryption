package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AccountManagement {
	ClientManagement client = new ClientManagement();
	EncryDecryAlgorithm algorithm = new EncryDecryAlgorithm();

	public String registrationVerification(String userName, String password, String mode) throws Exception {
		String encryptedName = encryptedName(userName);
		String hashedPassword = hashedPassword(password);
		return client.registrationVerification(encryptedName, hashedPassword, mode);
	}

	public String loginVerification(int userID) throws Exception {
		if (userID != 0) {
			return "successful login";
		} else {
			return "Account name and password pair is incorrect!";
		}
	}

	private String hashedPassword(String password) {
		return algorithm.passwordHashWithMD5(password);
	}

	private String encryptedName(String userName) throws Exception {
		return algorithm.caesarEncryp(0, userName, null);
	}

	public int getID(String userName, String password, String mode) throws Exception {
		String encryptedName = encryptedName(userName);
		String hashedPassword = hashedPassword(password);
		return client.getID(encryptedName, hashedPassword, mode);
	}

	public void createUserFolder(int userID) throws Exception {
		String ID = "" + userID;
		Path folderPath = Paths.get("users", ID);
		if (!Files.exists(folderPath)) {
			try {
				Files.createDirectories(folderPath);
				System.out.println("Folder created successfully");
			} catch (Exception ex) {
				System.out.println("Failed to create folder: " + ex.getMessage());
			}
		} else {
			System.out.println("Folder already exists");
		}
	}

	public boolean isDarkTheme(int userID) throws UnknownHostException, IOException {
		return client.isDarkTheme(userID);
	}

	public void updateThemeMode(int userID, boolean isSelected) throws UnknownHostException, IOException {
		System.out.println("boolean account: " + isSelected);
		client.updateThemeMode(userID, isSelected);
	}

	public String encryption(String plainText, String encryMode, String mode, int userID)
			throws UnknownHostException, IOException {
		String cipherText = "";
		String ID = "" + userID;

		if (encryMode.equalsIgnoreCase("DES") || encryMode.contains("AES")) {
			try {
				byte[] byteCipherText = algorithm.desAesEncryp(plainText, userID, encryMode);
				cipherText = Base64.getEncoder().encodeToString(byteCipherText);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (encryMode.equalsIgnoreCase("Ceasar")) {
			try {
				cipherText = algorithm.caesarEncryp(userID, plainText, encryMode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		client.saveCipherText(cipherText, ID, encryMode, mode);
		return cipherText;
	}

	public String decryption(String cipherText, String encryMode, int userID) throws UnknownHostException, IOException {
		String plainText = "";
		if (encryMode.equalsIgnoreCase("DES") || encryMode.contains("AES")) {
			byte[] byteCipherText = Base64.getDecoder().decode(cipherText);
			try {
				plainText = algorithm.desAesDecryp(byteCipherText, userID, encryMode);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (encryMode.equalsIgnoreCase("Ceasar")) {
			try {
				plainText = algorithm.caesarDecryp(userID, cipherText, encryMode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plainText;
	}

	public String uploadPlainText(Stage stage) {
		String plainText = "";

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Add extension filters
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		// Show the FileChooser dialog
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			try {
				Scanner sc = new Scanner(selectedFile);
				while (sc.hasNextLine()) {
					plainText += sc.nextLine();
				}
				System.out.println(plainText);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return plainText;
	}

	public void saveCipherText(String cipherText, int userID, Stage stage) {
		String ID = "" + userID;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");

		File defaultDirectory = new File("users/" + ID);
		fileChooser.setInitialDirectory(defaultDirectory);

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		// Show the FileChooser dialog
		File selectedFile = fileChooser.showSaveDialog(stage);

		if (selectedFile != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
				writer.write(cipherText);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<History> getHistoryListOfAUser(String mode, int userID)
			throws UnknownHostException, ClassNotFoundException, IOException {
		ArrayList<History> histories = client.getHistory(mode, userID);
		return histories;

	}

	public boolean deleteItem(int itemID) throws UnknownHostException, IOException {
		return client.deleteItem(itemID);
	}

	public boolean clearHistory(int userID) throws UnknownHostException, IOException {
		return client.clearHistory(userID);
	}
}
