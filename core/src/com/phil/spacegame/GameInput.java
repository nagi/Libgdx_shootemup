package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
// import javafx.scene.input.MouseButton;

import java.security.Key;

// handles gameplay-input (Keyboard, Touchscreen, Controller)
public class GameInput implements InputProcessor {

	//Reference to Screen
	private GameplayScreen gamePlayScreen;
	//Reference to gameplay
	private Gameplay gamePlay;

	int width = Gdx.app.getGraphics().getWidth();
	int height = Gdx.app.getGraphics().getHeight();

	public GameInput(GameplayScreen screen, Gameplay gamePlay) {
		this.gamePlayScreen = screen;
		this.gamePlay = gamePlay;
	}

	// ------------  Input Processor Methods  ------------
	@Override
	public boolean keyDown(int keycode) {
		if (gamePlay.isStarted()) {
			if (keycode == Keys.A || keycode == Keys.UP) {
				gamePlay.playerMoveUp();
			}
			if (keycode == Keys.Z || keycode == Keys.DOWN) {
				gamePlay.playerMoveDown();
			}
			if (keycode == Keys.S
					|| keycode == Keys.RIGHT) {
				gamePlayScreen.actionSuperShot();
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (gamePlay.isStarted()) {
			if (keycode == Keys.A || keycode == Keys.UP || keycode == Keys.Z || keycode == Keys.DOWN) {
				if (gamePlay.isPaused()){
					gamePlayScreen.resumeGame();
				}
				else {
					gamePlay.playerStayStill();
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
				|| keycode == Keys.A
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
		System.out.println("touched screen at " + screenY);
		System.out.println("player at " + gamePlay.player.getY());
		float sX = screenX * Spacegame.ratioX;
		float sY = (Gdx.graphics.getHeight() - screenY) * Spacegame.ratioY;
		if (gamePlay.isPaused()) {
			gamePlayScreen.resumeGame();
		} else
		if (gamePlay.isStarted()) {
			if (screenX < width / 2 && screenY < height / 2){
				System.out.println("Supershot!");
				gamePlayScreen.actionSuperShot();
			}
			if (screenX < width / 2 && screenY > height / 2) {
				System.out.println("Pause");
				gamePlayScreen.pause();
			}
			if (sY > gamePlay.player.getY() && screenX > width / 2) {
				gamePlay.playerMoveUpTo(sY);
			}
			if (sY < gamePlay.player.getY() && screenX > width / 2) {
				gamePlay.playerMoveDownTo(sY);
			}
		}
		else {
			gamePlayScreen.startGame(gamePlay.isGameover());
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (gamePlay.isStarted()) {
			gamePlay.playerStayStill();
		}

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
