package com.hyman.plantvszombies.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/*
 * ������ֲ��
 * 
 * @author Administrator
 * 
 */
public abstract class AttackPlant extends Plant {
	// ����
	protected List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();

	public AttackPlant(String filepath) {
		super(filepath);
	}

	/*
	 * �������ڹ������ӵ�
	 * 
	 * @return
	 */
	public abstract Bullet createBullet();
	/*
	 * ����  �����Ҳ������ӵ�
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}



}
