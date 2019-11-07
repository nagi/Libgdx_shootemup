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
		if (gamePlay.isStarted()) {
			if (screenX > Spacegame.screenCenterX) {
				System.out.println("Power shot!");
			}

			if (topOfScreen(screenX, screenY)) {
				gamePlay.playerMoveUp();
			} else {
				gamePlay.playerMoveDown();
			}
		} else {
			gamePlayScreen.startGame(gamePlay.isGameover());		}

		return true;
	}

	private boolean topOfScreen(int screenX, int screenY) {
		float sX = screenX * Spacegame.ratioX;
		float sY = (Gdx.graphics.getHeight() - screenY) * Spacegame.ratioY;
		System.out.println(sY);
		if(sY > (Gdx.graphics.getHeight() / 2)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean bottomOfScreen(int screenX, int screenY) {
		if(screenY > Spacegame.screenCenterY + 100) {
			return true;
		} else {
			return false;
		}
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
