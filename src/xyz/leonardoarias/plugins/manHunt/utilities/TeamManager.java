package xyz.leonardoarias.plugins.manHunt.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import xyz.leonardoarias.plugins.manHunt.Config;
import xyz.leonardoarias.plugins.manHunt.enums.ManHuntRole;

public class TeamManager {
	private final Scoreboard board;
	
	public TeamManager(Plugin plugin, Config config) {
		ScoreboardManager manager = plugin.getServer().getScoreboardManager();
		Config c = config;
		if(manager == null) {
			throw new RuntimeException("Cannot load scoreboard manager!");
		}
		this.board = manager.getMainScoreboard();
		
		if (board.getTeam(ManHuntRole.HUNTER.toString()) == null) {
			this.board.registerNewTeam(ManHuntRole.HUNTER.toString());
			setTeamColor(ManHuntRole.HUNTER, ChatColor.RED);
		}
		if (board.getTeam(ManHuntRole.RUNNER.toString())==null) {
			board.registerNewTeam(ManHuntRole.RUNNER.toString());
			if(c.isRunnerInvisibleNameTag()) {
			 setInvisibleNameTag(ManHuntRole.RUNNER);
			}
			setTeamColor(ManHuntRole.RUNNER, ChatColor.BLUE);
		}
	}
	
	public void addPlayer(ManHuntRole teamName, Player player) {
		Team team = this.board.getTeam(teamName.toString());
		if(team == null) {
			throw new RuntimeException("Team "+ teamName + " not fount");
		}
		team.addEntry(player.getName());
	}
	public void removePlayer(ManHuntRole teamName, Player player) {
		Team team = this.board.getTeam(teamName.toString());
		if(team == null) {
			throw new RuntimeException("Team "+ teamName + " not fount");
		}
		team.removeEntry(player.getName());
		
	}

	private void setInvisibleNameTag(ManHuntRole teamName) {
		Team team = this.board.getTeam(teamName.toString());
		team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM); //Makes nametags invisible for speedrunner
		
	}
	private void setTeamColor(ManHuntRole teamName, ChatColor color) {
		Team team = this.board.getTeam(teamName.toString());
		team.setColor(color);;
	}
}
