package com.hyman.plantvszombies.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.transitions.CCFlipXTransition;
import org.cocos2d.types.CGPoint;

public class CommonUtils {

	public CommonUtils() {
		
	}

	/*
	 * 改变图层
	 */
	public static void changeLayer(CCLayer newLayer) {
		
		CCScene ccScene = CCScene.node();
		ccScene.addChild(newLayer);
		CCFlipXTransition transition = CCFlipXTransition.transition(1, ccScene, 0);
		CCDirector.sharedDirector().replaceScene(transition);
		
	}

	/*
	 * 解析地图
	 */
	public static List<CGPoint> getSpritePoint(CCTMXTiledMap map,String name) {
		
		ArrayList<CGPoint> points = new ArrayList<CGPoint>();
		CCTMXObjectGroup objectGroupNamed = map.objectGroupNamed(name);
		
		ArrayList<HashMap<String, String>> objects=objectGroupNamed.objects;
		
		for (HashMap<String, String> hashMap : objects) {
			int x = Integer.parseInt(hashMap.get("x"));
			int y = Integer.parseInt(hashMap.get("y"));
			CGPoint cgPoint = CCNode.ccp(x, y);
			points.add(cgPoint);
		}
		return points;
	}

	
	/*
	 * 创建帧动画
	 */
	
	public  static CCAction getAnimate(String format,int num,boolean isForerver){
		ArrayList<CCSpriteFrame> frames=new ArrayList<CCSpriteFrame>();
		for(int i=1;i<=num;i++){
			CCSpriteFrame spriteFrame = CCSprite.sprite(String.format(format, i)).displayedFrame();
			frames.add(spriteFrame);
		}
		CCAnimation anim=CCAnimation.animation("", 0.5f, frames);
		// 序列帧一般必须永不停止的播放  不需要永不停止播放,需要指定第二个参数 false
		if(isForerver){
			CCAnimate animate=CCAnimate.action(anim);
			CCRepeatForever forever=CCRepeatForever.action(animate);
			return forever;
		}else{
			CCAnimate animate=CCAnimate.action(anim,false);
			return animate;
		}
		
	}
	
	
}
