package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends GameObject implements SpawnObject {

    private boolean spawned;

    private boolean restartAnimRandom;
    private float animTimer;
    private float animTime = 1.5f;
    private float animTimeRand;

    public Obstacle(String arg) {
        super();
    }

    public void init(int type, float posX, float posY) {
        restartAnimRandom = false;
        animTimer = 0.0f;

        if (type > 0)
            type = 0;

        if (type == 0) {
            if (!containsAnimation("ANIM1"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.animCloud, Texture.class),
                        8, 8, 0, 64, 0.1f, "ANIM1", false);
            setAnimation("ANIM1");
            setSize(140, 140);
            setCollisionArea(30, 50, 80, 75);
            setSpeed(-425.0f, 42.75f);
            restartAnimRandom = true;
        }
        setPosition(posX, posY);
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
    public void update(float delta, float boostFactor) {
        //update position and animation
        super.update(delta, boostFactor);
        //remove from gameplay when out of screen
        if (getX() < -getWidth()) {
            kill(Gameplay.spawnPool);
        }
        //set random animation restart time of cloud
        if (restartAnimRandom) {
            animTimer += delta;
            if (animTimer >= animTimeRand) {
                animTimer = 0.0f;
                restartActiveAnimation();
                animTimeRand = animTime + Gameplay.random.nextFloat();
            }
        }
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
    }
}
