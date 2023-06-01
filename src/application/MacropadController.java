package application;
	
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import application.communication.usb.SerialComManager;
import application.gui.menu.MacropadMenuBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;




public class MacropadController extends Application {
	
	static ObservableList<Key> data;
	static ProfileManager profileManager;
	
	static SerialComManager scm;
	
	static Stage primaryStage;
	
	@Override
	public void start(Stage _primaryStage) {
		
		primaryStage = _primaryStage;
		primaryStage.getIcons().add(new javafx.scene.image.Image("/application/enter_key.png"));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent> () {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
				System.out.println("Event consumed.");
				primaryStage.hide();
				
			}
		});
		
		try {
			BorderPane bl = new BorderPane();
			
			bl.setTop(new MacropadMenuBar(this));
			
			
			HBox root = new HBox();
			root.setSpacing(5.0);
			root.setPadding(new Insets(5, 5, 5, 5));
			bl.setCenter(root);
			
			
			
			
			Scene scene = new Scene(bl,680,420);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.setTitle("Macro Keypad");
			primaryStage.show();
			Platform.setImplicitExit(false);
			
			
			
			
			/***********************************************************************************/
			VBox leftPanel = new VBox();
			leftPanel.setSpacing(5.0);
			leftPanel.getChildren().add(new Label("Saved program profiles."));
			
			ListView<Profile> listView = new ListView<Profile> ();
			listView.setCellFactory(new ProfileCellFactory(profileManager));
			/*listView.setCellFactory(new Callback<ListView<Profile>>, ListCell<Profile> () {
				
			});*/
			listView.setItems(FXCollections.observableArrayList(profileManager.getProfiles()));
			//listView.setPadding(new Insets(10, 10, 10, 10));
			
			//root.setLeft(listView);
			leftPanel.getChildren().add(listView);
			root.getChildren().add(leftPanel);
			
			
			
			
			VBox rightPanel = new VBox();
			rightPanel.setSpacing(5);
			rightPanel.setPadding(new Insets(5, 5, 5, 5));
			HBox.setHgrow(rightPanel, Priority.ALWAYS);
			
			Label programNameLabel = new Label("Program name");
			programNameLabel.setStyle("-fx-font-size: 16pt; ");
			programNameLabel.setWrapText(true);
			HBox.setHgrow(programNameLabel, Priority.ALWAYS);
			
			Button newProfileButton = new Button("New profile");
			
			newProfileButton.setAlignment(Pos.BASELINE_RIGHT);
			
			Region r = new Region();
			HBox.setHgrow(r, Priority.ALWAYS);
			HBox programNameHbox = new HBox(programNameLabel, r, newProfileButton);
			
			rightPanel.getChildren().addAll(programNameHbox);
			
			TableView table = new TableView();
			table.setEditable(true);
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			TableColumn<Key, String> shortPressColumn = new TableColumn<Key, String>("Short Key Press");
			shortPressColumn.setCellValueFactory(new PropertyValueFactory<Key, String>("shortCmd"));
			shortPressColumn.setPrefWidth(table.getWidth()*0.4);
			shortPressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			shortPressColumn.setOnEditCommit(new EventHandler<CellEditEvent<Key, String>>() {
				@Override
				public void handle(CellEditEvent<Key, String> k) {
					((Key) k.getTableView().getItems().get(k.getTablePosition().getRow())).setShortCmd(k.getNewValue());
					profileManager.saveChanges(profileManager.getCurrentProfile());
					
				}
			});
			
			
			
			TableColumn<Key, String> longPressColumn = new TableColumn<Key, String>("Long Key Press");
			//longPressColumn.setCellValueFactory(new PropertyValueFactory<Key, String>("longCmd"));
			longPressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			longPressColumn.setCellValueFactory(new PropertyValueFactory<Key, String>("longCmd"));
			longPressColumn.setPrefWidth(table.getWidth()*0.4);
			longPressColumn.setOnEditCommit(new EventHandler<CellEditEvent<Key, String>>() {
				@Override
				public void handle(CellEditEvent<Key, String> k) {
					((Key) k.getTableView().getItems().get(k.getTablePosition().getRow())).setLongCmd(k.getNewValue());
					profileManager.saveChanges(profileManager.getCurrentProfile());
					
				}
			});
			
			
			table.getColumns().addAll(shortPressColumn, longPressColumn);
			table.setItems(profileManager.getCurrentKeys());
			
			
			/*KeyboardPane keyboardPane = new KeyboardPane(profileManager);
			//keyboardPane.setKeys(profileManager);
			//rightPanel.getChildren().add(table);*/
			rightPanel.getChildren().add(table);
			root.getChildren().add(rightPanel);

			
			/***********************************************************************************/
			
			
			//primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)   {
		
		profileManager = new ProfileManager();
		
		createSystemTray();

		launch(args);
	}
	
	
	
	public void connect(String portName) {
		scm = new SerialComManager(this);
		scm.setPortName(portName);
		
		scm.conectar();
		
	}
	
	
	
	public void disconnect () {
		scm.desconectar();
	}
	
	
	
	private static void createSystemTray() {
		final java.awt.TrayIcon trayIcon;
		
		if (java.awt.SystemTray.isSupported()) {
			java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
			
			
			java.awt.PopupMenu popup = new java.awt.PopupMenu();
			java.awt.MenuItem showMenuItem = new java.awt.MenuItem("Show");
			showMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Showing application window.");
					if (primaryStage != null) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								
								primaryStage.show();
								
							}
							
						});
					}
					else {
						System.out.println("Is null");
					}
					
				}
			});
			popup.add(showMenuItem);
			
			java.awt.MenuItem quitMenuItem = new java.awt.MenuItem("Quit");
			quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting application.");
					Platform.exit();
					System.exit(0);
					
				}
			});
			popup.add(quitMenuItem);
			
			
			
			
			//final URL resource = getClass().getResource("keyboard.png");
			
			
			
			//ImageIcon icon = new ImageIcon(img);
			//Image icon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/src/application.images/enter_key.png"));
			Image icon = Toolkit.getDefaultToolkit().getImage(MacropadController.class.getResource("enter_key.png"));
			
			
			trayIcon = new java.awt.TrayIcon(icon, "Macro Keypad", popup);
			trayIcon.setImageAutoSize(true);
			trayIcon.setToolTip("Macropad");
			
			
			try {
				tray.add(trayIcon);
				trayIcon.displayMessage("Macropad controller", "You can access app configuration in system tray", TrayIcon.MessageType.INFO);
			}
			catch (java.awt.AWTException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
}
