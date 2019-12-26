package test;


import kr.or.catdogfoot.usermgt.UserConfigData;
import kr.or.catdogfoot.usermgt._UserData;

public class UserData implements _UserData{
	int idx = 0;
	UserData(int idx){
		this.idx = idx;
	}
	UserData(){
		
	}
	@Override
	public UserConfigData saveConfig() {
		return null;
	}
	@Override
	public void loadConfig(UserConfigData data) {
		
	}
}
