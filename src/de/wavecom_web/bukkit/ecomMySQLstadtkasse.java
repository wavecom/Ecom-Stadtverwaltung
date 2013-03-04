package de.wavecom_web.bukkit;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ecomMySQLstadtkasse implements CommandExecutor {
	
	private ecomMySQL plugin;
	 
	public ecomMySQLstadtkasse(ecomMySQL plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
        	ecomMySQL.log.info("Dieser Command kann nicht von der Console gestartet werden!");
            return true;
        }

        Player player = (Player) sender;
        
        if(command.getLabel().equals("stadtkasse")) {
            if (args.length < 1) {
            	sender.sendMessage("===== Stadtkasse =====");
            	sender.sendMessage("/stadtkasse einzahlen - Geld einzahlen");
            	sender.sendMessage("/stadtkasse auszahlen - Geld auszahlen");
            	sender.sendMessage("/stadtkasse info - Zeigt Stadtkasse an");
            	return true;
            } 
//Einzahlen
            if (args[0].equalsIgnoreCase("einzahlen") && args.length < 2){
                sender.sendMessage("Bitte gib einen Betrag an");
            
            } else if (args[0].equalsIgnoreCase("einzahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.armedanien")) {
                EconomyResponse r1 = ecomMySQL.econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));	
                if(r1.transactionSuccess()) {
                	EconomyResponse r2 = ecomMySQL.econ.bankDeposit("Armedanien", java.lang.Double.parseDouble(args[1]));
                	if(r2.transactionSuccess()) {
                		sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", ecomMySQL.econ.format(r2.amount)));
                	}
                }		
            } else if(args[0].equalsIgnoreCase("einzahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.karafiliem")){
                EconomyResponse r1 = ecomMySQL.econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                if(r1.transactionSuccess()) {
                	EconomyResponse r2 = ecomMySQL.econ.bankDeposit("Karafiliem", java.lang.Double.parseDouble(args[1]));
                	if(r2.transactionSuccess()) {
                		sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", ecomMySQL.econ.format(r2.amount)));
                	}
                }	
            } else if(args[0].equalsIgnoreCase("einzahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.raquilem")){
                EconomyResponse r1 = ecomMySQL.econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                if(r1.transactionSuccess()) {
                	EconomyResponse r2 = ecomMySQL.econ.bankDeposit("Raquilem", java.lang.Double.parseDouble(args[1]));
                	if(r2.transactionSuccess()) {
                		sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", ecomMySQL.econ.format(r2.amount)));
                	}
                }		
            } else if (args[0].equalsIgnoreCase("einzahlen") && ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
            	sender.sendMessage("Es gab ein Error mit deinen Adminrechten! Kontaktiere wavecom.");
            	sender.sendMessage("Error: Deine Stadt wurde dir nicht zugewiesen!");
            } else if (args[0].equalsIgnoreCase("einzahlen") && !ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
            	sender.sendMessage("Du gehörst keiner Stadt an!");
//Auszahlen
            } else if (args[0].equalsIgnoreCase("auszahlen") && args.length < 2) {
            	sender.sendMessage("Bitte gib einen Betrag an");
            } else if (args[0].equalsIgnoreCase("auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.armedanien")){
            	EconomyResponse r1 = ecomMySQL.econ.bankWithdraw("Armedanien", java.lang.Double.parseDouble(args[1]));
                if(r1.transactionSuccess()) {
                    EconomyResponse r2 = ecomMySQL.econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    if(r2.transactionSuccess()) {
                    	sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", ecomMySQL.econ.format(r2.amount)));
                    }
                }
            } else if (args[0].equalsIgnoreCase("auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.karafiliem")){
            	EconomyResponse r1 = ecomMySQL.econ.bankWithdraw("Karafiliem", java.lang.Double.parseDouble(args[1]));
            	if(r1.transactionSuccess()) {
                 	EconomyResponse r2 = ecomMySQL.econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    if(r2.transactionSuccess()) {
                    	sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", ecomMySQL.econ.format(r2.amount)));
                    }
                }
            } else if (args[0].equalsIgnoreCase("auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.raquilem")){
                EconomyResponse r1 = ecomMySQL.econ.bankWithdraw("Raquilem", java.lang.Double.parseDouble(args[1]));
                if(r1.transactionSuccess()) {
                    EconomyResponse r2 = ecomMySQL.econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    if(r2.transactionSuccess()) {
                    	sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", ecomMySQL.econ.format(r2.amount)));
                    }
                }
            } else if (args[0].equalsIgnoreCase("auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen") && ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
            	sender.sendMessage("Es gab ein Error mit deinen Adminrechten! Kontaktiere wavecom.");
            	sender.sendMessage("Error: Deine Stadt wurde dir nicht zugewiesen!");
            } else if (args[0].equalsIgnoreCase("auszahlen") && ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen") && !ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
                sender.sendMessage("Du gehörst keiner Stadt an!");
            } else if (args[0].equalsIgnoreCase("auszahlen") && !ecomMySQL.perms.has(player, "wavecom.stadt.auszahlen")) {
                		sender.sendMessage("Du darfst nichts auszahlen!");
//Info
            } else if (args[0].equalsIgnoreCase("info")){
            	
            	if(ecomMySQL.perms.has(player, "wavecom.stadt.raquilem")){
            		sender.sendMessage(String.format("Die Stadtkasse von Raquilem beträgt: %s", ecomMySQL.econ.format(ecomMySQL.econ.getBalance("Raquilem"))));
            		
            	} else if(ecomMySQL.perms.has(player, "wavecom.stadt.karafiliem")){
            		sender.sendMessage(String.format("Die Stadtkasse von Karafiliem beträgt: %s", ecomMySQL.econ.format(ecomMySQL.econ.getBalance("Karafiliem"))));
            		
            	} else if(ecomMySQL.perms.has(player, "wavecom.stadt.armedanien")){
            		sender.sendMessage(String.format("Die Stadtkasse von Armedanien beträgt: %s", ecomMySQL.econ.format(ecomMySQL.econ.getBalance("Armedanien"))));
            		
            	} else {
            		if (ecomMySQL.perms.getPrimaryGroup(player).equalsIgnoreCase("admin")){
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
        	return false;
        }
	}
}
