package com.gmail.filoghost.healthbar.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class MobBarsUtils {

	/*
	 *  Used to retrieve the array that contains the health bars
	 */
	public static String[] getBarArray(FileConfiguration config) {
		String[] barArray = new String[21];
		int mobBarStyle = config.getInt("mob-bars.display-style");
		
		if (mobBarStyle == 2)
		{
			  barArray[0] = "§c|§7|||||||||||||||||||";  			barArray[1] = "§c|§7|||||||||||||||||||";
			  barArray[2] = "§c||§7||||||||||||||||||";			  	barArray[3] = "§c|||§7|||||||||||||||||";
			  barArray[4] = "§c||||§7||||||||||||||||";			  	barArray[5] = "§e|||||§7|||||||||||||||";
			  barArray[6] = "§e||||||§7||||||||||||||";			 	barArray[7] = "§e|||||||§7|||||||||||||";
			  barArray[8] = "§e||||||||§7||||||||||||";			  	barArray[9] = "§e|||||||||§7|||||||||||";
			  barArray[10] = "§e||||||||||§7||||||||||";			barArray[11] = "§a|||||||||||§7|||||||||";
			  barArray[12] = "§a||||||||||||§7||||||||";			barArray[13] = "§a|||||||||||||§7|||||||";
			  barArray[14] = "§a||||||||||||||§7||||||";			barArray[15] = "§a|||||||||||||||§7|||||";
			  barArray[16] = "§a||||||||||||||||§7||||";			barArray[17] = "§a|||||||||||||||||§7|||";
			  barArray[18] = "§a||||||||||||||||||§7||";			barArray[19] = "§a|||||||||||||||||||§7|";
			  barArray[20] = "§a||||||||||||||||||||";
		}
		else if (mobBarStyle == 3)
		{
			barArray[0] = "§c❤§7❤❤❤❤❤❤❤❤❤";				barArray[1] = "§c❤§7❤❤❤❤❤❤❤❤❤";
			barArray[2] = "§c❤§7❤❤❤❤❤❤❤❤❤";				barArray[3] = "§c❤❤§7❤❤❤❤❤❤❤❤";
			barArray[4] = "§c❤❤§7❤❤❤❤❤❤❤❤";				barArray[5] = "§e❤❤❤§7❤❤❤❤❤❤❤";
			barArray[6] = "§e❤❤❤§7❤❤❤❤❤❤❤";			barArray[7] = "§e❤❤❤❤§7❤❤❤❤❤❤";
			barArray[8] = "§e❤❤❤❤§7❤❤❤❤❤❤";			barArray[9] = "§e❤❤❤❤❤§7❤❤❤❤❤";
			barArray[10] = "§e❤❤❤❤❤§7❤❤❤❤❤";			barArray[11] = "§a❤❤❤❤❤❤§7❤❤❤❤";
			barArray[12] = "§a❤❤❤❤❤❤§7❤❤❤❤";			barArray[13] = "§a❤❤❤❤❤❤❤§7❤❤❤";
			barArray[14] = "§a❤❤❤❤❤❤❤§7❤❤❤";			barArray[15] = "§a❤❤❤❤❤❤❤❤§7❤❤";
			barArray[16] = "§a❤❤❤❤❤❤❤❤§7❤❤";			barArray[17] = "§a❤❤❤❤❤❤❤❤❤§7❤";
			barArray[18] = "§a❤❤❤❤❤❤❤❤❤§7❤";			barArray[19] = "§a❤❤❤❤❤❤❤❤❤❤";
			barArray[20] = "§a❤❤❤❤❤❤❤❤❤❤";
		}
		else if (mobBarStyle == 4) 
		{
			barArray[0] = "§a▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";			barArray[1] = "§a▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[2] = "§a▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";			barArray[3] = "§a▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[4] = "§a▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";			barArray[5] = "§a▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[6] = "§a▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌▌";			barArray[7] = "§a▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[8] = "§a▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌▌";			barArray[9] = "§a▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌▌";
			barArray[10] = "§a▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌▌";			barArray[11] = "§a▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌▌";
			barArray[12] = "§a▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌▌";			barArray[13] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌▌";
			barArray[14] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌▌";			barArray[15] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌▌";
			barArray[16] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌▌";			barArray[17] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌▌";
			barArray[18] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌▌";			barArray[19] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§4▌";
			barArray[20] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
		}
		else
		{
		//default (1 or anything else)
		barArray[0] = "§c▌                   ";		barArray[1] = "§c▌                   ";
		barArray[2] = "§c█                  ";		barArray[3] = "§c█▌                 ";
		barArray[4] = "§c██                ";		barArray[5] = "§e██▌               ";
		barArray[6] = "§e███              ";		barArray[7] = "§e███▌             ";
		barArray[8] = "§e████            ";			barArray[9] = "§e████▌           ";
		barArray[10] = "§e█████          ";			barArray[11] = "§a█████▌         ";
		barArray[12] = "§a██████        ";			barArray[13] = "§a██████▌       ";
		barArray[14] = "§a███████      ";			barArray[15] = "§a███████▌     ";
		barArray[16] = "§a████████    ";			barArray[17] = "§a████████▌   ";
		barArray[18] = "§a█████████  ";				barArray[19] = "§a█████████▌ ";
		barArray[20] = "§a██████████";
		}
		
		return barArray;
	}
}
