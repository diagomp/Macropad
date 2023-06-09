package macropad.communication.usb;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListenerWithExceptions;
import com.fazecast.jSerialComm.SerialPortEvent;

import macropad.MacropadController;
import macropad.ProfileManager;

public class SerialComManager extends Thread implements SerialPortDataListenerWithExceptions {
	
	public static void main (String[] args) {
		System.out.println("SerialComManager tester");
		
		//Creamos una instancia de la clase
		SerialComManager scm = new SerialComManager (new MacropadController());
		
		//Especificamos el nombre del puerto
		scm.setPortName("COM4");
		
		//Establecemos la conexión
		scm.connect();
		
		
		
	}
	
	/**
	 * Reference to main App object for communication purposes.
	 */
	private MacropadController app;
	
	
	private SerialPort port;
	
	
	private String portName;
	
	
	
	
	public SerialComManager (MacropadController app) {
		this.app = app;
	}
	
	public SerialComManager (MacropadController app, String portName) {
		this.portName = portName;
		this.app = app;
		
	}
	
	/**
	 * Establece el puerto al que conectarse.
	 * @param portName Nombre del puerto.
	 * @return Devuelve true si el puerto existe, false en caso contrario.
	 */
	public void setPortName (String portName) {
		this.portName = portName;
	}
	
	public boolean connect () {
		
		if (isAvailable(portName)) {
			port = SerialPort.getCommPort(portName);
			System.out.println("Existe el puerto " + portName + ".\nIntentando conectar");
			if (port.openPort()) {
				System.out.println("Conectado!");
				port.addDataListener(this);
				return true;
			}
		}
		return false;
	}
	
	public void disconnect () {
		if (isConnected()) {
			app.changeColor(0,  0,  0);
		}
		port.closePort();
		System.out.println("Desconectando " + portName);
	}
	
	public boolean isConnected() {
		return port != null? port.isOpen(): false;
	}
	
	
	
	
	
	private void processSerialRequest (byte b) {
		
		if (b >= 0) {
			
			/*if (b == 0) {
				System.out.println("ENCODER DOWN");
				return;
			}*/
			
			//SHORT PRESS
			System.out.println("SHORT PRESS: " + ((int) b));
			
			app.getProfileManager().shortPress((int) (b));
		}
		else  {
			/*if (b == 0) {
				System.out.println("ENCODER DOWN");
			}*/
			
			
			//LONG PRESS
			System.out.println("LONG PRESS: " + ((b) & 0b01111111));
			app.getProfileManager().longPress((int) ((b) & 0b01111111));
		}
	}
	
	@Override
	public void run () {
		//connect();
	}


	/*public void changeColor(int r, int g, int b) {
		String colorMsg = r + "," + g + "," + b + '\n';
		
		byte[] bytes = new byte[colorMsg.length()];
		if (port != null && port.isOpen()) {
			//System.out.println("Color msg: " + colorMsg);
			
			for (int i = 0; i < colorMsg.length(); i++) {
				bytes[i] = (byte)colorMsg.charAt(i);
				System.out.print(colorMsg.charAt(i));
				
			}
			port.writeBytes(bytes, bytes.length);	
			System.out.println();
		}
		else {
			System.out.println("NULL PORT");
		}
		
		
	}*/
	
	public boolean sendMessage (String msg) {
		if (port != null) {
			byte[] bytes = new byte[msg.length()];
			
			for (int i = 0; i < msg.length(); i++) {
				bytes[i] = (byte)msg.charAt(i);
				
			}
			port.writeBytes(bytes, bytes.length);
			return true;
			
		}
		return false;
	}
	
	public static String[] getAvailablePorts () {
		SerialPort[] availablePorts = SerialPort.getCommPorts();
		
		String[] portNames = new String[availablePorts.length];
		for (int i = 0; i < portNames.length; i++) {
			portNames[i] = availablePorts[i].getSystemPortName();
		}
		
		return portNames;
	}
	
	public static boolean isAvailable (String portName) {
		for (String s: getAvailablePorts()) {
			if (s.equals(portName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
	}

	@Override
	public void serialEvent(SerialPortEvent serialEvent) {
		if (serialEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
			//System.out.println("Receiving data.");
		
			byte[] newData = new byte[port.bytesAvailable()];
			port.readBytes(newData,  newData.length);
			for (int i = 0; i < newData.length; i++) {
				processSerialRequest(newData[i]);
			}
		}
		else if (serialEvent.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
			System.out.println("Port disconnected.");
			port.closePort();
		}
		
	}

	@Override
	public void catchException(Exception e) {
		e.printStackTrace();
	}
	

}
