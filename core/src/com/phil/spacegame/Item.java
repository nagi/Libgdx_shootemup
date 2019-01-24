package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Item extends GameObject implements SpawnObject {

    //for SpawnObject interface
    private boolean spawned;

    private int type;
    private Sprite bubble;

    private float angle;

    public Item(String arg) {}

    public void init(int type, float posX, float posY) {
        this.type = type;

        //debug
        System.out.println("Spawn ITEM type: " + type);

        String animName;

        if (type == 0) { //Repair kit
            animName = "ANIM1";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.animItemRepair, Texture.class),
                        4, 4, 0, 8, 0.05f, animName, true);
            setAnimation(animName);
            setSize(45, 45);
        }
        else if (type == 1) { //Random gun
            animName = "ANIM2";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 12, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 2) { //Shield
            animName = "ANIM3";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 13, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 3) { //Boost
            animName = "ANIM4";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 14, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }

        //GUNS
        else if (type == 10) { //Gun Green 1
            animName = "ANIM10";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 0, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 11) { //Gun purple 1
            animName = "ANIM11";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 4, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 12) { //Gun green 2
            animName = "ANIM12";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 1, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 13) { //Gun purple 2
            animName = "ANIM13";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 5, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 14) { //Gun green 3
            animName = "ANIM14";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 2, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 15) { //Gun purple 3
            animName = "ANIM15";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 6, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 16) { //Gun green 4
            animName = "ANIM16";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 3, 1, 1.00f, animName, true);
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 17) { //Gun purple 4
            animName = "ANIM17";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 7, 1, 1.00f, animName, true );
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 18) { //Gun yellow 1
            animName = "ANIM18";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 8, 1, 1.00f, animName, true );
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 19) { //Gun yellow 2
            animName = "ANIM19";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 9, 1, 1.00f, animName, true );
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 20) { //Gun yellow 3
            animName = "ANIM20";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 10, 1, 1.00f, animName, true );
            setAnimation(animName);
            setSize(64, 64);
        }
        else if (type == 21) { //Gun yellow 4
            animName = "ANIM21";
            if (!containsAnimation(animName))
                addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetGunUpgrades, Texture.class),
                        4, 4, 11, 1, 1.00f, animName, true );
            setAnimation(animName);
            setSize(64, 64);
        }
        else {
            System.out.println("Item.init(): Item of type "+  type + "not found.");
        }

        setCollisionArea(-12, -12, 70, 70);
        setSpeed(-250.0f, 0.0f);

        if (bubble == null) {
            bubble = new Sprite(Spacegame.resources.get(Spacegame.resources.itemBubble, Texture.class));
            bubble.setSize(70, 70);
        }

        setPosition(posX, posY);

        bubble.setOriginCenter();
    }

    public int getType() {
        return type;
    }

    public void draw(SpriteBatch sb) {
        super.draw(sb);
        bubble.draw(sb);
    }

    @Override
    public void update(float delta, float boostFactor) {
        super.update(delta, boostFactor);
        //calculate pulse of bubble
        angle += 7 * delta;
        float size = (float)Math.sin(angle);
        bubble.setSize(65 + 5 * size, 65 + 5 * size);
        bubble.setOriginCenter();
        bubble.setOriginBasedPosition(getX() + getWidth()/2, getY() + getHeight() / 2);
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
