package macropad.gui.profile;

import javax.swing.GroupLayout.Alignment;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import macropad.Macropad;
import macropad.ProfileManager;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;

public class KeyboardPane extends AnchorPane {
	
	private ProfileManager profileManager;
	
	private GridPane gridPane;
	private ColorPicker colorPicker;
	private TextField profileNameTextField;
	private GridPane commandsGridPane;
	private VBox content;
	private ToggleGroup tg;

	public KeyboardPane(ProfileManager pm) {
		super();
		
		profileManager = pm;
		
		//this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		ScrollPane scrollPane = new ScrollPane();
		AnchorPane.setTopAnchor(scrollPane, 0.0);
		AnchorPane.setRightAnchor(scrollPane, 0.0);
		AnchorPane.setBottomAnchor(scrollPane, 0.0);
		AnchorPane.setLeftAnchor(scrollPane, 0.0);
		scrollPane.setFitToWidth(true);
		
		content = new VBox();
		//content.setPrefWidth(Double.POSITIVE_INFINITY);
		//content.widthProperty().bind();
		HBox.setHgrow(content, Priority.ALWAYS);
		//content.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
		content.setAlignment(Pos.TOP_CENTER);
		content.setPadding(new Insets(10));
		
		HBox header = new HBox();
		header.setPadding(new Insets(0, 0, 20, 0));
		header.setSpacing(5.0);
		
		profileNameTextField = new TextField("Profile name");
		HBox.setHgrow(profileNameTextField, Priority.ALWAYS);
		profileNameTextField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		profileNameTextField.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		profileNameTextField.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent ae) {
				profileManager.getProfile().rename(profileNameTextField.getText());
				profileManager.setCurrentProfile(profileManager.getCurrentProfile());
				
			}
		});
		//profileNameTextField.setPadding(new Insets(0, 0, 20, 0));
		//profileNameTextField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		header.getChildren().add(profileNameTextField);
		
		colorPicker = new ColorPicker();
		colorPicker.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent ae) {
				System.out.println(colorPicker.getValue().getRed());
				profileManager.getProfile().setColor(colorPicker.getValue());
				profileManager.setCurrentProfile(profileManager.getCurrentProfile());
				profileManager.saveChanges(profileManager.getCurrentProfile());
				
			}
		});
		header.getChildren().add(colorPicker);
		
		content.getChildren().add(header);
		
		gridPane = new GridPane();
		gridPane.setPrefWidth(290.0);
		gridPane.setMaxWidth(290.0);
		gridPane.setMinWidth(290.0);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(10), new Insets(0))));
		gridPane.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(6), new Insets(-6))));
		
		
		tg = new ToggleGroup();
		int enc = 0;
		//Macropad macropad = new Macropad(Macropad.SETTINGS_PATH + "/settings.xml");
		for (int j = 0; j < Macropad.NUM_ROWS; j++) {
			for (int i = 0; i < Macropad.NUM_COLUMNS; i++) {
				int switchNumber = j*Macropad.NUM_COLUMNS + i;
				ControlPane tf;
				if (switchNumber == 0 || switchNumber == 3) {
					tf = ControlPane.getControlPane(ControlPane.ENCODER_CONTROL, profileManager);
					tf.setIds((byte)( 2*(i + j*Macropad.NUM_COLUMNS)), (byte) (2*(i + j*Macropad.NUM_COLUMNS) + 1), (byte) (2*Macropad.NUM_COLUMNS*Macropad.NUM_ROWS + 2*enc), (byte)(2*Macropad.NUM_COLUMNS*Macropad.NUM_ROWS + 2*enc + 1));
					enc += 1;
				}
				else {
					tf = ControlPane.getControlPane(ControlPane.SWITCH_CONTROL, profileManager);
					tf.setIds((byte)( 2*(i + j*Macropad.NUM_COLUMNS)), (byte) (2*(i + j*Macropad.NUM_COLUMNS) + 1));
				}
				tf.setToggleGroup(tg);
				
				
				if (switchNumber == 0) tf.setSelected(true);
				gridPane.add(tf, i, j);
			}
		}
		
		tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				System.out.println("Toggle changed");
				updateCommandPane();
			}
			
		});
		
		
		commandsGridPane = ((ControlPane)tg.getSelectedToggle()).getSettingsUI();
		commandsGridPane.setAlignment(Pos.CENTER);
		
		content.getChildren().addAll(gridPane, commandsGridPane);
		scrollPane.setContent(content);
		this.getChildren().add(scrollPane);
		
	}
	
	public void update () {
		profileNameTextField.setText(profileManager.getProfiles().get(profileManager.getCurrentProfile()).getProfileName());
		
		int r = profileManager.getProfile().getR(); 
		int g = profileManager.getProfile().getG(); 
		int b = profileManager.getProfile().getB(); 
		colorPicker.setValue(Color.rgb(r, g, b));
		changeBorder(r, g, b);
		updateCommandPane();
		
		
	}
	
	public void updateCommandPane () {
		content.getChildren().remove(content.getChildren().size()-1);
		commandsGridPane = ((ControlPane)tg.getSelectedToggle()).getSettingsUI();
		commandsGridPane.setAlignment(Pos.CENTER);
		content.getChildren().add(commandsGridPane);
	}
	
	public void changeBorder(int r, int g, int b) {
		gridPane.setBorder(new Border(new BorderStroke(Color.rgb(r, g, b), BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(6), new Insets(-6))));
	}

}
