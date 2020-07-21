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
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public Map<String, Location[]> victims = new HashMap<String, Location[]>();
	public Map<String, Integer> hunters = new HashMap<String, Integer>();
	List<String> victimsInOrder = new ArrayList<String>();
	List<String> huntersInOrder = new ArrayList<String>();
	public int headStart = 15;
	public int compassDelay = 15;
	public boolean movementRestriction = false;
	
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
				sender.sendMessage("[Manhunt] " + "Sorry! This command is players only!");
				return true;
			}
			if (args[0].equalsIgnoreCase("victim")) {


				
				

				if(!(victimsInOrder.contains(sender.getName()))){
				  victimsInOrder.add(sender.getName());
				  sender.sendMessage("[Manhunt] " + "You are now in the victim team!");
				} else{
 					sender.sendMessage("[Manhunt] " + "You are already joined to the victim team!");
				}

				
			}
			if (args[0].equalsIgnoreCase("settings")){
				switch(args.length) {
				case 1:
					//Выводим текущие настройки
					sender.sendMessage("[Manhunt] " + ChatColor.GOLD + "Head Start is " + Double.toString(headStart));
					sender.sendMessage("[Manhunt] " + ChatColor.GOLD + "Compass Delay is " + Double.toString(compassDelay));
					break;
				case 2:
					//Ошибка ввода, добавить значение
					sender.sendMessage("[Manhunt] " + ChatColor.DARK_RED + "Error. Add value after " + args[1]);
					break;
				case 3:
					//Проверяем аргумент настроек
					if (args[1].equalsIgnoreCase("headStart")) {
						try {
							headStart = Integer.parseInt(args[2]);
							Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.GOLD + "HeadStart is set by "+ sender.getName() +" to " + args[2] + " seconds");
						}
						catch (NumberFormatException e)
						{
							sender.sendMessage("[Manhunt] " + ChatColor.DARK_RED + "Error. Write an integer value.");
						}
					}
					if (args[1].equalsIgnoreCase("compassDelay")) {
						try {
							if(Integer.parseInt(args[2]) >= 0) {
								compassDelay = Integer.parseInt(args[2]);
								Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.GOLD + "CompassDelay is set by "+ sender.getName() +" to " + args[2] + " seconds");
							}
							else {
								sender.sendMessage("[Manhunt] " + ChatColor.DARK_RED + "Error. Send an positive integer value or zero.");
							}
						}
						catch (NumberFormatException e)
						{
							sender.sendMessage("[Manhunt] " + ChatColor.DARK_RED + "Error. Send an integer value.");
						}
					}
					break;
				}
			}
			if (args[0].equalsIgnoreCase("leave")) {
				if((victimsInOrder.contains(sender.getName()))){
				
				victimsInOrder.remove(sender.getName());
				sender.sendMessage("[Manhunt] You aren't in the victim team anymore!");
				} else{
					sender.sendMessage("[Manhunt] U stupid~! You weren't in the victim team!");
				}
			}
			if (args[0].equalsIgnoreCase("start")) {
				
				if (gameActive == true) {
					sender.sendMessage("[Manhunt] " + ChatColor.DARK_RED + "The game has already started!");
					return true;
				}
				
				if (victimsInOrder.size() == 0) {
					Bukkit.getServer().broadcastMessage("[Manhunt] Cannot start the game! There are no victims!");
					return true;
				}
				PotionEffect blindnessEff = new PotionEffect(PotionEffectType.BLINDNESS,headStart * 20,100);
				movementRestriction = true;
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!(victimsInOrder.contains(player.getName()))) {
						blindnessEff.apply(player);
						hunters.put(player.getName(), 0);
						huntersInOrder.add(player.getName());
						player.sendMessage("Your role - " + ChatColor.RED + "the Hunter" + ChatColor.WHITE + "! Take down these " + ChatColor.ITALIC + "'speedy' " 
						+ ChatColor.WHITE + "bastards, before they slay the Enderdragon! Сompass will show you the direction where the last victim you chose was! gl & hf :)");
					} else {

						Location[] loc = new Location[compassDelay]; 
						for(int i = 0;i<loc.length;i++) {
							loc[i] = new Location(Bukkit.getWorld("world"),1.0,1.0,1.0);
						}

						victims.put(player.getName(), loc);
						player.sendMessage("Your role - " + ChatColor.BLUE + "the Victim" + ChatColor.WHITE 
								+ "! You must defeat the Enderdragon as fast, as you can! But beware hunters, " + ChatColor.ITALIC + "they are always follow closely..." + ChatColor.WHITE 
								+ " Good luck, you'll really need it ;)");

					}
				}
				
				gameActive = true;
				if(compassDelay != 0) {
					gameRunning();
				}
				runGame();
				Bukkit.getServer().broadcastMessage("");
				Bukkit.getServer().broadcastMessage("");
				Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.GOLD + "The game will start soon!");
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Hunters: " + ChatColor.RED + hunters.keySet());
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Victims: " + ChatColor.BLUE + victims.keySet());
				return true;
				
				
				
			}
			if (args[0].equalsIgnoreCase("stop")) {
				if(gameActive == false) {
					sender.sendMessage("[Manhunt] " + ChatColor.RED + "The game has not been started yet!");
				}else {
					gameActive = false;
					Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.RED + "The game is over!");
					victims.clear();
					hunters.clear();
					victimsInOrder.clear();
					huntersInOrder.clear();
				}
			}
		}
		
		return false;
	}
	
	
	public void runGame() {
		BukkitRunnable game = new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.RED + "The game has started!");
				movementRestriction = false;
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (hunters.containsKey(player.getName())) {
						player.getInventory().addItem(getCompass());
					}
				}

			}
		};
		
		
		game.runTaskLater(this, 20L * headStart);
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
				
				if (counter == compassDelay-1) counter = 0;
				else counter++;

				gameRunning();
			}
		};
		
		
		gameRunning.runTaskLater(this, 20L);
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(movementRestriction == true) {
			if(hunters.containsKey(event.getPlayer().getName())) {
				event.setCancelled(true);
			}
		}
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
						
						if(compassDelay == 0) {
							Location loc = Bukkit.getPlayer(victims.keySet().toArray()[hunters.get(player.getName())].toString()).getLocation();
							player.setCompassTarget(loc);
						}
						else {
							Location[] loc = victims.get(victims.keySet().toArray()[hunters.get(player.getName())]);
							player.setCompassTarget(loc[counter]);
						}
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
		if(gameActive == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		
		if (victims.containsKey(player.getName())) {
			victims.remove(player.getName());
			
			victimsInOrder.remove(victimsInOrder.indexOf(player.getName()));
			player.setGameMode(GameMode.SPECTATOR);
			
			Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.BLUE + player.getName() + " " + ChatColor.DARK_RED + "is dead! They are sprectating now!");
		}
		if(victims.size() == 0) {
			gameActive = false;
			victims.clear();
			hunters.clear();
			victimsInOrder.clear();
			huntersInOrder.clear();
			Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.GOLD + "All victims are dead! The game is over!");
		}
	}
	
	@EventHandler
	public void onSlayingEnderDragon(EntityDeathEvent event) {
		if(event.getEntity() instanceof EnderDragon) {
			gameActive = false;
			Bukkit.getServer().broadcastMessage("[Manhunt] " + ChatColor.GOLD + "Ender Dragon has been slayed! The game is over!");
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
