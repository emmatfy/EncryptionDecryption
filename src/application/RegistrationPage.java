package application;

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

public class RegistrationPage extends StackPane {
	AccountManagement account = new AccountManagement();

	Stage stage;
	String mode = "registration";

	StackPane rootStackPane = new StackPane();
	GridPane createAccountGridPane = new GridPane();

	Label titleLabel = new Label("Register an account");

	Label nameLabel = new Label("Account name:");
	Label alertName = new Label("");
	TextField nameField = new TextField();
	Label nameRequirement = new Label("(maximum 20 characters, A-Z, a-z, 0-9, -)");

	Label alertPassword = new Label("");
	Label passwordLabel = new Label("Password:");
	PasswordField passwordField = new PasswordField();
	Label passwordRequirement = new Label("(6~20 characters, a-z, A-Z, 0-9, !@#$%^&*()-=_+)");
	Label confirmPasswordLabel = new Label("Confirm password:");
	PasswordField confirmPasswordField = new PasswordField();

	Button registerButton = new Button("Create");
	Label haveAccount = new Label("I have an account and I want to");
	Button loginButton = new Button("Login");

	// response page of registerButton
	StackPane alertPane = new StackPane();
	Label outputLabel = new Label("");
	Button ok = new Button("OK");
	Button loginAlert = new Button("Login");

