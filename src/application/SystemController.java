package application;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

public class SystemController {
	
	Robot robot;
	ProfileManager pm;
	
	public SystemController (ProfileManager pm) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		this.pm = pm;
	}
	
	public void executeCommand (String cmd) {
		//Primero discernimos entre el tipo de comando
		
		//Atajos de teclado
		//Comandos para CMD
		//Escritura de texto
		//Archivo XML
		
		/*if (cmd != null && cmd.length() > 0) {
			if (cmd.charAt(0) == '>') {
				//String[] cmdCommands
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec(new String[] {"cmd.exe"});
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		if (cmd != null && cmd.length() > 0) {
			cmd = cmd.toUpperCase();
			ArrayList<String> cmds = new ArrayList<String>();
			cmds.add("");
			boolean stringMode = false;
			
			for (int i = 0; i < cmd.length(); i++) {
				char c = cmd.charAt(i);
				
				if (c == '\"') {
					stringMode = !stringMode;
					if (stringMode) cmds.remove(cmds.size()-1);
					continue;
				}
				
				if (!stringMode) {
					if (c == '-') {
						cmds.add("");
						continue;
					}
					if (c == ' ') continue;
					cmds.set(cmds.size()-1, cmds.get(cmds.size()-1) + c); 
				}
				else {
					if (c == ':') {
						cmds.add("SHIFT+.");
					}
					else if (c == '/') {
						cmds.add("SHIFT+7");
					}
					else {
						cmds.add("" + c);
					}
				}
			}
			for (int i = 0; i < cmds.size(); i++)
				System.out.println(cmds.get(i));
			
			//Hasta aquí tenemos una lista de todos los comandos que se ejecutan uno detrás del otro.
			for (int i = 0; i < cmds.size(); i++) {
				//Aquí ejecutaremos todos los comandos que son simultáneos
				
				String[] simultaneousCmds = cmds.get(i).split("\\+");
				
				
				for (String c: simultaneousCmds) {
					int code = getConstant(c);
					
					if (code != 0) {
						robot.keyPress(code);
					}
					else {
						//COMANDOS PERSONALIZADOS
						if (c.equals("NEXT_PROFILE")) {
							pm.nextProfile();
						}
						else if (c.equals("PREV_PROFILE")) {
							pm.prevProfile();
						}
						else if (c.length() >= 8 && c.substring(0, 8).equals("PROFILE:")) {
							System.out.println("Change profile to " + c.substring(8, c.length()));
							String name = c.substring(8, c.length());
							pm.setCurrentProfile(name);
						}
						else if (c.equals("MOUSEWHEEL_UP")) {
							robot.mouseWheel(1);
						}
						else if (c.equals("MOUSEWHEEL_DOWN")) {
							robot.mouseWheel(-1);
						}
						else if (c.length() >= 10 && c.substring(0,10).equals("MOUSEMOVE:")) {
							String[] params = c.substring(10, c.length()).split(",");
							System.out.println(params.length);
							if (params.length >= 2) {
								int x = Integer.parseInt(params[0]);
								int y = Integer.parseInt(params[1]);
								
								robot.mouseMove(x, y);
							}
						}
						else if (c.equals("MOUSECLICK")) {
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						}
						else if (c.equals("DELAY")) {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (String c: simultaneousCmds) {
					int code = getConstant(c);
					if (code != 0) {
						robot.keyRelease(code);
					}
					
				}
				
			}
			
			
			
			
			
			
		}
			
			
			
			
			
		
		
	}
	
	private int getConstant (String c) {
		if (c.length() == 1) {
			//Números o letras -- coinciden con sus códigos ASCII
			char ch = c.charAt(0);
			if ((ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
				//Números y letras
				return c.charAt(0);
			}
			else {
				if (ch == ' ') {
					return KeyEvent.VK_SPACE;
				}
				else if (ch == '/') {
					return KeyEvent.VK_SLASH;
				}
				/*else if (ch == ':') {
					return KeyEvent.VK_COLON;
				}*/
				else if (ch == ';') {
					return KeyEvent.VK_SEMICOLON;
				}
				else if (ch == '-') {
					return KeyEvent.VK_MINUS;
				}
				else if (ch == '_') {
					return KeyEvent.VK_UNDERSCORE;
				}
				else if (ch == '.') {
					return KeyEvent.VK_PERIOD;
				}
				else if (ch == '\\') {
					return KeyEvent.VK_BACK_SLASH;
				}
				
			}
			
		}
		else {
			//Comandos de varios caracteres
			if (c.equals("CTRL")) {
				return KeyEvent.VK_CONTROL;
			}
			else if (c.equals("ALT")) {
				return KeyEvent.VK_ALT;
			}
			else if (c.equals("SHIFT")) {
				return KeyEvent.VK_SHIFT;
			}
			else if (c.equals("ENTER")) {
				return KeyEvent.VK_ENTER;
			}
			else if (c.equals("ESCAPE")) {
				return KeyEvent.VK_ESCAPE;
			}
			else if (c.equals("SPACE")) {
				return KeyEvent.VK_SPACE;
			}
			else if (c.equals("WINDOWS")) {
				return KeyEvent.VK_WINDOWS;
			}
			else if (c.equals("PERIOD")) {
				return KeyEvent.VK_PERIOD;
			}
			else if (c.equals("UP")) {
				return KeyEvent.VK_UP;
			}
			else if (c.equals("DOWN")) {
				return KeyEvent.VK_DOWN;
			}
			else if (c.equals("LEFT")) {
				return KeyEvent.VK_LEFT;
			}
			else if (c.equals("TAB")) {
				return KeyEvent.VK_TAB;
			}
			else if (c.equals("RIGHT")) {
				return KeyEvent.VK_RIGHT;
			}
		}
		return 0;
	}

}
