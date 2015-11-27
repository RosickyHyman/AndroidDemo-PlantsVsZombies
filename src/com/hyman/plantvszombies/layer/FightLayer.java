package com.hyman.plantvszombies.layer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

import com.hyman.plantvszombies.bean.ShowPlant;
import com.hyman.plantvszombies.bean.ShowZombies;
import com.hyman.plantvszombies.engine.GameCotroller;
import com.hyman.plantvszombies.utils.CommonUtils;

public class FightLayer extends BaseLayer {

	private CGSize contentSize;
	private CCTMXTiledMap map;
	private CCSprite choseSprite;
	private CCSprite chooseSprite;
	private List<CGPoint> zombilesPoints;
	private List<ShowPlant> showPlants;

	public FightLayer() {

		init();
	}

	private void init() {

		loadMap();
		parserMap();
		showZombies();
		moveMap();
	}

	/*
	 * չʾ��ʬ
	 */
	private void showZombies() {

		for (int i = 0; i < zombilesPoints.size(); i++) {
			CGPoint cgPoint = zombilesPoints.get(i);
			ShowZombies showZombies = new ShowZombies();
			showZombies.setPosition(cgPoint);
			map.addChild(showZombies);
		}

	}

	/*
	 * ������ͼ
	 */
	private void parserMap() {

		zombilesPoints = CommonUtils.getSpritePoint(map, "zombies");

	}

	/*
	 * �ƶ���ͼ
	 */

	private void moveMap() {

		int x = (int) (winSize.width - map.getContentSize().width);

		CCMoveBy by = CCMoveBy.action(2, ccp(x, 0));
		CCDelayTime delayTime = CCDelayTime.action(2);
		CCSequence actions = CCSequence.actions(delayTime, by, delayTime,
				CCCallFunc.action(this, "showContainer"));
		map.runAction(actions);

	}

	/*
	 * ���ص�ͼ
	 */
	private void loadMap() {
		map = CCTMXTiledMap.tiledMap("image/fight/map_day.tmx");
		map.setAnchorPoint(0.5f, 0.5f);
		contentSize = map.getContentSize();
		map.setPosition(contentSize.width / 2, contentSize.height / 2);

		this.addChild(map);
	}

	/*
	 * ��ʾ���� -
	 */

	public void showContainer() {

		// ������ѡ��
		choseSprite = CCSprite.sprite("image/fight/chose/fight_chose.png");
		choseSprite.setAnchorPoint(0, 1);
		choseSprite.setPosition(0, winSize.height);
		this.addChild(choseSprite);

		// ������ѡ��
		chooseSprite = CCSprite.sprite("image/fight/chose/fight_choose.png");
		chooseSprite.setAnchorPoint(0, 0);
		this.addChild(chooseSprite);

		loadShowPlant();

		start = CCSprite.sprite("image/fight/chose/fight_start.png");
		start.setPosition(chooseSprite.getContentSize().width / 2, 30);
		chooseSprite.addChild(start);

	}

	/*
	 * ��ʾֲ��
	 */
	private void loadShowPlant() {

		showPlants = new ArrayList<ShowPlant>();
		for (int i = 1; i <= 9; i++) {

			ShowPlant plant = new ShowPlant(i);

			// ���ñ���ֲ��(��ѡ�к�ʰ�ɫ)
			CCSprite bgSprite = plant.getBgSprite();
			bgSprite.setPosition(16 + ((i - 1) % 4) * 56,
					175 - ((i - 1) / 4) * 59);
			chooseSprite.addChild(bgSprite);

			// ����ǰ��ֲ��(δ��ѡ��)
			CCSprite sprite = plant.getSprite();
			sprite.setPosition(16 + ((i - 1) % 4) * 56,
					175 - ((i - 1) / 4) * 59);
			chooseSprite.addChild(sprite);

			showPlants.add(plant);
		}

		System.out.println(showPlants.size());
		setIsTouchEnabled(true);
	}

	public void unlock() {
		islock = false;
	}

