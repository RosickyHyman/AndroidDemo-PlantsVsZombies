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
	 * ����ֲ���ӽ�ʬ��ֲ�﹥����ʬ����ʬ����ֲ��
	 */
	private int lineNum;

	private List<Zombies> zombiesList = new ArrayList<Zombies>();// ����ʬ
	private Map<Integer, Plant> plants = new HashMap<Integer, Plant>(); // ����ֲ��,keyΪֲ���Ӧ���к�
	private List<AttackPlant> attackPlants = new ArrayList<AttackPlant>();// ����ֲ��

	
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
	 * ���ֲ��
	 */
	public void addPlant(final Plant plant) {
		plants.put(plant.getRow(), plant);
		if (plant instanceof AttackPlant) {
			attackPlants.add((AttackPlant) plant);
		}

		// ������������
		plant.setDieListener(new DieListener() {

			@Override
			public void die() {
				// �����ݿ����Ƴ�
				plants.remove(plant.getRow());
				if (plant instanceof AttackPlant) {
					attackPlants.remove((AttackPlant)plant);
				}
			}
		});
	}


	/*
	 * �����ӵ�
	 */
	public void createBullet(float t) {

		if (zombiesList.size() > 0 && attackPlants.size() > 0) {
			for (AttackPlant attackPlant : attackPlants) {
				attackPlant.createBullet();
			}
		}

	}
	
	/*
	 * ������ʬ
	 * */
	public void attactZombies(float t){
		
		if (zombiesList.size() > 0 && attackPlants.size() > 0) {
			//������ʬ���õ���ʬ����λ�õ�����
			for (Zombies zombies : zombiesList) {
				float x = zombies.getPosition().x;
				float left = x-20;
				float right = x+20;
				//�������й���ֲ��ĵ���
				for (AttackPlant attackPlant : attackPlants) {
					List<Bullet> bullets = attackPlant.getBullets();
					//�����������ӵ�
					for (Bullet bullet : bullets) {
						 float bulletX = bullet.getPosition().x;
						 if (bulletX>left&&bulletX<right) {
							//�ӵ������˽�ʬ
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
	 * ��ӽ�ʬ
	 */
	public void addZombies(final Zombies zombies) {
		zombiesList.add(zombies);
		// ������������
		zombies.setDieListener(new DieListener() {

			@Override
			public void die() {
				// ���б����Ƴ���ʬ
				zombiesList.remove(zombies);
			}
		});
	}

	/*
	 * ����ֲ��
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
	 * �жϸ����Ƿ���ֲ��
	 */

	public boolean containsPlant(int row) {

		return plants.containsKey(row);
	}

}
