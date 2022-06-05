package xyz.leonardoarias.plugins.manHunt.utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import xyz.leonardoarias.plugins.manHunt.Config;
import xyz.leonardoarias.plugins.manHunt.ManHunt;
import xyz.leonardoarias.plugins.manHunt.enums.ManHuntRole;

public class Worker extends BukkitRunnable {

	ManHunt plugin;
	PlayerData playerData;
	Config config;

	public Worker(ManHunt plugin, PlayerData playerData, Config config) {
		this.plugin = plugin;
		this.playerData = playerData;
		this.config = config;
	}

	@Override
	public void run() {
		Set<Player> frozeThisTick = new HashSet<>();
		if (config.freeze()) {
			for (Player player : playerData.getPlayersByRole(ManHuntRole.RUNNER)) {
				Entity target = getTarget(player);
				if (target == null || target.getType() != EntityType.PLAYER)
					continue;
				Player targetPlayer = (Player) target;
				if (playerData.getRole(targetPlayer) != ManHuntRole.HUNTER)
					continue;
				playerData.setFrozen(targetPlayer, true);
				frozeThisTick.add(targetPlayer);
				drawLine(player.getEyeLocation(), targetPlayer.getEyeLocation(), 1);
			}
		}

		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (playerData.isFrozen(player) && !frozeThisTick.contains(player))
				playerData.setFrozen(player, false);
		}
		if (config.isCompassTracking()) {
			for (Player player : playerData.getPlayersByRole(ManHuntRole.HUNTER)) {
				updateCompass(player);
			}
		}

	}

	private void updateCompass(Player player) {
		Player nearest = getNearestSpeedrunner(player);
		if (nearest == null) {
			// Random direction if different world
			if (config.isCompassRandomInDifferentWorlds()) {
				float angle = (float) (Math.random() * Math.PI * 2);
				float dx = (float) (Math.cos(angle) * 5);
				float dz = (float) (Math.sin(angle) * 5);
				player.setCompassTarget(player.getLocation().add(new Vector(dx, 0, dz)));
			}
		} else {
			player.setCompassTarget(nearest.getLocation());
			PlayerInventory inventory = player.getInventory();
			if (inventory.getItemInMainHand().getType() == Material.COMPASS
					|| inventory.getItemInOffHand().getType() == Material.COMPASS) {
				if(config.isDrawLine()) {
					drawDirection(player.getLocation(), nearest.getLocation(), 3);
				}
					player.setCompassTarget(nearest.getLocation());
			}
		}

	}

	public void drawLine(Location location1, Location location2, int i) {
		World w = location1.getWorld();
		Validate.isTrue(location2.getWorld().equals(w), TextFormatter.Color(plugin.prefix + " &aCannot be in different worlds!"));
		double distance = location1.distance(location2);
		Vector p1 = location1.toVector();
		Vector p2 = location2.toVector();
		Vector v = p2.clone().subtract(p1).normalize().multiply(i);
		
		for (double length = 0; length < distance; length += i) {
			Particle.DustOptions d = new Particle.DustOptions(Color.TEAL, 1);
			if(p1.distance(location1.toVector())> 1 && p1.distance(p2) >1) {
				w.spawnParticle(Particle.DRAGON_BREATH, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0,d);
			}
			p1.add(v);
		}
	}
	
	 public void drawDirection(Location point1, Location point2, double space) {
	        World world = point1.getWorld();
	        Validate.isTrue(point2.getWorld().equals(world), TextFormatter.Color(plugin.prefix + " &aCannot be in different worlds!"));
	        Vector p1 = point1.toVector();
	        Vector dir = point2.toVector().clone().subtract(p1).setY(0).normalize().multiply(space);
	        Vector p = p1.add(dir);
	        Particle.DustOptions dust = new Particle.DustOptions(Color.TEAL, 1);
	        world.spawnParticle(Particle.DRAGON_BREATH, p.getX(), p1.getY() + 1.25, p.getZ(), 0, 0, 0, 0, dust);
	    }

	private Player getNearestSpeedrunner(Player player) {
		Location playerLocation = player.getLocation();

		return plugin.getServer().getOnlinePlayers().stream().filter(p -> !p.equals(player))
				.filter(p -> playerData.getRole(p) == ManHuntRole.RUNNER)
				.filter(p -> p.getWorld().equals(player.getWorld()))
				.min(Comparator.comparing(p -> p.getLocation().distance(playerLocation))).orElse(null);
	}

	private LivingEntity getTarget(Player player) {
		int range = 100;
		List<Entity> nearbyEntities = player.getNearbyEntities(range, range, range);
		ArrayList<LivingEntity> entities = new ArrayList<>();
		
		for(Entity e: nearbyEntities) {
			if(e instanceof LivingEntity) {
				entities.add((LivingEntity) e);
			}
		}
		
		LivingEntity target = null;
		BlockIterator bItor = new BlockIterator(player, range);
		Block b;
		Location l;
		int bx,by,bz;
		double ex,ey,ez;
		while(bItor.hasNext()) {
			b = bItor.next();
			if(b.getType() != Material.AIR && b.getType() != Material.WATER) break;
			bx = b.getX();
			by = b.getY();
			bz = b.getZ();
			for(LivingEntity e : entities) {
				l = e.getLocation();
				ex = l.getX();
				ey = l.getY();
				ez = l.getZ();
				if((bx -.15 <= ex && ex <= bx + 1.15) && (bz -.15 <= ez && ez<=bz+1.15) && (by -1 <= ey && ey <= by +1)) {
					target = e;
					break;
				}
			}
		}
		return target;
	}

}
