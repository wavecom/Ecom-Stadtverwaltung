package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ecomMySQLlizenzinfo implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLlizenzinfo(ecomMySQL plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
        if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }
        
        Player player = (Player) sender;
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
		} catch (SQLException e1) {
			sender.sendMessage("Datenbank Fehler: Konnte keine Verbindung herstellen!");
			e1.printStackTrace();
			return true;
		}
		
		if(command.getLabel().equals("lizenzinfo")) {

    		ecomMySQL x = new ecomMySQL();
    		if (!x.checkUser(player.getName()) == true){
    			sender.sendMessage("Du hast keinen Job");
    			return true;
    		} 
    		
			String job = "";
			String date = "";
			Date date1;
			
			try {
				Statement stmt = conn.createStatement();                 
				ResultSet rs = stmt.executeQuery("Select lizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
				if (rs.next()){
					job = rs.getString(1);
				}
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler: Dein Job konnte nicht ermittelt werden.");
				e.printStackTrace();
				return true;
			}
			if (job ==""){
				sender.sendMessage("Du hast keinen Job");
				return true;
			}
			
			try {
				Statement stmt = conn.createStatement();                 
				ResultSet rs = stmt.executeQuery("Select lizenz_date from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
				if (rs.next()){
					date = rs.getString(1);
				}
				if(date.equals("")){
					sender.sendMessage("Du hast keinen Job!");
					return true;
				}
				SimpleDateFormat sdfToDate = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
				date1 = sdfToDate.parse(date);
				if(date1.equals("")){
					sender.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
					return true;
				}
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler: Lizenz Datum konnte nicht ermittelt werden!.");
				e.printStackTrace();
				return true;
			} catch (ParseException e) {
				sender.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
				e.printStackTrace();
				return true;
			}
			
			sender.sendMessage(ChatColor.YELLOW + "==========Lizenz==========");
			sender.sendMessage(ChatColor.YELLOW + "Job: " + job);
			sender.sendMessage(ChatColor.YELLOW + "Angenommen am: " + date);
			if (job == "schmelzer"){
				sender.sendMessage(ChatColor.YELLOW + "Als Schmelzer kannst du Erze schmelzen.");
			}else if (job == "schmied"){
				sender.sendMessage(ChatColor.YELLOW + "Als Schmied kannst du Werzeuge und Waffen craften");
				sender.sendMessage(ChatColor.YELLOW + "und den Amboss benutzen.");
			}else if (job == "jäger"){
				sender.sendMessage(ChatColor.YELLOW + "Als Jäger kannst du Tiere töten.");
			}else if (job == "holzfäller"){
				sender.sendMessage(ChatColor.YELLOW + "Als Holzfäller kannst du Bäume fällen.");
			}else if (job == "gräber"){
				sender.sendMessage(ChatColor.YELLOW + "Als Gräber kannst du Sand, Erde und Kies in der Grube abbauen.");
			}else if (job == "miner"){
				sender.sendMessage(ChatColor.YELLOW + "Als Miner kannst du in der Mine abbauen gehen.");
			}else if (job == "händler"){
				sender.sendMessage(ChatColor.YELLOW + "Als Händler darfst du Chest Shops aufstellen.");
			}
			sender.sendMessage(ChatColor.YELLOW + "==========================");
    		
		}
		return true;
	}
}
