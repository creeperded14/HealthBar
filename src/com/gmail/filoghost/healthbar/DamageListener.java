package com.gmail.filoghost.healthbar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ThaH3lper.com.EpicBoss;

import org.apache.commons.lang.WordUtils;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.filoghost.healthbar.utils.MobBarsUtils;
import com.gmail.filoghost.healthbar.utils.Utils;

public class DamageListener extends JavaPlugin implements Listener {
	
	final Plugin instance = Main.main;
	private 	PlayerBar playerBar;
	private 	BukkitScheduler scheduler = instance.getServer().getScheduler();
	
	//mob vars
	private 	Boolean mobEnabled;
	private		String[] barArray;
	private		Boolean mobUseText;
	private		Boolean mobUseCustomText;
	private		String mobCustomText;
	private		Boolean customTextContains_Name;
	private		Boolean mobSemiHidden;
	private		long mobHideDelay;
	private		Boolean mobUseCustomBar;
	private		Boolean restoreCustomNames = true; //TODO
	private		BarStyle barStyle;
	
	private		FileConfiguration customMobBarConfig;
	
	//player vars
	private		Boolean playerEnabled;
	private		long playerHideDelay;
	private		Boolean playerUseAfter;
	
	//hooks and instances of other plugins
	Boolean hook_epicboss;
	EpicBoss instance_epicboss;
	
	//task manager
	Map<String,Integer> playerTable = new HashMap<String, Integer>();
	Map<Integer,Integer> mobTable = new HashMap<Integer, Integer>();
	
	Map<Integer,StringBoolean> namesTable = new HashMap<Integer, StringBoolean>();
	//disabled worlds names
	Boolean mobUseDisabledWorlds;
	List<String> mobDisabledWorlds;	
	
	//fix for villagers
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onVillagerClick(PlayerInteractEntityEvent event) {
		Entity e = event.getRightClicked();
		
		if (!(e instanceof LivingEntity)) return;
		LivingEntity l = (LivingEntity) e;
		
		if (!(l instanceof Villager)) return;
		Villager v = (Villager) e;
		
		String name = v.getCustomName();
		if (name == null) return;
		if (!name.startsWith("§r")) return;
		if (!v.isAdult()) return;
		
		
		if (restoreCustomNames) {
			//let remove it to the scheduled thread
			StringBoolean sb = namesTable.get(v);
			  
			if (sb != null) {
				v.setCustomName(sb.getString());
				v.setCustomNameVisible(sb.getBoolean());
				return;
			}
		}
		
		v.setCustomName("");
		v.setCustomNameVisible(false);						
	}
	
	
	
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		 Entity entity = event.getEntity();
		 
		 
		 if (playerEnabled) {
			 if (entity instanceof Player) {
// PLAYER CHECK -----------------------------------------
				 final Player player = (Player) entity;
				 String pname = player.getName();
				 
				 //first of all updates the health under the name, whatever is the cause
				 playerBar.updateHealthBelow(player);				

				 //if not enabled return
				 if (!playerUseAfter) return;
				 
				 //check for delay == 0
				 if (playerHideDelay == 0L) {
					 parsePlayerHealth(player, false);
					 return;
				 }
			 
				 //check for delay != 0
				 if (event instanceof EntityDamageByEntityEvent) {
					 //display always if hit by entity
					 if (playerTable.containsKey(pname)) {
						 //eventually remove from tables
						 scheduler.cancelTask(playerTable.get(pname));
						 mobTable.remove(pname);
					 }
					 parsePlayerHealth(player, true);
					 return;
				 }
				 else {
					 //it's not damaged by entity
					 if (playerTable.containsKey(pname)) {
						 parsePlayerHealth(player, false);
					 }
					 return;
				 }
			 }
		 }
		 
		 
		 
