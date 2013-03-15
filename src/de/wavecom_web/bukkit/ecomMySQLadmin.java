package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ecomMySQLadmin implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLadmin(ecomMySQL plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }

        Player player = (Player) sender;
        
        Connection conn = null;
    	try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
		} catch (SQLException e1) {
			sender.sendMessage("Datenbankfehler");
			e1.printStackTrace();
			return true;
		}
        
    	if(command.getLabel().equals("startlogo")) {
    		ecomMySQL x = new ecomMySQL();
    		x.ecomLogo();
    		
    	}else if(command.getLabel().equals("editstadt")) {
        	if (ecomMySQL.perms.playerInGroup(player, "admin") | ecomMySQL.perms.playerInGroup(player, "owner")){
        		if(args.length < 2) {
    				sender.sendMessage(ChatColor.GOLD + "Benutzung: /editstadt <Spieler> <Stadt> - verändert die Stadt des Spielers. ");
    				return true;
    			} else if(args.length > 2){
    				sender.sendMessage("Error: Zu viele Argumente!");
    				return true;
    			}
        		
        		ecomMySQL x = new ecomMySQL();
        		
        		if (!x.checkUser(args[0]) == true){
        			sender.sendMessage("Spieler hat keine Stadt!");
        			return true;
        		} 
        		
        		if (args[1].equalsIgnoreCase("raquilem") | args[0].equalsIgnoreCase("r") | args[1].equalsIgnoreCase("raquilemer")){
        			
        			try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `stadt` =  '1' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+args[0]+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						sender.sendMessage(ChatColor.YELLOW + "Der Spieler "+args[0]+" wurde in die Stadt "+args[1]+" versetzt!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
        			
        		} else if (args[1].equalsIgnoreCase("armedanien") | args[1].equalsIgnoreCase("a") | args[1].equalsIgnoreCase("armedanier") | args[1].equalsIgnoreCase("armedaner")){

        			try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `stadt` =  '2' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+args[0]+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						sender.sendMessage(ChatColor.YELLOW + "Der Spieler "+args[0]+" wurde in die Stadt "+args[1]+" versetzt!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
        		} else if (args[1].equalsIgnoreCase("karafiliem") | args[1].equalsIgnoreCase("k") | args[1].equalsIgnoreCase("karafiliemer")){

        			try {
						Connection conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `stadt` =  '3' WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+args[0]+"';");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
						sender.sendMessage(ChatColor.YELLOW + "Der Spieler "+args[0]+" wurde in die Stadt "+args[1]+" versetzt!");
						
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
        		} else {
        			sender.sendMessage("Stadt gibt es nicht!");
        		}
        		
        		return true;
    			
    		}
        }else if(command.getLabel().equals("deljob")) {
        	if (ecomMySQL.perms.playerInGroup(player, "admin") | ecomMySQL.perms.playerInGroup(player, "owner")){
        		if(args.length < 1) {
    				sender.sendMessage(ChatColor.GOLD + "Benutzung: /deljob <Spieler> - verändert die Stadt des Spielers. ");
    				return true;
    			} else if(args.length > 1){
    				sender.sendMessage(ChatColor.GOLD + "Benutzung: /deljob <Spieler> - verändert die Stadt des Spielers. ");
    				return true;
    			}
        		
        		ecomMySQL x = new ecomMySQL();
        		
        		if (!x.checkUser(args[0]) == true){
        			sender.sendMessage("Spieler hat keine Stadt!");
        			return true;
        		} 
        		
        		Connection conn1;
				try {
					conn1 = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
					PreparedStatement sampleQueryStatement;
					sampleQueryStatement = conn1.prepareStatement("UPDATE  `"+ecomMySQL.user+"`.`stadtverwaltung_spieler`  SET  `lizenz_date` =  'Fri Mar 15 15:44:27 CET 2011'  WHERE  `stadtverwaltung_spieler`.`Spieler` =  '"+args[0]+"';");
					sampleQueryStatement.executeUpdate();
					sampleQueryStatement.close(); 
					sender.sendMessage(ChatColor.YELLOW + "Der Spieler "+args[0]+" kann jetzt mit /canceljob seinen Job beenden.");
					
				} catch (SQLException e) {
					sender.sendMessage("Datenbank Fehler");
					e.printStackTrace();
				}
        		return true;
    			
    		}
        }
        
		return true;
	}

}
