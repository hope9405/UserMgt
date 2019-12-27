package kr.or.catdogfoot.usermgt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class UserList<UserData extends _UserData> {
	private UserMgt<UserData> userMgt;
	HashMap<UUID, UserData> list = new HashMap<>();
	private String teamName;
	
	UserList(UserMgt<UserData> userMgt, String teamName){
		this.userMgt = userMgt;
		this.teamName = teamName;
	}
	
	public int size() {
		return list.size();
	}
	
	public UserData getUser(UUID user) {
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
			UserData userData = userMgt.getUserData(user);
			list.put(user, userData);
		}else {
			list.put(user,null);
			userMgt.allUserList.put(user, new HashSet<>());
		}
		userMgt.allUserList.get(user).add(this);
	}
	public void putUser(UUID user, UserData data) {
		if(userMgt.containsUser(user)) { 
			UserData userData = userMgt.getUserData(user);
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
		HashSet<UserList<UserData>> userList = userMgt.allUserList.get(user);
		
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
	public void moveTeam(UserList<UserData> userList, UUID user) {
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
	public void copyTeam(UserList<UserData> userList, UUID user) {
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
			HashSet<UserList<UserData>> set = userMgt.allUserList.get(uuid);
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
}
