package com.gmail.filoghost.healthbar;


import java.io.File;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.healthbar.api.BarHideEvent;
import com.gmail.filoghost.healthbar.utils.Utils;

public class PlayerBar {
	final Plugin instance = Main.main;
	Scoreboard sb = instance.getServer().getScoreboardManager().getMainScoreboard();
	BukkitScheduler scheduler = instance.getServer().getScheduler();
	
	Boolean playerEnabled;
	Boolean textMode;
	Boolean useBelow;
	Boolean belowUseProportion;
	int belowNameProportion;
	Objective belowObj;
	
	Boolean useCustomBar;
	FileConfiguration customPlayerBarConfig = null;
	
	public void setupBelow() {
		
		//remove previous objectives under the name
		removeBelowObj();
		
		if (playerEnabled && useBelow) {
			//create the objective
			belowObj = sb.registerNewObjective("healthbarbelow", "dummy");
			belowObj.setDisplayName(getBelowNameText());
			belowObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		
	}
	
	public void removeBelowObj() {
		if (sb.getObjective(DisplaySlot.BELOW_NAME)!=null)
			sb.getObjective(DisplaySlot.BELOW_NAME).unregister();
		if (sb.getObjective("healthbarbelow") != null)
			sb.getObjective("healthbarbelow").unregister();
	}
	
	private String getBelowNameText() {
		String text = instance.getConfig().getString("player-bars.below-name.text");
		text = text.replace("&", "§");
		text = text.replace("<3", "❤");
		return text;
	}
	
	public void setAllTeamsInvisibility() {
		
		//setup for invisibility
		Set<Team> teamList = sb.getTeams();
		for (Team team : teamList) {
			if (team.getName().contains("hbr")) {
				team.setCanSeeFriendlyInvisibles(false);
			}
		}
	}

	public void removeAllHealthbarTeams() {
		
		//remove all the teams
		Set<Team> teamList = sb.getTeams();
		for (Team team : teamList) {
			if (team.getName().contains("hbr")) {
				team.unregister();
			}
		}
	}
	
	public void create10DisplayTeams() {
		int displayStyle = instance.getConfig().getInt("player-bars.after-name.display-style");
		if (displayStyle == 2) {
			sb.registerNewTeam("hbr1").setSuffix(" §c▌");
			sb.registerNewTeam("hbr2").setSuffix(" §c█");
			sb.registerNewTeam("hbr3").setSuffix(" §e█▌");
			sb.registerNewTeam("hbr4").setSuffix(" §e██");
			sb.registerNewTeam("hbr5").setSuffix(" §e██▌");
			sb.registerNewTeam("hbr6").setSuffix(" §a███");
			sb.registerNewTeam("hbr7").setSuffix(" §a███▌");
			sb.registerNewTeam("hbr8").setSuffix(" §a████");
			sb.registerNewTeam("hbr9").setSuffix(" §a████▌");
		   sb.registerNewTeam("hbr10").setSuffix(" §a█████");
			return;
		}
		if (displayStyle == 3) {
			sb.registerNewTeam("hbr1").setSuffix(" §cI§8IIIIIIIII");
			sb.registerNewTeam("hbr2").setSuffix(" §cII§8IIIIIIII");
			sb.registerNewTeam("hbr3").setSuffix(" §eIII§8IIIIIII");
			sb.registerNewTeam("hbr4").setSuffix(" §eIIII§8IIIIII");
			sb.registerNewTeam("hbr5").setSuffix(" §eIIIII§8IIIII");
			sb.registerNewTeam("hbr6").setSuffix(" §aIIIIII§8IIII");
			sb.registerNewTeam("hbr7").setSuffix(" §aIIIIIII§8III");
			sb.registerNewTeam("hbr8").setSuffix(" §aIIIIIIII§8II");
			sb.registerNewTeam("hbr9").setSuffix(" §aIIIIIIIII§8I");
		   sb.registerNewTeam("hbr10").setSuffix(" §aIIIIIIIIII");
			return;
		}
		if (displayStyle == 4) {
			sb.registerNewTeam("hbr1").setSuffix(" §c1❤");
			sb.registerNewTeam("hbr2").setSuffix(" §c2❤");
			sb.registerNewTeam("hbr3").setSuffix(" §e3❤");
			sb.registerNewTeam("hbr4").setSuffix(" §e4❤");
			sb.registerNewTeam("hbr5").setSuffix(" §e5❤");
			sb.registerNewTeam("hbr6").setSuffix(" §a6❤");
			sb.registerNewTeam("hbr7").setSuffix(" §a7❤");
			sb.registerNewTeam("hbr8").setSuffix(" §a8❤");
			sb.registerNewTeam("hbr9").setSuffix(" §a9❤");
		   sb.registerNewTeam("hbr10").setSuffix(" §a10❤");
			return;
		}
		if (displayStyle == 5) {
			sb.registerNewTeam("hbr1").setSuffix(" §c♦§7♦♦♦♦ ");
			sb.registerNewTeam("hbr2").setSuffix(" §c♦§7♦♦♦♦ ");
			sb.registerNewTeam("hbr3").setSuffix(" §e♦♦§7♦♦♦ ");
			sb.registerNewTeam("hbr4").setSuffix(" §e♦♦§7♦♦♦ ");
			sb.registerNewTeam("hbr5").setSuffix(" §a♦♦♦§7♦♦ ");
			sb.registerNewTeam("hbr6").setSuffix(" §a♦♦♦§7♦♦ ");
			sb.registerNewTeam("hbr7").setSuffix(" §a♦♦♦♦§7♦ ");
			sb.registerNewTeam("hbr8").setSuffix(" §a♦♦♦♦§7♦ ");
			sb.registerNewTeam("hbr9").setSuffix(" §a♦♦♦♦♦ ");
		   sb.registerNewTeam("hbr10").setSuffix(" §a♦♦♦♦♦ ");
			return;
		}
		if (displayStyle == 6) {
			sb.registerNewTeam("hbr1").setSuffix(" §c❤§7❤❤❤❤");
			sb.registerNewTeam("hbr2").setSuffix(" §c❤§7❤❤❤❤");
			sb.registerNewTeam("hbr3").setSuffix(" §c❤❤§7❤❤❤");
			sb.registerNewTeam("hbr4").setSuffix(" §c❤❤§7❤❤❤");
			sb.registerNewTeam("hbr5").setSuffix(" §c❤❤❤§7❤❤");
			sb.registerNewTeam("hbr6").setSuffix(" §c❤❤❤§7❤❤");
			sb.registerNewTeam("hbr7").setSuffix(" §c❤❤❤❤§7❤");
			sb.registerNewTeam("hbr8").setSuffix(" §c❤❤❤❤§7❤");
			sb.registerNewTeam("hbr9").setSuffix(" §c❤❤❤❤❤");
		   sb.registerNewTeam("hbr10").setSuffix(" §c❤❤❤❤❤");
			return;
		}
		sb.registerNewTeam("hbr1").setSuffix(" §c|§8|||||||||");
		sb.registerNewTeam("hbr2").setSuffix(" §c||§8||||||||");
		sb.registerNewTeam("hbr3").setSuffix(" §e|||§8|||||||");
		sb.registerNewTeam("hbr4").setSuffix(" §e||||§8||||||");
		sb.registerNewTeam("hbr5").setSuffix(" §e|||||§8|||||");
		sb.registerNewTeam("hbr6").setSuffix(" §a||||||§8||||");
		sb.registerNewTeam("hbr7").setSuffix(" §a|||||||§8|||");
		sb.registerNewTeam("hbr8").setSuffix(" §a||||||||§8||");
		sb.registerNewTeam("hbr9").setSuffix(" §a|||||||||§8|");
	   sb.registerNewTeam("hbr10").setSuffix(" §a||||||||||");
		return;
		
	}
	
	public void create10CustomTeams() {
		for (int i=1; i<11; i++) {
			try {
				
				Team t = sb.registerNewTeam("hbr" + i);
				if (!customPlayerBarConfig.isSet(i + "0" + "-percent.prefix")) {
					customPlayerBarConfig.set(i + "0" + "-percent.prefix", "");
				}
				if (!customPlayerBarConfig.isSet(i + "0" + "-percent.suffix")) {
					customPlayerBarConfig.set(i + "0" + "-percent.suffix", "");
				}
				String prefix = customPlayerBarConfig.getString(i + "0" + "-percent.prefix");
				String suffix = customPlayerBarConfig.getString(i + "0" + "-percent.suffix");
				if ((prefix != null) && (!prefix.equals(""))) {
					t.setPrefix(Utils.replaceSymbols(prefix));
				}
				if ((suffix != null) && (!suffix.equals(""))) {
					t.setSuffix(Utils.replaceSymbols(suffix));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	public boolean hasHealthDisplayed(Player player) {
		Team team = sb.getPlayerTeam((OfflinePlayer)player);
		if (team == null){
			return false;
		}
		if (sb.getPlayerTeam((OfflinePlayer)player).getName().contains("hbr")) return true;
		return false;
	}
	
	public void hideHealthBar (Player player) {
		Team team = sb.getTeam("hbr0");
		if (team == null) {
			team = sb.registerNewTeam("hbr0");
			team.setCanSeeFriendlyInvisibles(false);
		}
		OfflinePlayer offPlayer = (OfflinePlayer) player;
		team.addPlayer(offPlayer);
		
		//api
		instance.getServer().getPluginManager().callEvent(new BarHideEvent(offPlayer));
	}
	
	public void updateHealthBelow (final Player player) {
		if (useBelow) {
			scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
				public void run() {
					int score = 0;
					if (belowUseProportion) {
						score = roundForBelow( ((double) player.getHealth()) * ((double) belowNameProportion) / ((double) player.getMaxHealth()) );
					} else {
						score = player.getHealth();
					}
					sb.getObjective(DisplaySlot.BELOW_NAME)
					.getScore((OfflinePlayer)player)
					.setScore(score);
	    	 	}
			});
		}
	}
	
	public void setHealthSuffix (Player player, int health, int max) {
		
		OfflinePlayer op = (OfflinePlayer) player;
		
		if (useCustomBar || (!textMode)) {
			int healthOn10 = roundToNearest(((double)health)*10.0/((double)max));
			sb.getTeam("hbr" + Integer.toString(healthOn10)).addPlayer((op));
			return;
			
		} else {
			String color = getColor(health, max);
			Team team = sb.getTeam("hbr" + health + "-" + max);
			if (team == null) {
				team = sb.registerNewTeam("hbr" + health + "-" + max);
				team.setSuffix(" - " + color + health + "§7/§a" + max);
				team.setCanSeeFriendlyInvisibles(false);
			}
			team.addPlayer(op);	
			return;
		}

	}

	public String getColor (int health, int max) {
		double ratio = ((double) health)/((double) max);
		if (ratio > 0.5) { return "§a"; } //more than half health -> green
		if (ratio > 0.25) { return "§e"; } //more than quarter health -> yellow
		return "§c"; //critical health -> red
	}

	private int roundToNearest(double d) {
	    int i = (int) d;
	    if ((d - (double)i)>0.001) {
	    	i++;
	    }
	    if (i<1) { 	return 1; }
	    if (i>10) {	return 10; }
	    return i;
	}
	
	private int roundForBelow(double d) {
	    int i = (int) d;
	    if ((d - (double)i)>0.001) {
	    	i++;
	    }
	    if (i<0) return 0;
	    
	    return i;
	    
	}
	
	public void loadConfiguration() {
		        
        //remove all teams
		removeAllHealthbarTeams();
		playerEnabled = instance.getConfig().getBoolean("player-bars.enable");
		sb = instance.getServer().getScoreboardManager().getMainScoreboard();
		textMode = instance.getConfig().getBoolean("player-bars.after-name.text-mode");
		useCustomBar = instance.getConfig().getBoolean("player-bars.after-name.use-custom-file");
		useBelow = instance.getConfig().getBoolean("player-bars.below-name.enable");
		belowUseProportion = instance.getConfig().getBoolean("player-bars.below-name.use-proportion");
		belowNameProportion = instance.getConfig().getInt("player-bars.below-name.proportional-to");
		
		setupBelow();
		
		if (useCustomBar) {
			customPlayerBarConfig = Utils.loadFile("custom-player-bar", instance);
			create10CustomTeams();
		} else if (!textMode) {
			create10DisplayTeams();
		}
		
		setAllTeamsInvisibility();
        
	}
}
