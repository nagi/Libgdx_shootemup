package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;

public class Item extends GameObject implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    private int type;

    public Item(String arg) {}

    public void init(int type, float posX, float posY) {
        this.type = type;

        if (type > 0)
            type = 0;

        if (type == 0) { //Repair kit
            if (!containsAnimation("ANIM1"))
                addAnimation(Spacegame.resources.get(Spacegame.resources.animItemRepair, Texture.class),
                        4, 4, 0, 8, 0.05f, "ANIM1", true);
            setAnimation("ANIM1");
            setSize(60, 60);
            setCollisionArea(0, 0, 60, 60);
            setSpeed(-200.0f, 0.0f);
        }

        setPosition(posX, posY);
    }

    public int getType() {
        return type;
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
}
