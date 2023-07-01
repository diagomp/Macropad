package macropad.gui.profile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import macropad.MacropadController;
import macropad.ProfileManager;
import macropad.Profile;

public class ProfileManagerView extends AnchorPane  {
	
	private ProfileManager pm;
	private MacropadController app;
	
	ListView<Profile> listView;


	public ProfileManagerView (MacropadController app, ProfileManager pm) {
		super();
		
		this.app = app;
		this.pm = pm;
		
		
		VBox vbox = new VBox();
		vbox.setSpacing(5.0);
		vbox.setPadding(new Insets(5));
		
		Label profilesLabel = new Label("Program profiles");
		profilesLabel.setStyle("-fx-font-weight: bold;");
		
		listView = new ListView<Profile>();
		VBox.setVgrow(listView, Priority.ALWAYS);
		listView.setCellFactory(new ProfileCellFactory(pm));
		listView.getSelectionModel().select(pm.getCurrentProfile());
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Profile> () {
			@Override
			public void changed(ObservableValue<? extends Profile> property, Profile oldVal, Profile newVal) {
				if (newVal != null && ((oldVal != null)&&!(newVal.getProfileName().equals(oldVal.getProfileName())))) {
					pm.setCurrentProfile(newVal.getProfileName());
				}
				else {
					//pm.setCurrentProfile(pm.getCurrentProfile());
				}
				
			}
		});
		
		
		
		
		//listView.setItems(FXCollections.observableArrayList(pm.getProfiles()));
		update();
		
		Button newButton = new Button("New");
		newButton.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent e) {
				pm.createNewProfile();
				
			}
			
		});
		
		Button deleteButton = new Button ("Delete");
		deleteButton.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent e) {
				pm.deleteCurrentProfile();
				
			}
			
		});
		
		
		HBox hbox = new HBox();
		hbox.setSpacing(5.0);
		hbox.getChildren().addAll(deleteButton, newButton);
		hbox.setAlignment(Pos.CENTER_RIGHT);
		
		vbox.getChildren().addAll(profilesLabel, listView);
		
		AnchorPane.setTopAnchor(vbox, 5.0);
		AnchorPane.setRightAnchor(vbox, 5.0);
		AnchorPane.setLeftAnchor(vbox, 5.0);
		AnchorPane.setBottomAnchor(vbox, 35.0);
		
		AnchorPane.setBottomAnchor(hbox, 5.0);
		AnchorPane.setRightAnchor(hbox, 5.0);
		AnchorPane.setLeftAnchor(hbox, 5.0);
		this.getChildren().addAll(vbox, hbox);
		
	}
	
	public void update () {
		/*TODO: Cambiar el método para no borrar y actualizar todos los valores, sino
		 * simplemente añadir los nuevos y quitar los que ya no valen.
		 * De esta forma la selección se mantiene activa. */
		listView.getItems().clear();
		listView.setItems(FXCollections.observableArrayList(pm.getProfiles()));
		//listView.getSelectionModel().select(pm.getCurrentProfile());
	}
	
	public void updateSelection () {
		//listView.setItems(FXCollections.observableArrayList(pm.getProfiles()));
		listView.getSelectionModel().select(pm.getCurrentProfile());
		//app.changeColor(pm.getProfiles().get(pm.getCurrentProfile()).getR(), pm.getProfiles().get(pm.getCurrentProfile()).getB(), pm.getProfiles().get(pm.getCurrentProfile()).getB());
	}
}
