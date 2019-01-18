package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle extends AnimatedSprite implements SpawnObject {

    private float speed;
    private boolean spawned;

    private float animTimer;
    private float animTime = 1.0f;

    public Obstacle(String arg) {
        super();
    }

    public void init(int type, float posX, float posY) {
        if (type > 0)
            type = 0;

        if (type == 0) {
            if (!containsAnimation("ANIM1"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.animCloud, Texture.class),
                        2, 2, 0, 4, 0.05f, "ANIM1", false);
            setAnimation("ANIM1");
            setSize(256, 256);
            speed = -400.0f;
        }
        setPosition(posX, posY);
    }

    private void move(float delta) {
        //update position
        setX(getX() + (speed * delta));

        //remove from gameplay when out of screen
        if (getX() < -getWidth()) {
            kill(Gameplay.spawnPool);
        }
    }

    @Override
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public void kill(SpawnPool pool) {
        //despawn
        pool.returnToPool(this);
        setPosition(Spacegame.screenWidth, 0);
    }

    @Override
    public void update(float delta) {
        super.animate(delta);
        move(delta);
        animTimer += delta;
        if (animTimer >= animTime) {
            animTimer = 0.0f;
            restartActiveAnimation();
        }
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
    }
}
