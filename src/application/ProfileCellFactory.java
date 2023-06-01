package application;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class ProfileCellFactory implements Callback<ListView<Profile>, ListCell<Profile>> {
	
	ProfileManager profileManager;
	
	public ProfileCellFactory (ProfileManager profileManager) {
		super();
		
		this.profileManager = profileManager;
	}

	@Override
	public ListCell<Profile> call(ListView<Profile> arg0) {
		return new ListCell<Profile> () {
			@Override
			public void updateItem (Profile profile, boolean empty) {
				super.updateItem(profile, empty);
				if (empty || profile == null) {
					setText(null);
					
				}
				else {
					setText(profile.getProfileName());
					/*this.setBackground(new Background(new BackgroundFill(new Color(
							profile.getR()/255.0,
							profile.getG()/255.0,
							profile.getB()/255.0, 1.0), null, null)));*/
					this.styleProperty().bind(Bindings.when(this.selectedProperty())
                            .then("-fx-background-color: " + profile.getHexColor() + ";")
                            .otherwise(""));
					this.setOnMouseClicked(new EventHandler<MouseEvent> () {
						@Override
						public void handle(MouseEvent arg0) {
							System.out.println("Seleccionado perfil: " + profile.getProfileName());
							profileManager.setCurrentProfile(profile.getProfileName());
							
							
							//Toast msg
							/*Thread thread = new Thread(Platform.runLater(new Runnable() {

								@Override
								public void run() {
									
									Stage stage = new Stage();
									//stage.initStyle(StageStyle.TRANSPARENT);
									
									HBox root = new HBox ();
									Scene scene = new Scene(root, 480, 100);
									
									root.getChildren().add(new Button("Hola"));
									
									stage.setScene(scene);
									stage.show();
									
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									stage.hide();
									
								}
								
							});*/
							
							//thread.start();
							
						}
					});
				}
			}
			
		};
	}

}
