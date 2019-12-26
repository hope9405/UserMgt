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
	
	public UserMgt() {} // TODO test �� �����!!
	// TODO OfflinePlayer�� UUID��ü�ؼ� ����� �� ���� �� ���� �ʿ�
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
		// �̹� teamName�� ���� ������ ������ ���� ��ȯ.
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
	
	// ���� ���� �մϴ�.
	public void removeTeam(UserList<UserData> team) {
		String teamName = team.getTeamName();
		
		for(Iterator<UUID> iter = allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<UserData>> set = allUserList.get(uuid);
			// ���� �Ϸ��� ���� ���ְ�,
			if(set.contains(team)) {
				// �� �ִ� ���� 1������ ������,
				if(set.size() <= 1) {
					// ��°�� ��������
					iter.remove();
				}
				// �� �ִ� ���� ��������,
				else {
					// �� ���� ����
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
			// ���� �Ϸ��� ���� ���ְ�,
			if(set.contains(team)) {
				// �� �ִ� ���� 1������ ������,
				if(set.size() <= 1) {
					// ��°�� ��������
					iter.remove();
				}
				// �� �ִ� ���� ��������,
				else {
					// �� ���� ����
					set.remove(team);
				}
			}
		}
		teamList.remove(teamName);
	}
	// ������ ���� �մϴ�.
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
