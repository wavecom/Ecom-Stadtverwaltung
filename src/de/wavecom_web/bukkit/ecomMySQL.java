package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ecomMySQL extends JavaPlugin {
	
	public static String url = "jdbc:mysql://localhost:3306/";
	public static String user = "";
	public static String pass = "";
	 
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
       
        getCommand("stadtwahl").setExecutor(new ecomMySQLstadtwahl(this));
        getCommand("stadtkasse").setExecutor(new ecomMySQLstadtkasse(this));
        getCommand("getlizenz").setExecutor(new ecomMySQLgetlizenz(this));
        getCommand("canceljob").setExecutor(new ecomMySQLcanceljob(this));
        getCommand("lizenzgetrights").setExecutor(new ecomMySQLlizenzgetrights(this));
        
        log.info("==========================================================");
        log.info("Ecom Stadtverwaltung by wavecom wurde erfolgreich geladen!");
        log.info("Dieses Plugin wurde für Ecom Medieval Roleplay entwickelt!");
        log.info("Dieses Plugin ignoriert OPs");
        log.info("(C) by wavecom");
        log.info("==========================================================");
    }

    
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }    
    
    public boolean checkUser(String player) {
		String anzahl = "";
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("SELECT count(*) Stadt FROM stadtverwaltung_spieler where Spieler = '"+player+"'");
			
			if (rs.next()){
				anzahl = rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if (anzahl.equalsIgnoreCase("1")){
			return true;
		} else {
			log.warning("Spieler " +player+ "hat keinen oder mehrere Eintrage in der Stadtverwaltung Datenbank!");
			return false;
		}
    } 
    
    public boolean checkJobLimit(String job, CommandSender sender) {
    	Player player = (Player) sender;
    	
    	if (perms.playerInGroup(player, "newbie")){
    		sender.sendMessage("Du bist noch Newbie!");
    		return false;
    	}
    	
		String anzahl = "";
		String stadt = "";
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("Select Stadt from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
			
			if (rs.next()){
				stadt = rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if (stadt.equalsIgnoreCase("")){
			sender.sendMessage("Du hast keine Stadt");
			return false;
		}
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("SELECT `"+job+"` FROM `stadtverwaltung_limits` WHERE `Stadt` = "+stadt+";");
			
			if (rs.next()){
				anzahl = rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if (anzahl.equalsIgnoreCase("")){
			log.warning("Ecom Stadtverwaltung Datenbankfehler für "+job+" in "+stadt+"");
			return false;
		} 
		
		if (anzahl.equalsIgnoreCase("0")){
			try {
				PreparedStatement sampleQueryStatement;
				sampleQueryStatement = conn.prepareStatement("UPDATE  `stadtverwaltung_limits` SET `"+job+"` =  '1' WHERE  `stadtverwaltung_limits`.`Stadt` = "+stadt+";");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close(); 
				return true;
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler");
				e.printStackTrace();
				return false;
			}
		} else if (anzahl.equalsIgnoreCase("1")){
			try {
				PreparedStatement sampleQueryStatement;
				sampleQueryStatement = conn.prepareStatement("UPDATE  `stadtverwaltung_limits` SET `"+job+"` =  '2' WHERE  `stadtverwaltung_limits`.`Stadt` = "+stadt+";");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close(); 
				return true;
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler");
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
    }
    
    public boolean minusJobLimit(String job, CommandSender sender) {
    	Player player = (Player) sender;
    	
    	if (perms.playerInGroup(player, "newbie")){
    		sender.sendMessage("Du bist noch Newbie!");
    		return false;
    	}
    	
		String anzahl = "";
		String stadt = "";
		Connection conn;
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("Select Stadt from stadtverwaltung_spieler where Spieler = '"+player.getName()+"'");
			
			if (rs.next()){
				stadt = rs.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		if (stadt.equalsIgnoreCase("")){
			sender.sendMessage("Du hast keine Stadt");
			return false;
		}
		
		try {
			conn = DriverManager.getConnection(ecomMySQL.url, ecomMySQL.user, ecomMySQL.pass);
			Statement stmt = conn.createStatement();                 
			ResultSet rs = stmt.executeQuery("SELECT `"+job+"` FROM `stadtverwaltung_limits` WHERE `Stadt` = "+stadt+";");
			
			if (rs.next()){
				anzahl = rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if (anzahl.equalsIgnoreCase("")){
			log.warning("Ecom Stadtverwaltung Datenbankfehler für "+job+" in "+stadt+"");
			return false;
		} 
		
		if (anzahl.equalsIgnoreCase("0")){
			try {
				PreparedStatement sampleQueryStatement;
				sampleQueryStatement = conn.prepareStatement("UPDATE  `stadtverwaltung_limits` SET `"+job+"` =  '0' WHERE  `stadtverwaltung_limits`.`Stadt` = "+stadt+";");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close(); 
				return true;
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler");
				e.printStackTrace();
				return false;
			}
		} else if (anzahl.equalsIgnoreCase("1")){
			try {
				PreparedStatement sampleQueryStatement;
				sampleQueryStatement = conn.prepareStatement("UPDATE  `stadtverwaltung_limits` SET `"+job+"` =  '0' WHERE  `stadtverwaltung_limits`.`Stadt` = "+stadt+";");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close(); 
				return true;
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler");
				e.printStackTrace();
				return false;
			}
			
		} else if (anzahl.equalsIgnoreCase("2")){
			try {
				PreparedStatement sampleQueryStatement;
				sampleQueryStatement = conn.prepareStatement("UPDATE  `stadtverwaltung_limits` SET `"+job+"` =  '1' WHERE  `stadtverwaltung_limits`.`Stadt` = "+stadt+";");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close(); 
				return true;
			} catch (SQLException e) {
				sender.sendMessage("Datenbank Fehler");
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
    }
    
    //    public int tutorial (int stadt, Entity player){
    //   	player.teleport(new Location(Bukkit.getWorld("ecomMedieval"), 0, 64, 0));
    //  	player.getLocation().setPitch(90F);
    //  	player.getLocation().setYaw(90F);
    //   	player.setPassenger(player);
    //  	try {
    //		Thread.sleep(5000);
    //		} catch (InterruptedException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //    	player.leaveVehicle();
    //		return 0;
    //    }
    
}       