package application;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EncryptionDecryptionPage extends StackPane {
	AccountManagement account = new AccountManagement();

	Stage stage;
	String mode = "encryption";
	String encryMode = "";
	int userID = 0;
	boolean themeMode;
	ObservableList<History> history = null;

	String backgroundColor = "-fx-background-color:null;";
	String boldFont = "-fx-font-weight: bold;";
	String whiteBorderColor = "-fx-border-color: white;";
	String greyBorderColor = "-fx-border-color: grey;";
	String borderRadius = "-fx-border-radius: 5;";
	String whiteTextColor = "-fx-text-fill: white;";
	String blackTextColor = "-fx-text-fill: black;";
	String fontSizeLabel = "-fx-font-size: 13px;";
	String titleFontSize = "-fx-font-size: 20px;";

	StackPane root = new StackPane();

	BorderPane borderPane = new BorderPane();
	GridPane gridPane = new GridPane();

	Button backHome = new Button("Home");
	ToggleButton theme = new ToggleButton("Light theme");//

	Label titleLabel = new Label("Encryption & Decryption");

	Label plainTextLabel = new Label("Plain text:");
	TextArea plainTextArea = new TextArea();
	Button clearPlainText = new Button("Clear text");
	Button uploadText = new Button("Upload text");
	ComboBox<String> encryModeBox = new ComboBox<String>();
	HBox buttonolderPlainSide = new HBox(16);

	Label encryptedTextLabel = new Label("Encrypted text:");
	TextArea cipherTextArea = new TextArea();
	Button clearCipherText = new Button("Clear text");
	Button saveCipherText = new Button("Save text");
	HBox buttonHolderEncryptedSide = new HBox(16);

	Label alert = new Label("");

	Button encrypt = new Button("Encrypt >>");
	Button decrypt = new Button("<< Decrypt");
	VBox buttonHolderMiddle = new VBox(20);

	Label historyLabel = new Label("Encryption history:");
	Button clearHistory = new Button("Clear history");
	TableView<History> historyList = new TableView<>();

	public EncryptionDecryptionPage(Stage stage, int userID) throws IOException {
		this.stage = stage;
		this.userID = userID;

		themeMode = account.isDarkTheme(userID);
		if (themeMode) {
			setDarkMode();
		} else {
			setLightMode();
		}

//		clearHistory.setOnMousePressed(event -> clearHistory.setStyle("-fx-background-color: #ff0000;")); // Red
//		clearHistory.setOnMouseReleased(event -> clearHistory.setStyle("-fx-background-color: #00ff00;")); // Green

		cipherTextArea.setEditable(false);

		backHome.setFocusTraversable(false);
		theme.setFocusTraversable(false);
		plainTextArea.setFocusTraversable(false);
		cipherTextArea.setFocusTraversable(false);
		clearPlainText.setFocusTraversable(false);
		uploadText.setFocusTraversable(false);
		encryModeBox.setFocusTraversable(false);
		clearCipherText.setFocusTraversable(false);
		saveCipherText.setFocusTraversable(false);
		encrypt.setFocusTraversable(false);
		decrypt.setFocusTraversable(false);
		clearHistory.setFocusTraversable(false);
		historyList.setFocusTraversable(false);

		this.titleLabel.setMaxWidth(880);
		this.titleLabel.setAlignment(Pos.CENTER);

		plainTextArea.setWrapText(true); // Enable text wrapping
		plainTextArea.setPrefRowCount(9); // Set the preferred number of rows
		cipherTextArea.setWrapText(true);
		cipherTextArea.setPrefRowCount(9);

		encryModeBox.setValue("AES-256");
		encryModeBox.getItems().addAll("Ceasar", "DES", "AES-128", "AES-192", "AES-256");

		this.gridPane.setPadding(new Insets(10, 20, 0, 20));
		ColumnConstraints col1 = new ColumnConstraints(360);
		ColumnConstraints col2 = new ColumnConstraints(100);
		ColumnConstraints col3 = new ColumnConstraints(360);
		this.gridPane.getColumnConstraints().addAll(col1, col2, col3);
		this.gridPane.setVgap(6);
		gridPane.setAlignment(Pos.CENTER);

		this.gridPane.add(backHome, 0, 0);
		GridPane.setHalignment(theme, javafx.geometry.HPos.RIGHT);
		this.gridPane.add(theme, 2, 0);//
		GridPane.setColumnSpan(titleLabel, 3);
		this.gridPane.add(titleLabel, 0, 1);
		this.gridPane.add(plainTextLabel, 0, 3);
		this.gridPane.add(encryptedTextLabel, 2, 3);
		this.gridPane.add(plainTextArea, 0, 4);
		this.gridPane.add(cipherTextArea, 2, 4);
		this.gridPane.add(buttonHolderMiddle, 1, 4);
		this.gridPane.add(buttonolderPlainSide, 0, 5);
		this.gridPane.add(buttonHolderEncryptedSide, 2, 5);
		GridPane.setColumnSpan(alert, 3);
		GridPane.setHalignment(alert, javafx.geometry.HPos.CENTER);
		this.gridPane.add(alert, 0, 6);
		this.gridPane.add(historyLabel, 0, 7);
		GridPane.setHalignment(clearHistory, javafx.geometry.HPos.RIGHT);
		this.gridPane.add(clearHistory, 2, 7);

		borderPane.setTop(gridPane);
		borderPane.setCenter(historyList);
		buttonolderPlainSide.getChildren().addAll(uploadText, encryModeBox, clearPlainText);
		buttonHolderEncryptedSide.getChildren().addAll(saveCipherText, clearCipherText);
		buttonHolderMiddle.getChildren().addAll(encrypt, decrypt);

		buttonolderPlainSide.setAlignment(Pos.CENTER_RIGHT);
		buttonHolderEncryptedSide.setAlignment(Pos.CENTER_RIGHT);
		buttonHolderMiddle.setAlignment(Pos.CENTER);

		this.getChildren().add(borderPane);

		// set table view
		TableColumn<History, Integer> numberCol = new TableColumn<>("No.");
		numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

		TableColumn<History, String> timeCol = new TableColumn<>("Time");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

		TableColumn<History, String> cipherTextCol = new TableColumn<>("Cipher Text");
		cipherTextCol.setCellValueFactory(new PropertyValueFactory<>("cipherText"));

		TableColumn<History, String> encryModeCol = new TableColumn<>("Encry Mode");
		encryModeCol.setCellValueFactory(new PropertyValueFactory<>("encryMode"));

		historyList.getColumns().addAll(numberCol, timeCol, cipherTextCol, encryModeCol);

		try {
			history = FXCollections.observableArrayList(account.getHistoryListOfAUser("historyList", userID));
			System.out.println("1");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		if (history != null) {
			historyList.setItems(history);
		}

		// set width of each column
		numberCol.prefWidthProperty().bind(historyList.widthProperty().multiply(0.05));
		timeCol.prefWidthProperty().bind(historyList.widthProperty().multiply(0.21));
		cipherTextCol.prefWidthProperty().bind(historyList.widthProperty().multiply(0.63));
		encryModeCol.prefWidthProperty().bind(historyList.widthProperty().multiply(0.11));

		// center text of each cell
		numberCol.setStyle("-fx-alignment: CENTER;");
		timeCol.setStyle("-fx-alignment: CENTER;");
		encryModeCol.setStyle("-fx-alignment: CENTER;");

		BorderPane.setMargin(historyList, new javafx.geometry.Insets(10, 30, 20, 30));

		// set actions
		this.backHome.setOnAction(e -> {
			homePage();
		});

		this.theme.setOnAction(event -> {
			alert.setText("");
			boolean isSelected = false;
			if (theme.isSelected()) {
				setDarkMode();
				isSelected = true;
			} else {
				setLightMode();
			}
			try {
				System.out.println("boolean UI: " + isSelected);
				account.updateThemeMode(userID, isSelected);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		encrypt.setDisable(true);
		clearPlainText.setDisable(true);
		// listener to the text field
		this.plainTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
			// checks whether the new text content of the TextField is empty after removing
			// leading and trailing whitespaces. If it is empty, the expression evaluates to
			// true, and if it's not empty, the expression evaluates to false.
			encrypt.setDisable(newValue.trim().isEmpty());
			clearPlainText.setDisable(newValue.trim().isEmpty());
			plainTextArea.setStyle("-fx-text-fill: black;");
			alert.setText("");
		});

		decrypt.setDisable(true);
		saveCipherText.setDisable(true);
		clearCipherText.setDisable(true);
		this.cipherTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
			decrypt.setDisable(newValue.trim().isEmpty());
			saveCipherText.setDisable(newValue.trim().isEmpty());
			clearCipherText.setDisable(newValue.trim().isEmpty());
			alert.setText("");
		});

		this.encrypt.setOnAction(e -> {
			alert.setText("");
			encryMode = (String) encryModeBox.getValue();
			String plainText = plainTextArea.getText();
			String cipherText = "";
			try {
				cipherText = account.encryption(plainText, encryMode, mode, userID);
				refreshTableView();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			plainTextArea.setStyle("-fx-text-fill: black;");
			cipherTextArea.setText(cipherText);
		});

		this.decrypt.setOnAction(e -> {
			String selectedEncryMode = (String) encryModeBox.getValue();
			String cipherText = cipherTextArea.getText();
			String plainText = "";

			if (!selectedEncryMode.equals(encryMode)) {
				alert.setText("You can decrypt this text by " + encryMode + " only!");
			} else {
				alert.setText("");
				try {
					plainText = account.decryption(cipherText, encryMode, userID);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				plainTextArea.setText(plainText);
				plainTextArea.setStyle("-fx-text-fill: blue;");
			}
		});

		this.borderPane.setOnMouseClicked(event -> {
			// Make the label invisible when any part of the BorderPane is clicked
			alert.setText("");
		});

		this.uploadText.setOnAction(e -> {
			alert.setText("");
			String plainText = account.uploadPlainText(this.stage);
			if (!plainText.isEmpty()) {
				plainTextArea.setText(plainText);
				plainTextArea.setStyle("-fx-text-fill: black;");
			}
		});

		this.saveCipherText.setOnAction(e -> {
			alert.setText("");
			String cipherText = cipherTextArea.getText();
			account.saveCipherText(cipherText, userID, stage);

		});

		this.clearPlainText.setOnAction(e -> {
			alert.setText("");
			plainTextArea.setStyle("-fx-text-fill: black;");
			plainTextArea.clear();
		});

		this.clearCipherText.setOnAction(e -> {
			alert.setText("");
			cipherTextArea.clear();
			plainTextArea.setStyle("-fx-text-fill: black;");
		});

		this.clearHistory.setOnAction(e -> {
			alert.setText("");
			try {
				if (account.clearHistory(userID)) {
					refreshTableView();
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		PauseTransition pause = new PauseTransition(Duration.millis(300));
		this.historyList.setOnMouseClicked(event -> {
			alert.setText("");
			if (event.getClickCount() == 1) {
				pause.setOnFinished(null);
				pause.setOnFinished(e -> {
					History selectedHistory = historyList.getSelectionModel().getSelectedItem();
					if (selectedHistory != null) {
						cipherTextArea.setText(selectedHistory.getCipherText());
						encryModeBox.setValue(selectedHistory.getEncryMode());
						encryMode = (String) encryModeBox.getValue();
						plainTextArea.clear();
					}
				});
				pause.play();
			} else if (event.getClickCount() == 2) {
				// Handle the double-click event here
				History selectedHistory = historyList.getSelectionModel().getSelectedItem();
				if (selectedHistory != null) {
					int itemID = selectedHistory.getItemID();
					try {
						if (account.deleteItem(itemID)) {
							refreshTableView();
						}
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void refreshTableView() throws UnknownHostException, ClassNotFoundException, IOException {
		history = FXCollections.observableArrayList(account.getHistoryListOfAUser("historyList", userID));
		historyList.setItems(history);
		// Refresh the TableView to reflect the changes
//		Platform.runLater(() -> historyList.refresh());
		historyList.refresh();
	}

	private void setDarkMode() {
		String darkThemeButton = backgroundColor + boldFont + whiteBorderColor + borderRadius + whiteTextColor;
		String darkThemeLabel = backgroundColor + boldFont + whiteTextColor;

		theme.setText("Light mode");
		borderPane.setStyle("-fx-background-color: black;");
		backHome.setStyle(
				"-fx-text-fill: #de693e;-fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color:null;-fx-border-color: white;-fx-border-radius: 5;");
		theme.setStyle(
				"-fx-text-fill: #de693e;-fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color:null;-fx-border-color: white;-fx-border-radius: 5;");

		clearPlainText.setStyle(darkThemeButton);
		uploadText.setStyle(darkThemeButton);

		encryModeBox.setStyle(darkThemeButton);
		setDropdownBackground(encryModeBox, "black");

		clearCipherText.setStyle(darkThemeButton);
		saveCipherText.setStyle(darkThemeButton);
		encrypt.setStyle(darkThemeButton);
		decrypt.setStyle(darkThemeButton);
		clearHistory.setStyle(darkThemeButton);

		titleLabel.setStyle(darkThemeLabel + titleFontSize);
		alert.setStyle(backgroundColor + boldFont + whiteTextColor + "-fx-font-size: 15px");
		plainTextLabel.setStyle(darkThemeLabel + fontSizeLabel);
		encryptedTextLabel.setStyle(darkThemeLabel + fontSizeLabel);
		historyLabel.setStyle(darkThemeLabel + fontSizeLabel);
	}

	private void setLightMode() {
		String lightThemeButton = backgroundColor + boldFont + greyBorderColor + borderRadius + blackTextColor;
		String lightThemeLabel = backgroundColor + boldFont + blackTextColor;

		theme.setText("Dark mode");
		borderPane.setStyle("-fx-background-color: white;");
		backHome.setStyle(
				"-fx-text-fill: #de693e;-fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color:null;-fx-border-color: grey;-fx-border-radius: 5;");
		theme.setStyle(
				"-fx-text-fill: #de693e;-fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color:null;-fx-border-color: grey;-fx-border-radius: 5;");

		clearPlainText.setStyle(lightThemeButton);
		uploadText.setStyle(lightThemeButton);

		encryModeBox.setStyle(lightThemeButton);
		setDropdownBackground(encryModeBox, "white");

		clearCipherText.setStyle(lightThemeButton);
		saveCipherText.setStyle(lightThemeButton);
		encrypt.setStyle(lightThemeButton);
		decrypt.setStyle(lightThemeButton);
		clearHistory.setStyle(lightThemeButton);

		titleLabel.setStyle(lightThemeLabel + titleFontSize);
		alert.setStyle(backgroundColor + boldFont + "-fx-font-size: 15px");
		alert.setTextFill(Color.web("#f70707"));

		plainTextLabel.setStyle(lightThemeLabel + fontSizeLabel);
		encryptedTextLabel.setStyle(lightThemeLabel + fontSizeLabel);
		historyLabel.setStyle(lightThemeLabel + fontSizeLabel);
	}

	private void setDropdownBackground(ComboBox<String> comboBox, String backgroundColor) {
		ComboBoxListViewSkin<String> comboBoxListViewSkin = new ComboBoxListViewSkin<>(comboBox);
		@SuppressWarnings("unchecked")
		ListView<String> listView = (ListView<String>) comboBoxListViewSkin.getPopupContent();
		// Set the background color of the ListView (dropdown area)
		listView.setStyle("-fx-control-inner-background: " + backgroundColor + ";");
		// Apply the modified skin to the ComboBox
		comboBox.setSkin(comboBoxListViewSkin);
	}

	private void homePage() {
		stage.setTitle("Home | Welcome");
		HomePage homePage = new HomePage(stage);
		Scene scene = new Scene(homePage, 550, 345);
		stage.setScene(scene);
		stage.show();
	}

}
