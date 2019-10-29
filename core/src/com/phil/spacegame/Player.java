package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends ShootingObject {

    private float lifeMax = 1000.0f;
    private float life = 1000.0f;
    private boolean dead;
    //collision margins
    private int collisionMarginTop = 50;
    private int collisionMarginBottom = 30;
    //acceleration speeds
    private float accelerationUp = 2000;
    private float accelerationDown = 2000;
    private float moveStepY;
    private boolean accelerateUp;
    private boolean accelerateDown;
    //ground shadow
    private Sprite shadow;
    private float shadowPosY;
    //gun
    private int gunLevel;
    private int gunLevelMax = 13;
    //shield
    private boolean shieldActive;
    private float shieldTimer;
    private float shieldBlinkingTimer;
    private float shieldBlinkingTimerStart = 2.0f;
    private float shieldBlinkingInterval = 0.1f;
    private boolean hideShield;
    private Sprite shield;
    private Sprite shieldBoost;
    private Sprite activeShield;
    //animation sparkles
    private AnimatedSprite sparkles;
    private boolean showSparkles;
    //supergun
    private int lastGunLevel;

    public Player() {
        addAnimation(Spacegame.resources.get(Spacegame.resources.tilesetSpaceships2, Texture.class),
                2, 8, 12, 4, 0.1f, "ANIM", true);
        setAnimation("ANIM");
        //init shadow
        shadow = new Sprite(Spacegame.resources.get(Spacegame.resources.shadow, Texture.class));
        shadow.setBounds(100, collisionMarginBottom -10, 150, 20);
        //init shield sprites
        //normal shield from shield-item
        shield = new Sprite(Spacegame.resources.get(Spacegame.resources.shield, Texture.class));
        shield.setBounds(-300, 0, 222, 92);
        //shield showing at boost
        shieldBoost = new Sprite(Spacegame.resources.get(Spacegame.resources.shieldBoost, Texture.class));
        shieldBoost.setBounds(-300, 0, 222, 92);//sparkles
        sparkles = new AnimatedSprite();
        sparkles.addAnimation(Spacegame.resources.get(Spacegame.resources.animItemCollect, Texture.class),
                8, 2, 0, 16, 0.04f, "SPARKLES", false);
        sparkles.setAnimation("SPARKLES");
        sparkles.setSize(210, 105);
        sparkles.setAlpha(0.8f);
        //sound
        setShotSound("sounds/laser4.mp3", 0.97f);
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
        else if (level < 0)
            level = 0;
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
                expl.init(getX() + 50, getY(), 150);
                Explosion expl2 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                expl2.init(getX() + 110, getY(), 150);
                Explosion expl3 = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                expl3.init(getX() + 70, getY() + 30, 150);

                super.playExplosionSound();
            }
        }
    }

    public void showSparkles(){
        this.sparkles.restartActiveAnimation();
        this.showSparkles = true;
    }

    public void setShield(float time, int type) {
        if (type == 0)
            activeShield = shield;
        else if (type == 1)
            activeShield = shieldBoost;
        shieldTimer = time;
        shieldActive = true;
        hideShield = false;
    }

    public void heal(float percent) {
        life += lifeMax * percent;
        if (life > lifeMax)
            life = lifeMax;
    }

    public void update(float delta) {
        if (!dead) {
            //handle updates of super class ShootingObject
            super.update(delta, 1.0f);
            //move player up and down
            move(delta);
            //update shield
            if (shieldActive) {
                activeShield.setPosition(getX() -10 , getY() - 15);
                shieldTimer -= delta;
                //set shield blinking
                if (shieldTimer <= shieldBlinkingTimerStart) {
                    shieldBlinkingTimer += delta;
                    if (shieldBlinkingTimer >= shieldBlinkingInterval) {
                        hideShield = !hideShield;
                        shieldBlinkingTimer = 0.0f;
                    }
                }
                if (shieldTimer <= 0.0f) {
                    shieldActive = false;
                    activeShield.setPosition(-activeShield.getWidth(), 0);
                }
            }
            //shadow
            shadow.setAlpha((Spacegame.screenHeight - getY()) / Spacegame.screenHeight );
            //sparkles
            if (showSparkles) {
                sparkles.setPosition(getX() - 20, getY() - 20);
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
        } 
        if (accelerateDown) {
            accelerateDown(delta);
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
            if (shieldActive) {
                if (!hideShield) {
                    activeShield.draw(sb);
                }
            }
        }
    }

    public void setAccelerateUp() {
        accelerateUp = true;
        accelerateDown = false;
        moveStepY = 0.0f;
    }
    public void setAccelerateDown() {
        accelerateDown = true;
        accelerateUp = false;
        moveStepY = 0.0f;
    }

    public void setAccelerateOff() {
        accelerateDown = false;
        accelerateUp = false;
        moveStepY = 0.0f;
    }

    private  void accelerateUp(float delta) {
        if (moveStepY < 650) {
            moveStepY += accelerationUp * delta;
        }
    }

    private void accelerateDown(float delta) {
        if (moveStepY > -650) {
            moveStepY -= accelerationDown * delta;
        }
    }

    public void setSuperGun(int type) {
        float offsetY = 0;

        if (type != -1) {
            lastGunLevel = gunLevel;
            resetGuns();
            if (type == 0) {
                setGunPower(100.0f);
                setShootingInterval(0.16f);
                setGunType(6);
                addGun(0.0f, 1100.0f, 150, offsetY);
                addGun(5.0f, 1100.0f, 150, offsetY);
                addGun(-5.0f, 1100.0f, 150, offsetY);
                addGun(10.0f, 1100.0f, 150, offsetY);
                addGun(-10.0f, 1100.0f, 150, offsetY);
                addGun(15.0f, 1100.0f, 150, offsetY);
                addGun(-15.0f, 1100.0f, 150, offsetY);
            }
            else if (type == 1) {
                setGunPower(100.0f);
                setShootingInterval(0.1f);
                setGunType(5);
                addGun(0.0f, 1400.0f, 150, offsetY);
                addGun(5.0f, 1400.0f, 150, offsetY);
                addGun(-5.0f, 1400.0f, 150, offsetY);
                addGun(10.0f, 1400.0f, 150, offsetY);
                addGun(-10.0f, 1400.0f, 150, offsetY);
                addGun(15.0f, 1400.0f, 150, offsetY);
                addGun(-15.0f, 1400.0f, 150, offsetY);
                addGun(20.0f, 1400.0f, 150, offsetY);
                addGun(-20.0f, 1400.0f, 150, offsetY);
            }
        }
        else {
            setGunLevel(lastGunLevel);
        }
    }

    private void setGunsByLevel(int gunLevel) {
        float offsetY = 0;

        float intervalGreenGun = 0.35f;
        float intervalPurpleGun = 0.22f;
        float intervalYellowGun = 0.14f;
        float intervalRedGun = 0.45f;
        float speedGreenGun = 800.0f;
        float speedPurpleGun = 1100.0f;
        float speedYellowGun = 1200.0f;
        float speedRedGun = 1400.0f;

        resetGuns();

        if (gunLevel == 0) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, offsetY);
        }
        else if (gunLevel == 1) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, offsetY);
        }
        else if (gunLevel == 2) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, offsetY);
            addGun(0, speedGreenGun, 150, offsetY + 20);
        }
        else if (gunLevel == 3) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, offsetY);
            addGun(0, speedPurpleGun, 150, offsetY +20);
        }
        else if (gunLevel == 4) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(0, speedGreenGun, 150, offsetY);
            addGun(3.5f, speedGreenGun, 150, offsetY);
            addGun(-3.5f, speedGreenGun, 150, offsetY);
        }
        else if (gunLevel == 5) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(0, speedPurpleGun, 150, offsetY);
            addGun(3.5f, speedPurpleGun, 150, offsetY);
            addGun(-3.5f, speedPurpleGun, 150, offsetY);
        }
        else if (gunLevel == 6) {
            setGunPower(100.0f);
            setShootingInterval(intervalGreenGun);
            setGunType(1); //green
            addGun(2f, speedGreenGun, 150, offsetY);
            addGun(-2f, speedGreenGun, 150, offsetY);
            addGun(4f, speedGreenGun, 150, offsetY + 10);
            addGun(-4f, speedGreenGun, 150, offsetY -10);
        }
        else if (gunLevel == 7) {
            setGunPower(100.0f);
            setShootingInterval(intervalPurpleGun);
            setGunType(2); //purple
            addGun(2f, speedPurpleGun, 150, offsetY);
            addGun(-2f, speedPurpleGun, 150, offsetY);
            addGun(4f, speedPurpleGun, 150, offsetY + 10);
            addGun(-4f, speedPurpleGun, 150, offsetY - 10);
        }
        else if (gunLevel == 8) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(0.0f, speedYellowGun, 150, offsetY);
        }
        else if (gunLevel == 9) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(20.0f, speedYellowGun, 150, offsetY);
            addGun(-20.0f, speedYellowGun, 150, offsetY);
        }
        else if (gunLevel == 10) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(0.0f, speedYellowGun, 150, offsetY);
            addGun(30.0f, speedYellowGun, 150, offsetY);
            addGun(-30.0f, speedYellowGun, 150, offsetY);
        }
        else if (gunLevel == 11) {
            setGunPower(100.0f);
            setShootingInterval(intervalYellowGun);
            setGunType(6); //yellow
            addGun(20.0f, speedYellowGun, 130, offsetY + 20);
            addGun(20.0f, speedYellowGun, 150, offsetY);
            addGun(-20.0f, speedYellowGun, 150, offsetY);
            addGun(-20.0f, speedYellowGun, 130, offsetY - 20);
        }
        else if (gunLevel == 12) {
            setGunPower(100.0f);
            setShootingInterval(intervalRedGun);
            setGunType(4); //red
            addGun(15.0f, speedRedGun, 110, offsetY + 60);
            addGun(15.0f, speedRedGun, 130, offsetY + 30);
            addGun(15.0f, speedRedGun, 150, offsetY);
            addGun(-15.0f, speedRedGun, 150, offsetY);
            addGun(-15.0f, speedRedGun, 130, offsetY - 30);
            addGun(-15.0f, speedRedGun, 110, offsetY - 60);
        }
        else if (gunLevel == 13) {
            setGunPower(100.0f);
            setShootingInterval(intervalRedGun);
            setGunType(4); //red
            addGun(10.0f, speedRedGun, 150, offsetY);
            addGun(20.0f, speedRedGun, 150, offsetY );
            addGun(30.0f, speedRedGun, 150, offsetY);
            addGun(40.0f, speedRedGun, 150, offsetY);
            addGun(0.0f, speedRedGun, 150, offsetY);
            addGun(-10.0f, speedRedGun, 150, offsetY);
            addGun(-20.0f, speedRedGun, 150, offsetY);
            addGun(-30.0f, speedRedGun, 150, offsetY);
            addGun(-40.0f, speedRedGun, 150, offsetY);
        }

        //Don't forget to set gunLevelMax after adding more gun levels !!
    }
}
