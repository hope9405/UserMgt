package kr.or.catdogfoot.usermgt;

import java.util.HashMap;
import java.util.Map;

public class UserConfigData {
	private Map<String, Object> datas;
	
	public UserConfigData() {
		datas = new HashMap<String, Object>();
	}
	public void save(String varName, Object var) {
		datas.put(varName, var);
	}
	public boolean getBoolean(String varName) {
		return (boolean)datas.get(varName);
	}
	public short getShort(String varName) {
		return (short)datas.get(varName);
	}
	public int getInt(String varName) {
		return (int)datas.get(varName);
	}
	public long getLong(String varName) {
		return (long)datas.get(varName);
	}
	public float getFloat(String varName) {
		return (float)datas.get(varName);
	}
	public double getDouble(String varName) {
		return (double)datas.get(varName);
	}
	public char getChar(String varName) {
		return (char)datas.get(varName);
	}
	public String getString(String varName) {
		return (String)datas.get(varName);
	}
	public Object getObject(String varName) {
		return datas.get(varName);
	}
	
	
	void setDatas(Map<String, Object> datas){
		this.datas = datas;
	}
	Map<String,Object> getDatas(){
		return datas;
	}
}
