package kr.or.catdogfoot.usermgt;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UserConfig<UserData extends _UserData> {
	public void save(JavaPlugin plugin, UserMgt<UserData> userMgt) {
		File file = new File(plugin.getDataFolder(),File.separator); // File.separator: window = \\, Linux = /
		File configFile = new File(file,File.separator+"UserMgt.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		config.createSection("AllUserList");
		
		for(Iterator<Entry<UUID, HashSet<UserList<UserData>>>> iter = userMgt.allUserList.entrySet().iterator(); iter.hasNext();) {
			Entry<UUID, HashSet<UserList<UserData>>> data = iter.next();
			UUID uuid = data.getKey();
			HashSet<UserList<UserData>> set = data.getValue();
			
			ConfigurationSection section = config.createSection("AllUserList."+uuid);
			
			ArrayList<String> arr = new ArrayList<String>();
		
			for(Iterator<UserList<UserData>> iterList = set.iterator() ; iterList.hasNext();) {
				UserList<UserData> userList = iterList.next();
				arr.add(userList.getTeamName());
			}
			section.set("team", arr);
			
			if(userMgt.getUserData(uuid) != null) {
				UserConfigData configData = userMgt.getUserData(uuid).saveConfig();
				
				if(configData != null) {
					section.set("data",configData.getDatas());
				}
			}
		}
		
		try {
			config.save(configFile);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	public void load(JavaPlugin plugin, UserMgt<UserData> userMgt, Class<UserData> userClass){
		File file = new File(plugin.getDataFolder(),File.separator);
		File configFile = new File(file,File.separator+"UserMgt.yml");
		
		if(!configFile.exists()) return;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		ConfigurationSection section = config.getConfigurationSection("AllUserList");
		
		if(section == null) return;
		
		for(String uuidStr : section.getKeys(false)) {
			ConfigurationSection user = section.getConfigurationSection(uuidStr);
			UUID uuid = UUID.fromString(uuidStr);
			
			UserData userData = null;
			
			if(userClass != null) {
				try {
					userData = userClass.getDeclaredConstructor().newInstance();
					UserConfigData configData = new UserConfigData();
					configData.setDatas(user.getConfigurationSection("data").getValues(false));
					userData.loadConfig(configData);
				}catch(NoSuchMethodException e) { e.printStackTrace();
				}catch(InvocationTargetException e) { e.printStackTrace();
				}catch(IllegalAccessException e) { e.printStackTrace();
				}catch(InstantiationException e) { e.printStackTrace();}
			}
			
			// team
			List<String> team = user.getStringList("team");
			for(Iterator<String> iter = team.iterator();iter.hasNext();) {
				String teamName = iter.next();
				userMgt.createTeam(teamName).putUser(uuid,userData);
			}
			
		}
	}
}
