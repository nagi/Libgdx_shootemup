package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends ShootingObject {

    private float lifeMax = 1000.0f;
    private float life = 1000.0f;
    private boolean dead;
    //collision margins
    private int collisionMarginTop = 130;
    private int collisionMarginBottom = 0;
    //acceleration speeds
    private float accelerationUp = 2000;
    private float accelerationDown = 1600;
    private float moveStepY;
    private boolean accelerateUp;
    //ground shadow
    private Sprite shadow;
    private float shadowPosY;

    public Player() {
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                4, 8, 20, 1, 1.0f, "ANIM", false);
        setAnimation("ANIM");
        //init shadow
        shadow = new Sprite(Spacegame.resources.get(Spacegame.resources.shadow, Texture.class));
        shadow.setBounds(80, collisionMarginBottom + 20, 179, 25);
        //initialize and define pool with missiles
        init(SpawnType.MissilePlayer);
    }

    public boolean isDead() {
        return dead;
    }

    public void setMaxHealth(float health) {
        this.lifeMax = health;
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
            //spawn explosions
            Explosion expl = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
            expl.init(getX(), getY());
            Explosion expl2 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
            expl2.init(getX() + 60, getY());
            Explosion expl3 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
            expl3.init(getX() + 20, getY() + 30);
        }
    }

    public void heal(float percent) {
        life += lifeMax * percent;
        if (life > lifeMax)
            life = lifeMax;
    }

    public void update(float delta) {
        if (!dead) {
            //handle updates of super class ShootingObject
            super.update(delta);
            //move player up and down
            move(delta);
            //shadow
            shadow.setAlpha((Spacegame.screenHeight - getY()) / Spacegame.screenHeight );
        }
    }

    private void move(float delta) {
        //collide on top and bottom of screen
        if (getY() > Spacegame.screenHeight - collisionMarginTop) {
            moveStepY = 0;
            setY(Spacegame.screenHeight - collisionMarginTop);
        } else if (getY() < collisionMarginBottom) {
            moveStepY = 0;
            setY(collisionMarginBottom);
            hit(999999);
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

    public void draw(SpriteBatch sb) {
        if (!dead) {
            super.draw(sb);
            shadow.draw(sb);
        }
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
