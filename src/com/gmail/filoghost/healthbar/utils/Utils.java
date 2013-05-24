package com.gmail.filoghost.healthbar.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Utils {
	
	//enforce non-instantiability with a private constructor
	private Utils() {}
	
	/*
	 *  Replace symbols used for the health bars
	 */	
	public static String replaceSymbols(String string) {
		
		String s = string;
		
		//replaces colors and symbols
		s= s.replace("&", "§")
			.replace("<3", "❤")
			.replace("[x]", "█")
			.replace("[/]", "▌")
			.replace("[*]", "★")
			.replace("[p]", "●")		
			.replace("[+]", "♦")
			.replace("[++]", "✦");

		return s;
	}
	
	/*
	 *  Load and save a custom file
	 */
	public static FileConfiguration loadFile(String path, Plugin plugin) {
		
		File file = new File(plugin.getDataFolder(), path);
			
		if (!file.exists()) {
			try
			{
				plugin.saveResource(path, false);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("-------------------------------------------------");
		        System.out.println("[HealthBar] Cannot save " + path + " to disk!");
		        System.out.println("-------------------------------------------------");
		        return null;
		     }
		}
			 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config;
	}
	
	
	
	
}
