package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryDecryAlgorithm {
	private char[] codes = new char[94];
	int userID = 0;
	String filePath = "";
	String tempFilePath = "users/tempkey.txt";
	private SecretKey secretkey = null;

	// get Caesar key
	public int getCaesarKey(int userID, String encryMode) throws Exception {
		int key = 0;
		String filePath = "";
		boolean isExist = false;

		if (userID == 0) {
			filePath = "users/tempkey.txt";
			key = generateCaesarKey();
			System.out.println("tempKey:" + key);
		} else {
			String ID = "" + userID;
			String type = encryMode.toLowerCase();
			String filename = type + "key.txt";
			filePath = "users/" + ID + "/" + filename;
			if (keyFileExist(userID, filename)) {
				// load and decrypt the key
				String stringkey = loadEncryptedKey(filePath);
				key = Integer.parseInt(stringkey);
				isExist = true;
			} else {
				key = generateCaesarKey();
			}
		}
		if (!isExist) {
			// encrypt and save the key
			String caesarKey = "" + key;
			saveEncryptedKey(caesarKey, filePath);
		}
		return key;
	}

	// generate random Caesar key
	private int generateCaesarKey() {
		Random random = new Random();
		int caesarKey = random.nextInt(94) + 1;
		return caesarKey;
	}

	// encryption using Caesar cipher encryption
	public String caesarEncryp(int userID, String plainText, String encryMode) throws Exception {
		String cipherText = "";
		char cipherCharacter = ' ';
		codes = caesarCodes();

		int key = getCaesarKey(userID, encryMode);

		for (int n = 0; n < plainText.length(); n++) {
			char plainCharacter = plainText.charAt(n);
			boolean existCode = false;
			for (char code : codes) {
				if (code == plainCharacter) {
					existCode = true;
					break;
				}
			}
			if (!existCode) {
				cipherCharacter = '|';
			} else {
				int position = indexOf(codes, plainCharacter);
				int newPosition = (position + key) % codes.length;
				cipherCharacter = codes[newPosition];
			}
			cipherText += cipherCharacter;
		}
		System.out.println("caesarCipherText: " + cipherText);
		return cipherText;
	}

	// decryption using Caesar cipher encryption
	public String caesarDecryp(int userID, String cipherText, String encryMode) throws Exception {
		String plainText = "";
		codes = caesarCodes();
		int key = 0;

		if (userID == 0) {
			String stringkey = loadEncryptedKey(tempFilePath);
			key = Integer.parseInt(stringkey);
			System.out.println("verificationKey: " + key);
		} else {
			key = getCaesarKey(userID, encryMode);
		}

		for (int i = 0; i < cipherText.length(); i++) {
			char cipherCharacter = cipherText.charAt(i);
			int position = indexOf(codes, cipherCharacter);
			int newPosition = 0;
			char plainCharacter = ' ';

			if (cipherCharacter == '|') {
				plainCharacter = '|';
			} else {
				if ((position - key) < 0) {
					newPosition = (codes.length + position - key) % codes.length;
				} else {
					newPosition = (position - key) % codes.length;
				}
				plainCharacter = codes[newPosition];
			}
			plainText += plainCharacter;
		}
		return plainText;
	}

	// get DES and AES key
	private SecretKey getDesAesKey(int userID, String encryMode) throws Exception {
		String type = encryMode.toLowerCase();
		String ID = "" + userID;
		String fileName = type + "key.txt";
		filePath = "users/" + ID + "/" + fileName;
		KeyGenerator keyGen = null;

		if (!keyFileExist(userID, fileName)) {
			if (encryMode.contains("AES")) {
				keyGen = KeyGenerator.getInstance("AES");
				String[] parts = encryMode.split("-");
				int keySize = Integer.parseInt(parts[1]);
				System.out.println("key size:" + keySize);
				keyGen.init(keySize);
			} else {
				keyGen = KeyGenerator.getInstance(encryMode);
			}
			secretkey = keyGen.generateKey();
		} else {
			// load encrypted key and decrypt it
			String keyString = loadEncryptedKey(filePath);
			secretkey = convertKeyStringToSecretKey(keyString, encryMode);
		}
		return secretkey;
	}

	// DES and AES encryption
	public byte[] desAesEncryp(String strDataToEncrypt, int userID, String encryMode) throws Exception {
		Cipher desCipher = null;
		if (encryMode.contains("AES")) {
			desCipher = Cipher.getInstance("AES");
			System.out.println("This is AES mode");
		} else {
			desCipher = Cipher.getInstance(encryMode);
		}
		secretkey = getDesAesKey(userID, encryMode);
		desCipher.init(Cipher.ENCRYPT_MODE, secretkey);
		byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
		byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);

		// encrypt and save the key
		byte[] keyBytes = secretkey.getEncoded();
		String key = Base64.getEncoder().encodeToString(keyBytes);
		saveEncryptedKey(key, filePath);

		System.out.println("AES CipherText.");
		return byteCipherText;
	}

	// DES and AES decryption
	public String desAesDecryp(byte[] strCipherText, int userID, String encryMode) throws Exception {
		Cipher desCipher = null;
		if (encryMode.contains("AES")) {
			desCipher = Cipher.getInstance("AES");
		} else {
			desCipher = Cipher.getInstance(encryMode);
		}
		desCipher.init(Cipher.DECRYPT_MODE, this.getDesAesKey(userID, encryMode));
		byte[] byteDecryptedText = desCipher.doFinal(strCipherText);
		return new String(byteDecryptedText);
	}

	// encrypt other key and save to a key file
	private void saveEncryptedKey(String key, String filePath) throws Exception {
		byte[] encryptedKey = encryptKey(key);
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			// Write the byte array to the file
			fos.write(encryptedKey);
			System.out.println("encrypted key saved to file successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load the key file and decrypt the key to string format
	private String loadEncryptedKey(String filePath) throws NumberFormatException, Exception {
		String key = "";

		try (FileInputStream fis = new FileInputStream(filePath)) {
			// Determine the size of the file
			long fileSize = fis.available();
			// Create a byte array to store the file content
			byte[] encryptedKey = new byte[(int) fileSize];
			// Read all bytes from the file into the byte array
			fis.read(encryptedKey);
			System.out.println("encrypted key loaded from file successfully.");

			key = decryptKey(encryptedKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

	// convert decrypted secret key in String format to SecretKey format
	public SecretKey convertKeyStringToSecretKey(String stringKey, String encryMode)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Decode the Base64 string to get the byte array
		byte[] keyBytes = Base64.getDecoder().decode(stringKey);
		if (encryMode.equalsIgnoreCase("DES")) {
			// Create a DES key specification
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			// Generate a DES key from the key specification
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			secretkey = keyFactory.generateSecret(desKeySpec);
			System.out.println("DES key generated.");
		} else {
			// Generate an AES key from the key bytes
			secretkey = new SecretKeySpec(keyBytes, "AES");
			System.out.println("AES key generated.");
		}
		return secretkey;
	}

	// generate and save master key when start the application
	public void saveMasterKeyFile(String fileName) throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		secretkey = keyGen.generateKey();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
		oos.writeObject(secretkey);
		oos.close();
		System.out.println("master key saved.");
	}

	// load master key from a file for decrytion
	public SecretKey loadMasterKeyFile(String fileName) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
		SecretKey secretKey = (SecretKey) ois.readObject();
		ois.close();
		System.out.println("master key loaded.");
		return secretKey;
	}

	// using DES as a master key for encrytion of other key
	public byte[] encryptKey(String strDataToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES");
		try {
			secretkey = loadMasterKeyFile("users/masterkey.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		desCipher.init(Cipher.ENCRYPT_MODE, secretkey);
		byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
		byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);
		System.out.println("key is encryped");
		return byteCipherText;
	}

	// using DES as a master key for derytion of other key
	public String decryptKey(byte[] strCipherText) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher desCipher = Cipher.getInstance("DES");
		try {
			secretkey = loadMasterKeyFile("users/masterkey.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		desCipher.init(Cipher.DECRYPT_MODE, secretkey);
		byte[] byteDecryptedText = desCipher.doFinal(strCipherText);
		System.out.println("decryped key string: " + new String(byteDecryptedText));
		return new String(byteDecryptedText);
	}

	// check if a key file exists
	private boolean keyFileExist(int userID, String fileName) {
		String ID = "" + userID;
		File fileToCheck = new File("users/" + ID, fileName);

		if (fileToCheck.exists()) {
			System.out.println("key file exists in the folder.");
			return true;
		} else {
			System.out.println("key file does not exist in the folder.");
			return false;
		}
	}

	// codes for Caesar cipher encryption
	public char[] caesarCodes() {
		try {
			int count = 0;
			Scanner sc = new Scanner(new File("TASCII.txt"));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split("	");

				if (parts.length == 6) {
					codes[count] = parts[1].charAt(0);
					codes[32 + count] = parts[3].charAt(0);
					codes[64 + count] = parts[5].charAt(0);
					count++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return codes;
	}

	// get index of a char for Caesar cipher encryption
	private int indexOf(char[] array, int targetValue) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == targetValue) {
				return i;
			}
		}
		return -1;
	}

	// HashMD5
	public String passwordHashWithMD5(String password) {
		String passwordToHash = password;
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// The bytes[] has bytes in decimal format. Convert it to hexadecimal format
			// StringBuilder is used to efficiently build and concatenate strings
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
}
