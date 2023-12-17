package application;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginPage extends StackPane {
	AccountManagement account = new AccountManagement();

	Stage stage;
	String mode = "login";
	String message = "";
	int userID = 0;

	StackPane loginLayout = new StackPane();
	GridPane loginGrid = new GridPane();

	Label heading = new Label("Login to Your Account");
	Button backHome = new Button("Home");

	Label nameLabel = new Label("Account name:");
	TextField nameField = new TextField();
	Label passwordLabel = new Label("Password:");
	PasswordField passwordField = new PasswordField();
	Button loginButton = new Button("Log In");

	// error page of login
	StackPane errorPane = new StackPane();
	Label outputLabel = new Label("");
	Button ok = new Button("OK");

	public LoginPage(Stage stage) {
		this.stage = stage;

		loginLayout.setMaxWidth(550);
		loginLayout.setMaxHeight(360);

		loginGrid.setMaxWidth(550);
		loginGrid.setMaxHeight(360);

		backHome.setFocusTraversable(false);
		nameField.setFocusTraversable(false);
		passwordField.setFocusTraversable(false);
		loginButton.setFocusTraversable(false);

		this.getChildren().addAll(loginGrid, errorPane);

		// set error pane
		this.errorPane.getChildren().addAll(outputLabel, ok);
		errorPane.setVisible(false);

		this.errorPane.setMaxWidth(250);
		this.errorPane.setMaxHeight(150);
		this.errorPane.setStyle("-fx-background-color: rgba(227, 225, 225, 0.7);");
		this.errorPane.setMargin(outputLabel, new javafx.geometry.Insets(8, 30, 60, 30));

		this.outputLabel.setWrapText(true);
		this.outputLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		this.outputLabel.setContentDisplay(ContentDisplay.CENTER);

		this.ok.setPrefSize(40, 25);
		this.ok.setStyle("-fx-font-weight: bold;");
		this.ok.setFont(new Font(12));
		this.ok.setTextFill(Color.web("#de693e"));
		this.ok.setTranslateX(0);
		this.ok.setTranslateY(30);

		// set log in page
		this.loginGrid.setPadding(new Insets(10, 30, 0, 50));
		ColumnConstraints col1 = new ColumnConstraints(135);
		ColumnConstraints col2 = new ColumnConstraints(300);
		this.loginGrid.getColumnConstraints().addAll(col1, col2);
		this.loginGrid.setVgap(8);

		this.heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.heading.setMaxWidth(550);
		this.heading.setAlignment(Pos.CENTER);

		this.nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		this.passwordLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

		this.nameField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		this.passwordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

		this.loginButton.setPrefSize(80, 20);
		this.loginButton.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
		this.loginButton.getStyleClass().add("button");
		this.loginButton.setTextFill(Color.web("#de693e"));

		this.backHome.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
		this.backHome.setTextFill(Color.web("#de693e"));

		this.loginGrid.add(backHome, 0, 0);
		GridPane.setColumnSpan(heading, 2);
		this.loginGrid.add(heading, 0, 1);
		this.loginGrid.add(nameLabel, 0, 7);
		this.loginGrid.add(nameField, 1, 7);
		this.loginGrid.add(passwordLabel, 0, 10);
		this.loginGrid.add(passwordField, 1, 10);
		GridPane.setColumnSpan(loginButton, 2);
		GridPane.setHalignment(loginButton, javafx.geometry.HPos.RIGHT);
		this.loginGrid.add(loginButton, 0, 15);

		// set actions
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 20) {
				nameField.setText(oldValue);
			}
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 20) {
				passwordField.setText(oldValue);
			}
		});

		loginButton.setOnAction(e -> {
			String userName = nameField.getText();
			String password = passwordField.getText();

			System.out.println("name: " + userName + ", password: " + password);

			if (userName.isEmpty() || password.isEmpty()) {
				errorPane.setVisible(true);
				loginGrid.setDisable(true);
				outputLabel.setText("Please fill in all fields!");
			} else {
				try {
					userID = account.getID(userName, password, mode);
					System.out.println("userID on login page: " + userID);
					message = account.loginVerification(userID);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (message.equals("successful login")) {
					try {
						account.createUserFolder(userID);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						encryptionDecryptionPage();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					errorPane.setVisible(true);
					loginGrid.setDisable(true);
					outputLabel.setText(message);
				}
			}
		});

		ok.setOnAction(e -> {
			errorPane.setVisible(false);
			loginGrid.setDisable(false);
		});

		this.backHome.setOnAction(e -> {
			homePage();
		});

	}

	private void encryptionDecryptionPage() throws IOException {
		stage.setTitle("Encryption<->Decryption");
		EncryptionDecryptionPage encryptionDecryptionPage = new EncryptionDecryptionPage(stage, userID);
		Scene scene = new Scene(encryptionDecryptionPage, 880, 640);
		stage.setScene(scene);
		stage.show();
	}

	private void homePage() {
		stage.setTitle("Home | Welcome");
		HomePage homePage = new HomePage(stage);
		Scene scene = new Scene(homePage, 550, 345);
		stage.setScene(scene);
		stage.show();
	}

}
