package com.gmail.filoghost.healthbar.metrics;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.healthbar.metrics.Metrics.Graph;

public class MetricsManager {

	public static void startMetrics(Plugin plugin, FileConfiguration config) {
		try {
			Metrics metrics = new Metrics(plugin);
			metrics.start();
			
			onOrOffGraph("Custom Mob Bars", "mob-bars.use-custom-file", config, metrics);
			
			onOrOffGraph("Custom Player Bars", "player-bars.after-name.use-custom-file", config, metrics);
			
			onOrOffGraph("Mob Bars", "mob-bars.enable", config, metrics);
			
			onOrOffGraph("Player Bars", "player-bars.enable", config, metrics);
			
			
		} catch (IOException e) {}
	}
	
	private static void onOrOffGraph(String name, String path, FileConfiguration config, Metrics m) {
		Graph graph = m.createGraph(name);
		if (config.getBoolean(path)) { 
			addToGraph(graph, "Enabled");
		} else {
			addToGraph(graph, "Disabled");
		}
	}
	
		private static void addToGraph(Graph graph, String label) {
		
		graph.addPlotter(new Metrics.Plotter(label) {
			@Override
			public int getValue() { return 1; }
		});
		
	}
	
//end of the class
}
