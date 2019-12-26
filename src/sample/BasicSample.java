package sample;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import kr.or.catdogfoot.usermgt.UserList;
import kr.or.catdogfoot.usermgt.UserMgt;

public class BasicSample extends JavaPlugin implements CommandExecutor {
	UserMgt<UserData> userMgt;
	@Override
	public void onEnable() {
		// init
		userMgt = new UserMgt<>(this);
		userMgt.loadConfig(UserData.class);
		
		getCommand("sample").setExecutor(this);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equals("sample")) {
			if(args.length != 0) {
				// create Team
				// arg[1] - team name
				if(args[0].equalsIgnoreCase("team")) {
					if(args.length != 1) {
						userMgt.createTeam(args[1]);
					}else {
						sender.sendMessage("put team name");
					}
				}	
				// show teams
				// arg[1] - team name
				else if(args[0].equalsIgnoreCase("show")) {
					if(args.length != 1) {
						if(args[1].equalsIgnoreCase("team")) {
							sender.sendMessage(userMgt.getTeamList().toString());
						}else if(args[1].equalsIgnoreCase("user")) {
							if(args.length != 2) {
								UserData data = userMgt.getUserData(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
								
								if(data != null) {
									sender.sendMessage(args[2] + " = "+data.getHp());
								}else {
									sender.sendMessage("not user");
								}
							}else {
								sender.sendMessage("put user name");
							}
						}
					}else {
						sender.sendMessage("put command");
					}
				}
				
				// put user
				// arg[1] - team name
				// arg[2] - user name
				// arg[3] - hp
				else if(args[0].equalsIgnoreCase("put")) {
					if(!(args.length < 4)) {
						UserList<UserData> team = userMgt.getTeam(args[1]);
						if(team != null) {
							try {
								OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
								if(p == null) return false;
								
								UserData data = new UserData(Integer.parseInt(args[3]));
								team.putUser(p.getUniqueId(),data);
							}catch(NumberFormatException e) {
								sender.sendMessage("plz, write integer");
							}
						}else {
							sender.sendMessage("not exist team");
						}
					}else {
						sender.sendMessage("put arguments");
					}
				}
			}
		}
		return false;
	}


	@Override
	public void onDisable() {
		userMgt.saveConfig();
	}
}