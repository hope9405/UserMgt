package kr.or.catdogfoot.usermgt;


public interface _UserData{
	public UserConfigData saveConfig(); // UserData를 저장하기 위한 함수
	public void loadConfig(UserConfigData data); // UserData를 불러오기 위한 함수
}