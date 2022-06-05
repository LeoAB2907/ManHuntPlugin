package xyz.leonardoarias.plugins.manHunt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.leonardoarias.plugins.manHunt.Config;
import xyz.leonardoarias.plugins.manHunt.ManHunt;
import xyz.leonardoarias.plugins.manHunt.enums.ManHuntRole;
import xyz.leonardoarias.plugins.manHunt.utilities.TextFormatter;
import xyz.leonardoarias.plugins.manHunt.utilities.PlayerData;
import xyz.leonardoarias.plugins.manHunt.utilities.TeamManager;

/**
 * Implements the runner command
 * @author leoab2907
 */
public class RunnerCommand implements CommandExecutor {

	private final ManHunt plugin;
	private final TeamManager teamManager;
	private final PlayerData playerData;
	
	public RunnerCommand(ManHunt plugin, TeamManager teamManager, PlayerData playerData, Config config) {
		this.plugin = plugin;
		this.teamManager = teamManager;
		this.playerData = playerData;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aOnly players can use this command"));
			return true;
		}
		if (sender.hasPermission("manhunt.roles")) {
			if(args.length <= 1) {
				sender.sendMessage(TextFormatter.Color(plugin.prefix + " &6Usage: " + cmd.toString() + "[add/remove] <player>"));
				return true;
			}

			Player player = plugin.getServer().getPlayer(args[1]);

			if (args.length == 2 && args[0].equals("add")) {
				addPlayer(player);
				sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aAdded " + player.getName() + " to runners"));
				return true;
			} else if (args.length == 2 && args[0].equals("remove")) {
				removePlayer(player);
				sender.sendMessage(
						TextFormatter.Color(plugin.prefix + " &aRemoved " + player.getName() + " from runners"));
				return true;
			}
		} else {
			sender.sendMessage(TextFormatter.Color(plugin.prefix + " &aYou don't have permission to use this command"));
			return true;
		}
		
		return false;
	}

	private void removePlayer(Player player) {
		playerData.reset(player);
		teamManager.removePlayer(ManHuntRole.RUNNER, player);
		player.sendMessage(TextFormatter.Color(plugin.prefix + " &aYou've been added to runners"));
	}

	private void addPlayer(Player player) {
		playerData.setRole(player, ManHuntRole.RUNNER);
		teamManager.addPlayer(ManHuntRole.RUNNER, player);
		player.sendMessage(TextFormatter.Color(plugin.prefix + " &aYou've been removed from runners"));
	}

}
