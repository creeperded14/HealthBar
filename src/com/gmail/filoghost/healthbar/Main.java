package com.gmail.filoghost.healthbar;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.healthbar.metrics.Metrics;
import com.gmail.filoghost.healthbar.metrics.Metrics.Graph;
import com.gmail.filoghost.healthbar.metrics.MetricsManager;
import com.gmail.filoghost.healthbar.utils.Utils;


public class Main extends JavaPlugin {
	public static 	Main main;
	private static 	DamageListener damageListener;
	private static	DeathListener deathListener;
	private static	PlayerBar playerBar;
	private static	MiscListeners miscListeners;
	
	@Override
	public void onEnable() {
		main = this;
		damageListener = new DamageListener();
		deathListener = new DeathListener();
		playerBar = new PlayerBar();
		miscListeners = new MiscListeners();
		
			//create the folder
			if (getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			
			//forces to generate new files
			Utils.loadFile("custom-mob-bar.yml", this);
			Utils.loadFile("custom-player-bar.yml", this);
		
			FileConfiguration config = getConfig();
			
			//try to check updates
			if (config.getBoolean("update-notification")) {
			try {
				(new Updater()).checkForUpdates(getServer().getConsoleSender(), false);
				} catch (Exception e) {
					System.out.println("The update checker failed!");
				}
			}
			
			//register events
			getServer().getPluginManager().registerEvents(damageListener, this);
			getServer().getPluginManager().registerEvents(deathListener, this);
			getServer().getPluginManager().registerEvents(miscListeners, this);
			
			reloadConfigFromDisk();
			
			//setup for command executor
			getCommand("healthbar").setExecutor(new Commands(this));
			
			//metrics
			MetricsManager.startMetrics(this, config);
			
//end of onEnable
	}	

	public void onDisable() {
		playerBar.removeAllHealthbarTeams();
		playerBar.removeBelowObj();
		damageListener.removeAllMobHealthBars();
		System.out.println("Health Display disabled, all health bars have been removed.");
	}
	
	
	private void checkDefaultConfigs() {		
		FileConfiguration config = getConfig();
		
		if (!config.isSet("player-bars.enable")) config.set("player-bars.enable", true);
		
		if (!config.isSet("player-bars.after-name.enable")) config.set("player-bars.after-name.enable", true);
		if (!config.isSet("player-bars.after-name.display-style")) config.set("player-bars.after-name.display-style", 1);
		if (!config.isSet("player-bars.after-name.text-mode")) config.set("player-bars.after-name.text-mode", false);
		if (!config.isSet("player-bars.after-name.hide-delay-seconds")) config.set("player-bars.after-name.hide-delay-seconds", 5);
		if (!config.isSet("player-bars.after-name.use-custom-file")) config.set("player-bars.after-name.use-custom-file", false);
		
		if (!config.isSet("player-bars.below-name.enable")) config.set("player-bars.below-name.enable", true);
		if (!config.isSet("player-bars.below-name.text")) config.set("player-bars.below-name.text", "&cHealth");
		if (!config.isSet("player-bars.below-name.use-proportion")) config.set("player-bars.below-name.use-proportion", true);
		if (!config.isSet("player-bars.below-name.proportional-to")) config.set("player-bars.below-name.proportional-to", 10);
		
		if (!config.isSet("player-bars.world-disabling")) config.set("player-bars.world-disabling", false);
		if (!config.isSet("player-bars.disabled-worlds")) config.set("player-bars.disabled-worlds", "world_nether,world_the_end,exampleWorld");
		
		if (!config.isSet("mob-bars.enable")) config.set("mob-bars.enable", true);
		if (!config.isSet("mob-bars.display-style")) config.set("mob-bars.display-style", 1);
		if (!config.isSet("mob-bars.text-mode")) config.set("mob-bars.text-mode", false);
		if (!config.isSet("mob-bars.custom-text-enable")) config.set("mob-bars.custom-text-enable", false);
		if (!config.isSet("mob-bars.custom-text")) config.set("mob-bars.custom-text", "{name} - &a{health}/{max}");
		if (!config.isSet("mob-bars.show-only-if-looking")) config.set("mob-bars.show-only-if-looking", false);
		if (!config.isSet("mob-bars.hide-delay-seconds")) config.set("mob-bars.hide-delay-seconds", 5);	
		if (!config.isSet("mob-bars.use-custom-file")) config.set("mob-bars.use-custom-file", false);
		if (!config.isSet("mob-bars.world-disabling")) config.set("mob-bars.world-disabling", false);
		if (!config.isSet("mob-bars.disabled-worlds")) config.set("mob-bars.disabled-worlds", "world_nether,world_the_end,exampleWorld");
		
		if (!config.isSet("hooks.epicboss")) config.set("hooks.epicboss", false);
		
		if (!config.isSet("fix-tab-names")) config.set("fix-tab-names", true);
		if (!config.isSet("fix-death-messages")) config.set("fix-death-messages", true);
		if (!config.isSet("update-notification")) config.set("update-notification", true);
		if (!config.isSet("use-player-bar-permissions")) config.set("use-player-bar-permissions", false);

		saveConfig();
	}
	
	
	public void reloadConfigFromDisk() {
		reloadConfig();
		checkDefaultConfigs();
		damageListener.loadConfiguration();
		deathListener.loadConfiguration();
		playerBar.loadConfiguration();
		miscListeners.loadConfiguration();
	}
	
	public PlayerBar getPlayerBarInstance() {
		return playerBar;
	}
	
	public MiscListeners getLoginListenerInstance() {
		return miscListeners;
	}

//end of the class
}