		 if (mobEnabled) {
			 if (entity instanceof LivingEntity) {
// ENTITY CHECK -----------------------------------------
				 LivingEntity mob = (LivingEntity) entity;
				 
				 //check for delay == 0
				 if (mobHideDelay == 0L) {
					 parseMobHealth(mob, false);
					 return;
				 }
				 
				 //check for delay != 0
				 if (event instanceof EntityDamageByEntityEvent) {
					 //display always if hit by entity
					 if (mobTable.containsKey(mob.getEntityId())) {
						 //eventually remove from tables
						 scheduler.cancelTask(mobTable.get(mob.getEntityId()));
						 mobTable.remove(mob.getEntityId());
					 }
					 parseMobHealth(mob, true);
					 return;
				 }
				 else {
					 //it's not damaged by entity, if the health was displayed only update it
					 if (mobTable.containsKey(mob.getEntityId())) {
						 parseMobHealth(mob, false);
					 }
					 return;
				 }
			 }
		 }	 
 
		 
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityRegain(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();
		if (playerEnabled) {
			if (entity instanceof Player) {
// PLAYER CHECK ---------------------------------------------
			final Player player = (Player) entity;
			String pname = player.getName();
			
			//always update this
			playerBar.updateHealthBelow(player);
			
			//if not enabled return
			if (!playerUseAfter) return;
			
			//always update this if delay = 0
			if (playerHideDelay == 0L) {
				 parsePlayerHealth(player, false);
				 return;
			 }
			
			if (event.getAmount() > 1) {
				//healing > 1
				
				
				//remove the task and create a new one, the heal was > 1
				if (playerTable.containsKey(pname)) {
					scheduler.cancelTask(playerTable.get(pname));
					playerTable.remove(pname);
				}
				parsePlayerHealth(player, true);
				return;
			}
			
			else {
				
				//the healing was = 1, update only if was present
				if (playerTable.containsKey(pname)) {
					parsePlayerHealth(player, false);
					return;
				}
			}
			return;
		}
		}
		if (mobEnabled) {
			if (entity instanceof LivingEntity) {
// MOB CHECK ------------------------------------------------
				LivingEntity mob = (LivingEntity) entity;
			
				if (mobHideDelay == 0L) {
					
					//delay 0 parse
					String cname = mob.getCustomName();
					if (cname != null) {
						if (cname.startsWith("§r")) {
							parseMobHealth(mob, false);
						}
					}
					return;
				 
				}
			}
		}
	}
	
// [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[end of the listeners]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
	
