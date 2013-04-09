package de.wavecom_web.bukkit;

import java.io.BufferedReader; 
import java.io.InputStreamReader;
import java.net.URL; 
import java.net.URLConnection;

public class AntiTheft{
    public static boolean antitheft() {
    	
        try {
            URL game = new URL("http://wavecom-web.de/ecom/theft.txt");
            URLConnection connection = game.openConnection();
            BufferedReader in = new BufferedReader(new
            InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	if (inputLine.equalsIgnoreCase("true")){
            		return true;
            	}
            }
    		System.out.println("[EcomStadtverwaltung] Ecom Stadtverwaltung aufgrund einer Copyrightverletzung ausgeschaltet!");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
    		return false;
        }
		return false;
    }
}

