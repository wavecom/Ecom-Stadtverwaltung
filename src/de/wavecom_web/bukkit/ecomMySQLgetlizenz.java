package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    	Date dt = new Date();
    	Connection conn = null;
    	try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
		} catch (SQLException e1) {
			sender.sendMessage("Datenbankfehler");
			e1.printStackTrace();
			return true;
		}

    	if(command.getLabel().equals("getlizenz")) {
    		
    		ecomMySQL x = new ecomMySQL();
    		if (!x.checkUser(player.getName()) == true){
    			return true;
    		} 
    		
    		if(args.length < 1) {
    			sender.sendMessage("Error: Kein Job angegeben!");
    			return true;
    			
    		} else if(args.length > 1){
    			sender.sendMessage("Error: Zu viele Argumente!");
    			return true;
    		
    		} else if(ecomMySQL.perms.has(player, "wavecom.lizenz.1")){ //Keine Lizens + 1
    			ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.inTask.g");
    			ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.inTask.m");
    			ecomMySQL.perms.playerRemove(player, "wavecom.lizenz.inTask.h");
    //Lizenzgebung
    //Schmelzer		
    			if (args[0].equalsIgnoreCase("schmelzer")){
    				
    				if (x.checkJobLimit("Schmelzer", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
    				
    				try {
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.put.ironore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.put.goldore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.ironore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.goldore.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.ironingot.of.furnace");
						ecomMySQL.perms.playerAdd(player, "modifyworld.items.take.goldingot.of.furnace");
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage(ChatColor.YELLOW + "================================");
    				sender.sendMessage(ChatColor.YELLOW + "Du bist nun Schmelzer!");
    				sender.sendMessage(ChatColor.YELLOW + "Als Schmelzer kannst du Erze schmelzen.");
    				sender.sendMessage(ChatColor.YELLOW + "================================");
	//Gräber			
    			} else if (args[0].equalsIgnoreCase("gräber")){
    				
    				if (x.checkJobLimit("Gräber", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
    				try {
    					PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTask.g");
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage("Sicherheitskontrolle:");
    				sender.sendMessage("Gib bitte /gräberlizenz ein, um die Lizenz zu bestätigen!!!");
	//Miner			
    			} else if (args[0].equalsIgnoreCase("miner")){
    				
    				if (x.checkJobLimit("Miner", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
        			
    				try {
    					PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTask.m");
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage("Sicherheitskontrolle:");
    				sender.sendMessage("Gib bitte /minerlizenz ein, um die Lizenz zu bestätigen!!!");
	//Holzfäller			
    			} else if (args[0].equalsIgnoreCase("holzfäller")){
    				
    				if (x.checkJobLimit("Holzfäller", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
    				try {
    					PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.inTask.h");
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage("Sicherheitskontrolle:");
    				sender.sendMessage("Gib bitte /holzfällerlizenz ein, um die Lizenz zu bestätigen!!!");
	//Jäger			
    			} else if (args[0].equalsIgnoreCase("jäger")){
    				
    				if (x.checkJobLimit("Jäger", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
    				try {
    					PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						ecomMySQL.perms.playerAdd(player, "modifyworld.damage.deal.animal.*");
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage(ChatColor.YELLOW + "================================");
    				sender.sendMessage(ChatColor.YELLOW + "Du bist nun Jäger!");
    				sender.sendMessage(ChatColor.YELLOW + "Als Jäger kannst du Tiere töten.");
    				sender.sendMessage(ChatColor.YELLOW + "================================");
     //Schmied   			
    			} else if (args[0].equalsIgnoreCase("schmied")){
    				
    				if (x.checkJobLimit("Schmied", sender) == false){
    					sender.sendMessage("Das maximale Joblimit für diesen Job ist erreicht!");
    					return true;
    				}
    				
        			ecomMySQL.perms.playerAdd(player, "wavecom.lizenz.2");
        			ecomMySQL.perms.playerAdd(player, "-wavecom.lizenz.1");
    				try {
    					PreparedStatement sampleQueryStatement;
    					sampleQueryStatement = conn.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` SET  `lizenz` =  '"+args[0]+"',`lizenz_date` =  '"+dt+"' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+player.getName()+"' LIMIT 1 ;");
    					sampleQueryStatement.executeUpdate();
    					sampleQueryStatement.close(); 

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
				    
    				} catch (SQLException e) {
    					sender.sendMessage("Datenbank Fehler");
    					e.printStackTrace();
    				}
    				sender.sendMessage(ChatColor.YELLOW + "================================");
    				sender.sendMessage(ChatColor.YELLOW + "Du bist nun Schmied!");
    				sender.sendMessage(ChatColor.YELLOW + "Als Schmied kannst du Werkzeuge herstellen.");
    				sender.sendMessage(ChatColor.YELLOW + "================================");
    			}
    			return true;
    //Ende Lizenzgebung
    		
    		} else if (ecomMySQL.perms.has(player, "wavecom.lizenz.2")){ // eine Lizens + 1 wenn VIP
    			if (ecomMySQL.perms.has(player, "wavecom.vip")){
    //Lizenzgebung 2te Lizens VIP
    			
    //Ende Lizenzgebung 2te Lizens VIP
    			
    			} else {
    				sender.sendMessage("Du darfst keine weiteren Lizenzen mehr erwerben!");
    			}
    			return true;
    		} else if (ecomMySQL.perms.has(player, "wavecom.lizenz.3")){ // 2 oder mehr = zu viel
    			sender.sendMessage("Du darfst keine weiteren Lizenzen mehr erwerben!");
    			return true;
    		} else {
    			sender.sendMessage("Error: Es gab ein Fehler bei deinem Lizenzzähler");
    			return true;
    		}
    	}
    	return false;
	}
}
