package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;

public class Explosion extends AnimatedSprite implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    public Explosion(String arg) {
        //load and set animation. Looping is disabled
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetExplosion, Texture.class),
                4, 4, 0, 8, 0.05f, "ANIM", false);
        setAnimation("ANIM");
    }

    public void init(float x, float y) {
        //initialize with new position
        setSize(150, 150);
        setPosition(x - 40,y - 20);
        //restart animation (because loop is set to false)
        restartActiveAnimation();
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
        pool.returnToPool(this);
    }

    @Override
    public void update(float delta) {
        super.animate(delta);
        //destroy after one animation loop
        if(super.activeAnimFinished)
            kill(Gameplay.spawnPool);
    }
}
