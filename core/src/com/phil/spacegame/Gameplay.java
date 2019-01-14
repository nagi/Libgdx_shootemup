package com.phil.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import jdk.nashorn.internal.ir.SplitReturn;

import java.util.ArrayList;
import java.util.Random;

public class Gameplay {

    //random number generator
    public static Random random;
    //pool containing objects
    public static SpawnPool spawnPool;
    //instance of the parallax background
    private ParallaxBackground parallaxBackground;
    //scrolling speed of the background
    private float bgSpeed = -200.0f;
    //player object
    private Player player;
    //Lists for different types of spawn objects (for spawnpool and collisions)
    ArrayList<SpawnObject> enemies = new ArrayList<SpawnObject>();
    ArrayList<SpawnObject> missilesEnemies = new ArrayList<SpawnObject>();
    ArrayList<SpawnObject> missilesPlayer = new ArrayList<SpawnObject>();
    //spawn parameters
    private int spawnLevel = 0;
    private int spawnLevelMax = 2;
    private float spawnInterval = 4.0f; //seconds
    private float levelDuration = 30.0f; //seconds

    private float spawnTimer;
    private float levelTimer;

    private boolean started;
    private boolean paused;

    public Gameplay() {
        //initialization
        random = new Random(System.currentTimeMillis());
        initBackground();
        initPlayer();
        initSpawnPool();
    }

    private void initBackground() {
        //create three layers with own scrolling speeds
        ParallaxLayer layers[] = new ParallaxLayer[3];
        layers[0] = new ParallaxLayer(0.1f, 0.0f); //background
        layers[1] = new ParallaxLayer(0.3f, 0.0f); //mountains
        layers[2] = new ParallaxLayer(1.0f, 0.0f); //ground
        //fill layers with textures
        layers[0].addPart(new TextureRegion(Spacegame.resources.get(Spacegame.resources.bgLayerBack, Texture.class)));
        layers[1].addPart(new TextureRegion(Spacegame.resources.get(Spacegame.resources.bgLayerMid, Texture.class)));
        layers[2].addPart(new TextureRegion(Spacegame.resources.get(Spacegame.resources.bgLayerFront, Texture.class)));
        //create a ParallaxBackground instance with these layers
        parallaxBackground = new ParallaxBackground(layers);
    }

    private void initPlayer() {
        player = new Player();
        player.setSize(180, 90);
        player.setPosition(70, 500);
        //set shooting parameters
        player.setGunPower(100.0f);
        player.setShootingInterval(0.3f);
        player.setGunType(1);
        //add some guns
        player.addGun(0, 900.0f, 150, 50);

//        //for testing:
//        player.addGun(10, 900.0f);
//        player.addGun(20, 900.0f);
//        player.addGun(-10, 900.0f);
//        player.addGun(-20, 900.0f);
    }

    private void initSpawnPool() {
        spawnPool = new SpawnPool();
        spawnPool.addPool(SpawnType.MissileEnemy, missilesEnemies);
        spawnPool.addPool(SpawnType.MissilePlayer, missilesPlayer);
        spawnPool.addPool(SpawnType.Enemy, enemies);
    }

    public void start() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void resume(){
        paused = false;
    }

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void update(float delta){
        if (!paused) {
            parallaxBackground.move(delta * bgSpeed, 0.0f);
            if (started) {
                player.update(delta);
                //calculate spawn level
                calcLevel(delta);
                //spawn new enemies
                spawnEnemies(delta);
                //update spawn objects
                updateSpawns(delta);
                //do collisions
                calcCollisions();
            }
        }
    }

    public void draw(SpriteBatch sb) {
        parallaxBackground.draw(sb);
        if (started) {
            player.draw(sb);
            //draw spawn objects
            drawSpawns(sb);
        }
    }

    private void calcLevel(float delta) {
        //count level time and increase level
        levelTimer += delta;
        if (levelTimer >= levelDuration) {
            spawnLevel++;
            if (spawnLevel >= spawnLevelMax)
                spawnLevel = spawnLevelMax;
            levelTimer = 0.0f;
        }
    }

    private void spawnEnemies(float delta) {
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy(spawnLevel);
            spawnEnemy(spawnLevel);
            spawnTimer = 0.0f;
        }
    }

    private void spawnEnemy(int type) {
        //get enemy from pool
        Enemy e = (Enemy) spawnPool.getFromPool(SpawnType.Enemy);
        //initialize with given enemy type and random y-position
        e.init(type, Spacegame.screenWidth, 20 + Gameplay.random.nextInt(600));
    }

    private void calcCollisions() {
        //collide player missiles with enemies
        for (SpawnObject e : enemies) {
            if (e.isSpawned()) {
                Enemy enemy = (Enemy) e;
                for (SpawnObject mp : missilesPlayer) {
                    if (mp.isSpawned()) {
                        Missile m = (Missile) mp;
                        if (enemy.getBoundingRectangle().overlaps(m.getBoundingRectangle())) {
                            //collision between player missile and enemy
                            m.kill(spawnPool);
                            enemy.hit(m.power);
                        }
                    }
                }
            }
        }
        //collide enemy missiles with player
        for (SpawnObject me : missilesEnemies) {
            Missile m = (Missile) me;
            if (m.isSpawned() && m.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                //enemy missile hit player
                m.kill(spawnPool);
                player.hit(m.power);
            }
        }
    }

    public void updateSpawns(float delta) {
        // could also be done in SpawnPool class but it
        // would be less performant to iterate through a HashMap
        for (SpawnObject e: enemies) {
            if (e.isSpawned())
                e.update(delta);
        }
        for (SpawnObject m: missilesEnemies) {
            if (m.isSpawned())
                m.update(delta);
        }
        for (SpawnObject mp: missilesPlayer) {
            if (mp.isSpawned())
                mp.update(delta);
        }
    }

    public void drawSpawns(SpriteBatch sb) {
        // could also be done in SpawnPool class but it
        // would be less performant to iterate through a HashMap
        for (SpawnObject e: enemies) {
            if (e.isSpawned())
                e.draw(sb);
        }
        for (SpawnObject m: missilesEnemies) {
            if (m.isSpawned())
                m.draw(sb);
        }
        for (SpawnObject mp: missilesPlayer) {
            if (mp.isSpawned())
                mp.draw(sb);
        }
    }

    public void playerMoveUp() {
        //move player upwards
        player.setAccelerateUp(true);
    }

    public void playerMoveDown() {
        //stop the upward movement
        player.setAccelerateUp(false);
    }

    public void touchDown(float screenX, float screenY) {
        playerMoveUp();
    }

    public void touchUp(float screenX, float screenY) {
        playerMoveDown();
    }
}
