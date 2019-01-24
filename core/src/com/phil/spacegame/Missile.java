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
        setRotation(angle);

        //set animation/image
        if (type == 0) { //orange
            setRegion(Spacegame.resources.get(Spacegame.resources.missile1, Texture.class));
            setBounds(position.x, position.y, 17, 17);
        }
        else if (type == 1) { //green
            setRegion(Spacegame.resources.get(Spacegame.resources.missile2, Texture.class));
            setBounds(position.x, position.y, 17, 17);
        }
        else if (type == 2) { //purple
            setRegion(Spacegame.resources.get(Spacegame.resources.missile3, Texture.class));
            setBounds(position.x, position.y, 15, 15);
        }
        else if (type == 3) { //blue
            setRegion(Spacegame.resources.get(Spacegame.resources.missile4, Texture.class));
            setBounds(position.x, position.y, 24, 9);
        }
        else if (type == 4) { //red
            setRegion(Spacegame.resources.get(Spacegame.resources.missile5, Texture.class));
            setBounds(position.x, position.y, 24, 9);
        }
        else if (type == 5) { //grey
            setRegion(Spacegame.resources.get(Spacegame.resources.missile6, Texture.class));
            setBounds(position.x, position.y, 15, 15);
        }
        else if (type == 6) { //yellow
            setRegion(Spacegame.resources.get(Spacegame.resources.missile7, Texture.class));
            setBounds(position.x, position.y, 26, 12);
        }
        else {
            System.err.println("GUN TYPE NOT FOUND! " + type);
            setRegion(Spacegame.resources.get(Spacegame.resources.missile1, Texture.class));
        }
        setOriginCenter();
    }

    //Interface methods

    public void update(float delta, float boostFactor) {
        super.animate(delta);
        if (isSpawned()) {
            movement.set(direction).scl(speed * delta * boostFactor);
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
