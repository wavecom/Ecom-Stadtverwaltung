package de.wavecom_web.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ecomMySQLlizenzgetrights implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLlizenzgetrights(ecomMySQL plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }

        Player player = (Player) sender;
        
		if(command.getLabel().equals("lizenzgetrights")) {
			if (ecomMySQL.perms.playerHas(player, "wavecom.lizenz.inTask.g") | ecomMySQL.perms.playerHas(player, "wavecom.lizenz.inTask.h") | ecomMySQL.perms.playerHas(player, "wavecom.lizenz.inTask.m")){
				
				ecomMySQL.log.info("Spieler "+player.getName()+" hat Worldguard Lizenzrechte bekommen.");
				
				ecomMySQL.perms.playerAdd(player, "permissions.manage.users.permissions."+player.getName());
				ecomMySQL.perms.playerAdd(player, "worldguard.region.addowner.*");
				
			} else if (ecomMySQL.perms.playerHas(player, "wavecom.lizenz.inTast.cancel.g") | ecomMySQL.perms.playerHas(player, "wavecom.lizenz.inTast.cancel.h") | ecomMySQL.perms.playerHas(player, "wavecom.lizenz.cancel.inTast.m")){
					
				ecomMySQL.log.info("Spieler "+player.getName()+" hat Worldguard Cancel Lizenzrechte bekommen.");
					
				ecomMySQL.perms.playerAdd(player, "permissions.manage.users.permissions."+player.getName());
				ecomMySQL.perms.playerAdd(player, "worldguard.region.removeowner.*");
					
			} else {
				sender.sendMessage("Du darfst diesen Command nicht benutzen!");
			}
			
			return true;
		}
		return false;
	}
}