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
	public boolean gameActive = false;
	
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
				for(int i = 0;i<loc.length;i++) {
					loc[i] = new Location(Bukkit.getWorld("world"),i,1.0,1.0);
				}
//				for(Location l : loc) {
//					Bukkit.getServer().broadcastMessage(Double.toString(l.getX()));
//				}
//				Bukkit.getServer().broadcastMessage(loc.toString());
				victims.put(player.getName(), loc);
				
				
			}
			if (args[0].equalsIgnoreCase("leave")) {
				// leave
			}
			if (args[0].equalsIgnoreCase("start")) {
				gameActive = true;
				runGame();
				Bukkit.getServer().broadcastMessage("The game will start soon!");
				
			}
			if (args[0].equalsIgnoreCase("stop")) {
				gameActive = false;
				Bukkit.getServer().broadcastMessage("The game is ended!");
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
				if(gameActive == true) {
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
//					Location[] loc = victims.get("Shibatsui");
//					Bukkit.getServer().broadcastMessage(Double.toString(loc[counter].getX()));
					if (counter == 14) counter = 0;
					else counter++;
					
					gameRunning();
				}
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
