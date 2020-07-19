package ml.govnoed.Manhunt;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
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
					// join here
				}
				if (args[0].equalsIgnoreCase("leave")) {
					// leave
				}
			}
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
			
			return false;
		}
		}
		
		
		@EventHandler
		public void onMove(PlayerMoveEvent event) {
			// code here
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
