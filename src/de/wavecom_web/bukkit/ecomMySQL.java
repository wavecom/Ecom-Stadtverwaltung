package de.wavecom_web.bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


@SuppressWarnings("deprecation")
public final class ecomMySQL extends JavaPlugin implements Listener {
	
	List<Player> players = new ArrayList<Player>();
	
	public static String url = "jdbc:mysql://localhost:3306/";
	public static String user = "";
	public static String pass = "";
	 
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;

	public static Server server;
	public static ArrayList<LivingEntity> spawned = new ArrayList<LivingEntity>();

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
        getWorldGuard();
        
        getServer().getPluginManager().registerEvents(this, this);
       
        getCommand("stadtwahl").setExecutor(new ecomMySQLstadtwahl(this));
        getCommand("stadtkasse").setExecutor(new ecomMySQLstadtkasse(this));
        getCommand("getlizenz").setExecutor(new ecomMySQLgetlizenz(this));
        getCommand("canceljob").setExecutor(new ecomMySQLcanceljob(this));
        getCommand("lizenzgetrights").setExecutor(new ecomMySQLlizenzgetrights(this));
        getCommand("lizenzgetrights").setExecutor(new ecomMySQLlizenzgetrights(this));
        getCommand("editstadt").setExecutor(new ecomMySQLadmin(this));
        getCommand("deljob").setExecutor(new ecomMySQLadmin(this));
        getCommand("startlogo").setExecutor(new ecomMySQLadmin(this));
        log.info("==========================================================");
        log.info("Ecom Stadtverwaltung by wavecom wurde erfolgreich geladen!");
        log.info("Dieses Plugin wurde für Ecom Medieval Roleplay entwickelt!");
        log.info("Dieses Plugin ignoriert OPs");
        log.info("Hooked into Worldguard");
        log.info("Hooked into Vault");
        log.info("(C) by wavecom");
        log.info("==========================================================");
    }

    private WorldGuardPlugin getWorldGuard() {
    	if (getServer().getPluginManager().getPlugin("WorldGuard")!=null){
    		WorldGuardPlugin wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
    		return wg;
    	 }
    	return null;
    }
    

    public boolean setOwner(Player player, String regionName) {
    	WorldGuardPlugin worldGuard = getWorldGuard();
    	World world =  Bukkit.getServer().getWorld("ecomMedieval");
    	
        DefaultDomain domain = new DefaultDomain();
        domain.addPlayer(player.getName());
    	
    	RegionManager rm = worldGuard.getRegionManager(world);
    	ProtectedRegion region = rm.getRegion(regionName);
    	
    	region.setOwners(domain);
    	
    	try {
			rm.save();
		} catch (ProtectionDatabaseException e) {
			return false;
		}
    	return true;
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
    
    
    public boolean ecomLogo() {
    	
    	Bukkit.broadcastMessage(" ______                    ");
    	Bukkit.broadcastMessage(" |  ____|                   ");
    	Bukkit.broadcastMessage(" | |__    ___   ___    _ _ __ ");
    	Bukkit.broadcastMessage(" |  __|  /  _|  /  _ |  |  _  _  |");
    	Bukkit.broadcastMessage(" | |___  | (_   | (_) |  | | |  | | |");
    	Bukkit.broadcastMessage(" |____|  |__/  |___/  |_| |_| |_|");
    	Bukkit.broadcastMessage(ChatColor.RED+ "      Medieval Roleplay");
                  
		return true;
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
    
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (c.getName().equalsIgnoreCase("muteall")) {
            if (!(s instanceof Player)) {
                s.sendMessage("This can only be done as a player.");
                return true;
            }
            Player p = ((Player) s);
            if (!p.hasPermission("muteall.mute")) {
                s.sendMessage("You don't have permission to do that.");
                return true;
            } else {
                players.add(p);
                s.sendMessage("You have muted everyone, but can speak yourself");
                return true;
            }
        } else if (c.getName().equalsIgnoreCase("unmuteall")) {
            if (!(s instanceof Player)) {
                s.sendMessage("This can only be done as a player.");
                return true;
            }
            Player p = ((Player) s);
            if (!p.hasPermission("muteall.unmute")) {
                s.sendMessage("You don't have permission to do that.");
                return true;
            } else {
                players.clear();
                s.sendMessage("Everyone can speak again.");
                return true;
            }
        }
        return false;
    }
    
	@EventHandler	
    public void onPlayerChat(PlayerChatEvent e) {
        if (players.size() > 0) {
            if (players.contains(e.getPlayer())) return;
            e.setCancelled(true);
        }
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