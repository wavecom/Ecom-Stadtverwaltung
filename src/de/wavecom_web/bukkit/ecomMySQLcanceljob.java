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
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
		} catch (SQLException e1) {
			e1.printStackTrace();
			sender.sendMessage("Datenbankverbindung fehlgeschlagen");
			return true;
		}
        
		if(command.getLabel().equals("canceljob")) {

    		if (!plugin.checkUser(player.getName()) == true){
    			sender.sendMessage("Du hast keinen Job");
    			return true;
    		} 
    		
			if(args.length > 1){
    			sender.sendMessage("Error: Zu viele Argumente!");
    			return true;
			}

			String hasvip = "";
			boolean vip = false;
			
			
			try {
				Statement stmt = conn.createStatement();                 
				ResultSet rs = stmt.executeQuery("Select viplizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
				
				if (rs.next()){
					hasvip = rs.getString(1);
				}
				
				if(!hasvip.equals("")){
					vip = true;
				}
				
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler: VIP Check konnte nicht durchgeführt werden!");
				e.printStackTrace();
				return true;
			}
			
			if (vip == true && args.length < 1){
				sender.sendMessage(ChatColor.YELLOW + "Gib /canceljob vip ein um deine VIP Lizenz zu kündigen!");
				sender.sendMessage(ChatColor.YELLOW + "Gib /canceljob normal ein um deine standart Lizenz zu kündigen!");
			} else if (vip == true && args[0].equalsIgnoreCase("vip")){
				cancelquerry(player, "vip");
			} else if (vip == true && args[0].equalsIgnoreCase("normal")){
				cancelquerry(player, "");
			} else if (vip == false && args.length < 1){
				cancelquerry(player, "");
			} else if (vip == false && args[0].equalsIgnoreCase("vip")){
				player.sendMessage(ChatColor.YELLOW + "Du bist kein VIP!");
			} else if (vip == false && args[0].equalsIgnoreCase("normal")){
				cancelquerry(player, "");
			}
			return true;
		}
		return false;
	}
	
	public boolean cancelquerry(Player player, String isvip){
        
			Connection conn;
	    	Date dt = new Date(); //Datum heute
			String date = "";     //Lizenzdatum
			Date date1; //Lizenzdatum formatiert
			
//Datenbank Verbindung		
			try {
				conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			} catch (SQLException e1) {
				e1.printStackTrace();
				player.sendMessage("Datenbankverbindung fehlgeschlagen");
				return false;
			}
			
//DB Datum			
			try {
				Statement stmt = conn.createStatement();             
				ResultSet rs = stmt.executeQuery("Select "+isvip+"lizenz_date from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
				
				if (rs.next()){
					date = rs.getString(1);
				}
				if(date.equals("")){
					player.sendMessage("Du hast keinen Job!");
					return false;
				}
				
			} catch (SQLException e) {
				player.sendMessage("Datenbank Fehler: Lizenz Datum konnte nicht ermittelt werden!.");
				e.printStackTrace();
				return false;
			}

//Datum konvertieren
			
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
				date1 = sdfToDate.parse(date);
				
				if(date1.equals("")){
					player.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
					return false;
				}
			
			} catch (ParseException e) {
				e.printStackTrace();
				player.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
				return false;
			}

//Time Check			
			long lizenzdate = date1.getTime();
			long heute = dt.getTime();
			
			long differenz = heute - lizenzdate;
			long vierWochen = 2419200000L;
			
			if (differenz < vierWochen){ //4 Wochen 
				player.sendMessage(ChatColor.YELLOW +"Du musst deinen Job 4 Wochen behalten!");
				return false;
			}
			
//Job auswerten	
			if (!date1.equals("") && !(differenz < vierWochen)){
				String job = "";
				try {
					Statement stmt = conn.createStatement();                 
					ResultSet rs = stmt.executeQuery("Select "+isvip+"lizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
					if (rs.next()){
						job = rs.getString(1);
					}
				} catch (SQLException e) {
					player.sendMessage("Datenbank Fehler: Dein Job konnte nicht ermittelt werden.");
					e.printStackTrace();
					return false;
				}
				
				if (job.equalsIgnoreCase("")){
					player.sendMessage("Du hast keinen Job!");
					return false;
				
				} else if (plugin.minusJobLimit(job, player) == false){
					player.sendMessage("Fehler beim Joblimit");
					return false;
				}
//Datenbank löschen				
				try {
					PreparedStatement sampleQueryStatement;
					sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `"+isvip+"lizenz` =  '',`"+isvip+"lizenz_date` =  '' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"';");
					sampleQueryStatement.executeUpdate();
					sampleQueryStatement.close(); 
						
				} catch (SQLException e) {
					player.sendMessage("Datenbankfehler!");
					e.printStackTrace();
					return false;
				}

//Gräber	
				if (job.equalsIgnoreCase("gräber")){
					plugin.delOwner(player, "grube1");
					plugin.delOwner(player, "grube2");
					plugin.delOwner(player, "grube3");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");

//Holzfäller						
				} else if (job.equalsIgnoreCase("holzfäller")){
					plugin.delOwner(player, "wald1");
					plugin.delOwner(player, "wald2");
					plugin.delOwner(player, "wald3");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");

//Miner						
				} else if (job.equalsIgnoreCase("miner")){
					plugin.delOwner(player, "mine1");
					plugin.delOwner(player, "mine2");
					plugin.delOwner(player, "mine3");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");

//Jäger				
				} else if (job.equalsIgnoreCase("jäger")){
					ecomMySQL.perms.playerRemove(player, "modifyworld.damage.deal.animal.*");
					ecomMySQL.perms.playerRemove(player, "animalprotect.bypass");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
//Händler			
				} else if (job.equalsIgnoreCase("händler")){
					ecomMySQL.perms.playerRemove(player, "ChestShop.shop.create.*");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
//Schmelzer				
				} else if (job.equalsIgnoreCase("schmelzer")){
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.ironore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.goldore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironingot.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldingot.of.furnace");
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
//Schmied					
				} else if (job.equalsIgnoreCase("schmied")){
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
    					
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.ironore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.goldore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldore.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.ironingot.of.furnace");
					ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.goldingot.of.furnace");
    					
    				ecomMySQL.perms.playerRemove(player, "modifyworld.items.put.*.of.anvil");
    	    		ecomMySQL.perms.playerRemove(player, "modifyworld.items.take.*.of.anvil");
    	    	    ecomMySQL.perms.playerRemove(player, "modifyworld.blocks.interact.anvil:*");
    	    	    ecomMySQL.perms.playerRemove(player, "modifyworld.blocks.interact.anvil");
    	    	    
					player.sendMessage(ChatColor.YELLOW + "Du hast deinen Job gekündigt!");
					
				} else {
					player.sendMessage("Datenbankfehler: Dein Job ist uns nicht bekannt:");
					player.sendMessage(job);
				}
//Security Check				
				if (ecomMySQL.perms.playerHas(player, "wavecom.lizenz.2")) {
					ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.1");
					ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.1");
					ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.2");
					ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.2");
					ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.3");
					ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.3");
				} else if (ecomMySQL.perms.playerHas(player, "wavecom.lizenz.3")) {
					ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.1");
					ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.2");
					ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.3");
					ecomMySQL.perms.playerRemove(player, "-wavecom.lizenz.3");
					ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
					ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
				} 	
			} else {
				player.sendMessage("Datenbank Fehler: Lizenz Datum falsch formatiert!");
			}
			return true;
	}

}
