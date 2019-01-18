package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends AnimatedSprite implements SpawnObject {

    private float speed;
    private boolean spawned;

    private float animTimer;
    private float animTime = 1.5f;
    private float animTimeRand;

    private Rectangle rectCollision = new Rectangle(0, 0, 150, 60);
    private float collOffsetX = 20;
    private float collOffsetY = 10;

    public Obstacle(String arg) {
        super();
    }

    public void init(int type, float posX, float posY) {
        if (type > 0)
            type = 0;

        if (type == 0) {
            if (!containsAnimation("ANIM1"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.animCloud, Texture.class),
                        2, 2, 0, 4, 0.02f, "ANIM1", false);
            setAnimation("ANIM1");
            setSize(256, 256);
            setCollisionArea(40, 40, 130, 130);
            speed = -300.0f;
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

    public void setCollisionArea(int offsetX, int offsetY, int width, int height) {
        rectCollision = new Rectangle(0, 0, width, height);
        collOffsetX = offsetX;
        collOffsetY = offsetY;
    }

    public Rectangle getCollisionRectangle() {
        rectCollision.setPosition(getX() + collOffsetX, getY() + collOffsetY);
        return rectCollision;
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
        if (animTimer >= animTimeRand) {
            animTimer = 0.0f;
            restartActiveAnimation();
            animTimeRand = animTime + Gameplay.random.nextFloat();
        }
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
    }
}
