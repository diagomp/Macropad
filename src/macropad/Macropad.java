package macropad;

import java.io.File;

import macropad.gui.profile.ControlPane;

public class Macropad {
	public static int NUM_ROWS = 3;
	public static int NUM_COLUMNS = 4;
	public static String SETTINGS_PATH = "D:\\MacroKeypad";

	private int numRows;
	private int numColumns;
	private ControlPane[] controls;
	
	
	
	public Macropad (String configPath) {
		//File f = new File (configPath);
		this.numRows = 3;
		this.numColumns = 4;
		
		controls = new ControlPane[12];
		int encoder = 0;
		/*for (int i = 0; i < controls.length; i++) {
			ControlPane cp;;
			if (i == 0 || i == 3) {
				cp = ControlPane.getControlPane(ControlPane.ENCODER_CONTROL);
				cp.setIds((byte)i, (byte) (i & 0b10000000), (byte) (numRows + numColumns + encoder), (byte) ((numRows + numColumns + encoder) & 0b100000000));
				encoder += 1;
			}
			else {
				cp = ControlPane.getControlPane(ControlPane.SWITCH_CONTROL);
				cp.setIds((byte)i, (byte) (i & 0b10000000));
			}
			
		}*/
		
		
	}
	
}
