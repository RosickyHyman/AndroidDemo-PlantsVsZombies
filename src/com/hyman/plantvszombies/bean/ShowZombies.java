package com.hyman.plantvszombies.bean;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGRect;

import com.hyman.plantvszombies.utils.CommonUtils;

import android.graphics.Bitmap;

public class ShowZombies extends CCSprite {

	public ShowZombies() {
		super("image/zombies/zombies_1/shake/z_1_01.png");
		setScale(0.5f);
		setAnchorPoint(0.5f, 0);
		CCAction animate = CommonUtils.getAnimate("image/zombies/zombies_1/shake/z_1_%02d.png", 2, true);
		this.runAction(animate);
	}

}
