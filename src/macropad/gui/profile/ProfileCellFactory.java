package macropad.gui.profile;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import macropad.Profile;
import macropad.ProfileManager;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ProfileCellFactory implements Callback<ListView<Profile>, ListCell<Profile>> {
	
	ProfileManager profileManager;
	
	public ProfileCellFactory (ProfileManager profileManager) {
		super();
		
		this.profileManager = profileManager;
	}

	@Override
	public ListCell<Profile> call(ListView<Profile> listView) {
		return new ListCell<Profile> () {
			@Override
			public void updateItem (Profile profile, boolean empty) {
				super.updateItem(profile, empty);
				if (empty || profile == null) {
					setText(null);
					setGraphic(null);
					
				}
				else {
					//setText(profile.getProfileName());
					setText(null);
					
					HBox hbox = new HBox();
					
					Label nameLabel = new Label (profile.getProfileName());
					//nameTextField.setEditable(false);
					Region region = new Region();
					HBox.setHgrow(region, Priority.ALWAYS);
					
					Pane led = new Pane();
					led.setPrefWidth(20);
					led.setMaxWidth(20);
					led.setMinWidth(20);
					
					led.setPrefHeight(20);
					led.setMaxHeight(20);
					led.setMinHeight(20);
					led.setBackground(new Background(new BackgroundFill(Color.rgb(profile.getR(), profile.getG(), profile.getB()), new CornerRadii(10), new Insets(0))));
					led.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, new CornerRadii(10), null)));
					
					
					
					
					hbox.getChildren().addAll(nameLabel, region, led);
					setGraphic(hbox);
					
					getGraphic().setOnMousePressed(new EventHandler<MouseEvent> () {
					//getGraphic().setOnMouseClicked(new EventHandler<MouseEvent> () {

						@Override
						public void handle(MouseEvent me) {
							//profileManager.setCurrentProfile(profile.getProfileName());
							
						}
						
					});
					
					
					
					
					this.styleProperty().bind(Bindings.when(this.selectedProperty())
                            .then("-fx-background-color: " + profile.getHexColor() + ";")
                            .otherwise(""));
				}
			}
			
		};
	}

}
