package kr.or.catdogfoot.usermgt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UserMgt<T extends _UserData>{
	private JavaPlugin plugin;
	private UserConfig<T> config;
	
	private HashMap<String, UserList<T>> teamList = new HashMap<>();
	protected HashMap<UUID, HashSet<UserList<T>>> allUserList = new HashMap<>();
	
	public UserMgt(JavaPlugin plugin) {
		this.plugin = plugin;
		config = new UserConfig<>();
	}
	public void loadConfig() {
		config.load(plugin,this,null);
	}
	public void loadConfig(Class<T> userClass) {
		config.load(plugin,this,userClass);
	}
	public void saveConfig() {
		config.save(plugin, this);
	}
	
	public UserList<T> createTeam(String teamName) {
		if(teamList.containsKey(teamName)) {
			return teamList.get(teamName);
		}else {
			UserList<T> userList = new UserList<>(this,teamName);
			teamList.put(teamName, userList);
			return userList;
		}
	}
	public boolean containsTeam(String teamName) {
		return teamList.containsKey(teamName);
	}
	public boolean containsUser(UUID user) {
		return allUserList.containsKey(user);
	}
	
	public void removeTeam(UserList<T> team) {
		String teamName = team.getTeamName();
		
		for(Iterator<UUID> iter = allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<T>> set = allUserList.get(uuid);
			if(set.contains(team)) {
				if(set.size() <= 1) {
					iter.remove();
				}
				else {
					set.remove(team);
				}
			}
		}
		teamList.remove(teamName);
	}
	public void removeTeam(String teamName) {
		UserList<T> team = teamList.get(teamName);
		
		for(Iterator<UUID> iter = allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<T>> set = allUserList.get(uuid);
			if(set.contains(team)) {
				if(set.size() <= 1) {
					iter.remove();
				}
				else {
					set.remove(team);
				}
			}
		}
		teamList.remove(teamName);
	}
	public void removeUser(UUID user) {
		for(Iterator<UserList<T>> iter = allUserList.get(user).iterator() ; iter.hasNext() ; ) {
			iter.next().list.remove(user);
		}
		allUserList.remove(user);
	}
	
	public UserList<T> getTeam(String teamName) {
		return teamList.get(teamName);
	}
	public Set<String> getTeamList() {
		return teamList.keySet();
	}
	public Set<UUID> getAllUser(){
		return allUserList.keySet();
	}
	public T getUserData(UUID user) {
		HashSet<UserList<T>> data = allUserList.get(user);
		if(data == null) return null;
		
		UserList<T> team = data.iterator().next();
		if(team == null) return null;
		return team.getUser(user);
	}
	
	public void sendMessage(String message) {
		for( UUID u : allUserList.keySet()) {
			Player p = (Player)Bukkit.getOfflinePlayer(u);
			if(p!=null && p.isOnline()) {
				p.sendMessage(message);
			}
		}
	}
	
}
