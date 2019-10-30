package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.utils.ShaderLoader;
import com.bitfire.postprocessing.filters.CrtScreen.RgbMode;
import com.bitfire.postprocessing.filters.CrtScreen.Effect;

public class GameplayScreen implements Screen {

	//reference to the main Game class
	public Spacegame game;
	//SpriteBatch for rendering ingame world
	private SpriteBatch spriteBatch;
	//class for initialization, update and rendering of gameplay
	private Gameplay gamePlay;
	//stage for rendering GUI
	private GUIStage guiStage;
	//handling input (keyboard, controller, mouse)
	private GameInput gameInput;
	//putting GUIStage-input and GameInput together
	private InputMultiplexer ingameUI;
	//Instance of background music
	private Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/back_music.ogg"));
	private float volumeMusic = 0.28f;
	private PostProcessor postProcessor;

	public GameplayScreen(Spacegame game){
		//keeping a reference to the main game class
		this.game = game;
	}
	
	public void init() {
		//instantiate Spritebatch to render ingame world
		spriteBatch = new SpriteBatch();
		//initialize UI
		StretchViewport viewport = new StretchViewport(Spacegame.screenWidth, Spacegame.screenHeight);
		//create stage for gui elements with given viewport
		Stage uiStage = new Stage(viewport);
		guiStage = new GUIStage(uiStage);
		guiStage.init();
		ShaderLoader.BasePath = "shaders/";
		postProcessor = new PostProcessor( false, false, false);
		Bloom bloom = new Bloom( (int)(Gdx.graphics.getWidth() * 0.03f), (int)(Gdx.graphics.getHeight() * 0.03d) );
		int effects = Effect.PhosphorVibrance.v | Effect.Tint.v;
		CrtMonitor crtMonitor = new CrtMonitor(Spacegame.screenWidth, Spacegame.screenHeight, false, false, RgbMode.RgbShift, effects);
		postProcessor.addEffect( bloom );
		postProcessor.addEffect( crtMonitor );
		initGame(); //Todo: call initGame() from Menu
	}

	private void initGame() {
		//load new gameplay instance
		gamePlay  = new Gameplay(this);
		//handler for non-stage inputs (keyboard, mouse, controller)
		gameInput = new GameInput(this, gamePlay);
		//multiplexer for handling both input classes (guiStage and gameInput) as one input processor
		ingameUI = new InputMultiplexer();
		ingameUI.addProcessor(guiStage.getStage()); //for handling dialogs which use a stage (Actors)
		ingameUI.addProcessor(gameInput); //for handling input without stage
		//activate GUI for input
		Gdx.input.setInputProcessor(ingameUI);
		//show "Press to start"
		guiStage.showMenuGUI(true);
		//play music
		music.setVolume(volumeMusic);
		music.setLooping(true);
		music.play();
	}

	public void startGame(boolean restart) {
		//start gameplay
		guiStage.showMenuGUI(false);
		guiStage.showGameGUI(true);
		if (restart)
			gamePlay.restart();
		else
			gamePlay.start();
	}

	public void setGameOver() {

		gamePlay.gameover();
		guiStage.showGameOver(gamePlay.highscore);
	}

	public void resumeGame() {
		guiStage.showPause(false);
		Gdx.input.setInputProcessor(ingameUI);
		gamePlay.resume();
		music.play();
	}

	public void actionSuperShot() {
		if (gamePlay.isSuperShotLoaded()) {
			gamePlay.setSuperShot(true);
			guiStage.setSuperShotActive();
		}
	}

	@Override
	//Rendering und Update
	public void render(float delta) {
		postProcessor.capture();
		//update gamePlay
		if (gamePlay != null) {
			gamePlay.update(delta);
			guiStage.updateScore(gamePlay.score);
			guiStage.updateHealth(gamePlay.player.getHealth());
			guiStage.updateSuperShot(gamePlay.getSuperShotPoints(), gamePlay.isSuperShotLoaded());
		}

		//render gamePlay
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
			if (gamePlay != null)
				gamePlay.draw(spriteBatch);
		spriteBatch.end();

		//render GUI
		if (guiStage != null)
			guiStage.draw(delta);
		postProcessor.render();
	}

	@Override
	public void resize(int width, int height) {
		//stretch/crop SpriteBatch on a bigger/smaller screen resolution
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Spacegame.screenWidth, Spacegame.screenHeight);
		//get the ratio to stretch/crop on a different resolution
		Spacegame.initGraphicRatio();
	}

	@Override
	public void show() {
		//resume game
		if (gamePlay != null)
			gamePlay.resume();
	}

	@Override
	public void hide() {
		if (gamePlay != null)
			gamePlay.pause();
	}

	@Override
	public void pause() {
		//game window looses focus
		if (gamePlay.isStarted()) {
			guiStage.showPause(true);
			gamePlay.pause();
		}
		music.pause();
	}

	@Override
	public void resume() {
		//when game window becomes focused again
		music.play();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		music.dispose();
	}
}
