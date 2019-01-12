package com.phil.spacegame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface SpawnObject {

    void setSpawned(boolean spawned);

    boolean isSpawned();

    void kill(SpawnPool pool);

    void update(float delta);

    void draw(SpriteBatch sb);
}
