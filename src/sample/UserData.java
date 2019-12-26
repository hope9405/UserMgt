package sample;

import kr.or.catdogfoot.usermgt.UserConfigData;
import kr.or.catdogfoot.usermgt._UserData;

public class UserData implements _UserData{
	private int hp;
	public UserData(){}
	public UserData(int hp){
		this.hp = hp;
	}
	
	@Override
	public void loadConfig(UserConfigData data) {
		hp = data.getInt("hp");
	}

	@Override
	public UserConfigData saveConfig() {
		UserConfigData data = new UserConfigData();
		data.save("hp", hp);
		return data;
	}
	
	public int getHp() {
		return hp;
	}
}
