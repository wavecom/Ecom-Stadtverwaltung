package de.wavecom_web.bukkit;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	
	String url = "jdbc:mysql://localhost:3306/DB";
	String user = "";
	String pass = "";
	 
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
        log.info("Ecom Stadtverwaltung by wavecom wurde erfolgreich geladen!");
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

        if(command.getLabel().equals("stadtkasse")) {
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
                		EconomyResponse r1 = econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                		
                		if(r1.transactionSuccess()) {
                			EconomyResponse r2 = econ.bankDeposit("Armedanien", java.lang.Double.parseDouble(args[1]));
                			if(r2.transactionSuccess()) {
                				sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", econ.format(r2.amount)));
                			}
                		}
                		
                		
                	} else if(perms.has(player, "wavecom.stadt.karafiliem")){
                		EconomyResponse r1 = econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                		
                		if(r1.transactionSuccess()) {
                			EconomyResponse r2 = econ.bankDeposit("Karafiliem", java.lang.Double.parseDouble(args[1]));
                			if(r2.transactionSuccess()) {
                				sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", econ.format(r2.amount)));
                			}
                		}
                		
                	} else if(perms.has(player, "wavecom.stadt.raquilem")){
                		EconomyResponse r1 = econ.withdrawPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                		
                		if(r1.transactionSuccess()) {
                			EconomyResponse r2 = econ.bankDeposit("Raquilem", java.lang.Double.parseDouble(args[1]));
                			if(r2.transactionSuccess()) {
                				sender.sendMessage(String.format("Du hast erfolgreich %s eingezahlt!", econ.format(r2.amount)));
                			}
                		}
                		
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
                    		EconomyResponse r1 = econ.bankWithdraw("Armedanien", java.lang.Double.parseDouble(args[1]));
                    		
                    		if(r1.transactionSuccess()) {
                    			EconomyResponse r2 = econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    			if(r2.transactionSuccess()) {
                    				sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", econ.format(r2.amount)));
                    			}
                    		}
                			
                		} else if(perms.has(player, "wavecom.stadt.karafiliem")){
                    		EconomyResponse r1 = econ.bankWithdraw("Karafiliem", java.lang.Double.parseDouble(args[1]));
                    		
                    		if(r1.transactionSuccess()) {
                    			EconomyResponse r2 = econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    			if(r2.transactionSuccess()) {
                    				sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", econ.format(r2.amount)));
                    			}
                    		}
                			
            			
                		} else if(perms.has(player, "wavecom.stadt.raquilem")){
                    		EconomyResponse r1 = econ.bankWithdraw("Raquilem", java.lang.Double.parseDouble(args[1]));
                    		
                    		if(r1.transactionSuccess()) {
                    			EconomyResponse r2 = econ.depositPlayer(player.getName(), java.lang.Double.parseDouble(args[1]));
                    			if(r2.transactionSuccess()) {
                    				sender.sendMessage(String.format("Du hast erfolgreich %s ausgezahlt!", econ.format(r2.amount)));
                    			}
                    		}
                			
                			
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
            		sender.sendMessage(String.format("Die Stadtkasse beträgt: %s", econ.bankBalance("Armedanien")));
            		
            	} else if(perms.has(player, "wavecom.stadt.karafiliem")){
            		sender.sendMessage(String.format("Die Stadtkasse beträgt: %s", econ.bankBalance("Karafiliem")));
            		
            	} else if(perms.has(player, "wavecom.stadt.raquilem")){
            		sender.sendMessage(String.format("Die Stadtkasse beträgt: %s", econ.bankBalance("Raquilem")));
            		
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
        } else if(command.getLabel().equals("stadtwahl")) {
        	
        	if(perms.getPrimaryGroup(player).equalsIgnoreCase("newbie")){
        		if (args.length < 1) {
                	sender.sendMessage("===== Stadtwahl =====");
                	sender.sendMessage("Raquilem: /stadtwahl raquilemer");
                	sender.sendMessage("Armedanien: /stadtwahl armedaner");
                	sender.sendMessage("Karafilem: /stadtwahl karafiliemer");
                	
                } else if (args.length > 1) {
                	sender.sendMessage("===== Stadtwahl =====");
                	sender.sendMessage("Raquilem: /stadtwahl raquilem");
                	sender.sendMessage("Armedanien: /stadtwahl armedanien");
                	sender.sendMessage("Karafilem: /stadtwahl karafiliem");
                	
                } else if (args[0].equalsIgnoreCase("raquilem")){
                	perms.playerAddGroup(player, "raquilemer");
                	perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Raquilem an!");
                	
					try {
						Connection conn = DriverManager.getConnection(url, user, pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`) VALUES ('"+player.getName()+"', '1');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
                	
                } else if (args[0].equalsIgnoreCase("armedanien")){
                	perms.playerAddGroup(player, "armedaner");
                	perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Armedanien an!");
                	
					try {
						Connection conn = DriverManager.getConnection(url, user, pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`) VALUES ('"+player.getName()+"', '2');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
                	
                } else if (args[0].equalsIgnoreCase("karafiliem")){
                	perms.playerAddGroup(player, "karafiliemer");
                	perms.playerRemoveGroup(player, "newbie");
                	sender.sendMessage("Du gehörst nun Karafiliem an!");
                	
					try {
						Connection conn = DriverManager.getConnection(url, user, pass);
						PreparedStatement sampleQueryStatement;
						sampleQueryStatement = conn.prepareStatement("INSERT INTO `"+user+"`.`stadtverwaltung_spieler` (`Spieler`, `Stadt`) VALUES ('"+player.getName()+"', '3');");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close(); 
					} catch (SQLException e) {
						sender.sendMessage("Datenbankfehler!");
						e.printStackTrace();
					}
                	
                } else {
            		sender.sendMessage("Diese Stadt gibt es nicht!");
                }
        	}else {
        		sender.sendMessage("Du darfst keine Stadt mehr wählen!");
        	}
        	
        	return true;
        } else {
        	return false;
        }
	}
}       