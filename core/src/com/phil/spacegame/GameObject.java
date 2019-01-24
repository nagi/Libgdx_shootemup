package com.phil.spacegame;

import com.badlogic.gdx.math.Rectangle;

public class GameObject extends AnimatedSprite {

    //movement
    private float speedX;
    private float speedY;
    //collision
    private Rectangle rectCollision = new Rectangle();
    private float collOffsetX = 20;
    private float collOffsetY = 10;

    public void setCollisionArea(int offsetX, int offsetY, int width, int height) {
        rectCollision = new Rectangle(0, 0, width, height);
        collOffsetX = offsetX;
        collOffsetY = offsetY;
    }

    public Rectangle getCollisionRectangle() {
        rectCollision.setPosition(getX() + collOffsetX, getY() + collOffsetY);
        return rectCollision;
    }

    public void setSpeed(float x, float y){
        this.speedX = x;
        this.speedY = y;
    }

    public void update(float delta, float boostFactor) {
        super.animate(delta);
        setX(getX() + (speedX * delta * boostFactor));
        setY(getY() + (speedY * delta * boostFactor));
    }
}
