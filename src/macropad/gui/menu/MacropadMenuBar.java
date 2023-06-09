package macropad.gui.menu;

import java.util.ArrayList;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import macropad.MacropadController;
import macropad.communication.usb.SerialComManager;

public class MacropadMenuBar extends MenuBar {
	
	MacropadController app;			//Referencia a la clase principal
	
	private String selectedPortName;
	
	
	public MacropadMenuBar (MacropadController app) {
		super();
		this.app = app;
		
		
		
		Menu connectionMenu = new Menu("Connection");
		
		
		Menu portsMenu = new Menu ("Ports");
		//portsMenu.getItems().addAll(getPortsMenuItems());
		
		
		MenuItem connectMenuItem = new MenuItem("Connect");
		
		connectMenuItem.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent e) {
				MenuItem mi = (MenuItem) e.getSource();
				if (app.isConnected()) {
					app.disconnect();
					
				}
				else {
					app.connect(selectedPortName);
				}
				
				
			}
		});
		
		
		
		
		connectionMenu.setOnShowing(new EventHandler<Event> () {

			@Override
			public void handle(Event e) {
				//Update menu
				System.out.println("Showing menu");
				
				portsMenu.setText("Port: " + selectedPortName);
				portsMenu.getItems().clear();
				portsMenu.getItems().addAll(getPortsMenuItems());
				
				connectMenuItem.setText(app.isConnected()? "Disconnect": "Connect");
				
			}
			
		});
		
		
		connectionMenu.getItems().addAll(portsMenu, connectMenuItem);
		this.getMenus().addAll(connectionMenu);
		
		
		
	}

	
	private ArrayList<MenuItem> getPortsMenuItems () {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		String[] portNames = SerialComManager.getAvailablePorts();
		
		if (portNames.length > 0) {
		
			for (String s: portNames) {
				MenuItem newMenu = new MenuItem (s);
				newMenu.setOnAction(new EventHandler<ActionEvent> () {
					@Override
					public void handle(ActionEvent e) {
						System.out.println("Menu item");
						
						selectedPortName = s;
						
					}
	
				});
				
				menuItems.add(newMenu);
				
			}
		}
		else {
			MenuItem defaultMenu = new MenuItem ("No ports available.");
			defaultMenu.setDisable(true);
			menuItems.add(defaultMenu);
		}
		
		return menuItems;
	}
}
