package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Item extends GameObject implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    private int type;
    private Sprite bubble;

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
            setSize(45, 45);
            setCollisionArea(-12, -12, 70, 70);
            setSpeed(-200.0f, 0.0f);
        }

        if (bubble == null) {
            bubble = new Sprite(Spacegame.resources.get(Spacegame.resources.itemBubble, Texture.class));
            bubble.setSize(70, 70);
        }

        setPosition(posX, posY);
        bubble.setPosition(getX() + getWidth() - (bubble.getWidth() / 2), getY() + getHeight() - (bubble.getHeight() / 2));
    }

    public int getType() {
        return type;
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
        bubble.draw(sb);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bubble.setPosition(getX() + ((getWidth() - bubble.getWidth()) / 2), getY() + ((getHeight() - bubble.getHeight()) / 2));
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
