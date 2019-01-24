package com.phil.spacegame;

public class ShootingObject extends GameObject {

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

    private boolean shootingActive;

    //gun attributes
    private SpawnType missilesPool;
    private float gunPower = 1.0f;
    private float shootingInterval = 0.5f;
    private float timer;
    private int gunType;
    private Gun guns[] = new Gun[10];
    private int maxGuns = 10;
    private int gunsCount = 0;

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
        resetGuns();
        //reset timer
        timer = 0.0f;
        //activate shooting
        shootingActive = true;
    }

    public void resetGuns() {
        for(Gun g: guns)
            g.active = false;
        gunsCount = 0;
    }

    public void setShootingActive(boolean active) {
        this.shootingActive = active;
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

    public void update(float delta, float boostFactor) {
        super.update(delta, boostFactor);
        if (shootingActive) {
            timer += delta;
            if (timer >= shootingInterval) {
                shoot();
                timer = 0;
            }
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
