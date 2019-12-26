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
		// 무결성 유지를 위해 이미 존재하는 user는 data를 가져와서 넣음
		if(userMgt.containsUser(user)) { // 이미 존재하면
			UserData userData = userMgt.getUserData(user);
			list.put(user, userData);
		}else {
			list.put(user,null);
			userMgt.allUserList.put(user, new HashSet<>());
		}
		// allUserList에 새로운 팀을 넣어 줌.
		userMgt.allUserList.get(user).add(this);
	}
	public void putUser(UUID user, UserData data) {
		// 무결성 유지를 위해 이미 존재하는 user는 data를 가져와서 넣음
		if(userMgt.containsUser(user)) { // 이미 존재하면
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
		// 전체 유저 목록에서 이 팀 밖에 들어있지 않다면,
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
				throw new Exception(this.teamName+"팀에 user가 존재하지 않습니다.");
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
				throw new Exception(this.teamName+"팀에 user가 존재하지 않습니다.");
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
				throw new Exception(this.teamName+"팀에 user가 존재하지 않습니다.");
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
				throw new Exception(this.teamName+"팀에 user가 존재하지 않습니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clear() {
		for(Iterator<UUID> iter = userMgt.allUserList.keySet().iterator() ; iter.hasNext();) {
			UUID uuid = iter.next();
			HashSet<UserList<UserData>> set = userMgt.allUserList.get(uuid);
			// 제거 하려는 팀에 들어가있고,
			if(set.contains(this)) {
				// 들어가 있는 팀이 1개보다 적으면,
				if(set.size() <= 1) {
					// 통째로 날려버림
					iter.remove();
				}
				// 들어가 있는 팀이 여러개면,
				else {
					// 그 팀만 제거
					set.remove(this);
				}
			}
		}
		list.clear();
	}
}
