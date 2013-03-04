package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class ecomMySQLcanceljob implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLcanceljob(ecomMySQL plugin) {
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
    	Date dt = new Date();
    	
        
		if(command.getLabel().equals("canceljob")) {

    		ecomMySQL x = new ecomMySQL();
    		if (!x.checkUser(player.getName()) == true){
    			sender.sendMessage("Du hast keinen Job");
    			return true;
    		} 
    		
			if(args.length > 0){
    			sender.sendMessage("Error: Zu viele Argumente!");
    			return true;
			}
			String date = "";
			
			try {
				conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
				Statement stmt = conn.createStatement();                 
				ResultSet rs = stmt.executeQuery("Select lizenz_date from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
				
				if (rs.next()){
					date = rs.getString(1);
				}
				if(date.equals("")){
					sender.sendMessage("Du hast keinen Job!");
					return true;
				}
				
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler: Lizenz Datum konnte nicht ermittelt werden!.");
				e.printStackTrace();
				return true;
			}
			
			Date date1;
			
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
				date1 = sdfToDate.parse(date);
				
				if(date1.equals("")){
					sender.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
					return true;
				}
			
			} catch (ParseException e) {
				e.printStackTrace();
				sender.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
				return true;
			}
			
			long lizenzdate = date1.getTime();
			long heute = dt.getTime();
			
			long differenz = heute - lizenzdate;
			long vierWochen = 2419200000L;
			
			if (differenz < vierWochen){ //4 Wochen 
				sender.sendMessage("Du musst deinen Job 4 Wochen behalten!");
				return true;
			}
			
//Job auswerten	
			if (!date1.equals("") && !(differenz < vierWochen)){
				String job = "";
				try {
					conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
					Statement stmt = conn.createStatement();                 
					ResultSet rs = stmt.executeQuery("Select lizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
					if (rs.next()){
						job = rs.getString(1);
					}
				} catch (SQLException e) {
					sender.sendMessage("Datenbank Fehler: Dein Job konnte nicht ermittelt werden.");
					e.printStackTrace();
				}
				
				if (job.equalsIgnoreCase("")){
					sender.sendMessage("Du hast keinen Job!");
				
				} else if (x.minusJobLimit(job, sender) == false){
					sender.sendMessage("Fehler beim Joblimit");
					return true;
				}
//Gräber	
				if (job.equalsIgnoreCase("gräber")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTast.cancel.g");
						sender.sendMessage(ChatColor.YELLOW + "Sicherheitskontrolle: Bitte gib /cancelgräber ein um den Job zu kündigen!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
//Holzfäller					
				} else if (job.equalsIgnoreCase("holzfäller")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTast.cancel.h");
						sender.sendMessage(ChatColor.YELLOW + "Sicherheitskontrolle: Bitte gib /cancelholzfäller ein um den Job zu kündigen!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
//Miner					
				} else if (job.equalsIgnoreCase("miner")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTast.cancel.m");
						sender.sendMessage(ChatColor.YELLOW + "Sicherheitskontrolle: Bitte gib /cancelminer ein um den Job zu kündigen!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
//Jäger			
				} else if (job.equalsIgnoreCase("jäger")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerRemove(player, "modifyworld.damage.deal.animal.*");
						ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.1");
						ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.2");
						sender.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
//Schmelzer				
				} else if (job.equalsIgnoreCase("schmelzer")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.ironore.of.furnace");
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.goldore.of.furnace");
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironore.of.furnace");
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldore.of.furnace");
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironingot.of.furnace");
						ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldingot.of.furnace");
						ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.1");
						ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.2");
						sender.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
//Schmied					
				} else if (job.equalsIgnoreCase("schmied")){
					try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz` =  '',`lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.1");
						ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.2");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.ironsword");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.stonesword");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.diamondsword");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.woodsword");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.goldsword");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.diamondpickaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.ironpickaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.goldpickaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.stonepickaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.woodpickaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.diamondspade");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.ironspade");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.goldspade");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.stonespade");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.woodspade");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.diamondaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.ironaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.goldaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.stoneaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.woodaxe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.fishingrod");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.diamondhoe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.ironhoe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.goldhoe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.stonehoe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.woodhoe");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.flintandsteel");
    					ecomMySQL.perms.playerRemove(player, "modifyworld.items.craft.bow");
						sender.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
					
				} else {
					sender.sendMessage("Datenbankfehler: Dein Job ist uns nicht bekannt:");
					sender.sendMessage(job);
				}
			} else {
				sender.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
			}
			return true;
		}
		return false;
	}

}
