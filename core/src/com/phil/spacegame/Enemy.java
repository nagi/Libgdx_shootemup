package com.phil.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends ShootingObject implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;
    //the score player gets after killing this enemy
    private int score;

    public Enemy(String arg) {}

    //to call when object is respawned
    public void init(int type, float posX, float posY) {
        //reset guns
        super.init(SpawnType.MissileEnemy);

        if (type > 5)
            type = 5;

        //spawn type
        if (type == 0) {
            if (!containsAnimation("ANIM1"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships2, Texture.class),
                    4, 8, 0, 4, 0.05f, "ANIM1", true);
            setAnimation("ANIM1");
            setSize(128, 64);
            setCollisionArea(20, 5, 80, 50);
            setGunPower(40.0f);
            setShootingInterval(1.3f);
            setGunType(0);
            addGun(180, 600, 0, 20);
            setSpeed(-280.0f, 0.0f);
            setScore(100);
        }
        else if (type == 1) {
            if (!containsAnimation("ANIM2"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships2, Texture.class),
                        4, 8, 4, 4, 0.05f, "ANIM2", true);
            setAnimation("ANIM2");
            setSize(128, 64);
            setCollisionArea(20, 5    , 80, 50);
            setGunPower(40.0f);
            setShootingInterval(1.2f);
            setGunType(0);
            addGun(187, 600, 0, 20);
            addGun(173, 600, 0, 20);
            setSpeed(-300.0f, 0.0f);
            setScore(120);
        }
        if (type == 2) {
            if (!containsAnimation("ANIM3"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships2, Texture.class),
                        4, 4, 4, 4, 0.03f, "ANIM3", true);
            setAnimation("ANIM3");
            setSize(128, 128);
            setCollisionArea(20, 60, 80, 55);
            setGunPower(40.0f);
            setShootingInterval(1.4f);
            setGunType(6);
            addGun(180, 600, 0, 60);
            addGun(180, 600, 50, 60);
            setSpeed(-310.0f, 0.0f);
            setScore(100);
        }
        else if (type == 3) {
            if (!containsAnimation("ANIM4"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships2, Texture.class),
                        4, 4, 8, 4, 0.03f, "ANIM4", true);
            setAnimation("ANIM4");
            setSize(128, 128);
            setCollisionArea(20, 60, 80, 55);
            setGunPower(40.0f);
            setShootingInterval(1.3f);
            setGunType(5);
            addGun(187, 600, 0, 60);
            addGun(173, 600, 0, 60);
            addGun(180, 600, 0, 60);
            setSpeed(-310.0f, 0.0f);
            setScore(120);
        }
        else if (type == 4) {
            if (!containsAnimation("ANIM5"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                        4, 8, 8, 4, 0.05f, "ANIM5", true);
            setAnimation("ANIM5");
            setSize(200, 100);
            setCollisionArea(20, 20, 90, 70);
            setGunPower(40.0f);
            setShootingInterval(1.3f);
            setGunType(3);
            addGun(180, 500.0f, 0, 0);
            addGun(180, 500.0f, 0, 30);
            addGun(180, 500.0f, 0, 60);
            setSpeed(-310.0f, 0.0f);
            setScore(150);
        }
        else if (type == 5) {
            if (!containsAnimation("ANIM6"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                        4, 8, 12, 4, 0.05f, "ANIM6", true);
            setAnimation("ANIM6");
            setSize(200, 100);
            setCollisionArea(20, 20, 90, 70);
            setGunPower(40.0f);
            setShootingInterval(1.1f);
            setGunType(4);
            addGun(190, 450, 0, 60);
            addGun(190, 450, 0, 50);
            addGun(180, 450, 0, 50);
            addGun(170, 450, 0, 40);
            addGun(170, 450, 0, 30);
            setSpeed(-320.0f, 0.0f);
            setScore(200);
        }
        //set initial position (random y-position)
        setPosition(posX, posY);
    }

    public void setScore(int points) {
        this.score = points;
    }

    public int getScore() {
        return score;
    }

    public void hit(float power) {
        //Explosion expl = (Explosion)Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
        //expl.init(getX() + 30, getY() + getHeight() / 2, 90);
        //spawn explosion
        Explosion expl = (Explosion)Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
        expl.init(getX() + 20, getY() + getHeight() / 2, 150);
        //sound
        super.playExplosionSound();

        kill(Gameplay.spawnPool);
        //TODO reduce power instead of killing directly
    }

    //Interface methods

    public void update(float delta, float boostFactor) {
        //update shooting, animation and position
        super.update(delta, boostFactor);
        //remove from gameplay when out of screen
        if (getX() < -getWidth()) {
            kill(Gameplay.spawnPool);
        }
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void kill(SpawnPool pool) {
        //despawn
        pool.returnToPool(this);
        setPosition(Spacegame.screenWidth, 0);
    }
}
