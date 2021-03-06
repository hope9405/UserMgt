package kr.or.catdogfoot.usermgt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserList<T extends _UserData> {
	private UserMgt<T> userMgt;
	HashMap<UUID, T> list = new HashMap<>();
	private String teamName;
	
	UserList(UserMgt<T> userMgt, String teamName){
		this.userMgt = userMgt;
		this.teamName = teamName;
	}
	
	public int size() {
		return list.size();
	}
	
	public T getUser(UUID user) {
		return list.get(user);
	}
	public Set<UUID> getUserList() {
		return list.keySet();
	}
	public String getTeamName() {
		return teamName;
	}
	
	public void putUser(UUID user) {
		if(userMgt.containsUser(user)) { 
			T userData = userMgt.getUserData(user);
			list.put(user, userData);
		}else {
			list.put(user,null);
			userMgt.allUserList.put(user, new HashSet<>());
		}
		userMgt.allUserList.get(user).add(this);
	}
	public void putUser(UUID user, T data) {
		if(userMgt.containsUser(user)) { 
			T userData = userMgt.getUserData(user);
			list.put(user, userData);
		}else {
			list.put(user, data);
			userMgt.allUserList.put(user, new HashSet<>());
		}
		userMgt.allUserList.get(user).add(this);
	}
	
	public boolean containsUser(UUID user) {
		return list.containsKey(user);
	}
	public void removeUser(UUID user) {
		HashSet<UserList<T>> userList = userMgt.allUserList.get(user);
		
		userList.remove(this);
		if(userList.size() == 0) {
			userMgt.allUserList.remove(user);
		}
		list.remove(user);
	}
	public void moveTeam(String teamName, UUID user){
		if(list.containsKey(user)) {
			userMgt.getTeam(teamName).putUser(user);;
			removeUser(user);
		}else {
			try {
				throw new Exception(this.teamName+"에 user가 없습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void moveTeam(UserList<T> userList, UUID user) {
		if(list.containsKey(user)) {
			userList.putUser(user);
			removeUser(user);
		}else {
			try {
				throw new Exception(this.teamName+"에 user가 없습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void copyTeam(String teamName, UUID user){
		if(list.containsKey(user)) {
			userMgt.getTeam(teamName).putUser(user);
		}else {
			try {
				throw new Exception(this.teamName+"에 user가 없습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void copyTeam(UserList<T> userList, UUID user) {
		if(list.containsKey(user)) {
			userList.putUser(user);
		}else {
			try {
				throw new Exception(this.teamName+"에 user가 없습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clear() {
		for(Iterator<UUID> iter = userMgt.allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<T>> set = userMgt.allUserList.get(uuid);
			if(set.contains(this)) {
				if(set.size() <= 1) {
					iter.remove();
				}
				else {
					set.remove(this);
				}
			}
		}
		list.clear();
	}
	
	public void sendMessage(String message) {
		for( UUID u : list.keySet()) {
			Player p = (Player)Bukkit.getOfflinePlayer(u);
			if(p!=null && p.isOnline()) {
				p.sendMessage(message);
			}
		}
	}
}
