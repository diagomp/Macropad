package macropad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HexFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Profile {
	
	public static final String PROFILE_PATH = "D:/MacroKeypad/AppProfiles/";
	
	
	private String appName;
	private String profileName;
	
	private int[] color;
	private String hexColor;
	
	private Key[] keys;
	
	private String path;
	
	
	public static void main (String[] args)  {
		Profile p = new Profile("D:/MacroKeypad/AppProfiles/default.xml");
		
	}
	
	public Profile (String profileName, int r, int g, int b) {
		this.profileName = profileName;
		
		color = new int[3];
		color[0] = r;
		color[1] = g;
		color[2] = b;
		
		keys = new Key[Macropad.NUM_COLUMNS * Macropad.NUM_ROWS + 2];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new Key();
		}
		
	}
	

	
	public Profile (String path) {
		
		this.path = path;
		
		keys = new Key[Macropad.NUM_COLUMNS*Macropad.NUM_ROWS + 2];
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
	
	public void saveProfile () throws ParserConfigurationException, FileNotFoundException, TransformerException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.newDocument();
		Element rootElement = doc.createElement("Profile");
		doc.appendChild(rootElement);
		
		Element nameElement = doc.createElement("Name");
		nameElement.setTextContent(getProfileName());
		rootElement.appendChild(nameElement);
		
		Element appNameElement = doc.createElement("AppName");
		appNameElement.setTextContent(getAppName());
		rootElement.appendChild(appNameElement);
		
		Element colorElement = doc.createElement("Color");
		colorElement.setTextContent(getHexColor());
		rootElement.appendChild(colorElement);
		
		Element keysElement = doc.createElement("Keys");
		rootElement.appendChild(keysElement);
		for (Key k: getKeys()) {
			Element keyElement = doc.createElement("Key");
			keysElement.appendChild(keyElement);
			
			Element shortPressElement = doc.createElement("ShortPress");
			shortPressElement.setTextContent(k.getShortCmd());
			keyElement.appendChild(shortPressElement);
			
			Element longPressElement = doc.createElement("LongPress");
			longPressElement.setTextContent(k.getLongCmd());
			keyElement.appendChild(longPressElement);
		}
		
		
		
		//Saving to file
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(doc);
		FileOutputStream output = new FileOutputStream(Profile.PROFILE_PATH + getProfileName() + ".xml");
		StreamResult result = new StreamResult(output);
		
		transformer.transform(source,  result);
		
		System.out.println("Changes saved.");
	}
	
	public void deleteProfile () {
		File f = new File (Profile.PROFILE_PATH + profileName + ".xml");
		f.delete();
	}
	
	public void setName (String name) {
		
	}
	
	
	public String getPath () { return path; }
	public String getProfileName() { return profileName; }
	public String getAppName () { return appName; }
	public int getR () { return color[0]; }
	public int getG () { return color[1]; }
	public int getB () { return color[2]; }
	public String getHexColor () { 
		return String.format("#%02x%02x%02x", getR(), getG(), getB());
		}
	public String getShortCmd (int button) { return keys[button].getShortCmd(); }
	public String getLongCmd (int button) { return keys[button].getLongCmd(); }
	public Key[] getKeys () { return keys; }
	

}
