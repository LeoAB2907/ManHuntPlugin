package xyz.leonardoarias.plugins.manHunt;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import xyz.leonardoarias.plugins.manHunt.Events.Events;
import xyz.leonardoarias.plugins.manHunt.commands.HunterCommand;
import xyz.leonardoarias.plugins.manHunt.commands.ManHuntCommand;
import xyz.leonardoarias.plugins.manHunt.commands.RunnerCommand;
import xyz.leonardoarias.plugins.manHunt.utilities.TextFormatter;
import xyz.leonardoarias.plugins.manHunt.utilities.PlayerData;
import xyz.leonardoarias.plugins.manHunt.utilities.TeamManager;
import xyz.leonardoarias.plugins.manHunt.utilities.Worker;

/**
 * Implements the Man Hunt gamemode from Dream's videos
 * 
 * @version a3.5
 * @author leoab2907
 */
public class ManHunt extends JavaPlugin {
	public final ConsoleCommandSender console = getServer().getConsoleSender();
	public Config config = new Config();
	public String prefix;

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		config.setCompassTracking(getConfig().getBoolean("compass-enabled"));
		config.setFreeze(getConfig().getBoolean("freeze-hunter-when-seen"));
		config.setGiveCompass(getConfig().getBoolean("compass-give"));
		config.setInstaKill(getConfig().getBoolean("killer-instakill"));
		config.setCompassRandomInDifferentWorlds(getConfig().getBoolean("compass-random-different-worlds:"));
		config.setRunnerInvisibleNameTag(getConfig().getBoolean("runner-nametag-invisible"));
		prefix = getConfig().getString("prefix");
		
		
		TeamManager manager = new TeamManager(this, config);
		PlayerData playerData = new PlayerData();
		
		
		getServer().getPluginManager().registerEvents(new Events(this, manager, playerData, config), this);
		getCommand("hunter").setExecutor(new HunterCommand(this, manager, playerData, config));
		getCommand("runner").setExecutor(new RunnerCommand(this, manager, playerData, config));
		getCommand("manhunt").setExecutor(new ManHuntCommand(this, playerData, config));
		

		BukkitTask task = new Worker(this, playerData, config).runTaskTimer(this, 1, 1);

		console.sendMessage(TextFormatter.Color(prefix + " &aManHunt has been enabled!"));

	}

	@Override
	public void onDisable() {
		console.sendMessage(TextFormatter.Color(prefix + " &cManHunt has been disabled!"));
	}
}
