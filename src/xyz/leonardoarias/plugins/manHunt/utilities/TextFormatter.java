package xyz.leonardoarias.plugins.manHunt.utilities;

import org.bukkit.ChatColor;

/**
 * Replaces the text &+code with the correct chat color.
 * 
 * @author LeoAB2907
 * @version 1.0
 */
public class TextFormatter {

	/**
	 * Replaces all color codes with the respective color.
	 * Must NOT be used before Center(String st);
	 * 
	 * @param str the string to be reconstructed
	 * @return str
	 */
	public static String Color(String str) {
		str = str.replaceAll("&0", ChatColor.BLACK + "");
		str = str.replaceAll("&1", ChatColor.DARK_BLUE + "");
		str = str.replaceAll("&2", ChatColor.DARK_GREEN + "");
		str = str.replaceAll("&3", ChatColor.DARK_AQUA + "");
		str = str.replaceAll("&4", ChatColor.DARK_RED + "");
		str = str.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		str = str.replaceAll("&6", ChatColor.GOLD + "");
		str = str.replaceAll("&7", ChatColor.GRAY + "");
		str = str.replaceAll("&8", ChatColor.DARK_GRAY + "");
		str = str.replaceAll("&9", ChatColor.BLUE + "");
		str = str.replaceAll("&a", ChatColor.GREEN + "");
		str = str.replaceAll("&b", ChatColor.AQUA + "");
		str = str.replaceAll("&c", ChatColor.RED + "");
		str = str.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		str = str.replaceAll("&e", ChatColor.YELLOW + "");
		str = str.replaceAll("&f", ChatColor.WHITE + "");
		str = str.replaceAll("&k", ChatColor.MAGIC + "");
		str = str.replaceAll("&l", ChatColor.BOLD + "");
		str = str.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		str = str.replaceAll("&n", ChatColor.UNDERLINE + "");
		str = str.replaceAll("&o", ChatColor.ITALIC + "");
		str = str.replaceAll("&r", ChatColor.RESET + "");
		return str;
	}

	/**
	 * Centers a text.
	 * Must NOT be used after Color(String str)
	 * 
	 * @param str The string to be replaced
	 * @param maxLength The maximum length of the text box
	 * @return The centered string
	 * @throws IllegalStateException when the length of one line is greater than maxLength
	 */
	public static String Center(String str, int maxLength){
		String[] lines = str.split("\n");
		StringBuilder text;
		
		for (int i = 0; i<= lines.length; i++) {
			String temp = lines[i];
			
			/* Replace all color codes */
			for (int j = 0; j <= 9; ++j) temp = temp.replaceAll("&" + j, "");
			temp = temp.replaceAll("&a", "");
			temp = temp.replaceAll("&b", "");
			temp = temp.replaceAll("&c", "");
			temp = temp.replaceAll("&d", "");
			temp = temp.replaceAll("&e", "");
			temp = temp.replaceAll("&f", "");
			temp = temp.replaceAll("&k", "");
			temp = temp.replaceAll("&l", "");
			temp = temp.replaceAll("&m", "");
			temp = temp.replaceAll("&n", "");
			temp = temp.replaceAll("&o", "");
			temp = temp.replaceAll("&r", "");
			
			/* Add padding */
			int length = temp.length();
			
			if(length > maxLength) {
				throw new IllegalStateException("The length of line " + i + " is greater than maxLength");
			}
			else {
				int pad = maxLength - length;
				StringBuilder sb = new StringBuilder(lines[i]);
				for (int j = 0; j < pad; j++)
					if (j % 2 == 0)
						sb.insert(0, " ");
					else
						sb.append(" ");
				lines[i] = sb.toString();
			}
			
		}
		
		/* String builder */
		text = new StringBuilder("");
		for(String s : lines) {
			text.append(s);
			text.append("\n"); // this will add an extra \n at the end
		}
		text.replace(text.length()-1, text.length(), ""); // this removes the extra \n
		
		
		return text.toString();
	}
}
