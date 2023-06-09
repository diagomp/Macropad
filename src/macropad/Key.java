package macropad;

public class Key {
	
	private String shortCmd;
	private String longCmd;
	
	public Key (String shortCmd, String longCmd) {
		this.shortCmd = shortCmd;
		this.longCmd = longCmd;
	}
	
	public Key () {
		this("", "");
	}
	
	public void setShortCmd (String cmd) { this.shortCmd = cmd; }
	public void setLongCmd (String cmd) { this.longCmd = cmd; }
	
	public String getShortCmd () { return shortCmd; }
	public String getLongCmd () { return longCmd; }
}
