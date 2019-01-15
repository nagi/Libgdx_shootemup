package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

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

		initGame(); //Todo: call initGame() from Menu
	}

	private void initGame() {
		//load new gameplay instance
		gamePlay  = new Gameplay();
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
	}

	public void startGame() {
		//start gameplay
		guiStage.showMenuGUI(false);
		guiStage.showGameGUI(true);
		this.gamePlay.start();
	}

	@Override
	//Rendering und Update
	public void render(float delta) {
		//update gamePlay
		if (gamePlay != null) {
			gamePlay.update(delta);
			guiStage.updateScore(gamePlay.score);
			guiStage.updateHealth(gamePlay.player.getHealth());
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
		guiStage.showPause(true);
		gamePlay.pause();
	}

	@Override
	public void resume() {
		guiStage.showPause(false);
		Gdx.input.setInputProcessor(ingameUI);
		gamePlay.resume();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
}
