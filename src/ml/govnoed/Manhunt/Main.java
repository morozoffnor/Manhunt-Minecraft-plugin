package ml.govnoed.Manhunt;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public Map<String, Player> speedrunners = new HashMap<String, Player>();
	public Map<String, Player> hunters = new HashMap<String, Player>();
	
	@Override
		public void onEnable() {

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
			
			return false;
		}
		
		
		@EventHandler
		public void onMove(PlayerMoveEvent event) {
			// code here
		}
		
		@EventHandler
		public void onClick(PlayerInteractEvent event) {
			// event.getAction() == Action.RIGHT_CLICK_AIR
		}

}
