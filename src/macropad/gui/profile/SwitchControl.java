package macropad.gui.profile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class SwitchControl extends ControlPane {

	ContextMenu contextMenu;
	
	protected SwitchControl () {
		controlType = ControlPane.SWITCH_CONTROL;
		
		triggers = new String[2];
		triggers[0] = "Short press";
		triggers[1] = "Long press";
		
		
		keysId = new byte[2];
		
		this.setWidth(60.0);
		this.setPrefWidth(60.0);
		this.setMinWidth(60.0);
		this.setMaxWidth(60.0);
		
		this.setHeight(60.0);
		this.setPrefHeight(60.0);
		this.setMinHeight(60.0);
		this.setMaxHeight(60.0);
		
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.00), new Insets(0))));
		
		Group group = new Group();
		AnchorPane.setTopAnchor(group, 0.0);
		AnchorPane.setRightAnchor(group, 0.0);
		AnchorPane.setBottomAnchor(group, 0.0);
		AnchorPane.setLeftAnchor(group, 0.0);
		
		Rectangle rect = new Rectangle();
		rect.setArcHeight(10.0d);
		rect.setArcWidth(10.0d);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(1);
		rect.setFill(Color.WHITE);
		rect.setX(5);
		rect.setY(5);
		rect.setWidth(50);
		rect.setHeight(50);
		
		
		group.getChildren().addAll(new Line(0, 0, 30, 30), new Line(60,0,30,30), new Line(60, 60, 30, 30), new Line(0, 60, 30, 30), rect);
		this.setAlignment(Pos.CENTER);
		
		this.setGraphic(group);
		
		
		contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem ("Configure");
		contextMenu.getItems().add(menuItem);
		
		
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
		
		return grid;
	}*/

	@Override
	public ContextMenu getConfigUI() {
		
		return contextMenu;
	}
	
	@Override
	public void setIds(byte... ids) {
		for (int i = 0; i < 2; i++) {
			keysId[i] = ids[i];
		}
		
	}
	
}
