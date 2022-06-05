package xyz.leonardoarias.plugins.manHunt.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import xyz.leonardoarias.plugins.manHunt.Config;
import xyz.leonardoarias.plugins.manHunt.ManHunt;
import xyz.leonardoarias.plugins.manHunt.enums.ManHuntRole;
import xyz.leonardoarias.plugins.manHunt.utilities.TextFormatter;
import xyz.leonardoarias.plugins.manHunt.utilities.PlayerData;

/**
 * Implements the manhunt command
 * @author leoab2907
 */
public class ManHuntCommand implements CommandExecutor {

	private final PlayerData playerData;
	private final Config config;
	private final ManHunt plugin;
	
	public ManHuntCommand(ManHunt plugin, PlayerData playerData, Config config) {
		this.plugin = plugin;
		this.playerData = playerData;
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aOnly players can use this command"));
			return true;
		}
		if (!sender.hasPermission("manhunt.list")) {
			sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aYou don't have permission to use this command"));
			return true;
		}
		if(args.length == 0) {
			StringBuilder result = new StringBuilder(TextFormatter.Color("&6Current:\n\n"));
			
			String sr = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.RUNNER));
			String k = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.HUNTER));
			result.append(TextFormatter.Color("&aSpeedrunners: \n\n")).append(sr);
			result.append(TextFormatter.Color("\n\n&4Killers:\n\n")).append(k);
			
			sender.sendMessage(result.toString());
			return true;
		}
		if(args.length == 1 && args[0] == "reload") {
			sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aReloading..."));
			plugin.getServer().dispatchCommand(plugin.console, "reload ManHunt");
			return true;
		}
		return true;
	}

	private String formatPlayerList(List<Player> players) {
		return players.stream().map(HumanEntity::getName).collect(Collectors.joining("\n"));
	}

}
