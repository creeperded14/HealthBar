package com.gmail.filoghost.healthbar;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MiscListeners extends JavaPlugin implements Listener {
	
	final Plugin instance = Main.main;
	Boolean fixTabNames;
	Boolean usePlayerPermissions;
	Scoreboard fakeSb = instance.getServer().getScoreboardManager().getNewScoreboard();
	Scoreboard mainSb = instance.getServer().getScoreboardManager().getMainScoreboard();
	BukkitScheduler scheduler = instance.getServer().getScheduler();
	Boolean setBelow;
	Boolean playerEnabled;
	int playerHideDelay;
	PlayerBar playerBar;
	Boolean playerUseAfter;
	
	//disabled worlds names
	Boolean playerUseDisabledWorlds;
	List<String> playerDisabledWorlds;
	
	Boolean wantsUpdateNotifications;
	Boolean updateFound = false;
	String notifyUpdateNewVersion = "unknown";

	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		
		//always check this on all the events!
		if (!playerEnabled) return;
		
		final Player p = event.getPlayer();
		
		//update the tab name to remove the health bars
		fixTabName(p);
		
		//eventually update the scoreboard
		updateScoreboard(p);
		
		//update the health bars
		updatePlayer(p);
		
		//update notifications
		if (wantsUpdateNotifications) {
			if (updateFound && p.hasPermission("healthbar.update")) {
				
				//the message will appear after the join messages
				instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
					public void run() {
						p.sendMessage("§2[§aHealthBar§2] §aFound an update: " + notifyUpdateNewVersion + "   (Your version: " + instance.getDescription().getVersion() + ")");
						p.sendMessage("§aDownload at:§e§n dev.bukkit.org/server-mods/health-bar");
						p.sendMessage("");
					}
				}, 5L);
				
			}
		}
		
	}	
	
	@EventHandler (ignoreCancelled = false, priority = EventPriority.HIGH)
	public void playerTeleport(PlayerTeleportEvent event) {
		
		//always check this on all the events!
		if (!playerEnabled) return;
		
		final Player player = event.getPlayer();
		
		updateScoreboard(player, event.getTo().getWorld().getName());
		
		instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
			public void run() {
				updatePlayer(player);
			}
		}, 1L);
	}
	
	@EventHandler (ignoreCancelled = false, priority = EventPriority.HIGH)
	public void playerRespawn(PlayerRespawnEvent event) {
		
		//always check this on all the events!
		if (!playerEnabled) return;
		
		final Player player = event.getPlayer();
		
		updateScoreboard(player);
		
		instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
			public void run() {
				updatePlayer(player);
			}
		}, 1L);
	}
	
	
	
	private void updateScoreboard(Player p) {
		
		if (!p.isOnline()) return;
			
		//permission check
		if (usePlayerPermissions) {
			if (!p.hasPermission("healthbar.see.onplayer")) {
				p.setScoreboard(fakeSb);
				return;
			}
		}
		
		//world check
		if (playerUseDisabledWorlds) {
			if (playerDisabledWorlds.contains(p.getLocation().getWorld().getName())) {
				p.setScoreboard(fakeSb);
				return;
			}
		}
		
		//player is in correct world and with permissions
		p.setScoreboard(mainSb);
	}
	
	
	//just for the teleport event
	private void updateScoreboard(Player p, String worldName) {
		
		if (!p.isOnline()) return;
		
		//permission check
		if (usePlayerPermissions) {
			if (!p.hasPermission("healthbar.see.onplayer")) {
				p.setScoreboard(fakeSb);
				return;
			}
		}
		
		//world check
		if (playerUseDisabledWorlds) {
			if (playerDisabledWorlds.contains(worldName)) {
				p.setScoreboard(fakeSb);
				return;
			}
		}
		
		//player is in correct world and with permissions
		p.setScoreboard(mainSb);
	}
	
	
	private void updatePlayer(final Player p) {
		
		//first off, update health below
		playerBar.updateHealthBelow(p);
		
		//if the plugin uses health on the tag, and the delay is 0, set it
		if (playerUseAfter && playerHideDelay == 0) {
			scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
				public void run() {
					playerBar.setHealthSuffix(p, p.getHealth(), p.getMaxHealth());
				}
			});	
		}
	}
	
	//only needed at join
	private void fixTabName(Player p) {
		if (fixTabNames) {
			if (p.getName().length() > 14) {
				p.setPlayerListName(p.getName().substring(0, 14));
				p.setPlayerListName(p.getName());
			}
			else {
				p.setPlayerListName("§f" + p.getName());
			}
		}
	}
	
	public void loadConfiguration() {
		
		setBelow = instance.getConfig().getBoolean("player-bars.below-name.enable");
		usePlayerPermissions = instance.getConfig().getBoolean("use-player-bar-permissions");
		fixTabNames = instance.getConfig().getBoolean("fix-tab-names");
		playerHideDelay = instance.getConfig().getInt("player-bars.after-name.hide-delay-seconds");
		playerEnabled = instance.getConfig().getBoolean("player-bars.enable");
		playerUseAfter = instance.getConfig().getBoolean("player-bars.after-name.enable");
		
		wantsUpdateNotifications = instance.getConfig().getBoolean("update-notification");
		
		playerUseDisabledWorlds = instance.getConfig().getBoolean("player-bars.world-disabling");
		//if (mobDisabledWorlds != null) mobDisabledWorlds.clear();
		if (playerUseDisabledWorlds) {
			playerDisabledWorlds = Arrays.asList(
					instance.getConfig()
					.getString("player-bars.disabled-worlds")
					.toLowerCase()
					.replace(" ", "")
					.split(","));
		}
		
		playerBar = Main.main.getPlayerBarInstance();
		
		Player[] playerlist = instance.getServer().getOnlinePlayers();
		if (playerlist.length != 0) {
		    for (Player p : playerlist) {
		    	updatePlayer(p);
		    	updateScoreboard(p);
		    	fixTabName(p);
		    }
		}
		
	}
	
	public void setUpdateNotification(String newVersion) {
		updateFound = true;
		notifyUpdateNewVersion = newVersion;
	}
	
	//end of the class
}