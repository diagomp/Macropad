package application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.communication.usb.SerialComManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProfileManager {
	
	private ArrayList<Profile> profiles;
	private int currentProfile;
	
	private ObservableList<Key> currentKeys;
	
	private SerialComManager scm;
	
	SystemController robot;
	
	
	public ProfileManager () {
		
		profiles = new ArrayList<Profile>();
		currentProfile = 0;
		
		currentKeys = FXCollections.observableArrayList();
		
		robot = new SystemController(this);
		
		
		try {
			//Carga todos los perfiles del directorio AppProfiles
			File profilesDir = new File("D:/MacroKeypad/AppProfiles");
			profilesDir.mkdirs();
			
			File[] files = profilesDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				//Crear objeto de tipo Profile seg�n los archivos encontrados.
				File f = files[i];
				if (f.isFile() && f.getName().indexOf(".xml") == f.getName().length() - 4) {
					//Solo leemos archivos cuya extensi�n es '.xml'
				
					System.out.println(f.getAbsolutePath());
					profiles.add(new Profile(f.getAbsolutePath()));
					
					if (f.getName().equals("default.xml")) {
						//Coloca el archivo 'default.xml' siempre el primero de la lista.
						//El resto de entradas se mostrar�n por orden alfab�tico.
						Profile aux = profiles.get(i);
						
						for (int j = i; j > 0; j--) {
							profiles.set(j, profiles.get(j - 1));
						}
						profiles.set(0, aux);
						
						
					}
				}
			}
			
			
			
			//profileList = FXCollections.observableArrayList(profiles);
			
			
			currentKeys.clear();
			currentKeys.addAll(profiles.get(currentProfile).getKeys());
			
			
			
			System.out.println("-------------------------------------");
			for (Profile p: profiles) System.out.println(p.getPath());
			
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void shortPress (int button) {
		robot.executeCommand(profiles.get(currentProfile).getShortCmd(button));
	}
	
	public void longPress (int button) {
		robot.executeCommand(profiles.get(currentProfile).getLongCmd(button));
	}
	
	public void saveChanges (int profileId) {
		try {
			//File f = new File("C:/MacroKeypad/AppProfiles/" + profiles.get(profileId).getProfileName() + ".xml");
			//Creating XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.newDocument();
			Element rootElement = doc.createElement("Profile");
			doc.appendChild(rootElement);
			
			Element nameElement = doc.createElement("Name");
			nameElement.setTextContent(profiles.get(profileId).getProfileName());
			rootElement.appendChild(nameElement);
			
			Element appNameElement = doc.createElement("AppName");
			appNameElement.setTextContent(profiles.get(profileId).getAppName());
			rootElement.appendChild(appNameElement);
			
			Element colorElement = doc.createElement("Color");
			colorElement.setTextContent(profiles.get(profileId).getHexColor());
			rootElement.appendChild(colorElement);
			
			Element keysElement = doc.createElement("Keys");
			rootElement.appendChild(keysElement);
			for (Key k: profiles.get(profileId).getKeys()) {
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
			FileOutputStream output = new FileOutputStream("D:/MacroKeypad/AppProfiles/" + profiles.get(profileId).getProfileName() + ".xml");
			StreamResult result = new StreamResult(output);
			
			transformer.transform(source,  result);
			
			System.out.println("Changes saved.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSerialComManager (SerialComManager scm) {
		this.scm = scm;
	}
	
	
	public void setCurrentProfile (int currentProfile) {
		this.currentProfile = currentProfile;
		currentKeys.clear();
		currentKeys.addAll(profiles.get(currentProfile).getKeys());
		
		scm.changeColor(profiles.get(currentProfile).getR(), profiles.get(currentProfile).getG(), profiles.get(currentProfile).getB());
	}
	
	
	public void setCurrentProfile (String newProfName) {
		int i = 0;
		for (i = 0; i < profiles.size(); i++) {
			newProfName = newProfName.toLowerCase();
			newProfName = newProfName.replace(" ", "");
			
			String cmpProfName = profiles.get(i).getProfileName();
			cmpProfName = cmpProfName.toLowerCase();
			cmpProfName = cmpProfName.replace(" ", "");
			
			if (newProfName.equals(cmpProfName)) {
				currentProfile = i;
				setCurrentProfile(i);
				
				return;
			}
		}
		//Si el perfil no existe no se hace nada.
	}
	
	public void nextProfile () {
		if (currentProfile < profiles.size() - 1) {
			setCurrentProfile(currentProfile + 1);
		}
	}
	
	public void prevProfile () {
		if (currentProfile > 0) {
			setCurrentProfile(currentProfile-1);
		}
	}
	
	
	public ArrayList<Profile> getProfiles () { return profiles; }
	public ObservableList<Key> getCurrentKeys () { return currentKeys; }
	public int getCurrentProfile () { return currentProfile; }

}
