package ml.govnoed.Manhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	@Override
		public void onEnable() {

		}

		@Override
		public void onDisable() {

		}

		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

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
