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
	 * 展示僵尸
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
	 * 解析地图
	 */
	private void parserMap() {

		zombilesPoints = CommonUtils.getSpritePoint(map, "zombies");

	}

	/*
	 * 移动地图
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
	 * 加载地图
	 */
	private void loadMap() {
		map = CCTMXTiledMap.tiledMap("image/fight/map_day.tmx");
		map.setAnchorPoint(0.5f, 0.5f);
		contentSize = map.getContentSize();
		map.setPosition(contentSize.width / 2, contentSize.height / 2);

		this.addChild(map);
	}

	/*
	 * 显示容器 -
	 */

	public void showContainer() {

		// 创建已选框
		choseSprite = CCSprite.sprite("image/fight/chose/fight_chose.png");
		choseSprite.setAnchorPoint(0, 1);
		choseSprite.setPosition(0, winSize.height);
		this.addChild(choseSprite);

		// 创建备选框
		chooseSprite = CCSprite.sprite("image/fight/chose/fight_choose.png");
		chooseSprite.setAnchorPoint(0, 0);
		this.addChild(chooseSprite);

		loadShowPlant();

		start = CCSprite.sprite("image/fight/chose/fight_start.png");
		start.setPosition(chooseSprite.getContentSize().width / 2, 30);
		chooseSprite.addChild(start);

	}

	/*
	 * 显示植物
	 */
	private void loadShowPlant() {

		showPlants = new ArrayList<ShowPlant>();
		for (int i = 1; i <= 9; i++) {

			ShowPlant plant = new ShowPlant(i);

			// 设置背景植物(被选中后呈暗色)
			CCSprite bgSprite = plant.getBgSprite();
			bgSprite.setPosition(16 + ((i - 1) % 4) * 56,
					175 - ((i - 1) / 4) * 59);
			chooseSprite.addChild(bgSprite);

			// 设置前景植物(未被选中)
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

		// android点转化成cocos点
		CGPoint point = this.convertTouchToNodeSpace(event);
		// 备选框植物的边框
		CGRect boundingBox = chooseSprite.getBoundingBox();
		// 已选框植物的边框
		CGRect choseBox = choseSprite.getBoundingBox();
		// 反选植物
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
					continue;// 跳出，执行下次循环
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
						// 选中后立即加锁
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
	 * 点击一起来摇滚
	 * 
	 */
	private void ready(){
		
		// 缩小玩家已选植物容器
				choseSprite.setScale(0.65f);
				// 把选中的植物重新添加到 存在的容器上
				for(ShowPlant plant:selectPlants){

					plant.getSprite().setScale(0.65f);// 因为父容器缩小了 孩子一起缩小

					plant.getSprite().setPosition(
							plant.getSprite().getPosition().x * 0.65f,
							plant.getSprite().getPosition().y
							+ (CCDirector.sharedDirector().getWinSize().height - plant.getSprite().getPosition().y)* 0.35f);// 设置坐标
					this.addChild(plant.getSprite());
				}
		
		if (selectPlants.size()<5) {
			return;
		}else {
			//移除备选框
			chooseSprite.removeSelf();
			//移动地图
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
		ready.removeSelf();// 移除中间的序列帧
		
		GameCotroller cotroller=GameCotroller.getInstance();
		cotroller.startGame(map,selectPlants);
	
	}
	
	
}
