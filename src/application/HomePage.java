package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomePage extends BorderPane {
	Stage stage;

	BorderPane root = new BorderPane();
	VBox buttonHolder = new VBox();
	Button registrationButton = new Button("CREATE A NEW ACCOUNT");
	Button loginButton = new Button("LOGIN TO YOUR ACCOUNT");
	Image logoImage = new Image("background.png");
	ImageView logo = new ImageView(logoImage);

	public HomePage(Stage stage) {
		this.stage = stage;

		registrationButton.setFocusTraversable(false);
		loginButton.setFocusTraversable(false);

		BorderPane.setAlignment(logo, javafx.geometry.Pos.CENTER);
		logo.setFitWidth(500);
		logo.setFitHeight(240);
		this.setTop(logo);
		this.setStyle("-fx-background-color: #ffffff");

		this.buttonHolder.setSpacing(6);

		this.registrationButton.setPrefSize(550, 40);
		this.registrationButton.setStyle("-fx-font-weight: bold;");
		this.registrationButton.setFont(new Font(20));
		this.registrationButton.setTextFill(Color.web("#de693e"));

		this.loginButton.setPrefSize(550, 40);
		this.loginButton.setStyle("-fx-font-weight: bold;");
		this.loginButton.setFont(new Font(20));
		this.loginButton.setTextFill(Color.web("#de693e"));

		this.buttonHolder.getChildren().addAll(registrationButton, loginButton);
		this.setCenter(buttonHolder);

		this.registrationButton.setOnAction(e -> {
			openRegistrationPage();
		});

		this.loginButton.setOnAction(e -> {
			openLoginPage();
		});
	}

	private void openRegistrationPage() {
		stage.setTitle("Registration");
		RegistrationPage registrationPage = new RegistrationPage(stage);
		Scene scene = new Scene(registrationPage, 550, 435);
		stage.setScene(scene);
		stage.show();
	}

	private void openLoginPage() {
		stage.setTitle("Login");
		LoginPage lgoinPage = new LoginPage(stage);
		Scene scene = new Scene(lgoinPage, 550, 360);
		stage.setScene(scene);
		stage.show();
	}

}