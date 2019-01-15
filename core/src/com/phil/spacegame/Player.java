package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;

public class Player extends ShootingObject {

    private float lifeMax = 1000.0f;
    private float life = 1000.0f;
    private boolean dead;
    //collision margins
    private int collisionMarginTop = 130;
    private int collisionMarginBottom = 10;
    //acceleration speeds
    private float accelerationUp = 2000;
    private float accelerationDown = 1600;

    private float moveStepY;
    private boolean accelerateUp;

    public Player() {
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                4, 8, 20, 1, 1.0f, "ANIM", false);
        setAnimation("ANIM");

        //initialize and define pool with missiles
        init(SpawnType.MissilePlayer);
    }

    public boolean isDead() {
        return dead;
    }

    //return health in percent
    public float getHealth() {
        return life / lifeMax;
    }

    public void hit(float power) {
        life -= power;
        if (life < 0.0f) {
            life = 0.0f;
            dead = true;
        }
    }

    public void update(float delta) {
        //handle updates of super class ShootingObject
        super.update(delta);
        //move player up and down
        move(delta);
    }

    private void move(float delta) {
        //collide on top and bottom of screen
        if (getY() > Spacegame.screenHeight - collisionMarginTop) {
            moveStepY = 0;
            setY(Spacegame.screenHeight - collisionMarginTop);
        } else
        if (getY() < collisionMarginBottom) {
            moveStepY = 0;
            setY(collisionMarginBottom);
        }
        //calculate movement up and down
        if (accelerateUp) {
            accelerateUp(delta);
        } else {
            fall(delta);
        }
        //apply movement
        setY(getY() + moveStepY * delta);
    }

    public void setAccelerateUp(boolean acc) {
        this.accelerateUp = acc;
    }

    private  void accelerateUp(float delta) {
        if (moveStepY < 650) {
            moveStepY += accelerationUp * delta;
        }
    }

    private void fall(float delta) {
        if (moveStepY > -650) {
            moveStepY -= accelerationDown * delta;
        }
    }
}
