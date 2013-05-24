package com.gmail.filoghost.healthbar;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public Main instance;
	String pr = "§2[§aHealthBar§2] ";
		
	public Commands(Main main) {
		instance = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (args.length == 0) {
				sendInfo(sender);
				return true;
			}
			if (args[0].equals("help")) {
				sendCommandList(sender);
				return true;
			}
			if (args[0].equals("reload")) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					Sound sou = Sound.ENDERDRAGON_DEATH;
					p.getWorld().playSound(p.getLocation(), sou, 2F, 0.3F);
				}
				reloadConfigs(sender);
				return true;
			}
			if (args[0].equals("update")) {
				sendUpdateCheck(sender);
				return true;
			}
	    	sender.sendMessage(pr + "§eUnknown command. Type §a/healthbar §efor help.");
	    	return true;
	}
	
	
	private void reloadConfigs(CommandSender sender) {
		if (!sender.hasPermission("healthbar.reload")) {
			noPermissionMessage(sender);
			return;
		}
		try {
			instance.reloadConfigFromDisk();
			sender.sendMessage("§e>>§6 HealthBar reloaded");
		}
		catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage("§cFailed to reload configs, take a look at the console!");
		}
		
	}

	private void sendInfo(CommandSender sender) {
		sender.sendMessage(pr);
		sender.sendMessage("§aVersion: §7" + instance.getDescription().getVersion());
		sender.sendMessage("§aDeveloper: §7filoghost");
		sender.sendMessage("§aCommands: §7/hbr help");
	}
	
	private void sendCommandList(CommandSender sender) {
		if (!sender.hasPermission("healthbar.help")) {
			noPermissionMessage(sender);
			return;
		}
		sender.sendMessage("§e>>§6 HealthBar commands: ");
		sender.sendMessage("§2/hbr §7- §aDisplays general plugin info");
		sender.sendMessage("§2/hbr reload §7- §aReloads the configs");
		sender.sendMessage("§2/hbr update §7- §aChecks for updates");
	}
	
	private void sendUpdateCheck(CommandSender sender) {
		if (!sender.hasPermission("healthbar.update")) {
			noPermissionMessage(sender);
			return;
		}
		sender.sendMessage(pr + "§eChecking for updates, please wait...");
		(new Updater()).checkForUpdates(sender, true);	
	}
	
	private void noPermissionMessage(CommandSender sender) {
		sender.sendMessage("§cYou don't have permission.");
		
	}
}
