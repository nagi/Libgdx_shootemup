package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy extends ShootingObject implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    private float speed;

    public Enemy(String arg) {}

    //to call when object is respawned
    public void init(int type, float posX, float posY) {
        //reset guns
        super.init(SpawnType.MissileEnemy);

        //spawn type
        if (type == 0) {

            addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                    4, 8, 4, 4, 0.05f, "ANIM", true);
            setAnimation("ANIM");
            setSize(180, 90);
            setGunPower(10.0f);
            setShootingInterval(0.6f);
            setGunType(0);
            addGun(180, 700.0f, 0, 50);
            speed = -200.0f;
        }
        else if (type == 1) {
            addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                    4, 8, 8, 4, 0.05f, "ANIM", true);
            setAnimation("ANIM");
            setSize(180, 90);
            setGunPower(10.0f);
            setShootingInterval(0.5f);
            setGunType(0);
            addGun(180, 600.0f, 0, 30);
            addGun(180, 600.0f, 0, 70);
            speed = -200.0f;
        }
        else if (type == 2) {
            addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                    4, 8, 12, 4, 0.05f, "ANIM", true);
            setAnimation("ANIM");
            setSize(180, 90);
            setGunPower(10.0f);
            setShootingInterval(0.7f);
            setGunType(0);
            addGun(180, 500.0f, 0, 50);
            addGun(185, 500.0f, 0, 50);
            addGun(175, 500.0f, 0, 50);
            speed = -180.0f;
        }

        //set initial position (random y-position)
        setPosition(posX, posY);
    }

    public void hit(float power) {
        kill(Gameplay.spawnPool);
        //TODO reduce power instead of killing directly
    }

    private void move(float delta) {
        //update position
        setX(getX() + (speed * delta));

        //remove from gameplay when out of screen
        if (getX() < -getWidth()) {
            kill(Gameplay.spawnPool);
        }
    }

    //Interface methods

    public void update(float delta) {
        super.update(delta);
        move(delta);
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void kill(SpawnPool pool) {
        pool.returnToPool(this);
        setPosition(Spacegame.screenWidth, 0);
    }
}
