package com.hyman.plantvszombies.bean;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;

import com.hyman.plantvszombies.base.BaseElement;
import com.hyman.plantvszombies.base.Plant;
import com.hyman.plantvszombies.base.Zombies;
import com.hyman.plantvszombies.utils.CommonUtils;

public class PrimaryZombies extends Zombies {

	

	public PrimaryZombies(CGPoint startPoint, CGPoint endPoint) {
		super("image/zombies/zombies_1/walk/z_1_01.png");
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		setPosition(startPoint);
		move();
	}

	/*
	 * 僵尸移动
	 * */
	@Override
	public void move() {

		CCAction animate = CommonUtils.getAnimate("image/zombies/zombies_1/walk/z_1_%02d.png", 7, true);
		this.runAction(animate);
		float t=CGPointUtil.distance(getPosition(), endPoint)/speed;
		CCMoveTo moveTo = CCMoveTo.action(t, endPoint);
		CCSequence actions = CCSequence.actions(moveTo, CCCallFunc.action(this, "endGame"));
		
		this.runAction(actions);
		
	}

	/*
	 *结束游戏 
	 */
	public void endGame() {
		this.destroy();//让僵尸消失
	}

	Plant targetPlant;
	public void attack(BaseElement element){
		
		System.out.println("come here");
		
		if (element instanceof Plant) {
			
			System.out.println("begin attack");
			Plant plant = (Plant) element;
			
			if (targetPlant==null) {
				targetPlant=plant;
				
				stopAllActions();
				//切换攻击模式
				CCAction animate = CommonUtils.getAnimate("image/zombies/zombies_1/attack/z_1_attack_%02d.png", 10, true);
				this.runAction(animate);
				
				//攻击植物
				CCScheduler.sharedScheduler().schedule("attackPlant", this, 0.2f, false);
			}
			
		}
		
	}
	
	public void attackPlant(float t){
		//植物持续掉血
		targetPlant.attacked(attack);
		if (targetPlant.getLife()<0) {
			//解锁目标对象
			targetPlant=null;
			//取消攻击行为
			CCScheduler.sharedScheduler().unschedule("attackPlant", this);
			stopAllActions();
			move();
		}
		
	}
	
	
	
	
	/*
	 * 僵尸被攻击
	 * 参数为子弹的攻击力
	 * */

	@Override
	public void attacked(int attack) {

		life-=attack;
		if(life<0){
			destroy();
		}
	}

	@Override
	public void baseAction() {

	}

}
