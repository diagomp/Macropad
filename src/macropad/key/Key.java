package macropad.key;

public class Key {
	
	private byte id;
	private String cmd;
	
	public Key (byte id, String cmd) {
		this.id = id;
		this.cmd = cmd;
	}
	
	public Key (byte id) {
		this(id, "");
	}
	
	public void setCmd (String cmd) { this.cmd = cmd; }
	public String getCmd () { return cmd; }
	
	public void setId (byte id) { this.id = id; }
	public byte getId () { return id; }

}
