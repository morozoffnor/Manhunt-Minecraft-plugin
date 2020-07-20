package ml.govnoed.Manhunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public Map<String, Location[]> victims = new HashMap<String, Location[]>();
	public Map<String, Integer> hunters = new HashMap<String, Integer>();
	List<String> victimsInOrder = new ArrayList<String>();
	List<String> huntersInOrder = new ArrayList<String>();
	
	
	public int counter = 0;
	public boolean gameActive = false;
	
	@Override
	public void onEnable() {
	
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("manhunt").setTabCompleter(new Tab());
		
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
				if(!(Arrays.asList(player.getName()).contains(player.getName()))){
				Player player = (Player) sender;
				Location[] loc = new Location[15];
				for(int i = 0;i<loc.length;i++) {
					loc[i] = new Location(Bukkit.getWorld("world"),i,1.0,1.0);
				}
				
				victims.put(player.getName(), loc);
				victimsInOrder.add(player.getName());
				sender.sendMessage('You are now in the victim team!')
				} else{
 					sender.sendMessage("You are already joined to the victim team!")
				}
				
			}
			if (args[0].equalsIgnoreCase("leave")) {
				if((Arrays.asList(player.getName()).contains(player.getName()))){
				victim.remove(player.getName(),loc)
				victimInOrder.remove(player.getName())
				sender.sendMessage("You aren't in the victim team anymore!")
				} else{
					sender.sendMessage("U stupid~! You weren't in the victim team!")
				}
			}
			if (args[0].equalsIgnoreCase("start")) {
				
				if (gameActive == true) {
					sender.sendMessage(ChatColor.DARK_RED + "The game has already started!");
					return true;
				}

				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!(victims.containsKey(player.getName()))) {
						hunters.put(player.getName(), 0);
						huntersInOrder.add(player.getName());
						player.sendMessage("Your role - the hunter! Take down these 'speedy' bustards, before they slay the Enderdragon! Сompass will show you the direction where the last victim you chose was! gl & hf :)");
					} else {
						player.sendMessage("Your role - the victim! You must defeat the Enderdragon as faster, as you can! But beware hunters, they are always in your footsteps... Good luck, you'll really need it ;)");
					}
				}
				
				gameActive = true;
				gameRunning();
				runGame();
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "The game will start soon!");
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Hunters: " + ChatColor.RED + hunters.keySet());
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Victims: " + ChatColor.BLUE + victims.keySet());
				return true;
				
				
				
			}
			if (args[0].equalsIgnoreCase("stop")) {
				gameActive = false;
				Bukkit.getServer().broadcastMessage(ChatColor.RED + "The game is ended!");

			}
		}
		
		return false;
	}
	
	
	public void runGame() {
		BukkitRunnable game = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (hunters.containsKey(player.getName())) {
						player.getInventory().addItem(getCompass());
					}
				}

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
						// Bukkit.getServer().broadcastMessage("добавляю локацию в массив");
					}
				}
				if (counter == 14) counter = 0;
				else counter++;

				gameRunning();
			}
		};
		
		
		gameRunning.runTaskLater(this, 20L);
	}
	
	
	
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {

			Player player = event.getPlayer();
			if (hunters.containsKey(player.getName())) {

				if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {

					if (player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {

						
						if (hunters.get(player.getName()) == victimsInOrder.size() - 1) hunters.put(player.getName(), 0);
						else hunters.put(player.getName(), hunters.get(player.getName()) + 1);
						
						
						Environment env = Bukkit.getPlayer((String) victims.keySet().toArray()[hunters.get(player.getName())]).getWorld().getEnvironment();
						
						if (env == Environment.NETHER) {
							player.sendMessage(ChatColor.DARK_RED + "Cannot track player " + victims.keySet().toArray()[hunters.get(player.getName())]);
							return;
						}
						player.sendMessage(ChatColor.GREEN + "Your compass is tracking " + victims.keySet().toArray()[hunters.get(player.getName())]);
						
						
						Location[] loc = victims.get(victims.keySet().toArray()[hunters.get(player.getName())]);
						
						player.setCompassTarget(loc[counter]);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		
		Player player = event.getPlayer();
		
		if (hunters.containsKey(player.getName())) {
			player.getInventory().addItem(getCompass());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		Player player = (Player) event.getEntity();
		
		if (victims.containsKey(player.getName())) {
			victims.remove(player.getName());
			
			victimsInOrder.remove(victimsInOrder.indexOf(player.getName()));
			player.setGameMode(GameMode.SPECTATOR);
			
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getName() + " " + ChatColor.DARK_RED + "is dead! They are sprectating now!");
		}
	}
	
	public ItemStack getCompass() {
		ItemStack compass = new ItemStack(Material.COMPASS);
		
		ItemMeta meta = compass.getItemMeta();
		
		meta.setDisplayName("Hunter's compass");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7(R) &5Find victim"));
		meta.setLore(lore);
		
		meta.addEnchant(Enchantment.MULTISHOT, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setUnbreakable(true);
		
		compass.setItemMeta(meta);
		
		return compass;
	}

}
