package application;

import java.awt.im.InputContext;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class MainWindowController implements Initializable  {
	
	@ FXML Button b1;
	@ FXML Button b2;
	@ FXML Button b3;
	@ FXML Button b4;
	@ FXML Button b5;
	
	String s1;
	String s2;
	String s3;
	String s4;
	String s5;
	
	@ Override 
	public void initialize(URL arg0, ResourceBundle arg1) {
		InputContext context = InputContext.getInstance();
	    String lang = context.getLocale().toString();
	    //System.out.println(lang);
	    if(lang.equals("en_US")) {
	    	s1 = "File";
	    	s2 = "Edit";
	    	s3 = "Window";
	    	s4 = "Do";
	    	s5 = "Help";
	    }
	    
	    else if(lang.equals("iw_IL")) {
	    	s1 = "קובץ";
	    	s2 = "ערוך";
	    	s3 = "חלון";
	    	s4 = "עשה";
	    	s5 = "עזרה";
	    }
	    
	    else if(lang.equals("frc_001_#Latn")) {
	    	s1 = "Fichier";
	    	s2 = "Éditer";
	    	s3 = "La fenêtre";
	    	s4 = "Faire";
	    	s5 = "Aidez-moi";
	    }
	    
	    else {
	    	s1 = "File";
	    	s2 = "Edit";
	    	s3 = "Window";
	    	s4 = "Do";
	    	s5 = "Help";
	    }
	    
	    b1.setText(s1);
	    b2.setText(s2);
	    b3.setText(s3);
	    b4.setText(s4);
	    b5.setText(s5);
	}    
}
