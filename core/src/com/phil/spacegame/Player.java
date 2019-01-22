package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends ShootingObject {

    private float lifeMax = 1000.0f;
    private float life = 1000.0f;
    private boolean dead;
    //collision margins
    private int collisionMarginTop = 130;
    private int collisionMarginBottom = 0;
    //acceleration speeds
    private float accelerationUp = 2000;
    private float accelerationDown = 1600;
    private float moveStepY;
    private boolean accelerateUp;
    //ground shadow
    private Sprite shadow;
    private float shadowPosY;
    //gun
    private int gunLevel;
    private int gunLevelMax = 11;
    //shield
    private boolean shieldActive;
    private float shieldTimer;
    private Sprite shield;
    //animation sparkles
    private AnimatedSprite sparkles;
    private boolean showSparkles;

    public Player() {
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships, Texture.class),
                4, 8, 20, 1, 1.0f, "ANIM", false);
        setAnimation("ANIM");
        //init shadow
        shadow = new Sprite(Spacegame.resources.get(Spacegame.resources.shadow, Texture.class));
        shadow.setBounds(80, collisionMarginBottom + 20, 179, 25);
        //init shield sprite
        shield = new Sprite(Spacegame.resources.get(Spacegame.resources.shield, Texture.class));
        shield.setBounds(0, 0, 222, 92);
        //sparkles
        sparkles = new AnimatedSprite();
        sparkles.addAnimation(Spacegame.resources.get(Spacegame.resources.animItemCollect, Texture.class),
                2, 4, 0, 6, 0.08f, "SPARKLES", false);
        sparkles.setAnimation("SPARKLES");
        sparkles.setSize(210, 105);
        sparkles.setAlpha(0.8f);
        //initialize and define pool with missiles
        init(SpawnType.MissilePlayer);
    }

    public boolean isDead() {
        return dead;
    }

    public void setMaxHealth(float health) {
        this.lifeMax = health;
    }

    //return health in percent
    public float getHealth() {
        return life / lifeMax;
    }

    public int getGunLevel() {
        return gunLevel;
    }

    public int getGunLevelMax() {
        return gunLevelMax;
    }

    public void setGunLevel(int level) {
        if (level > gunLevelMax)
            level = gunLevelMax;
        this.gunLevel = level;
        setGunsByLevel(level);
    }

    public void hit(float power) {
        if (!shieldActive) {
            life -= power;
            if (life < 0.0f) {
                life = 0.0f;
                dead = true;
                //spawn explosions
                Explosion expl = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                expl.init(getX(), getY());
                Explosion expl2 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                expl2.init(getX() + 60, getY());
                Explosion expl3 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                expl3.init(getX() + 20, getY() + 30);
            }
        }
    }

    public void showSparkles(){
        this.sparkles.restartActiveAnimation();
        this.showSparkles = true;
    }

    public void setShield(float time) {
        shieldTimer = time;
        shieldActive = true;
    }

    public void heal(float percent) {
        life += lifeMax * percent;
        if (life > lifeMax)
            life = lifeMax;
    }

    public void update(float delta) {
        if (!dead) {
            //handle updates of super class ShootingObject
            super.update(delta);
            //move player up and down
            move(delta);
            //update shield
            if (shieldActive) {
                shieldTimer -= delta;
                if (shieldTimer <= 0.0f)
                    shieldActive = false;
                shield.setPosition(getX() -10 , getY() + 9);
            }
            //shadow
            shadow.setAlpha((Spacegame.screenHeight - getY()) / Spacegame.screenHeight );
            //sparkles
            if (showSparkles) {
                sparkles.setPosition(getX() - 20, getY());
                sparkles.animate(delta);
                if (sparkles.activeAnimFinished) {
                    showSparkles = false;
                }
            }
        }
    }

    private void move(float delta) {
        //collide on top and bottom of screen
        if (getY() > Spacegame.screenHeight - collisionMarginTop) {
            moveStepY = 0;
            setY(Spacegame.screenHeight - collisionMarginTop);
        } else if (getY() < collisionMarginBottom) {
            moveStepY = 0;
            setY(collisionMarginBottom);
            hit(999999);
        }
        //calculate movement up and down
        if (accelerateUp) {
            accelerateUp(delta);
        } else {
            fall(delta);
        }
        //apply movement
        setY(getY() + moveStepY * delta);
    }

    public void draw(SpriteBatch sb) {
        if (!dead) {
            super.draw(sb);
            shadow.draw(sb);
            if (showSparkles)
                sparkles.draw(sb);
            if (shieldActive)
                shield.draw(sb);
        }
    }

    public void setAccelerateUp(boolean acc) {
        this.accelerateUp = acc;
    }

    private  void accelerateUp(float delta) {
        if (moveStepY < 650) {
            moveStepY += accelerationUp * delta;
        }
    }

    private void fall(float delta) {
        if (moveStepY > -650) {
            moveStepY -= accelerationDown * delta;
        }
    }

    private void setGunsByLevel(int gunLevel) {
        float intervalGreenGun = 0.4f;
        float intervalPurpleGun = 0.25f;
        float intervalYellowGun = 0.14f;
        float speedGreenGun = 800.0f;
        float speedPurpleGun = 1100.0f;
        float speedYellowGun = 1200.0f;

        resetGuns();

        if (gunLevel == 0) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, 30);
        }
        else if (gunLevel == 1) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, 30);
        }
        else if (gunLevel == 2) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, 30);
            addGun(0, speedGreenGun, 150, 50);
        }
        else if (gunLevel == 3) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, 30);
            addGun(0, speedPurpleGun, 150, 50);
        }
        else if (gunLevel == 4) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, 30);
            addGun(4, speedGreenGun, 150, 30);
            addGun(-4, speedGreenGun, 150, 30);
        }
        else if (gunLevel == 5) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, 30);
            addGun(4, speedPurpleGun, 150, 30);
            addGun(-4, speedPurpleGun, 150, 30);
        }
        else if (gunLevel == 6) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(1.5f, speedGreenGun, 150, 30);
            addGun(-1.5f, speedGreenGun, 150, 30);
            addGun(4, speedGreenGun, 150, 40);
            addGun(-4, speedGreenGun, 150, 20);
        }
        else if (gunLevel == 7) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(1.5f, speedPurpleGun, 150, 30);
            addGun(-1.5f, speedPurpleGun, 150, 30);
            addGun(4, speedPurpleGun, 150, 40);
            addGun(-4, speedPurpleGun, 150, 20);
        }
        else if (gunLevel == 8) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(0.0f, speedYellowGun, 150, 30);
        }
        else if (gunLevel == 9) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(20.0f, speedYellowGun, 150, 30);
            addGun(-20.0f, speedYellowGun, 150, 30);
        }
        else if (gunLevel == 10) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(0.0f, speedYellowGun, 150, 30);
            addGun(30.0f, speedYellowGun, 150, 30);
            addGun(-30.0f, speedYellowGun, 150, 30);
        }
        else if (gunLevel == 11) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(20.0f, speedYellowGun, 130, 50);
            addGun(20.0f, speedYellowGun, 150, 30);
            addGun(-20.0f, speedYellowGun, 150, 30);
            addGun(-20.0f, speedYellowGun, 130, 10);
        }

        //Don't forget to set gunLevelMax after adding more gun levels !!
    }
}
