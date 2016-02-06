package com.hflrobotics.scouting;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Configuration
{

	JSONObject jsonObject;
	
	public Configuration(String configLocation)
	{
		try
		{
			FileReader reader = new FileReader(configLocation);
			jsonObject = (JSONObject) new JSONParser().parse(reader);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getConfigSetting(String key)
	{
		return jsonObject.get(key).toString();
	}
	
	public String[] getConfigSettingArray(String arrayKey, String key)
	{
		JSONArray jsonArray = (JSONArray) jsonObject.get(arrayKey);
		String[] result = new String[jsonArray.size()];
		
		for(int i = 0; i < jsonArray.size(); i++) 
		{
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			result[i] = (String) jsonObject.get(key);
		}
		
		return result;
	}
	
	public void test(String arrayKey, String key)
	{
		JSONArray jsonMainArr = (JSONArray) jsonObject.get(arrayKey);

		for(int i = 0; i < jsonMainArr.size(); i++) 
		{
			JSONObject childJSONObject = (JSONObject) jsonMainArr.get(i);
			System.out.println(childJSONObject.get(key));
		}
	}
}
