package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Server {

	static final String JDBC_URL = "jdbc:mysql://emma-db.ck1spccey0ds.ap-southeast-2.rds.amazonaws.com:3306/security";
	static final String USERNAME = "admin";
	static final String PASSWORD = "";

	static Connection connection;

	static EncryDecryAlgorithm algorithm = new EncryDecryAlgorithm();

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			System.out.println("Connected to database...");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error connecting to database...");
			e.printStackTrace();
		}

		int port = 9090;
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server started on 9090");

		try {
			while (true) {
				System.out.println("Waiting for client to connect!");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress());
				String message = "";

				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					System.out.println("A client request received at " + clientSocket);
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

					String mode = input.readLine();
					// registration verification
					if (mode.equalsIgnoreCase("registration")) {
						String encryptedName = input.readLine();
						String hashedPassword = input.readLine();
						message = registrationVerification(encryptedName, hashedPassword);
						out.println(message);
						out.close();
					}
					// login verification
					else if (mode.equalsIgnoreCase("login")) {
						String encryptedName = input.readLine();
						String hashedPassword = input.readLine();
						int userID = getId(encryptedName, hashedPassword);
						out.println(userID);
						out.close();
					}
					// save Cipher text
					else if (mode.equalsIgnoreCase("encryption")) {
						String cipherText = input.readLine();
						String time = input.readLine();
						String ID = input.readLine();
						String encryMode = input.readLine();
						saveCipherText(cipherText, time, ID, encryMode);
						out.close();
					}
					// get the encrypted history
					else if (mode.equalsIgnoreCase("historyList")) {
						String ID = input.readLine();
						int userID = Integer.parseInt(ID);
						ArrayList<History> histories = fetchDataFromMySQL(userID);
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
						dos.writeInt(histories.size());
						for (History history : histories) {
							dos.writeInt(history.getNumber());
							dos.writeUTF(history.getTime());
							dos.writeUTF(history.getCipherText());
							dos.writeUTF(history.getEncryMode());
							dos.writeInt(history.getItemID());
						}
						System.out.println("4");
						dos.close();
					}
					// delete an item
					else if (mode.equalsIgnoreCase("delete")) {
						String ID = input.readLine();
						int itemID = Integer.parseInt(ID);
						boolean deleted = deleteItem(itemID);
						out.println(deleted);
					}
					// clear history
					else if (mode.equalsIgnoreCase("clear")) {
						String ID = input.readLine();
						int userID = Integer.parseInt(ID);
						boolean cleared = clearHistory(userID);
						out.println(cleared);
					}
					// get theme mode
					else if (mode.equalsIgnoreCase("isDarkeTheme")) {
						String ID = input.readLine();
						int userID = Integer.parseInt(ID);
						boolean isDarkeTheme = isDarkTheme(userID);
						out.println(isDarkeTheme);
					}
					// update theme mode
					else if (mode.equalsIgnoreCase("updateTheme")) {
						String ID = input.readLine();
						int userID = Integer.parseInt(ID);
						boolean isSelected = Boolean.parseBoolean(input.readLine());
						updateThemeMode(userID, isSelected);
						out.close();
					}
				} finally {
					clientSocket.close();
					System.out.println("Connection closed!");
				}
			}
		} finally {
			serverSocket.close();
			System.out.println("program finished!");
		}
	}

	private static String registrationVerification(String encryptedName, String hashedPassword) throws Exception {
		try {
			// Register JDBC driver
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			// check if the account name has been used before
			String userName = algorithm.caesarDecryp(0, encryptedName, null);
			String sql = "SELECT * FROM users WHERE name = \"" + userName + "\"";
			System.out.println(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// create a new account
			if (!resultSet.next()) {
				String doubleHashedPassword = algorithm.passwordHashWithMD5(hashedPassword);
				sql = "INSERT INTO `users`(`name`, `password`,`dark_theme`) VALUES (\"" + userName + "\",\""
						+ doubleHashedPassword + "\",0)";
				statement.executeUpdate(sql);
				return "Create successful!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "This account name has been taken!";
	}

	private static int getId(String encryptedName, String hashedPassword) throws Exception {
		try {
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			String userName = algorithm.caesarDecryp(0, encryptedName, null);
			String doubleHashedPassword = algorithm.passwordHashWithMD5(hashedPassword);
			// check if input name and password are valid
			String sql = "SELECT * FROM users WHERE " + " name = \"" + userName + "\" AND password = \""
					+ doubleHashedPassword + "\"";
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				int ID = resultSet.getInt("id");
				return ID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isDarkTheme(int userID) {
		try {
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			String sql = "SELECT * FROM users WHERE " + " id = \"" + userID + "\" AND dark_theme = 1";
			ResultSet resultSet = statement.executeQuery(sql);
			System.out.println("SQL: " + sql);
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void updateThemeMode(int userID, boolean isSelected) {
		try {
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			int dark_theme = 0;
			if (isSelected) {
				dark_theme = 1;
			} else {
				dark_theme = 0;
			}
			System.out.println("dark_theme: " + dark_theme);
			String sql = "UPDATE `users` SET `dark_theme` = \"" + dark_theme + "\" WHERE `id` = \"" + userID + "\"";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void saveCipherText(String cipherText, String time, String ID, String encryMode) {
		String sql = "INSERT INTO `encryption_history`(`time`, `cipher_text`, `encry_mode`, `userID`) VALUES (?, ?, ?, ?)";
		int userID = Integer.parseInt(ID);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, time);
			preparedStatement.setString(2, cipherText);
			preparedStatement.setString(3, encryMode);
			preparedStatement.setInt(4, userID);

			preparedStatement.executeUpdate();
			System.out.println("Record inserted successfully.");
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception according to your application's error-handling strategy
		}
	}

	private static ArrayList<History> fetchDataFromMySQL(int userID) {
		ArrayList<History> histories = new ArrayList<>();
		try {
			// Register JDBC driver
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			String sql = "SELECT `no.`,`time`, `cipher_text`, `encry_mode` FROM encryption_history WHERE "
					+ " userID = \"" + userID + "\"";
			System.out.println(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// create a new account
			int number = 0;
			while (resultSet.next()) {
				String time = resultSet.getString("time");
				String cipherText = resultSet.getString("cipher_text");
				String encryMode = resultSet.getString("encry_mode");
				int itemID = resultSet.getInt("no.");
				number++;

				History history = new History(number, time, cipherText, encryMode, itemID);

				histories.add(0, history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return histories;
	}

	public static boolean deleteItem(int itemID) {
		try {
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM `encryption_history` WHERE `no.` = \"" + itemID + "\"";
			statement.executeUpdate(sql);

			sql = "SELECT `no.` FROM `encryption_history` WHERE `no.` = \"" + itemID + "\"";
			ResultSet resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean clearHistory(int userID) {
		try {
			System.out.println("creating statement...");
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM `encryption_history` WHERE `userID` = \"" + userID + "\"";
			// DELETE is a modification statement and doesn't return a result set
			int rowsAffected = statement.executeUpdate(sql);
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
