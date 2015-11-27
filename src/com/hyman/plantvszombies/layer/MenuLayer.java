package com.hyman.plantvszombies.layer;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;

import com.hyman.plantvszombies.utils.CommonUtils;

public class MenuLayer extends BaseLayer {

	public MenuLayer() {
		
		init();
	}

	private void init() {
		
		CCSprite bg = CCSprite.sprite("image/menu/main_menu_bg.jpg");
		bg.setAnchorPoint(0, 0);
		this.addChild(bg);
		
		CCSprite defaultSprite = CCSprite.sprite("image/menu/start_adventure_default.png");
		CCSprite pressSprite = CCSprite.sprite("image/menu/start_adventure_press.png");
		//第一参数代表默认造型，第二蚕食代表选择造型，第三参数代表目标对象，第四参数代表执行方法
		CCMenuItemSprite item = CCMenuItemSprite.item(defaultSprite, pressSprite, this, "click");
		
		CCMenu menu = CCMenu.menu(item);
		menu.setScale(0.5f);
		menu.setPosition(winSize.width/2-25,winSize.height/2-110);
		menu.setRotation(4.5f);
		
		this.addChild(menu);
		
	}
	
	public void click(Object object){
		CommonUtils.changeLayer(new FightLayer());
	}

}
