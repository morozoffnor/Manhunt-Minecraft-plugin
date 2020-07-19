package ml.govnoed.Manhunt;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	
	public Map<String, Location[]> victims = new HashMap<String, Location[]>();
	public Map<String, Location[]> hunters = new HashMap<String, Location[]>();
	public int counter = 0;
	
	@Override
	public void onEnable() {
	
		this.getServer().getPluginManager().registerEvents(this, this);
		
	}

	
	@Override
	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("manhunt")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Sorry! This command is players only!");
				return true;
			}
			if (args[0].equalsIgnoreCase("victim")) {
				
				Player player = (Player) sender;
				Location[] loc = new Location[15];
				Location dummy = new Location(Bukkit.getWorld("world1121"), 1.0, 1.0, 1.0);
				int jopa = 0;
				
				
//				Bukkit.getServer().broadcastMessage(loc.toString());
				victims.put(player.getName(), loc);
				
				
			}
			if (args[0].equalsIgnoreCase("leave")) {
				// leave
			}
			if (args[0].equalsIgnoreCase("start")) {
				
				runGame();
				Bukkit.getServer().broadcastMessage("The game will start soon!");
			}
		}
		
		return false;
	}
	
	
	public void runGame() {
		BukkitRunnable game = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				gameRunning();
				
			}
		};
		
		
		game.runTaskLater(this, 20L * 15);
	}
	
	public void gameRunning() {
		BukkitRunnable gameRunning = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (victims.containsKey(player.getName())) {
						// забрать массив из мапы
						Location[] loc = victims.get(player.getName());
						// поменять значение в массиве по индексу
						loc[counter] = player.getLocation();
						// положить обратно где взяли
						victims.put(player.getName(), loc);
					}
				}
				if (counter == 14) counter = 0;
				else counter++;
				
				// debug
				Location[] loc = victims.get("_morozoff");
				Bukkit.getServer().broadcastMessage(Double.toString(loc[counter].getX()));
				
				
				// debug
			}
		};
		
		
		gameRunning.runTaskLater(this, 20L);
	}
	
	
	
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		// event.getAction() == Action.RIGHT_CLICK_AIR
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		// check deaths
	}

}
