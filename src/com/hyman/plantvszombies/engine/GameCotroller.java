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
 * ������Ϸ��ʼ��Ĺ���
 */
public class GameCotroller {

	private CCTMXTiledMap map;
	private List<ShowPlant> selectPlants;
	private static List<FightLine> fightLines;
	

	private ShowPlant selectPlant; // ���ѡ���ֲ��
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
		// ��ʱ��������1��ִ�ж���������2��Ҫִ�еĶ��󡣲���3�����������4���Ƿ���ͣ
		CCScheduler.sharedScheduler().schedule("addZombies", this, 5, false);
		// addZombies();
		progress();
	}

	/*
	 * ���ص�ͼ
	 */
	CGPoint[][] towers=new CGPoint[5][9];
	private void loadMap() {

		roadPoint = CommonUtils.getSpritePoint(map, "road");
		
		//����ֲ�ﰲ�ŵص�
		for (int i = 1; i <= 5; i++) {
			List<CGPoint> towerPoints = CommonUtils.getSpritePoint(map, String.format("tower%02d", i));
			for (int j = 0; j < towerPoints.size(); j++) {
				towers[i-1][j]=towerPoints.get(j);
			}
		}
		
	}

	/*
	 * ����ÿһ��
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
	 * ������ʬ
	 */
	public void addZombies(float t) {

		Random random = new Random();
		int number = random.nextInt(5);

		PrimaryZombies primaryZombies = new PrimaryZombies(
				roadPoint.get(number * 2), roadPoint.get(number * 2 + 1));
		map.addChild(primaryZombies,1);//ָ�����ȼ����ý�ʬһֱ��ֲ������
		fightLines.get(number).addZombies(primaryZombies);
		
		progress+=5;
		progressTimer.setPercentage(progress);//�����µĽ���

	}

	/*
	 * ������
	 * */

	public void handleTouch(CGPoint point) {

		CCSprite choseSprite = (CCSprite) map.getParent().getChildByTag(
				FightLayer.TAG_CHOSE);

		if (CGRect.containsPoint(choseSprite.getBoundingBox(), point)) {
			
			if (selectPlant!=null) {
				selectPlant.getSprite().setOpacity(255);//����֮ǰѡ���ֲ���͸����
				selectPlant=null;
			}
			//�п���ѡ��ֲ��
			for (ShowPlant plant : selectPlants) {
				//ѡ����ĳһֲ��
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
			//����п��ܰ���ֲ��
			
			if (selectPlant != null) {
				
				int row=(int)point.x/46-1; //1--9,0--8
				int line = (int)((CCDirector.sharedDirector().getWinSize().height-point.y)/54)-1;//1--5,0--4
				if (row >= 0 && row <= 8 && line >= 0 && line <= 4) {
					
					//����ֲ��
					//installPlant.setPosition(point);
					installPlant.setLine(line);
					installPlant.setRow(row);
					installPlant.setPosition(towers[line][row]);//����ֲ������
					
					FightLine fightLine = fightLines.get(line);
					
					//ÿһ�в����ظ����
					if (!fightLine.containsPlant(row)) {
						
						fightLine.addPlant(installPlant);//��ֲ����ӵ���ս��
						map.addChild(installPlant);
					}
				}
				installPlant=null;
				selectPlant.getSprite().setOpacity(255);//����͸����
				selectPlant=null;
				
			}
		}

	}
	
	/*
	 * ���ý�����
	 * */
	CCProgressTimer progressTimer;
	int  progress=0;
	private void progress() {
		// �����˽�����
		progressTimer = CCProgressTimer.progressWithFile("image/fight/progress.png");
		// ���ý�������λ�� 
		progressTimer.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
		map.getParent().addChild(progressTimer); //ͼ������˽����� 
		progressTimer.setScale(0.8f);  //  ���������� 

		progressTimer.setPercentage(0);// ÿ����һ����ʬ��Ҫ�������ȣ�����5
		progressTimer.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarRL);  // ���ȵ���ʽ

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
