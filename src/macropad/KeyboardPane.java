package macropad;

import javax.swing.GroupLayout.Alignment;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class KeyboardPane extends ScrollPane {
	
	private ProfileManager profileManager;

	public KeyboardPane(ProfileManager pm) {
		super();
		
		profileManager = pm;
		
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		
		VBox content = new VBox();
		content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10), null)));
		
		for (int j = 0; j < Macropad.NUM_ROWS; j++) {
			for (int i = 0; i < Macropad.NUM_COLUMNS; i++) {
				int switchNumber = j*Macropad.NUM_COLUMNS + i;
				/*VBox vbox = new VBox();
				vbox.setPrefWidth(90.0);
				vbox.setPrefHeight(90.0);
				//vbox.setWrapText(true);
				//vbox.setTextAlignment(TextAlignment.CENTER);
				vbox.setAlignment(Pos.CENTER);
				vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10.0), null)));
				vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(1.0))));
				
				TextField shortCmdLabel = new TextField("CTRL+S");
				shortCmdLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
				shortCmdLabel.setAlignment(Pos.CENTER);
				
				TextField longCmdLabel = new TextField("CTRL+SHIFT+S");
				longCmdLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
				
				vbox.getChildren().addAll(shortCmdLabel, longCmdLabel);*/
				
				Button tf = new Button (""+ ((char) ('A' + switchNumber)));
				tf.setPrefWidth(60.0);
				tf.setPrefHeight(60.0);
				tf.setWrapText(true);
				tf.setTextAlignment(TextAlignment.CENTER);
				tf.setAlignment(Pos.CENTER);
				tf.setOnAction(new EventHandler<ActionEvent> () {
					@Override
					public void handle(ActionEvent ae) {
						System.out.println(profileManager.getCurrentKeys().get(switchNumber).getShortCmd());
						
					}
				});
				//tf.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10.0), null)));
				//tf.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(1.0))));
				
				
				if (switchNumber == 4 || switchNumber == 8) { 
					continue;
					//tf.setBorder(new Border(new BorderStroke(Color.RED, null, new CornerRadii(40.0), null)));
				}
				gridPane.add(tf, i, j);
			}
		}
		
		
		GridPane cmdGridPane = new GridPane();
		cmdGridPane.setHgap(10);
		cmdGridPane.setVgap(10);
		cmdGridPane.setPadding(new Insets(10, 10, 10, 10));
		
		
		cmdGridPane.add(new Label("Short press:"), 0, 0);
		cmdGridPane.add(new Label("Long press:"), 0, 1);
		
		cmdGridPane.add(new TextField(profileManager.getCurrentKeys().get(0).getShortCmd()), 1, 0);
		cmdGridPane.add(new TextField(profileManager.getCurrentKeys().get(0).getLongCmd()), 1, 1);
		
		
		content.getChildren().addAll(gridPane, cmdGridPane);
		this.setContent(content);
		//this.getChildren().addAll(scroll);
	}
	
	public void update () {
		
	}

}
