package com.hyman.plantvszombies.base;
/*
 * ������ֲ��
 * @author Administrator
 *
 */
public abstract class ProductPlant extends Plant {

	public ProductPlant(String filepath) {
		super(filepath);
	}

	/*
	 * ���⡢���
	 */
	public abstract void create();
	

}
