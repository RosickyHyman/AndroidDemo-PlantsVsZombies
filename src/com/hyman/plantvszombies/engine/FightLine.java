package com.hyman.plantvszombies.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.types.CGPoint;

import com.hyman.plantvszombies.base.AttackPlant;
import com.hyman.plantvszombies.base.BaseElement.DieListener;
import com.hyman.plantvszombies.base.Bullet;
import com.hyman.plantvszombies.base.Plant;
import com.hyman.plantvszombies.base.Zombies;

public class FightLine {

	/*
	 * 安放植物，添加僵尸，植物攻击僵尸，僵尸攻击植物
	 */
	private int lineNum;

	private List<Zombies> zombiesList = new ArrayList<Zombies>();// 管理僵尸
	private Map<Integer, Plant> plants = new HashMap<Integer, Plant>(); // 管理植物,key为植物对应的列号
	private List<AttackPlant> attackPlants = new ArrayList<AttackPlant>();// 攻击植物

	
	public FightLine(int lineNum) {
		this.lineNum = lineNum;
		CCScheduler.sharedScheduler()
				.schedule("attactPlant", this, 0.2f, false);
		CCScheduler.sharedScheduler().schedule("createBullet", this, 0.2f,
				false);
		CCScheduler.sharedScheduler().schedule("attactZombies", this, 0.1f,
				false);

	}


	/*
	 * 添加植物
	 */
	public void addPlant(final Plant plant) {
		plants.put(plant.getRow(), plant);
		if (plant instanceof AttackPlant) {
			attackPlants.add((AttackPlant) plant);
		}

		// 设置死亡监听
		plant.setDieListener(new DieListener() {

			@Override
			public void die() {
				// 从数据库中移除
				plants.remove(plant.getRow());
				if (plant instanceof AttackPlant) {
					attackPlants.remove((AttackPlant)plant);
				}
			}
		});
	}


	/*
	 * 创建子弹
	 */
	public void createBullet(float t) {

		if (zombiesList.size() > 0 && attackPlants.size() > 0) {
			for (AttackPlant attackPlant : attackPlants) {
				attackPlant.createBullet();
			}
		}

	}
	
	/*
	 * 攻击僵尸
	 * */
	public void attactZombies(float t){
		
		if (zombiesList.size() > 0 && attackPlants.size() > 0) {
			//遍历僵尸，拿到僵尸所在位置的区间
			for (Zombies zombies : zombiesList) {
				float x = zombies.getPosition().x;
				float left = x-20;
				float right = x+20;
				//遍历所有攻击植物的弹夹
				for (AttackPlant attackPlant : attackPlants) {
					List<Bullet> bullets = attackPlant.getBullets();
					//遍历弹夹中子弹
					for (Bullet bullet : bullets) {
						 float bulletX = bullet.getPosition().x;
						 if (bulletX>left&&bulletX<right) {
							//子弹打中了僵尸
							 zombies.attacked(bullet.getAttack());
							 bullet.setVisible(false);
							 bullet.setAttack(0);
							 
						}
					}
				}
				
				
			}
			
			
			
		}
	}
	
	
	
	
	
	/*
	 * 添加僵尸
	 */
	public void addZombies(final Zombies zombies) {
		zombiesList.add(zombies);
		// 设置死亡监听
		zombies.setDieListener(new DieListener() {

			@Override
			public void die() {
				// 从列表中移除僵尸
				zombiesList.remove(zombies);
			}
		});
	}

	/*
	 * 攻击植物
	 */
	public void attactPlant(float t) {
		if (zombiesList.size() > 0 && plants.size() > 0) {
			for (Zombies zombies : zombiesList) {
				CGPoint position = zombies.getPosition();
				int row = (int) (position.x / 46 - 1);
				Plant plant = plants.get(row);
				if (plant != null) {
					zombies.attack(plant);
					System.out.println("meeting plant");
				}
			}
		}
	}

	/*
	 * 
	 * 判断该列是否有植物
	 */

	public boolean containsPlant(int row) {

		return plants.containsKey(row);
	}

}
