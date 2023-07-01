package macropad;

import java.awt.Color;
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

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import macropad.communication.usb.SerialComManager;
import macropad.key.Key;

public class ProfileManager {
	private MacropadController app;
	
	private ArrayList<Profile> profiles;
	private int currentProfile;
	
	private ObservableList<Key> currentKeys;
	
	//private SerialComManager scm;
	
	SystemController robot;
	
	
	public ProfileManager (MacropadController app) {
		this.app = app;
		
		profiles = new ArrayList<Profile>();
		currentProfile = 0;
		
		currentKeys = FXCollections.observableArrayList();
		
		robot = new SystemController(this);
		
		
		
		try {
			//Carga todos los perfiles del directorio AppProfiles
			File profilesDir = new File(Profile.PROFILE_PATH);
			profilesDir.mkdirs();
			
			File[] files = profilesDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				//Crear objeto de tipo Profile seg�n los archivos encontrados.
				File f = files[i];
				if (f.isFile() && f.getName().indexOf(".xml") == f.getName().length() - 4) {
					//Solo leemos archivos cuya extensión es '.xml'
				
					System.out.println(f.getAbsolutePath());
					profiles.add(new Profile(f.getAbsolutePath()));
					
					if (f.getName().equals("default.xml")) {
						//Coloca el archivo 'default.xml' siempre el primero de la lista.
						//El resto de entradas se mostrar�n por orden alfab�tico.
						/*Profile aux = profiles.get(i);
						
						for (int j = i; j > 0; j--) {
							profiles.set(j, profiles.get(j - 1));
						}
						profiles.set(0, aux);*/
						currentProfile = i;
						
					}
				}
			}
			
			
			
			if (profiles.size() <= 0) {
				// No se han encontrado archivos, o ninguno tenía extensión .xml
				profiles.add(new Profile("Default", 255, 255, 255));
				currentProfile = 0;
				saveChanges(currentProfile);
			}
			else {
				//sortProfiles();
				//setCurrentProfile(currentProfile);
				
			}
			
			
			
			//profileList = FXCollections.observableArrayList(profiles);
			
			
			currentKeys.clear();
			currentKeys.addAll(profiles.get(currentProfile).getKeys());
			
			//setCurrentProfile(currentProfile);
			
			System.out.println("-------------------------------------");
			for (Profile p: profiles) System.out.println(p.getPath());
			
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void sortProfiles () {
		System.out.println("Sorting profiles");
		for (int i = 0; i < profiles.size(); i++) {
			for (int j = i + 1; j < profiles.size(); j++) {
				if (profiles.get(i).getProfileName().compareTo(profiles.get(j).getProfileName()) > 0) {
					Profile auxProfile = profiles.get(j);
					profiles.remove(j);
					
					profiles.add(j, profiles.get(i));
					
					profiles.remove(i);
					profiles.add(i, auxProfile);
					
					if (i == currentProfile) {
						currentProfile = j;
					}
				}
			}
		}
		currentProfile = 0;
	}
	
	
	/*TODO: Cambiar funciones short/longPress a solamente una función execute(byte id)*/
	public void shortPress (int button) {
		//robot.executeCommand(profiles.get(currentProfile).getShortCmd(button));
	}
	
	public void longPress (int button) {
		//robot.executeCommand(profiles.get(currentProfile).getLongCmd(button));
	}
	
	public void saveChanges (int profileId) {
		try {
			//File f = new File("C:/MacroKeypad/AppProfiles/" + profiles.get(profileId).getProfileName() + ".xml");
			//Creating XML
			profiles.get(currentProfile).saveProfile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createNewProfile () {
		
		
		String profileName = "";
		int counter = 1;
		boolean found = false;
		
		File folder = new File (Profile.PROFILE_PATH);
		File[] files = folder.listFiles();
		
		
		do {
			profileName = "Profile" + String.format("%03d", counter);
			
			int i;
			for (i = 0; i < files.length; i++) {
				if (files[i].getName().equals(profileName + ".xml")) {
					break;
				}
			}
			if (i >= files.length) {
				found = true;
				System.out.println("New file name: " + profileName);
			}
			else {
				counter++;
			}
			
		}
		while (!found);
	
		
		profiles.add(new Profile(profileName, (int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random())));
		
		currentProfile = profiles.size() - 1;
		saveChanges(profiles.size() - 1);
		
		sortProfiles();
		
		setCurrentProfile(profileName);
		
		
		//app.updateProfileView();
	}
	
	public void deleteCurrentProfile () {
		if (profiles.size() > 1) {
			profiles.get(currentProfile).deleteProfile();
			profiles.remove(currentProfile);
			
			//currentProfile = 
			System.out.println("Deleting profile " + currentProfile);
			
			setCurrentProfile(currentProfile < profiles.size()? currentProfile: currentProfile - 1);
			//app.updateProfileView();
		}
	}
	
	
	
	public void setCurrentProfile (int currentProfile) {
		this.currentProfile = currentProfile;
		currentKeys.clear();
		currentKeys.addAll(profiles.get(currentProfile).getKeys());
		
		System.out.println("Changing profile to: " + profiles.get(currentProfile).getProfileName());
		
		
		app.updateProfileView();
		app.changeColor(profiles.get(currentProfile).getR(), profiles.get(currentProfile).getG(), profiles.get(currentProfile).getB());
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
				//currentProfile = i;
				setCurrentProfile(i);
				
				return;
			}
		}
		
		//app.updateProfileView();
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
	public Profile getProfile() { return profiles.get(currentProfile); }

}
