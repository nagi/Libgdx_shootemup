package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Missile extends AnimatedSprite implements SpawnObject {

    public float power;
    //movement parameters
    private float speed;
    private Vector2 position = new Vector2();
    private Vector2 direction = new Vector2();
    private Vector2 movement = new Vector2();
    //for SpawnObject interface
    private boolean spawned;

    public Missile(String arg) {}

    public void init(int type, float power, float xStart, float yStart, float angle, float speed) {
        this.speed = speed;
        this.power = power;
        //init movement
        position.set(xStart, yStart);
        direction.set((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle))).nor();
        //set animation/image
        if (type == 0) {
            setRegion(Spacegame.resources.get(Spacegame.resources.missile1, Texture.class));
        }
        else if (type == 1) {
            setRegion(Spacegame.resources.get(Spacegame.resources.missile2, Texture.class));
        }

        setBounds(position.x, position.y, 21, 21);
    }

    //Interface methods

    public void update(float delta) {
        super.animate(delta);
        if (isSpawned()) {
            movement.set(direction).scl(speed * delta);
            position.add(movement);
            setPosition(position.x, position.y);
            if (position.x > Spacegame.screenWidth || position.x < 0
                    || position.y > Spacegame.screenHeight || position.y < 0) {
                Gameplay.spawnPool.returnToPool(this);
            }
        }
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
