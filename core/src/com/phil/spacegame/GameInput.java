package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import java.security.Key;

// handles gameplay-input (Keyboard, Touchscreen, Controller)
public class GameInput implements InputProcessor {

	//Reference to Screen
	private GameplayScreen gamePlayScreen;
	//Reference to gameplay
	private Gameplay gamePlay;

	public GameInput(GameplayScreen screen, Gameplay gamePlay) {
		this.gamePlayScreen = screen;
		this.gamePlay = gamePlay;
	}

	// ------------  Input Processor Methods  ------------
	@Override
	public boolean keyDown(int keycode) {
		if (gamePlay.isStarted()) {
			if (keycode == Keys.SPACE
					|| keycode == Keys.W
					|| keycode == Keys.UP) {
				gamePlay.playerMoveUp();
			}
			if (keycode == Keys.L) {
				gamePlayScreen.actionSuperShot();
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (gamePlay.isStarted()) {
			if (keycode == Keys.SPACE
				|| keycode == Keys.W
				|| keycode == Keys.UP) {
				if (gamePlay.isPaused()){
					gamePlayScreen.resumeGame();
				}
				else {
					gamePlay.playerMoveDown();
				}
			}
			else if (keycode == Keys.ESCAPE
					|| keycode == Keys.P) {
				if (gamePlay.isPaused()){
					gamePlayScreen.resumeGame();
				}
				else {
					gamePlayScreen.pause();
				}
			}
		}
		else { //not started
			if (keycode == Keys.SPACE
				|| keycode == Keys.W
				|| keycode == Keys.UP) {
				gamePlayScreen.startGame(gamePlay.isGameover());
			}
			else if (keycode == Keys.ESCAPE) {
				Gdx.app.exit();
			}
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float sX = screenX * Spacegame.ratioX;
		float sY = (Gdx.graphics.getHeight() - screenY) * Spacegame.ratioY;

		//touch actions...
//		System.out.println("Touched game ui.  Ratio: " + Spacegame.ratioX + " sx: " + sX + " , sy: " + sY); //debug

		if (gamePlay.isStarted())
			gamePlay.touchDown(sX, sY);
		else
			gamePlayScreen.startGame(gamePlay.isGameover());

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		float sX = screenX * Spacegame.ratioX;
		float sY = (Gdx.graphics.getHeight() - screenY) * Spacegame.ratioY;

		gamePlay.touchUp(sX, sY);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float sX = screenX * Spacegame.ratioX;
		float sY = (Gdx.graphics.getHeight() - screenY) * Spacegame.ratioY;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
