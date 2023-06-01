package application;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Profile {
	
	
	private String appName;
	private String profileName;
	
	private int[] color;
	private String hexColor;
	
	private Key[] keys;
	
	private String path;
	
	
	public static void main (String[] args)  {
		Profile p = new Profile("D:/MacroKeypad/AppProfiles/default.xml");
		
	}
	
	
	
	public Profile (String path) {
		
		this.path = path;
		
		keys = new Key[Macropad.NUM_COLUMNS*Macropad.NUM_ROWS];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new Key();
		}
		
		color = new int[3];
		color[0] =(int) (255*Math.random());
		color[1] =(int) (255*Math.random());
		color[2] =(int) (255*Math.random());
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(path));
		
		doc.getDocumentElement().normalize();
		
		
		profileName = doc.getElementsByTagName("Name").item(0).getTextContent();
		appName = doc.getElementsByTagName("AppName").item(0).getTextContent();
		
		hexColor = doc.getElementsByTagName("Color").item(0).getTextContent();
		System.out.println(hexColor);
		//System.out.println(Long.parseLong(c.replace("#", ""), 16));
		
		String hexColorAux = hexColor.replace("#", "");
		for (int a = 0; a < color.length; a ++) {
			color[a] = Integer.parseInt(hexColorAux.substring(2*a, 2*a+2), 16);
			//System.out.println(a + ": " + color[a]);
		}
		
		NodeList list = doc.getElementsByTagName("Key");
		for (int i = 0; i < list.getLength(); i++) {
			System.out.println(i + "-------------------------");
			Element key = (Element) list.item(i);
			System.out.println(key.getTextContent());
			
			keys[i].setShortCmd(key.getElementsByTagName("ShortPress").item(0).getTextContent());
			keys[i].setLongCmd(key.getElementsByTagName("LongPress").item(0).getTextContent());
		}
		
		
		/*NodeList list = doc.getChildNodes();
		System.out.println(list.getLength());
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				Element element = (Element) node;
				
				
				profileName = element.getElementsByTagName("Name").item(0).getTextContent();
				appName = element.getElementsByTagName("AppName").item(0).getTextContent();
				
				hexColor = element.getElementsByTagName("Color").item(0).getTextContent();
				System.out.println(hexColor);
				//System.out.println(Long.parseLong(c.replace("#", ""), 16));
				
				String hexColorAux = hexColor.replace("#", "");
				for (int a = 0; a < color.length; a ++) {
					color[a] = Integer.parseInt(hexColorAux.substring(2*a, 2*a+2), 16);
					//System.out.println(a + ": " + color[a]);
				}
				

				
				NodeList keyElements = doc.getElementsByTagName("Key");
				System.out.println(keyElements.getLength());
				for (int j = 0; j < keyElements.getLength(); j++) {
					System.out.println("SHORT PRESS: " + 
							((Element)keyElements.item(j)).getElementsByTagName("ShortPress").item(0).getTextContent());
					System.out.println("LONG PRESS: " + 
							((Element)keyElements.item(j)).getElementsByTagName("LongPress").item(0).getTextContent());
				}
				
				break;
			
			}
		}*/
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public String getPath () { return path; }
	public String getProfileName() { return profileName; }
	public String getAppName () { return appName; }
	public int getR () { return color[0]; }
	public int getG () { return color[1]; }
	public int getB () { return color[2]; }
	public String getHexColor () { return hexColor; }
	public String getShortCmd (int button) { return keys[button].getShortCmd(); }
	public String getLongCmd (int button) { return keys[button].getLongCmd(); }
	public Key[] getKeys () { return keys; }
	

}
