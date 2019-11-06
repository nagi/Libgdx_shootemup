package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;

public class Explosion extends AnimatedSprite implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    public Explosion(String arg) {
        //load and set animation. Looping is disabled
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetExplosion, Texture.class),
                8, 8, 17, 47, 0.02f, "ANIM", false);
        setAnimation("ANIM");
    }

    public void init(float x, float y, float size) {
        //initialize with new position
        setSize(size, size);
        setPosition(x - getWidth() / 2,y - getHeight() / 2);
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
    public void update(float delta, float boostFactor) {
        super.animate(delta);
        //destroy after one animation loop
        if(super.activeAnimFinished)
            kill(Gameplay.spawnPool);
    }
}
