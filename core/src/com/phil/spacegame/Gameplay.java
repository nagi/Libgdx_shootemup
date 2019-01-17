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
    //score, public for easier access
    public int score;
    public int highscore;
    //player object
    public Player player;

    //reference to screen
    private GameplayScreen gameplayScreen;
    //instance of the parallax background
    private ParallaxBackground parallaxBackground;
    //scrolling speed of the background
    private float bgSpeed = -300.0f;
    //Lists for different types of spawn objects (for spawnpool and collisions)
    private ArrayList<SpawnObject> enemies = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> missilesEnemies = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> missilesPlayer = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> explosions = new ArrayList<SpawnObject>();
    //flags for game state
    private boolean started;
    private boolean paused;
    private boolean gameover;
    private boolean gameoverSequence;
    private boolean justDied;
    //spawn parameters
    private int spawnLevel = 0;
    private int spawnLevelMax = 7;
    private float spawnIntervalMax = 1.5f; //seconds
    private float spawnInterval = 1.5f; //seconds
    private float levelDuration = 20.0f; //seconds
    private float gameoverTimerMax = 1.5f; //seconds
    //timer
    private float spawnTimer;
    private float levelTimer;
    private float gameoverTimer;


    public Gameplay(GameplayScreen gs) {
        this.gameplayScreen = gs;
        random = new Random(System.currentTimeMillis());
        init();
    }

    private void init() {
        //initialization
        initBackground();
        initPlayer();
        initSpawnPool();
        //reset flags and parameters
        gameoverSequence = false;
        gameoverTimer = gameoverTimerMax;
        score = 0;
        spawnLevel = 0;
        spawnTimer = 0;
        levelTimer = 0;
        spawnInterval = spawnIntervalMax;
        justDied = true;
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
        player.setCollisionArea(20, 20, 140, 50);
        //set shooting parameters
        player.setGunPower(100.0f);
        player.setShootingInterval(0.3f);
        player.setGunType(1);
        //add some guns
        player.addGun(0, 900.0f, 150, 30);
        player.setMaxHealth(1000);

//        //killer weapon for testing:
//        player.addGun(3, 900.0f,150, 30);
//        player.addGun(6, 900.0f,150, 30);
//        player.addGun(9, 900.0f, 150, 30);
//        player.addGun(12, 900.0f, 150, 30);
//        player.addGun(15, 900.0f, 150, 30);
//        player.addGun(-3, 900.0f,150, 30);
//        player.addGun(-6, 900.0f,150, 30);
//        player.addGun(-9, 900.0f,150, 30);
//        player.addGun(-12, 900.0f,150, 30);
//        player.addGun(-15, 900.0f,150, 30);
    }

    private void initSpawnPool() {
        spawnPool = new SpawnPool();
        spawnPool.addPool(SpawnType.MissileEnemy, missilesEnemies);
        spawnPool.addPool(SpawnType.MissilePlayer, missilesPlayer);
        spawnPool.addPool(SpawnType.Enemy, enemies);
        spawnPool.addPool(SpawnType.Explosion, explosions);
    }

    public void restart() {
        //clear spawns
        missilesEnemies.clear();
        missilesPlayer.clear();
        enemies.clear();
        explosions.clear();
        //re-initialize
        init();
        start();
    }

    public void start() {
        started = true;
        gameover = false;
    }

    public boolean isStarted() {
        return started;
    }

    public void gameover() {
        highscore = score > highscore ? score : highscore;
        started = false;
        gameover = true;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void resume(){
        paused = false;
    }

    public void update(float delta){
        if (!paused) {
            parallaxBackground.move(delta, bgSpeed, 0.0f);
            if (started) {
                player.update(delta);
                if (player.isDead()) {
                    processGameover(delta);
                }
                //calculate spawn level
                calcLevel(delta);
                //spawn new enemies
                if (!gameoverSequence)
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

    private void processGameover(float delta) {

        if (justDied) {
            parallaxBackground.shake();
            justDied = false;
        }

        gameoverSequence = true;
        gameoverTimer -= delta;
        if (gameoverTimer <= 0.0f)
            gameplayScreen.setGameOver();
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
            if (spawnLevel < 4)
                spawnEnemy(spawnLevel,
                        Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
            else if (spawnLevel < 8) {
                spawnEnemy(spawnLevel - 4,
                        Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
                spawnEnemy(spawnLevel - 4,
                        Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
            }
            spawnTimer = 0.0f;
        }
    }

    private void spawnEnemy(int type, float posX, float posY) {
        //get enemy from pool
        Enemy e = (Enemy) spawnPool.getFromPool(SpawnType.Enemy);
        //initialize with given enemy type and random y-position
        e.init(type, posX, posY);
    }

    private void calcCollisions() {
        //collide player and player-missiles with enemies
        for (SpawnObject e : enemies) {
            if (e.isSpawned()) {
                Enemy enemy = (Enemy) e;
                if (enemy.getX() < Spacegame.screenWidth) {
                    //collide player missiles with enemy
                    for (SpawnObject mp : missilesPlayer) {
                        if (mp.isSpawned()) {
                            Missile m = (Missile) mp;
                            if (enemy.getCollisionRectangle().overlaps(m.getBoundingRectangle())) {
                                //collision between player missile and enemy
                                m.kill(spawnPool);
                                score += enemy.getScore();
                                enemy.hit(m.power);
                            }
                        }
                    }
                    //collide player with enemy
                    if (!player.isDead()) {
                        if (player.getCollisionRectangle().overlaps(enemy.getCollisionRectangle())) {
                            enemy.kill(spawnPool);
                            player.hit(100000);
                        }
                    }
                }
            }
        }
        //collide enemy missiles with player
        if (!player.isDead()) {
            for (SpawnObject me : missilesEnemies) {
                Missile m = (Missile) me;
                if (m.isSpawned() && m.getBoundingRectangle().overlaps(player.getCollisionRectangle())) {
                    //enemy missile hit player
                    m.kill(spawnPool);
                    player.hit(m.power);
                }
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
        for (SpawnObject ex: explosions) {
            if (ex.isSpawned())
                ex.update(delta);
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
        for (SpawnObject ex: explosions) {
            if (ex.isSpawned())
                ex.draw(sb);
        }
    }

    public void playerMoveUp() {
        //move player upwards
        if (!player.isDead())
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
