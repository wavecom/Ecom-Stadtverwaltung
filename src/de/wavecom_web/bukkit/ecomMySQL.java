package de.wavecom_web.bukkit;

import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ecomMySQL extends JavaPlugin {
	 
    private static final Logger log = Logger.getLogger("Minecraft");
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
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        log.info("Ecom MySQL Writer by wavecom wurde erfolgreich geladen!");
    }

    private boolean setupEconomy() {
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

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getLabel().equals("test-economy")) {
            // Lets give the player 1.05 currency (note that SOME economic plugins require rounding!)
            sender.sendMessage(String.format("You have %s", econ.format(econ.getBalance(player.getName()))));
            EconomyResponse r = econ.depositPlayer(player.getName(), 1.00);
            if(r.transactionSuccess()) {
                sender.sendMessage(String.format("You were given %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            } else {
                sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
            }
            return true;
        } else if(command.getLabel().equals("test-permission")) {
            // Lets test if user has the node "example.plugin.awesome" to determine if they are awesome or just suck
            if(perms.has(player, "example.plugin.awesome")) {
                sender.sendMessage("You are awesome!");
            } else {
                sender.sendMessage("You suck!");
            }
            return true;
        } else if(command.getLabel().equals("stadtkasse")) {
            if (args.length < 1) {
            	sender.sendMessage("===== Stadtkasse =====");
            	sender.sendMessage("/stadtkasse einzahlen - Geld einzahlen");
            	sender.sendMessage("/stadtkasse auszahlen - Geld auszahlen");
            	sender.sendMessage("/stadtkasse info - Zeigt Stadtkasse an");
            	return false;
            } 
            if (args[0].equalsIgnoreCase("einzahlen")){
            	if (args.length < 2) {
                	sender.sendMessage("Bitte gib einen Betrag an");
                	
                } else {
               
                	if(perms.has(player, "wavecom.stadt.armedanien")){
                		sender.sendMessage("Einzahlen Armedanien Stadtkasse");
                		EconomyResponse r1 = econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                		
                		if(r1.transactionSuccess()) {
                			EconomyResponse r2 = econ.bankDeposit("Armedanien", java.lang.Double.parseDouble(args[1]));
                			if(r2.transactionSuccess()) {
                				sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", econ.format(r2.amount)));
                			}
                		}
                		
                		
                	} else if(perms.has(player, "wavecom.stadt.karafiliem")){
                		sender.sendMessage("Einzahlen Karafiliem Stadtkasse");
                		
                	} else if(perms.has(player, "wavecom.stadt.raquilem")){
                		sender.sendMessage("Einzahlen Raquilem Stadtkasse");
                		
                	} else {
                		
                	
                		if (perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
                			sender.sendMessage("Es gab ein Error mit deinen Adminrechten! Kontaktiere wavecom.");
                			sender.sendMessage("Error: Deine Stadt wurde dir nicht zugewiesen!");
                		} else {
                			sender.sendMessage("Du gehörst keiner Stadt an!");
                		}
                	}
                }
            } else if (args[0].equalsIgnoreCase("auszahlen")){
            	if (args.length < 2) {
                	sender.sendMessage("Bitte gib einen Betrag an");
                	
                } else {
            	
                	if(perms.has(player, "wavecom.stadt.auszahlen")){
                		
                		if(perms.has(player, "wavecom.stadt.armedanien")){
                			sender.sendMessage("Auszahlen Armedanien Stadtkasse");
                			
                		} else if(perms.has(player, "wavecom.stadt.karafiliem")){
                			sender.sendMessage("Auszahlen Karafiliem Stadtkasse");
            			
                		} else if(perms.has(player, "wavecom.stadt.raquilem")){
                			sender.sendMessage("Auszahlen Raquilem Stadtkasse");
                			
                		} else if (perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
                			sender.sendMessage("Es gab ein Error mit deinen Adminrechten! Kontaktiere wavecom.");
                			sender.sendMessage("Error: Deine Stadt wurde dir nicht zugewiesen!");
            			
                		} else {
                			sender.sendMessage("Du gehörst keiner Stadt an!");
            			
                		}
                	} else {
                		sender.sendMessage("Du darfst nichts auszahlen!");
                	}
                }
            } else if (args[0].equalsIgnoreCase("info")){
            	
            	if(perms.has(player, "wavecom.stadt.armedanien")){
            		sender.sendMessage("Info Armedanien Stadtkasse");
            		
            	} else if(perms.has(player, "wavecom.stadt.karafiliem")){
            		sender.sendMessage("Info Karafiliem Stadtkasse");
            		
            	} else if(perms.has(player, "wavecom.stadt.raquilem")){
            		sender.sendMessage("Info Raquilem Stadtkasse");
            		
            	} else {
            		if (perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
        				sender.sendMessage("Es gab ein Error mit deinen Adminrechten! Kontaktiere wavecom.");
        				sender.sendMessage("Error: Deine Stadt wurde dir nicht zugewiesen!");
            		} else {
            			sender.sendMessage("Du gehörst keiner Stadt an!");
            		}
            	}
            	
            } else {
            	sender.sendMessage("===== Stadtkasse =====");
            	sender.sendMessage("/stadtkasse einzahlen - Geld einzahlen");
            	sender.sendMessage("/stadtkasse auszahlen - Geld auszahlen");
            	sender.sendMessage("/stadtkasse info - Zeigt Stadtkasse an");
        	}
            return true;
        } else {
        	sender.sendMessage("Command Error");
            return false;
        }
    }
}       