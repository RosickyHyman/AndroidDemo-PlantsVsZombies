package com.hyman.plantvszombies.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.types.CGPoint;

import com.hyman.plantvszombies.bean.PrimaryZombies;
import com.hyman.plantvszombies.bean.ShowPlant;
import com.hyman.plantvszombies.utils.CommonUtils;
/*
 * 
 * 处理游戏开始后的工作
 */
public class GameCotroller {

	private CCTMXTiledMap map;
	private List<ShowPlant> selectPlants;
	private static List<FightLine> fightLines;

	public GameCotroller() {
	}

	private static GameCotroller cotroller=new GameCotroller();
	private boolean isStart;
	private List<CGPoint> roadPoint;
	public static GameCotroller getInstance() {
		
		return cotroller;
	}

	public void startGame(CCTMXTiledMap map, List<ShowPlant> selectPlants) {
		isStart = true;
		this.map = map;
		this.selectPlants = selectPlants;
		
		loadMap();
		//定时器，参数1，执行动作。参数2，要执行的对象。参数3，间隔。参数4，是否暂停
		CCScheduler.sharedScheduler().schedule("addZombies", this, 8,false);
		//addZombies();
	}

	/*
	 * 创建僵尸
	 * */
	public void addZombies(float t) {
		
		Random random = new Random();
		int number = random.nextInt(5);
		
		PrimaryZombies primaryZombies = new PrimaryZombies(roadPoint.get(number*2), roadPoint.get(number*2+1));
		map.addChild(primaryZombies);
		fightLines.get(number).addZombies(primaryZombies);
		
	}

	/*
	 * 加载地图
	 * */
	private void loadMap(){
		
		roadPoint = CommonUtils.getSpritePoint(map, "road");
	}
	/*
	 * 管理每一行
	 */
	static {
		fightLines = new ArrayList<FightLine>();
		for (int i = 0; i <5; i++) {
			FightLine fightLine = new FightLine(i);
			fightLines.add(fightLine);
		}
	}
	
	public void endGame() {
		isStart = false;
	}
}
