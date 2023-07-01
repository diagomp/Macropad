package macropad;
	
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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import macropad.communication.usb.SerialComManager;
import macropad.gui.menu.MacropadMenuBar;
import macropad.gui.profile.KeyboardPane;
import macropad.gui.profile.ProfileManagerView;
import macropad.key.Key;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;




public class MacropadController extends Application {
	
	//static ObservableList<Key> data;
	static ProfileManager profileManager;
	static SerialComManager scm;
	static Stage primaryStage;
	
	
	private SystemController robot;
	
	public ProfileManagerView pmv;
	public KeyboardPane kp;
	
	@Override
	public void init () {
		profileManager = new ProfileManager(this);
		robot = new SystemController(profileManager);
		createSystemTray();
	}
	
	@Override
	public void start(Stage _primaryStage) {
		primaryStage = _primaryStage;
		primaryStage.getIcons().add(new javafx.scene.image.Image("/macropad/enter_key.png"));
		primaryStage.setTitle("Macropad controller");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent> () {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
				System.out.println("Event consumed.");
				primaryStage.hide();
			}
		});
		
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene (root, 720, 480);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.DECORATED);
		//primaryStage.show();
		Platform.setImplicitExit(false);
		
		
		
		
		root.setTop(new MacropadMenuBar(this));
		
		SplitPane splitPane = new SplitPane();
		splitPane.setDividerPositions(0.4);
		
		pmv = new ProfileManagerView(this, profileManager);
		kp = new KeyboardPane(profileManager);
		changeColor(profileManager.getProfiles().get(profileManager.getCurrentProfile()).getR(),
				profileManager.getProfiles().get(profileManager.getCurrentProfile()).getG(),
				profileManager.getProfiles().get(profileManager.getCurrentProfile()).getB());
		updateProfileView();
		//profileManager.setCurrentProfile(profileManager.getCurrentProfile());
		splitPane.getItems().addAll(pmv, kp);
		
		root.setCenter(splitPane);
		
		
		primaryStage.show();

	}
	
	@Override
	public void stop () {
		System.out.println("STOP");
		changeColor(0, 0, 0);
	}
	
	public static void main(String[] args)   {
		System.out.println("Application started");
		launch(args);
	}
	
	
	
	public boolean connect(String portName)  {
		scm = new SerialComManager(this);
		scm.setPortName(portName);
		profileManager.setCurrentProfile(profileManager.getCurrentProfile());
		return scm.connect();
		
	}
	
	public void disconnect () {
		if (scm != null) scm.disconnect();
	}
	
	public boolean isConnected() {
		return scm != null? scm.isConnected(): false;
	}
	
	
	public ProfileManager getProfileManager() {
		return profileManager;
	}
	
	public void changeColor (int r, int g, int b) {
		System.out.println("Changing color");
		//kp.changeBorder(r, g, b);
		if (scm != null) {
			scm.sendMessage(r + "," + g + "," + b + "\n");
		}
	}
	
	public void updateProfileView () {
		//updateSelection();
		pmv.update();
		pmv.updateSelection();
		kp.update();
		
	}
	
	public void updateSelection () {
		//
	}
	
	public void executeCommand (String cmd) {
		robot.executeCommand(cmd);
	}
	
	public void processSerialRequest(byte b) {
		
		if (b < 2*(Macropad.NUM_COLUMNS*Macropad.NUM_ROWS + 2) && b >= 0) {
			System.out.println("Byte sent: " + b);
			robot.executeCommand(profileManager.getCurrentKeys().get(b).getCmd());
		}
		
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
