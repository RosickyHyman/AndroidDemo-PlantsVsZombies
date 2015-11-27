package com.hyman.plantvszombies;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import com.hyman.plantvszombies.layer.FightLayer;
import com.hyman.plantvszombies.layer.WelcomLayer;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private CCDirector director;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
		setContentView(surfaceView);
		
		
		director = CCDirector.sharedDirector();
		director.attachInView(surfaceView);//¿ªÆôÏß³Ì
		
		
		director.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
		director.setDisplayFPS(true);
		director.setScreenSize(480, 320);
		
		CCScene ccScene = CCScene.node();
		ccScene.addChild(new FightLayer());
		director.runWithScene(ccScene);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		director.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		director.resume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		director.end();
	}
}
