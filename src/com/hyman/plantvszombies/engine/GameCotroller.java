package com.hyman.plantvszombies.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.CCScheduler;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import com.hyman.plantvszombies.base.Plant;
import com.hyman.plantvszombies.bean.Nut;
import com.hyman.plantvszombies.bean.PeasePlant;
import com.hyman.plantvszombies.bean.PrimaryZombies;
import com.hyman.plantvszombies.bean.ShowPlant;
import com.hyman.plantvszombies.layer.FightLayer;
import com.hyman.plantvszombies.utils.CommonUtils;

/*
 * 
 * 处理游戏开始后的工作
 */
public class GameCotroller {

	private CCTMXTiledMap map;
	private List<ShowPlant> selectPlants;
	private static List<FightLine> fightLines;
	

	private ShowPlant selectPlant; // 玩家选择的植物
	private Plant installPlant;

	public GameCotroller() {
	}

	private static GameCotroller cotroller = new GameCotroller();
	public static boolean isStart;
	private List<CGPoint> roadPoint;

	public static GameCotroller getInstance() {

		return cotroller;
	}

	public void startGame(CCTMXTiledMap map, List<ShowPlant> selectPlants) {
		isStart = true;
		this.map = map;
		this.selectPlants = selectPlants;

		loadMap();
		// 定时器，参数1，执行动作。参数2，要执行的对象。参数3，间隔。参数4，是否暂停
		CCScheduler.sharedScheduler().schedule("addZombies", this, 5, false);
		// addZombies();
		progress();
	}

	/*
	 * 加载地图
	 */
	CGPoint[][] towers=new CGPoint[5][9];
	private void loadMap() {

		roadPoint = CommonUtils.getSpritePoint(map, "road");
		
		//修正植物安放地点
		for (int i = 1; i <= 5; i++) {
			List<CGPoint> towerPoints = CommonUtils.getSpritePoint(map, String.format("tower%02d", i));
			for (int j = 0; j < towerPoints.size(); j++) {
				towers[i-1][j]=towerPoints.get(j);
			}
		}
		
	}

	/*
	 * 管理每一行
	 */
	static {
		fightLines = new ArrayList<FightLine>();
		for (int i = 0; i < 5; i++) {
			FightLine fightLine = new FightLine(i);
			fightLines.add(fightLine);
		}
	}

	public void endGame() {
		isStart = false;
	}
	
	/*
	 * 创建僵尸
	 */
	public void addZombies(float t) {

		Random random = new Random();
		int number = random.nextInt(5);

		PrimaryZombies primaryZombies = new PrimaryZombies(
				roadPoint.get(number * 2), roadPoint.get(number * 2 + 1));
		map.addChild(primaryZombies,1);//指定优先级，让僵尸一直在植物上面
		fightLines.get(number).addZombies(primaryZombies);
		
		progress+=5;
		progressTimer.setPercentage(progress);//设置新的进度

	}

	/*
	 * 处理点击
	 * */

	public void handleTouch(CGPoint point) {

		CCSprite choseSprite = (CCSprite) map.getParent().getChildByTag(
				FightLayer.TAG_CHOSE);

		if (CGRect.containsPoint(choseSprite.getBoundingBox(), point)) {
			
			if (selectPlant!=null) {
				selectPlant.getSprite().setOpacity(255);//设置之前选择的植物的透明度
				selectPlant=null;
			}
			//有可能选择植物
			for (ShowPlant plant : selectPlants) {
				//选中了某一植物
				CGRect boundingBox = plant.getSprite().getBoundingBox();
				if (CGRect.containsPoint(boundingBox, point)) {
					selectPlant = plant;
					selectPlant.getSprite().setOpacity(150);
					
					int id = selectPlant.getId();
					switch (id) {
					case 1:
						installPlant =new PeasePlant();
						break;
					case 4:
						installPlant = new Nut();
						break;
					default:
						break;
					}
				}
				
			}
		}else {
			//玩家有可能安放植物
			
			if (selectPlant != null) {
				
				int row=(int)point.x/46-1; //1--9,0--8
				int line = (int)((CCDirector.sharedDirector().getWinSize().height-point.y)/54)-1;//1--5,0--4
				if (row >= 0 && row <= 8 && line >= 0 && line <= 4) {
					
					//安放植物
					//installPlant.setPosition(point);
					installPlant.setLine(line);
					installPlant.setRow(row);
					installPlant.setPosition(towers[line][row]);//修正植物坐标
					
					FightLine fightLine = fightLines.get(line);
					
					//每一列不能重复添加
					if (!fightLine.containsPlant(row)) {
						
						fightLine.addPlant(installPlant);//将植物添加到行战场
						map.addChild(installPlant);
					}
				}
				installPlant=null;
				selectPlant.getSprite().setOpacity(255);//设置透明度
				selectPlant=null;
				
			}
		}

	}
	
	/*
	 * 设置进度条
	 * */
	CCProgressTimer progressTimer;
	int  progress=0;
	private void progress() {
		// 创建了进度条
		progressTimer = CCProgressTimer.progressWithFile("image/fight/progress.png");
		// 设置进度条的位置 
		progressTimer.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
		map.getParent().addChild(progressTimer); //图层添加了进度条 
		progressTimer.setScale(0.8f);  //  设置了缩放 

		progressTimer.setPercentage(0);// 每增加一个僵尸需要调整进度，增加5
		progressTimer.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarRL);  // 进度的样式

		CCSprite sprite = CCSprite.sprite("image/fight/flagmeter.png");
		sprite.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
		map.getParent().addChild(sprite);
		sprite.setScale(0.8f);
		
		CCSprite name = CCSprite.sprite("image/fight/FlagMeterLevelProgress.png");
		name.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 5);
		map.getParent().addChild(name);
		name.setScale(0.8f);
	}

}
