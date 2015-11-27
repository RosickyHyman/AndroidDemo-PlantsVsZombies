package com.hyman.plantvszombies.bean;

import java.util.HashMap;

import org.cocos2d.nodes.CCSprite;

public class ShowPlant {

	
	static HashMap<Integer, HashMap<String, String>> db ;
	private CCSprite sprite;
	private CCSprite bgSprite;
	//模拟数据库
	static{
		db= new HashMap<Integer, HashMap<String,String>>();
		String format = "image/fight/chose/choose_default%02d.png";
		for (int i = 0; i <= 9; i++) {
			
			HashMap<String, String> value = new HashMap<String, String>();
			value.put("path", String.format(format, i));
			value.put("sun", 50+"");
			
			db.put(i, value);
		}

	}
	

	
	public ShowPlant(int id) {
		
		HashMap<String, String> hashMap = db.get(id);
		String path = hashMap.get("path");
		sprite = CCSprite.sprite(path);
		sprite.setAnchorPoint(0,0);
		
		bgSprite = CCSprite.sprite(path);
		bgSprite.setOpacity(150);//半透明效果
		bgSprite.setAnchorPoint(0,0);	
		
	}



	public CCSprite getSprite() {
		return sprite;
	}




	public CCSprite getBgSprite() {
		return bgSprite;
	}


	
	
}
