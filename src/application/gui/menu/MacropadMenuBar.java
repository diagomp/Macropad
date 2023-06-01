package application.gui.menu;

import java.util.ArrayList;

import application.MacropadController;
import application.communication.usb.SerialComManager;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class MacropadMenuBar extends MenuBar {
	
	MacropadController app;			//Referencia a la clase principal
	
	private String selectedPortName;
	
	
	public MacropadMenuBar (MacropadController app) {
		super();
		this.app = app;
		
		Menu connectionMenu = new Menu("Connection");
		
		Menu portsMenu = new Menu ("Ports");
		portsMenu.getItems().addAll(getPortsMenuItems());
		
		
		MenuItem connectMenuItem = new MenuItem("Connect");
		connectMenuItem.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent e) {
				MenuItem mi = (MenuItem) e.getSource();
				if (mi.getText() == "Connect") {
					app.connect(selectedPortName);
					
					mi.setText("Disconnect");
					
				}
				else {
					app.disconnect();
					mi.setText("Connect");
				}
				
				
			}
		});
		
		connectionMenu.getItems().addAll(portsMenu, connectMenuItem);
		this.getMenus().addAll(connectionMenu);
		
		
		
	}

	
	private ArrayList<MenuItem> getPortsMenuItems () {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		String[] portNames = SerialComManager.getAvailablePorts();
		
		for (String s: portNames) {
			MenuItem newMenu = new MenuItem (s);
			newMenu.setOnAction(new EventHandler () {
				@Override
				public void handle(Event e) {
					System.out.println("Menu item");
					
					selectedPortName = s;
					
				}
			});
			
			menuItems.add(newMenu);
			
		}
		
		return menuItems;
	}
}
