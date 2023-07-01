package macropad.gui.profile;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EncoderControl extends ControlPane {
	
	private double angle;
	private Group group;
	
	
	protected EncoderControl () {
		controlType = ControlPane.ENCODER_CONTROL;
		
		triggers = new String[4];
		triggers[0] = "Short press";
		triggers[1] = "Long press";
		triggers[2] = "CW turn";
		triggers[3] = "CCW turn";
		
		
		keysId = new byte[4];
		angle = 0;
		
		this.setWidth(60.0);
		this.setPrefWidth(60.0);
		this.setMinWidth(60.0);
		this.setMaxWidth(60.0);
		
		this.setHeight(60.0);
		this.setPrefHeight(60.0);
		this.setMinHeight(60.0);
		this.setMaxHeight(60.0);
		
		
		
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(30.0), new Insets(0))));
		//this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(30.0), new BorderWidths(3))));
		group = new Group();
		AnchorPane.setTopAnchor(group, 0.0);
		AnchorPane.setRightAnchor(group, 0.0);
		AnchorPane.setBottomAnchor(group, 0.0);
		AnchorPane.setLeftAnchor(group, 0.0);
		
		Circle circle = new Circle();
		circle.setStroke(Color.WHITE);
		circle.setStrokeWidth(3);
		circle.setCenterX(30);
		circle.setCenterY(30);
		circle.setRadius(30);
		group.getChildren().add(circle);
		
		//this.getChildren().add(group);
		
		
		Line line = new Line();
		line.setStartX(30);
		line.setStartY(10);
		line.setEndX(30);
		line.setEndY(0);
		line.setStroke(Color.WHITE);
		line.setStrokeWidth(3);
		group.getChildren().add(line);
		
		this.setGraphic(group);
		
		
		
		
		this.setOnScroll(new EventHandler<ScrollEvent> () {
			@Override
			public void handle(ScrollEvent se) {
				angle += se.getDeltaY()>0? 360/20:-360/20;
				
				group.getTransforms().clear();
				
				Transform t = Transform.rotate(angle, 30, 30);
				group.getTransforms().add(t);
				
			}
		});
	}


	/*@Override
	public GridPane getSettingsUI() {
		GridPane grid = new GridPane();
		grid.setHgap(5.0);
		grid.setVgap(5.0);
		grid.setPadding(new Insets(25, 0, 0, 0));

		
		grid.add(new Label("Short press: "), 0, 0);
		grid.add(new TextField("" + keysId[0]), 1, 0);
		grid.add(new Button(""), 2, 0);
		
		grid.add(new Label("Long press: "), 0, 1);
		grid.add(new TextField("" + keysId[1]), 1, 1);
		grid.add(new Button(""), 2, 1);
		
		grid.add(new Label("Clockwise: "), 0, 2);
		grid.add(new TextField("" + keysId[2]), 1, 2);
		grid.add(new Button(""), 2, 2);
		
		grid.add(new Label("Counter-clockwise: "), 0, 3);
		grid.add(new TextField("" + keysId[3]), 1, 3);
		grid.add(new Button(""), 2, 3);
		
		return grid;
	}*/


	@Override
	public ContextMenu getConfigUI() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setIds(byte... ids) {
		for (int i = 0; i < 4; i++) {
			keysId[i] = ids[i];
		}
		
	}
}
