package macropad.gui.profile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import macropad.ProfileManager;
import macropad.key.Key;

public abstract class ControlPane extends RadioButton {

	public static final String[] CONTROL_TYPE_NAMES = {"Switch", "Encoder"};
	public static final int SWITCH_CONTROL = 0;
	public static final int ENCODER_CONTROL = 1;
	
	protected int controlType;
	
	protected ProfileManager profileManager;
	
	
	protected byte keysId[];
	protected String[] triggers;
	
	public static ControlPane getControlPane (int type, ProfileManager profileManager) {
		ControlPane cp;
		
		switch (type) {
		case SWITCH_CONTROL:
			cp = new SwitchControl();
			break;
		case ENCODER_CONTROL:
			cp = new EncoderControl();
			break;
		default:
			cp = new SwitchControl();
		}
		cp.profileManager = profileManager;
		cp.setContextMenu(cp.getConfigUI());
		cp.getStyleClass().remove("radio-button");
		return (ControlPane) cp;
	}
	
	public int getType () {
		return controlType;
	}
	
	public GridPane getSettingsUI () {
		GridPane grid = new GridPane();
		grid.setHgap(5.0);
		grid.setVgap(5.0);
		grid.setPadding(new Insets(25, 0, 0, 0));
		
		for (int i = 0; i < triggers.length; i++) {
			grid.add(new Label(triggers[i]), 0, i);
			Key key = profileManager.getCurrentKeys().get(keysId[i]);
			
			TextField tf = new TextField(key.getCmd());
			tf.setOnAction(new EventHandler<ActionEvent> () {
				@Override
				public void handle(ActionEvent ae) {
					key.setCmd(tf.getText());
					profileManager.saveChanges(profileManager.getCurrentProfile());
					
				}
				
			});
			grid.add(tf, 1, i);
		}
		
		return grid;
	}
	
	public abstract ContextMenu getConfigUI ();
	
	public abstract void setIds (byte ...ids);
	
}
