package kr.or.catdogfoot.usermgt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class UserMgt<UserData extends _UserData>{
	private JavaPlugin plugin;
	private UserConfig<UserData> config;
	
	private HashMap<String, UserList<UserData>> teamList = new HashMap<>();
	protected HashMap<UUID, HashSet<UserList<UserData>>> allUserList = new HashMap<>();
	
	public UserMgt() {} // TODO test 용 지우기!!
	// TODO OfflinePlayer를 UUID대체해서 사용할 수 있을 듯 검토 필요
	public UserMgt(JavaPlugin plugin) {
		this.plugin = plugin;
		config = new UserConfig<>();
	}
	public void loadConfig() {
		config.load(plugin,this,null);
	}
	public void loadConfig(Class<UserData> userClass) {
		config.load(plugin,this,userClass);
	}
	public void saveConfig() {
		config.save(plugin, this);
	}
	
	public UserList<UserData> createTeam(String teamName) {
		// 이미 teamName의 팀이 있으면 기존의 팀을 반환.
		if(teamList.containsKey(teamName)) {
			return teamList.get(teamName);
		}else {
			UserList<UserData> userList = new UserList<>(this,teamName);
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
	
	// 팀을 제거 합니다.
	public void removeTeam(UserList<UserData> team) {
		String teamName = team.getTeamName();
		
		for(Iterator<UUID> iter = allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<UserData>> set = allUserList.get(uuid);
			// 제거 하려는 팀에 들어가있고,
			if(set.contains(team)) {
				// 들어가 있는 팀이 1개보다 적으면,
				if(set.size() <= 1) {
					// 통째로 날려버림
					iter.remove();
				}
				// 들어가 있는 팀이 여러개면,
				else {
					// 그 팀만 제거
					set.remove(team);
				}
			}
		}
		teamList.remove(teamName);
	}
	public void removeTeam(String teamName) {
		UserList<UserData> team = teamList.get(teamName);
		
		for(Iterator<UUID> iter = allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<UserData>> set = allUserList.get(uuid);
			// 제거 하려는 팀에 들어가있고,
			if(set.contains(team)) {
				// 들어가 있는 팀이 1개보다 적으면,
				if(set.size() <= 1) {
					// 통째로 날려버림
					iter.remove();
				}
				// 들어가 있는 팀이 여러개면,
				else {
					// 그 팀만 제거
					set.remove(team);
				}
			}
		}
		teamList.remove(teamName);
	}
	// 유저를 제거 합니다.
	public void removeUser(UUID user) {
		for(Iterator<UserList<UserData>> iter = allUserList.get(user).iterator() ; iter.hasNext() ; ) {
			iter.next().list.remove(user);
		}
		allUserList.remove(user);
	}
	
	public UserList<UserData> getTeam(String teamName) {
		return teamList.get(teamName);
	}
	public Set<String> getTeamList() {
		return teamList.keySet();
	}
	public Set<UUID> getAllUser(){
		return allUserList.keySet();
	}
	public UserData getUserData(UUID user) {
		return (UserData) allUserList.get(user).iterator().next().getUser(user);
	}
	
}
