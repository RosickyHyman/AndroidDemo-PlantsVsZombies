package com.hyman.plantvszombies.layer;

import java.util.ArrayList;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.hyman.plantvszombies.utils.CommonUtils;

public class WelcomLayer extends BaseLayer {

	private CCSprite start;

	public WelcomLayer() {
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				SystemClock.sleep(6000);//ģ���������
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				start.setVisible(true);
				setIsTouchEnabled(true);//���������¼�
			}
		}.execute();
		
		init();
		
	}

	/*
	 * �����¼�
	 */
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		
		//�Ѱ�׿����ϵ�е�ת����coco2d�еĵ�
		CGPoint point = this.convertTouchToNodeSpace(event);
		
		CGRect boundingBox = start.getBoundingBox();
		if (CGRect.containsPoint(boundingBox, point)) {
			CommonUtils.changeLayer(new MenuLayer());
		}
		
		return super.ccTouchesBegan(event);
	}
	
	/*
	 *��ʼ��ҳ��
	 */
	private void init() {
		logo();
	}

	/*
	 * չʾlogo
	 * */
	private void logo() {
		CCSprite logo = CCSprite.sprite("image/popcap_logo.png");
		logo.setPosition(winSize.width/2, winSize.height/2);
		this.addChild(logo);
		
		CCHide hide = CCHide.action();//����
		CCDelayTime delayTime = CCDelayTime.action(1);//ͣ��
		CCSequence ccSequence = CCSequence.actions(delayTime,delayTime, hide,delayTime,CCCallFunc.action(this, "loadWelcome"));//��ͣ���������أ�������ͣ��
		
		logo.runAction(ccSequence);
	}

	/*
	 * ������ִ����֮�����
	 */
	public void loadWelcome(){
		CCSprite bg = CCSprite.sprite("image/welcome.jpg");
		bg.setAnchorPoint(0, 0);
		this.addChild(bg);
		
		loading();
	}

	/*
	 * ��������
	 */
	private void loading() {
		
		CCSprite loadSprite = CCSprite.sprite("image/loading/loading_01.png");
		loadSprite.setPosition(winSize.width/2, 40);
		this.addChild(loadSprite);
		
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		String format ="image/loading/loading_%02d.png";
		for (int i = 1; i <=9; i++) {
			CCSpriteFrame spriteFrame = CCSprite.sprite(String.format(format, i)).displayedFrame();
			frames.add(spriteFrame);
		}
		CCAnimation anim=CCAnimation.animation("loading", 0.2f, frames);
		//����֡һ����Ҫѭ�����ţ����Ǵ˴�����Ҫѭ�����ţ������Ҫ�ƶ��ڶ�������Ϊfalse
		CCAnimate ccAnimate = CCAnimate.action(anim,false);
		loadSprite.runAction(ccAnimate);
		
		start = CCSprite.sprite("image/loading/loading_start.png");
		start.setPosition(winSize.width/2, 40);
		start.setVisible(false);
		this.addChild(start);
		
		
	}
	
	
}
