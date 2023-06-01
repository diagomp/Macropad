package application.communication.usb;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListenerWithExceptions;
import com.fazecast.jSerialComm.SerialPortEvent;

import application.MacropadController;
import application.ProfileManager;

public class SerialComManager extends Thread implements SerialPortDataListenerWithExceptions {
	
	public static void main (String[] args) {
		System.out.println("SerialComManager tester");
		
		//Creamos una instancia de la clase
		SerialComManager scm = new SerialComManager (new MacropadController());
		
		//Especificamos el nombre del puerto
		scm.setPortName("COM4");
		
		//Establecemos la conexión
		scm.conectar();
		
		
		
	}
	
	/**
	 * Reference to main App object for communication.
	 */
	private MacropadController app;
	
	
	private SerialPort port;
	
	
	private String portName;
	
	private String portInfo;
	
	
	
	private ProfileManager pm;
	
	
	
	public SerialComManager (MacropadController app) {
		this.app = app;
	}
	
	public SerialComManager (String portInfo, ProfileManager pm) {
		this.portInfo = portInfo;
		this.pm = pm;
		pm.setSerialComManager(this);
		
	}
	
	/**
	 * Establece el puerto al que conectarse.
	 * @param portName Nombre del puerto.
	 * @return Devuelve true si el puerto existe, false en caso contrario.
	 */
	public void setPortName (String portName) {
		this.portName = portName;
	}
	
	public boolean conectar () {
		
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
	
	public void desconectar () {
		port.closePort();
		System.out.println("Desconectando " + portName);
	}
	
	
	public void connect() {
		int portNumber = -1;
		while (true) {
			SerialPort[] availablePorts = SerialPort.getCommPorts();
			
			portNumber = 0;
			for (portNumber = 0; portNumber < availablePorts.length; portNumber++) {
				//if (availablePorts[portNumber].getDescriptivePortName().contains(portInfo)) break;
				if (availablePorts[portNumber].getSystemPortName().contains(portInfo)) break;
			}
			
			
			if (portNumber >= availablePorts.length) {
				//No se encuentra el puerto
				System.out.println("Buscando dispositivio...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			else {
				//El puerto se ha encontrado
				System.out.println("Conectando a puerto " + availablePorts[portNumber].getDescriptivePortName() + "...");
				
				port = availablePorts[portNumber];
				//comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
				//port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 100, 100);
				
				if (port.openPort()) {
					System.out.println("Conexión realizada con éxito.");
					
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					changeColor(pm.getProfiles().get(pm.getCurrentProfile()).getR(),
							pm.getProfiles().get(pm.getCurrentProfile()).getB(),
							pm.getProfiles().get(pm.getCurrentProfile()).getG());
					
					
					
					
					
					
					
					port.addDataListener(new SerialPortDataListenerWithExceptions () {
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
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								connect();
							}
						}
						@Override
						public void catchException(Exception e) {
							e.printStackTrace();
							
						}
					});
					
				}
				
				break;
			}
			
		}
		if (port != null) {
			//port.closePort();
		}
		
		
	}
	
	
	private void processSerialRequest (byte b) {
		
		if (b >= 0) {
			
			/*if (b == 0) {
				System.out.println("ENCODER DOWN");
				return;
			}*/
			
			//SHORT PRESS
			System.out.println("SHORT PRESS: " + ((int) b));
			
			pm.shortPress((int) (b));
		}
		else  {
			/*if (b == 0) {
				System.out.println("ENCODER DOWN");
			}*/
			
			
			//LONG PRESS
			System.out.println("LONG PRESS: " + ((b) & 0b01111111));
			pm.longPress((int) ((b) & 0b01111111));
		}
	}
	
	@Override
	public void run () {
		connect();
	}


	public void changeColor(int r, int g, int b) {
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