	private void parseMobHealth (final LivingEntity mob, final boolean hideLater) {
		scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
	    	  public void run() {
	    		  
	    		  //world checking!
	    		  if (mobDisabledWorlds != null) {
	    			  if (mobDisabledWorlds.contains(mob.getWorld().getName().toLowerCase())) {
	    				  //the world is disabled
	    				  return;
	    			  }
	    		  }
	    		  
	    		  final int id = mob.getEntityId();
	    		  
	    		  //check if the mob has a healthbar or a custom name
	    		  String customName = mob.getCustomName();
	    		  if (customName != null) {
	    				if (!customName.startsWith("§r")) {
	    					namesTable.put(id, new StringBoolean(customName, mob.isCustomNameVisible()));
	    					
	    					//return;
	    				}
	    		  }
	    		  
	    		  
	    		  //check for compatibility
	    		  int health = getRealHealth(mob);
	    		  int max = getRealMaxHealth(mob);
	    		  
	    		  
	    		  String mobType = mob.getType().toString();
	    		  
	    		  
	    		  //if the health is 0 remove the bar and return
	    		  if (health == 0) {
	    			  mob.setCustomName("");
			    	  mob.setCustomNameVisible(false);
			    	  return;
	    		  }
	    		  
	    		  
	    		  //check if is a wither or a dragon, if so return
			      if (mobType.equals("WITHER") || mobType.equals("ENDER_DRAGON")) return;
			      
	    		  
	    		  //what type of health should be displayed?
			      if (barStyle.equals(BarStyle.BAR))
			      {
			    	  mob.setCustomName("§r" + barArray[round0To20((((double)health) / ((double)max)) * 20.0)]);
			    	  
			      }
			      else if (barStyle.equals(BarStyle.CUSTOM_TEXT))
			      {
						String displayString = mobCustomText.replace("{health}", String.valueOf(health));
						displayString = displayString.replace("{max}", String.valueOf(max));
							
						//optimization, you don't need to check always if a string contains {name}
						if (customTextContains_Name) 
							displayString = displayString.replace("{name}", getRealName(mob, mobType));
							
						mob.setCustomName("§r" + displayString);
							
			      }
			      else if (barStyle.equals(BarStyle.DEFAULT_TEXT))
			      {
						mob.setCustomName("§r" + "Health: " + health + "/" + max);
			      }
			      
			      //check for visibility
			      if (!mobSemiHidden) mob.setCustomNameVisible(true);
	    		  
				  //don't create a new task if it should only refresh
			      if (!hideLater) return;
			      
			      //second level task
			      hideLater(mob);
	    		  
	    		  
	    		  //-----------------------------------------
	    	  }
		});
	}
	
	private void hideLater(final LivingEntity mob) {
		final int id = mob.getEntityId();
		mobTable.put(id, scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
	    	  public void run() {
	    		  
	    		  mobTable.remove(mob.getEntityId());
	    		  
	    		  if (restoreCustomNames) {
	    			  StringBoolean sb = namesTable.get(id);
	    			  
	    			  if (sb != null) {
	    				  mob.setCustomName(sb.getString());
	    				  mob.setCustomNameVisible(sb.getBoolean());
	    				  namesTable.remove(id);
	    				  return;
	    			  }
	    		  }
	    		  
	    		  //not a custom named mob, use default method (hide the name)
	    		  mob.setCustomName("");
	    		  mob.setCustomNameVisible(false);

	    	  }
		    }, mobHideDelay));
	}
	
	private void parsePlayerHealth (final Player p, final Boolean hideLater) {
		scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
	    	  public void run() {
	    		  
	    		  
	    		  //declares variables
	    		  int health = p.getHealth();
	    		  int max = p.getMaxHealth();
	    		  

	    		  //if the health is 0 remove the bar and return
	    		  if (health == 0) {
	    			  playerBar.hideHealthBar(p);
			    	  return;
	    		  }
	    		  
	    		  playerBar.setHealthSuffix(p, health, max);

				  //don't create a new task if it should only refresh
			      if (!hideLater) return;
			      
			      //second level task
			      playerTable.put(p.getName(), scheduler.scheduleSyncDelayedTask(instance, new Runnable() {
			    	  public void run() {
				    		  playerTable.remove(p.getName());
				    		  playerBar.hideHealthBar(p);
			    	  }
				    }, playerHideDelay));
	    		  
	    		  
	    		  //end of first level the runnable -----------------------------------------
	    	  }
		});
	}

	private String parseMobType(String oldname) {
		if (oldname.equals("PIG_ZOMBIE")) return "Zombie Pigman";
		if (oldname.equals("CAVE_SPIDER")) return "Cave Spider";
		if (oldname.equals("MAGMA_CUBE")) return "Magma Cube";
		if (oldname.equals("MUSHROOM_COW")) return "Mooshroom";
		if (oldname.equals("IRON_GOLEM")) return "Iron Golem";
		if (oldname.equals("ENDER_DRAGON")) return "Ender Dragon";
		return WordUtils.capitalizeFully(oldname);
	}
	
	public void removeAllMobHealthBars() {
		scheduler.cancelTasks(instance);
		mobTable.clear();
		List<World> worldsList = instance.getServer().getWorlds();
		for (World w : worldsList) {
			List<LivingEntity> entityList = w.getLivingEntities();
			for (LivingEntity e : entityList) {
				if (e.getCustomName()!=null) {
					if (e.getCustomName().startsWith("§r")) {
						e.setCustomName("");
						e.setCustomNameVisible(false);
					}
				}
			}
		}
	}
	
	private int getRealHealth(LivingEntity mob) {
		
		if (hook_epicboss) {
			  if (instance_epicboss.api.isBoss(mob)) {
				  return instance_epicboss.api.getHealth(mob);
			  }
		  }
		return mob.getHealth();
	}
	
	private int getRealMaxHealth(LivingEntity mob) {
		
		if (hook_epicboss) {
			  if (instance_epicboss.api.isBoss(mob)) {
				  return instance_epicboss.api.getMaxHealth(mob);
			  }
		  }
		return mob.getMaxHealth();
	}
	
	private String getRealName(LivingEntity mob, String mobType) {
		if (hook_epicboss) {
			  if (instance_epicboss.api.isBoss(mob)) {
				  return instance_epicboss.api.getBossName(mob);
			  }
		  }
		
		return parseMobType(mobType);
	}
	
	
	
	public void loadConfiguration() {
		
		removeAllMobHealthBars();	
        
        //load the file
        customMobBarConfig = Utils.loadFile("custom-mob-bar.yml", instance);
		
		//setup mobs
		mobEnabled = instance.getConfig().getBoolean("mob-bars.enable");
		mobUseText = instance.getConfig().getBoolean("mob-bars.text-mode");
		mobUseCustomText = instance.getConfig().getBoolean("mob-bars.custom-text-enable");
		mobCustomText = Utils.replaceSymbols(instance.getConfig().getString("mob-bars.custom-text"));
		mobSemiHidden = instance.getConfig().getBoolean("mob-bars.show-only-if-looking");
		mobHideDelay = (long) instance.getConfig().getInt("mob-bars.hide-delay-seconds")*20;
		mobUseCustomBar = instance.getConfig().getBoolean("mob-bars.use-custom-file");
		
		
		mobUseDisabledWorlds = instance.getConfig().getBoolean("mob-bars.world-disabling");
		//if (mobDisabledWorlds != null) mobDisabledWorlds.clear();
		if (mobUseDisabledWorlds) {
			mobDisabledWorlds = Arrays.asList(
					instance.getConfig()
					.getString("mob-bars.disabled-worlds")
					.toLowerCase()
					.replace(" ", "")
					.split(","));
		}
		
		playerBar = Main.main.getPlayerBarInstance();
		
		//setup players
		playerEnabled = instance.getConfig().getBoolean("player-bars.enable");
		
		playerHideDelay = (long) instance.getConfig().getInt("player-bars.after-name.hide-delay-seconds")*20;
		playerUseAfter = instance.getConfig().getBoolean("player-bars.after-name.enable");
		
		//setup for epicboss
		hook_epicboss = instance.getConfig().getBoolean("hooks.epicboss");
		
		if (hook_epicboss) {
			instance_epicboss = (EpicBoss) instance.getServer().getPluginManager().getPlugin("EpicBossRecoded");
			if (instance_epicboss == null) {
				//if epicboss is not loaded, disable hook
				hook_epicboss = false;
				instance.getServer().getConsoleSender().sendMessage("§a[HealthBar] §fCould not find plugin EpicBoss, " +
						"check that you have installed it and it's correctly loaded. If not, set 'hook-epicboss: false' in the configs. " +
						"If you think that is an error, contact the developer.");
			}
		}
		
		//setup for eventual custom text, not to run extra checks while plugin is running
		if (mobCustomText.contains("{name}")) customTextContains_Name = true;
		else 								  customTextContains_Name = false;
		
		//setup for health array
		mobDisplayBarSetup();
		
		//custom bars - highest priority on configs
		if (mobUseCustomBar) {
			barStyle = BarStyle.BAR;
		
		//text - maybe custom - medium priority on configs
		} else if (mobUseText) {
			if (mobUseCustomText) {
				barStyle = BarStyle.CUSTOM_TEXT;
			} else {
				barStyle = BarStyle.DEFAULT_TEXT;
			}
		} else {
			//default bar - low priority on configs
			barStyle = BarStyle.BAR;
		}
	}
	
	private void mobDisplayBarSetup() {
		if (mobUseCustomBar) {
			
			for (int i=1; i<21; i++) {
				try {
					
					if (!customMobBarConfig.isSet(i*5 + "-percent-bar")) {
						customMobBarConfig.set(i*5 + "-percent-bar", "");
					}

					String cname = customMobBarConfig.getString(i*5 + "-percent-bar");
					if (cname == null) {
						cname = "";
					}
					
					barArray[i] = Utils.replaceSymbols(cname);

				} catch (Exception e) { e.printStackTrace(); }

			}			
			return;
		}
		
		barArray = MobBarsUtils.getBarArray(instance.getConfig());
	}
	
	public int round0To20(double d) {
	    int i = (int) d;
	    if ((d - (double)i)>0.001) {
	    	i++;
	    }
	    if (i<1) { 	return 1; }
	    if (i>20) {	return 20; }
	    return i;
	}
	//end of the class
}