	public RegistrationPage(Stage stage) {
		this.stage = stage;

		rootStackPane.setMaxWidth(550);
		rootStackPane.setMaxHeight(435);

		createAccountGridPane.setMaxWidth(550);
		createAccountGridPane.setMaxHeight(435);

		nameField.setFocusTraversable(false);
		passwordField.setFocusTraversable(false);
		confirmPasswordField.setFocusTraversable(false);
		registerButton.setFocusTraversable(false);
		loginButton.setFocusTraversable(false);

		// set response page (stackPane)
		alertPane.setVisible(false);

		this.alertPane.setMaxWidth(250);
		this.alertPane.setMaxHeight(150);
		this.alertPane.setStyle("-fx-background-color: rgba(227, 225, 225, 0.7);");
		this.alertPane.setMargin(outputLabel, new javafx.geometry.Insets(8, 15, 60, 15));

		this.outputLabel.setWrapText(true);
		this.outputLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		this.outputLabel.setContentDisplay(ContentDisplay.CENTER);

		this.ok.setPrefSize(40, 25);
		this.ok.setStyle("-fx-font-weight: bold;");
		this.ok.setFont(new Font(12));
		this.ok.setTextFill(Color.web("#de693e"));
		this.ok.setTranslateX(0);
		this.ok.setTranslateY(30);

		this.loginAlert.setPrefSize(60, 25);
		this.loginAlert.setStyle("-fx-font-weight: bold;");
		this.loginAlert.setFont(new Font(12));
		this.loginAlert.setTextFill(Color.web("#de693e"));
		this.loginAlert.setTranslateX(60);
		this.loginAlert.setTranslateY(30);
		loginAlert.setVisible(false);

		this.alertPane.getChildren().addAll(outputLabel, ok, loginAlert);

		// set gridPane
		this.createAccountGridPane.setPadding(new Insets(25, 30, 0, 50));
		ColumnConstraints col1 = new ColumnConstraints(135);
		ColumnConstraints col2 = new ColumnConstraints(300);
		this.createAccountGridPane.getColumnConstraints().addAll(col1, col2);
		this.createAccountGridPane.setVgap(6);

		this.titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.titleLabel.setMaxWidth(550);
		this.titleLabel.setAlignment(Pos.CENTER);

		this.nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		this.alertName.setTextFill(Color.web("#f70707"));
		this.passwordLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		this.alertPassword.setTextFill(Color.web("#f70707"));
		this.confirmPasswordLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		this.haveAccount.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		this.nameField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		this.passwordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		this.confirmPasswordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

		this.registerButton.setPrefSize(80, 20);
		this.registerButton.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
		this.registerButton.getStyleClass().add("button");
		this.registerButton.setTextFill(Color.web("#de693e"));

		this.loginButton.setPrefSize(80, 20);
		this.loginButton.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
		this.loginButton.setTextFill(Color.web("#de693e"));

		GridPane.setColumnSpan(titleLabel, 2);
		this.createAccountGridPane.add(titleLabel, 0, 0);
		this.createAccountGridPane.add(nameLabel, 0, 3);
		this.createAccountGridPane.add(alertName, 1, 2);
		this.createAccountGridPane.add(nameField, 1, 3);
		this.createAccountGridPane.add(nameRequirement, 1, 4);
		this.createAccountGridPane.add(alertPassword, 1, 5);
		this.createAccountGridPane.add(passwordLabel, 0, 6);
		this.createAccountGridPane.add(passwordField, 1, 6);
		this.createAccountGridPane.add(passwordRequirement, 1, 7);
		this.createAccountGridPane.add(confirmPasswordLabel, 0, 8);
		this.createAccountGridPane.add(confirmPasswordField, 1, 8);
		GridPane.setHalignment(registerButton, javafx.geometry.HPos.RIGHT);
		this.createAccountGridPane.add(registerButton, 1, 12);
		GridPane.setColumnSpan(haveAccount, 2);
		this.createAccountGridPane.add(haveAccount, 0, 17);
		GridPane.setHalignment(loginButton, javafx.geometry.HPos.RIGHT);
		this.createAccountGridPane.add(loginButton, 1, 17);

		this.getChildren().addAll(createAccountGridPane, alertPane);

		// set action
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			String message = "";
			boolean isValid = true;
			if (newValue.length() > 20) {
				message = "Account name should be less than 20 characters long.";
				nameField.setText(oldValue);
				isValid = false;
			} else if (!newValue.isEmpty() && newValue.length() <= 20 && !newValue.matches("[a-zA-Z0-9!-]*")) {
				message = "Password can contain a-z, A-Z, 0-9, - only";
				isValid = false;
			}
			if (!isValid) {
				alertName.setText(message);
				registerButton.setDisable(true);
			} else {
				registerButton.setDisable(false);
				alertName.setText("");
			}
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			String message = "";
			boolean isValid = true;
			if (newValue.length() > 20) {
				message = "Password should be less than 20 characters long.";
				passwordField.setText(oldValue);
				isValid = false;
			} else if (!newValue.isEmpty() && newValue.length() < 6) {
				message = "Password should be at least 8 characters long.";
				isValid = false;
			} else if (newValue.length() >= 6 && newValue.length() <= 20
					&& !newValue.matches("[a-zA-Z0-9!@#$%^&*()-=_+]*")) {
				message = "Password can contain a-z, A-Z, 0-9, !@#$%^&*()-=_+ only";
				isValid = false;
			}
			if (!isValid) {
				alertPassword.setText(message);
				registerButton.setDisable(true);
			} else {
				registerButton.setDisable(false);
				alertPassword.setText("");
			}
		});

		confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 20) {
				confirmPasswordField.setText(oldValue);
			}
		});

		registerButton.setOnAction(e -> {
			String userName = nameField.getText();
			String password = passwordField.getText();
			String confirmPassword = confirmPasswordField.getText();

			alertPane.setVisible(true);
			createAccountGridPane.setDisable(true);

			if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
				outputLabel.setText("Password does not match!");
			} else if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
				outputLabel.setText("Please fill in all fields!");
			} else {
				String message = "";
				try {
					message = account.registrationVerification(userName, password, mode);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				outputLabel.setText(message);

				if (message.equals("Create successful!")) {
					loginAlert.setVisible(true);
				}
			}
		});

		ok.setOnAction(e -> {
			alertPane.setVisible(false);
			passwordField.clear();
			confirmPasswordField.clear();
			createAccountGridPane.setDisable(false);
		});

		loginAlert.setOnAction(e -> {
			openLoginPage();
		});

		loginButton.setOnAction(e -> {
			openLoginPage();
		});
	}

	private void openLoginPage() {
		stage.setTitle("Login");
		LoginPage lgoinPage = new LoginPage(stage);
		Scene scene = new Scene(lgoinPage, 550, 360);
		stage.setScene(scene);
		stage.show();
	}

}
