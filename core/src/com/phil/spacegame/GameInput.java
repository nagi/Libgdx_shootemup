package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

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
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
		return false;
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
		System.out.println("Touched game ui.  Ratio: " + Spacegame.ratioX + " sx: " + sX + " , sy: " + sY); //debug
		gamePlay.touchDown(sX, sY);
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
