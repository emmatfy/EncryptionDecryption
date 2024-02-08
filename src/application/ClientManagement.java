package application;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class ClientManagement {
	String serverAddress = "127.0.0.1";
	int port = 9090;

	public String registrationVerification(String encryptedName, String hashedPassword, String mode)
			throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);

		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println(mode);
			output.println(encryptedName);
			output.println(hashedPassword);

			// accept the response of server
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String message = input.readLine();
			return message;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public int getID(String encryptedName, String hashedPassword, String mode)
			throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);

		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println(mode);
			output.println(encryptedName);
			output.println(hashedPassword);

			// accept the response of server
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String ID = input.readLine();
			int userID = Integer.parseInt(ID);
			return userID;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public boolean isDarkTheme(int userID) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);
		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println("isDarkeTheme");
			output.println(userID);

			// accept the response of server
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			boolean receivedBoolean = Boolean.parseBoolean(input.readLine());
			return receivedBoolean;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public void updateThemeMode(int userID, boolean isSelected) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);
		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out.println("boolean client: " + isSelected);
			output.println("updateTheme");
			output.println(userID);
			output.println(String.valueOf(isSelected));
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public void saveCipherText(String cipherText, String ID, String encryMode, String mode)
			throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);

		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println(mode);
			output.println(cipherText);
			output.println(new Date().toString());
			output.println(ID);
			output.println(encryMode);
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public ArrayList<History> getHistory(String mode, int userID)
			throws UnknownHostException, IOException, ClassNotFoundException {
		Socket clientSocket = new Socket(serverAddress, port);
		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println(mode);
			output.println(userID);

			DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
			int size = dis.readInt();
			ArrayList<History> histories = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				int number = dis.readInt();
				String time = dis.readUTF();
				String cipherText = dis.readUTF();
				String encryMode = dis.readUTF();
				int itemID = dis.readInt();

				histories.add(new History(number, time, cipherText, encryMode, itemID));
			}
			return histories;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public boolean deleteItem(int itemID) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);
		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println("delete");
			output.println(itemID);

			// accept the response of server
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			boolean receivedBoolean = Boolean.parseBoolean(input.readLine());
			return receivedBoolean;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}

	public boolean clearHistory(int userID) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(serverAddress, port);
		try {
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
			output.println("clear");
			output.println(userID);

			// accept the response of server
			BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			boolean receivedBoolean = Boolean.parseBoolean(input.readLine());
			return receivedBoolean;
		} finally {
			clientSocket.close();
			System.out.println("Connection closed!!");
		}
	}
}
