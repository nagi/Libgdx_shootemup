package com.phil.spacegame;

import com.badlogic.gdx.math.Rectangle;

public class ShootingObject extends AnimatedSprite {

    public class Gun {
        boolean active = false;
        float speed = 100.0f;
        float angle;
        float offsetX;
        float offsetY;
        public Gun(float angle, float speed) {
            this.speed = speed;
            this.angle = angle;
        }
    }

    //Ggun attributes
    private SpawnType missilesPool;
    public float gunPower = 1.0f;
    private float shootingInterval = 0.5f;
    private float timer;
    private int gunType;
    private Gun guns[] = new Gun[10];
    private int maxGuns = 10;
    private int gunsCount = 0;
    //Collision
    private Rectangle rectCollision = new Rectangle(0, 0, 150, 60);
    private float collOffsetX = 20;
    private float collOffsetY = 10;

    public ShootingObject() {
        //creating guns
        for(int i=0; i<maxGuns; ++i) {
            Gun gun = new Gun(0, 0);
            guns[i] = gun;
        }
    }

    //to call when object is respawned
    public void init(SpawnType missilesPool) {
        this.missilesPool = missilesPool;
        //resetting guns
        for(Gun g: guns)
            g.active = false;
        gunsCount = 0;
        //reset timer
        timer = 0.0f;
    }

    public void setCollisionArea(int offsetX, int offsetY, int width, int height) {
        rectCollision = new Rectangle(0, 0, width, height);
        collOffsetX = offsetX;
        collOffsetY = offsetY;
    }

    public Rectangle getCollisionRectangle() {
        rectCollision.setPosition(getX() + collOffsetX, getY() + collOffsetY);
        return rectCollision;
    }

    public void setGunPower(float power) {
        this.gunPower = power;
    }

    public void setShootingInterval(float interval) {
        this.shootingInterval = interval;
    }

    public void setGunType(int type) {
        this.gunType = type;
    }

    public void addGun(float angle, float speed, float offsetX, float offsetY) {
        if (gunsCount < maxGuns) {
            Gun gun = guns[gunsCount];
            gun.speed = speed;
            gun.angle = angle;
            gun.offsetX = offsetX;
            gun.offsetY = offsetY;
            gun.active = true;
            gunsCount++;
        }
    }

    public void update(float delta) {
        super.animate(delta);
        timer += delta;
        if (timer >= shootingInterval) {
            shoot();
            timer = 0;
        }
    }

    public void shoot() {
        for (Gun g: guns) {
            if (g.active) {
                Missile m = (Missile) Gameplay.spawnPool.getFromPool(missilesPool);
                m.init(gunType, gunPower,getX() + g.offsetX, getY() + g.offsetY, g.angle, g.speed);
            }
        }
    }
}
