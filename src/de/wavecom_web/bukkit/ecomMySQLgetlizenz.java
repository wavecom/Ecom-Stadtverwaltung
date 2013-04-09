package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ecomMySQLgetlizenz implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLgetlizenz(ecomMySQL plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        
		if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }
        Player player = (Player) sender;

    	if(command.getLabel().equals("getlizenz")) {
    		
    		if (!plugin.checkUser(player.getName()) == true){
    			sender.sendMessage("Bitte wähle erst eine Stadt aus!");
    			return true;
    		} 
    		
    		if(args.length < 1) {
    			sender.sendMessage("==================");
    			sender.sendMessage(ChatColor.GOLD + "Jobliste:");
    			sender.sendMessage(ChatColor.GOLD + "Gräber, Holzfäller,");
    			sender.sendMessage(ChatColor.GOLD + "Miner, Jäger, Schmied, Händler");
    			sender.sendMessage(ChatColor.RED + "Verwendung: /getlizenz [JOB]");
    			sender.sendMessage(ChatColor.RED + "Pro Stadt und Job nur 4 Personen!");
    			return true;
    			
    		} else if(args.length > 1){
    			sender.sendMessage("Error: Zu viele Argumente!");
    			return true;
    		
    		} 
    //Gräber
    			if (args[0].equalsIgnoreCase("gräber")){
    				
    				if (plugin.checkJobLimit("Gräber", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				if (updateJob(player, "gräber") == true) {
    					plugin.setOwner(player, "grube1");
    					plugin.setOwner(player, "grube2");
    					plugin.setOwner(player, "grube3");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    					sender.sendMessage(ChatColor.YELLOW + "Du bist nun Gräber!");
    					sender.sendMessage(ChatColor.YELLOW + "In einer Grube kannst du Sand, Kies und Erde abbaun.");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
	//Miner			
    			} else if (args[0].equalsIgnoreCase("miner")){
    				
    				if (plugin.checkJobLimit("Miner", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				if (updateJob(player, "miner") == true) {
    					plugin.setOwner(player, "mine1");
    					plugin.setOwner(player, "mine2");
    					plugin.setOwner(player, "mine3");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    					sender.sendMessage(ChatColor.YELLOW + "Du bist nun Miner!");
    					sender.sendMessage(ChatColor.YELLOW + "In einer Mine kannst du Erze abbaun.");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
	//Holzfäller			
    			} else if (args[0].equalsIgnoreCase("holzfäller")){
    				
    				if (plugin.checkJobLimit("Holzfäller", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}

    				if (updateJob(player, "holzfäller") == true) {
    					plugin.setOwner(player, "wald1");
    					plugin.setOwner(player, "wald2");
    					plugin.setOwner(player, "wald3");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    					sender.sendMessage(ChatColor.YELLOW + "Du bist nun Holzfäller!");
    					sender.sendMessage(ChatColor.YELLOW + "In einem Wald kannst du Bäume fällen.");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
	//Jäger			
    			} else if (args[0].equalsIgnoreCase("jäger")){
    				
    				if (plugin.checkJobLimit("Jäger", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				if (updateJob(player, "jäger") == true) {
						ecomMySQL.perms.playerAdd(player, "modifyworld.damage.deal.animal.*");
						ecomMySQL.perms.playerAdd(player, "animalprotect.bypass");
						sender.sendMessage(ChatColor.YELLOW + "================================");
						sender.sendMessage(ChatColor.YELLOW + "Du bist nun Jäger!");
    					sender.sendMessage(ChatColor.YELLOW + "Als Jäger kannst du Tiere töten.");
    					sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
	//Händler		
    			} else if (args[0].equalsIgnoreCase("händler")){
    				
    				if (plugin.checkJobLimit("händler", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				if (updateJob(player, "händler") == true) {
						ecomMySQL.perms.playerAdd(player, "ChestShop.shop.create.*");
						sender.sendMessage(ChatColor.YELLOW + "================================");
						sender.sendMessage(ChatColor.YELLOW + "Du bist nun Händler!");
						sender.sendMessage(ChatColor.YELLOW + "Du kannst nun ChestShops aufstellen.");
						sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
     //Schmied   			
    			} else if (args[0].equalsIgnoreCase("schmied")){
    				
    				if (plugin.checkJobLimit("Schmied", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}

    				if (updateJob(player, "schmied") == true) {
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.ironsword");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.stonesword");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.diamondsword");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.woodsword");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.goldsword");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.diamondpickaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.ironpickaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.goldpickaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.stonepickaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.woodpickaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.diamondspade");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.ironspade");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.goldspade");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.stonespade");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.woodspade");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.diamondaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.ironaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.goldaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.stoneaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.woodaxe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.fishingrod");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.diamondhoe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.ironhoe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.goldhoe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.stonehoe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.woodhoe");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.flintandsteel");
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.craft.bow");
    					
    					ecomMySQL.perms.playerAdd(player, "modifyworld.items.put.*.of.anvil");
    	    			ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.*.of.anvil");
    	    	    	ecomMySQL.perms.playerAdd(player, "modifyworld.blocks.interact.anvil:*");
    	    	    	ecomMySQL.perms.playerAdd(player, "modifyworld.blocks.interact.anvil");
    	    	    	
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.put.ironore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.put.goldore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.ironore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.goldore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.ironingot.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.goldingot.of.furnace");
						
						sender.sendMessage(ChatColor.YELLOW + "================================");
						sender.sendMessage(ChatColor.YELLOW + "Du bist nun Schmied!");
						sender.sendMessage(ChatColor.YELLOW + "Als Schmied kannst du Werkzeuge herstellen");
						sender.sendMessage(ChatColor.YELLOW + "und Erze einschmelzen.");
						sender.sendMessage(ChatColor.YELLOW + "================================");
    				}
    			} //Close Jobcheck
    			return true; //for Getlizenz
    		} //Close Command Getlizent
    	return false;
	}
	
	public boolean updateJob (Player player, String job){
    	Connection conn = null;
    	boolean lizfrei = false;
    	boolean viplizfrei = false;
    	String columvip = "";
    	Date dt = new Date();
    	
    	try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
		} catch (SQLException e1) {
			player.sendMessage("Datenbankfehler");
			e1.printStackTrace();
			return true;
		}
    	
		try {
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("Select lizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
			
			if (rs.next()){
				 if (rs.getString(1).equalsIgnoreCase("")){
					 lizfrei = true;
				 } else if (ecomMySQL.perms.playerHas(player, "wavecom.vip")){
					 ResultSet rs2 = stmt.executeQuery("Select viplizenz from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
					 if (rs2.next()){
						 if (rs2.getString(1).equalsIgnoreCase("")){
							 viplizfrei = true;
						 } 
					 } 
				 } else {
					 return false;
				 }
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			player.sendMessage("SQL Exception");
			return false;
		}
		
		if (lizfrei = true){
			columvip = "";
		} else if (viplizfrei = true){
			columvip = "vip";
		} else { 
			return false;
		}
    	
		try {
			PreparedStatement sampleQueryStatement;
			sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `"+columvip+"lizenz` =  '"+job+"',`"+columvip+"lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
			sampleQueryStatement.executeUpdate();
			sampleQueryStatement.close(); 
			return true;
		} catch (SQLException e) {
			player.sendMessage(ChatColor.RED + "Datenbank Fehler");
			e.printStackTrace();
			return false;
		}
		
	}
}
