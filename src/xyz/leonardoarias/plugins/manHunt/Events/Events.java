package xyz.leonardoarias.plugins.manHunt.Events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import xyz.leonardoarias.plugins.manHunt.Config;
import xyz.leonardoarias.plugins.manHunt.ManHunt;
import xyz.leonardoarias.plugins.manHunt.enums.ManHuntRole;
import xyz.leonardoarias.plugins.manHunt.utilities.TextFormatter;
import xyz.leonardoarias.plugins.manHunt.utilities.PlayerData;
import xyz.leonardoarias.plugins.manHunt.utilities.TeamManager;

public class Events implements Listener {

	private final PlayerData playerData;
	private final Config config;
	private final ManHunt plugin;
	private final TeamManager manager;

	public Events(ManHunt plugin, TeamManager manager, PlayerData pd, Config c) {
		this.plugin = plugin;
		playerData = pd;
		config = c;
		this.manager = manager;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (playerData.isFrozen(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager().getType() != EntityType.PLAYER)
			return;
		if (e.getEntity().getType() != EntityType.PLAYER)
			return;
		Player p = (Player) e.getEntity();
		Player a = (Player) e.getDamager();

		if (playerData.isFrozen(a)) {
			e.setCancelled(true);
			return;
		}

		if (playerData.isFrozen(p) && playerData.getRole(a) == ManHuntRole.RUNNER) {
			e.setCancelled(true);
			return;
		}
		if (config.isInstaKill() && playerData.getRole(a) == ManHuntRole.HUNTER
				&& playerData.getRole(p) == ManHuntRole.RUNNER) {
			p.damage(99999);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(EntityDeathEvent e) {
		if (e.getEntityType() != EntityType.PLAYER)
			return;
		Player p = (Player) e.getEntity();

		if (playerData.getRole(p) == ManHuntRole.RUNNER) {
			plugin.getServer()
					.broadcastMessage(TextFormatter.Color(plugin.prefix + " &a" + p.getName() + " has died!"));
			playerData.reset(p);
			manager.removePlayer(ManHuntRole.RUNNER, p);
			p.setGameMode(GameMode.SPECTATOR);
		}
		if (playerData.getPlayersByRole(ManHuntRole.HUNTER).isEmpty()) {
			plugin.getServer().broadcastMessage(
					TextFormatter.Color(plugin.prefix + " &aAll runners have died!\n     The hunters WIN!!"));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (playerData.isFrozen(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent e) {
		if (playerData.isFrozen(e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		ItemStack compass = new ItemStack(Material.COMPASS);
		compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		Player p = e.getPlayer();
		if (config.giveCompass() && playerData.getRole(p) == ManHuntRole.HUNTER) {
			p.getInventory().addItem(compass);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		playerData.reset(e.getPlayer());
	}
}
