package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ecomMySQLstadtwahl implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLstadtwahl(ecomMySQL plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }

        Player player = (Player) sender;
        
		if(command.getLabel().equals("stadtwahl")) {
        	
        	if(ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("newbie")){
        		if (args.length < 1) {
                	sender.sendMessage("===== Stadtwahl =====");
                	sender.sendMessage("Raquilem: /stadtwahl raquilem");
                	sender.sendMessage("Armedanien: /stadtwahl armedanien");
                	sender.sendMessage("Karafilem: /stadtwahl karafiliem");
                	return true;
                	
                } else if (args.length > 1) {
                	sender.sendMessage("===== Stadtwahl =====");
                	sender.sendMessage("Raquilem: /stadtwahl raquilem");
                	sender.sendMessage("Armedanien: /stadtwahl armedanien");
                	sender.sendMessage("Karafilem: /stadtwahl karafiliem");
                	return true;
                	
                } else if (args[0].equalsIgnoreCase("raquilem")){
                	ecomMySQL.perms.playerAddGroup(player, "raquilemer");
                	ecomMySQL.perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Raquilem an!");
                	
					try {
						Connection conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`, `lizenz`, `lizenz_date`, `viplizenz`, `viplizenz_date`) VALUES ('"+player.getName()+"', '1', '', '', '', '');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
		                
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
		        	return true;
                	
                } else if (args[0].equalsIgnoreCase("armedanien")){
                	ecomMySQL.perms.playerAddGroup(player, "armedaner");
                	ecomMySQL.perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Armedanien an!");
                	
					try {
						Connection conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`, `lizenz`, `lizenz_date`, `viplizenz`, `viplizenz_date`) VALUES ('"+player.getName()+"', '2', '', '', '', '');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
		        	return true;
                	
                } else if (args[0].equalsIgnoreCase("karafiliem")){
                	ecomMySQL.perms.playerAddGroup(player, "karafiliemer");
                	ecomMySQL.perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Karafiliem an!");
                	
					try {
						Connection conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+ecomMySQL.user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`, `lizenz`, `lizenz_date`, `viplizenz`, `viplizenz_date`) VALUES ('"+player.getName()+"', '3', '', '', '', '');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
		        	return true;
                	
                } else {
            		sender.sendMessage("Diese Stadt gibt es nicht!");
                	return true;
                }
        	} else {
        		sender.sendMessage("Du darfst keine Stadt mehr wählen!");
            	return true;
        	}
		}
		return false;
	}
}
