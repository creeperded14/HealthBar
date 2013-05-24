package com.gmail.filoghost.healthbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Updater {
	final Plugin instance = Main.main;
	
	public void checkForUpdates(final CommandSender sender, final Boolean usedWithCommands) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
//					//external website where I update manually the version to the latest
					URLConnection url = new URL("http://mcwild.altervista.org/version.html").openConnection();
					url.setConnectTimeout(5000);
					url.setReadTimeout(8000);
					BufferedReader in = new BufferedReader(new InputStreamReader(url.getInputStream()));
	        
					String currentVersion = "1.5.9"; /*instance.getDescription().getVersion();*/
					String readVersion = in.readLine();
					in.close();
					
					String copyOfReadVersion = readVersion;
					if (readVersion.equals(currentVersion)) {
						
						//if used with command notify that there isn't an update
						if (usedWithCommands){
							noUpdates(sender);
							return;
						}
						else {
							return;
						}
						
					}
					
					//removes the .
					currentVersion = currentVersion.replace(".", "");
					readVersion = readVersion.replace(".", "");
					
					
					/*
					 * What exactly does? It adds zeros the the strings till they have the same length.
					 * 1.5 vs 1.5.6 will be transformed in 150 and 156. 156 > 150 so that is the newer version.
					 */
					if (currentVersion.length() < readVersion.length()) {
						while (currentVersion.length() < readVersion.length()) {
							currentVersion += "0";
						}
					}
					if (readVersion.length() < currentVersion.length()) {
						while (readVersion.length() < currentVersion.length()) {
							readVersion += "0";
						}
					}
					
					
					if (Integer.parseInt(currentVersion) < Integer.parseInt(readVersion)) {
						notifyUpdate(copyOfReadVersion, sender);
					}
					else {
						if (usedWithCommands) {
							noUpdates(sender);
						}
					}
				} catch (Exception e) {
					if (usedWithCommands) {
						sender.sendMessage("§cThe update checker failed! Retry later");
					}
				}
			}
		});
		thread.start();
	}
	
	private void notifyUpdate(final String newVersion, final CommandSender sender) {
		instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
			public void run() {
				sender.sendMessage("§2[§aHealthBar§2] §aFound an update: " + newVersion + "   (Your version: 1.6-dev" + /*instance.getDescription().getVersion()*/ ")");
				sender.sendMessage("§2[§aHealthBar§2] §aDownload at:§f dev.bukkit.org/server-mods/health-bar");
				(Main.main.getLoginListenerInstance()).setUpdateNotification(newVersion);
			}
		}, 5L);
	}
	
	private void noUpdates(final CommandSender sender) {
		instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
			public void run() {
				sender.sendMessage("§a§2[§aHealthBar§2] §aThe plugin is updated to the latest version!");
			}
		}, 5L);
	}
}
