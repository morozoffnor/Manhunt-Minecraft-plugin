package ml.govnoed.Manhunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Tab implements TabCompleter {
	
	List<String> arguments1 = new ArrayList<String>();
	List<String> arguments2 = new ArrayList<String>();
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (arguments1.isEmpty()) {
			arguments1.add("victim");
			arguments1.add("start");
			arguments1.add("leave");
			arguments1.add("settings");
			arguments1.add("stop");
		}
		if (arguments2.isEmpty()) {
			arguments2.add("headStart");
			arguments2.add("compassDelay");
		}
		
		List<String> result = new ArrayList<String>();
		switch(args.length) {
		case 1:
			for (String a : arguments1) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(a);
			}
			return result;
		case 2:
			for (String a : arguments2) {
				if (a.toLowerCase().startsWith(args[1].toLowerCase()))
					result.add(a);
			}
			return result;
		}
		
		return null;
	}

}