	private List<ShowPlant> selectPlants = new CopyOnWriteArrayList<ShowPlant>();
	boolean islock;
	boolean isdel;
	private CCSprite start;

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {

		// android��ת����cocos��
		CGPoint point = this.convertTouchToNodeSpace(event);
		// ��ѡ��ֲ��ı߿�
		CGRect boundingBox = chooseSprite.getBoundingBox();
		// ��ѡ��ֲ��ı߿�
		CGRect choseBox = choseSprite.getBoundingBox();
		// ��ѡֲ��
		if (CGRect.containsPoint(choseBox, point)) {
			isdel = false;
			for (ShowPlant showPlant : selectPlants) {

				CGRect selectBox = showPlant.getSprite().getBoundingBox();
				if (CGRect.containsPoint(selectBox, point)) {
					CCMoveTo moveTo = CCMoveTo.action(1, showPlant
							.getBgSprite().getPosition());
					showPlant.getSprite().runAction(moveTo);
					selectPlants.remove(showPlant);
					isdel = true;
					continue;// ������ִ���´�ѭ��
				}
				if (isdel) {
					CCMoveBy action = CCMoveBy.action(0.2f, ccp(-53, 0));
					showPlant.getSprite().runAction(action);
				}

			}
		} else if (CGRect.containsPoint(boundingBox, point)) {
			if (CGRect.containsPoint(start.getBoundingBox(), point)) {
				
				ready();

			} else if (selectPlants.size() < 5 && !islock) {

				for (ShowPlant showPlant : showPlants) {

					CGRect plantBox = showPlant.getSprite().getBoundingBox();
					if (CGRect.containsPoint(plantBox, point)) {
						// ѡ�к���������
						islock = true;
						CCMoveTo moveTo = CCMoveTo.action(0.2f,ccp(75 + selectPlants.size() * 53, 255));
						CCSequence actions = CCSequence.actions(moveTo,
								CCCallFunc.action(this, "unlock"));
						showPlant.getSprite().runAction(actions);
						selectPlants.add(showPlant);
					}
				}
			}
		}
		return super.ccTouchesBegan(event);
	}
	
	
	/*
	 * ���һ����ҡ��
	 * 
	 */
	private void ready(){
		
		// ��С�����ѡֲ������
				choseSprite.setScale(0.65f);
				// ��ѡ�е�ֲ��������ӵ� ���ڵ�������
				for(ShowPlant plant:selectPlants){

					plant.getSprite().setScale(0.65f);// ��Ϊ��������С�� ����һ����С

					plant.getSprite().setPosition(
							plant.getSprite().getPosition().x * 0.65f,
							plant.getSprite().getPosition().y
							+ (CCDirector.sharedDirector().getWinSize().height - plant.getSprite().getPosition().y)* 0.35f);// ��������
					this.addChild(plant.getSprite());
				}
		
		if (selectPlants.size()<5) {
			return;
		}else {
			//�Ƴ���ѡ��
			chooseSprite.removeSelf();
			//�ƶ���ͼ
			int x = (int) (map.getContentSize().width-winSize.width);
			CCMoveBy by = CCMoveBy.action(2, ccp(x, 0));
			CCSequence sequence=CCSequence.actions(by, CCCallFunc.action(this, "preGame"));
			map.runAction(sequence);	
		}
	}
	
	
	private CCSprite ready;
	public void preGame(){
		ready=CCSprite.sprite("image/fight/startready_01.png");
		ready.setPosition(winSize.width/2, winSize.height/2);
		this.addChild(ready);
		String format="image/fight/startready_%02d.png";
		CCAction animate = CommonUtils.getAnimate(format, 3, false);
		CCSequence sequence=CCSequence.actions((CCAnimate)animate, CCCallFunc.action(this, "startGame"));
		ready.runAction(sequence);
	}
	public void startGame(){
		ready.removeSelf();// �Ƴ��м������֡
		
		GameCotroller cotroller=GameCotroller.getInstance();
		cotroller.startGame(map,selectPlants);
	
	}
	
	
}
