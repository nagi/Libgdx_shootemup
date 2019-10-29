package com.phil.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.utils.ShaderLoader;
import com.bitfire.postprocessing.PostProcessor;

public class Spacegame extends Game {
	//Screen settings
	public static int screenWidth = 1280;
	public static int screenHeight = 720;
	public static float ratioX = 1.0f;
	public static float ratioY = 1.0f;
	public static float ASPECT_RATIO = (float)screenWidth/(float)screenHeight;
	public static float screenCenterX = (screenWidth / 2);
	public static float screenCenterY = (screenHeight / 2);
	//Resources
	public static Resources resources;
	//Screens
	public LoadingScreen loadingScreen;
	public GameplayScreen gameplayScreen;

	@Override
	public void create () {
		//instantiate AssetManager
		resources = new Resources();
		//instantiate Screens
		loadingScreen 	= new LoadingScreen(this);
		gameplayScreen 	= new GameplayScreen(this);
		//start loading resources (continues while LoadingScreen is shown)
		resources.loadAssets();
		//show LoadingScreen
		setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		resources.dispose();
	}

	//Ratio has to be recalculated when the window/screen is being resized
	public static void initGraphicRatio() {
		ratioX = (float)Spacegame.screenWidth /  (float)Gdx.graphics.getWidth() ;
		ratioY = (float)Spacegame.screenHeight / (float)Gdx.graphics.getHeight();
	}
}
