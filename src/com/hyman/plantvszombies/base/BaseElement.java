package com.hyman.plantvszombies.base;

import org.cocos2d.nodes.CCSprite;


/*
 * ��սԪ�ع���
 * 
 * @author Administrator
 * 
 */
public abstract class BaseElement extends CCSprite {
	public interface DieListener{  // ��©��һ���ӿ� 
		void die();
	}
	private DieListener dieListener;  // �����ļ���     ������һ���ӿ����� 
	public void setDieListener(DieListener dieListener) {  // ��©��һ������ 
		this.dieListener = dieListener;
	}

	public BaseElement(String filepath) {
		super(filepath);
	}

	/*
	 * ԭ�ز����Ļ�������
	 */
	public abstract void baseAction();

	/*
	 * ����
	 */
	public void destroy() {
		if(dieListener!=null){
			dieListener.die();     //��ֲ��������ʱ�� ���ýӿڵķ���
		}
		this.removeSelf();
	}
}
