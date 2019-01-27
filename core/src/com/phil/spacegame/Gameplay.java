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
    private float bgSpeed = -500.0f;
    //Lists for different types of spawn objects (for spawnpool and collisions)
    private ArrayList<SpawnObject> enemies = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> missilesEnemies = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> missilesPlayer = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> explosions = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> obstacles = new ArrayList<SpawnObject>();
    private ArrayList<SpawnObject> items = new ArrayList<SpawnObject>();
    //flags for game states
    private boolean started;
    private boolean paused;
    private boolean gameover;
    private boolean gameoverSequence;
    private boolean justDied;
    private boolean spawnObstacles;
    //spawn parameters
    private int spawnLevel = 0;
    private int spawnLevelMax = 7;
    private float spawnInterval; //seconds
    private float spawnIntervalDecreaseStep; //seconds
    private float spawnIntervalMinimum; //seconds
    private float spawnIntervalObstacles; //seconds
    private float spawnIntervalItems; //seconds
    private float levelDurationEnemies; //seconds
    private float levelDurationObstacles; //seconds
    private float levelDurationObstaclesIncreaseStep; //seconds
    private boolean obstaclePreviousPosUp;
    private float levelDuration; //seconds
    private float gameoverTimerMax = 1.5f; //seconds
    //timer
    private float spawnTimer;
    private float spawnTimerItems;
    private float levelTimer;
    private float gameoverTimer;
    //boost
    private boolean boostActive;
    private float boostTimer;
    private float boostTime;
    private float boostStoppingTime;
    private float speedUpFactor;
    private float boostSpeedMax;
    private float boostTimeMax;
    //supershot
    private float superShotPoints;
    private float superShotPointsMax;
    private int superShotLevel;
    private boolean superShotActive;
    private float superShotTimer;
    private float superShotTime;
    private float supetShotPointsMaxIncreaseInterval;

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
        spawnTimerItems = 0;
        levelTimer = 0;
        spawnInterval = 1.2f;
        spawnIntervalDecreaseStep = 0.3f;
        spawnIntervalMinimum = 0.3f;
        spawnIntervalObstacles = 0.9f;
        spawnIntervalItems = 10.0f;
        levelDurationEnemies = 25.0f;
        levelDurationObstacles = 10.0f;
        levelDurationObstaclesIncreaseStep = 2.0f;
        justDied = true;
        spawnObstacles = false;
        levelDuration = levelDurationEnemies;
        speedUpFactor = 1.0f;
        boostTimer = 0.0f;
        boostTime = 0.0f;
        boostStoppingTime = 2.0f;
        boostTimeMax = 5.0f;
        boostSpeedMax = 4.0f;
        boostActive = false;
        superShotTime = 7.0f;
        superShotPoints = 0.0f;
        superShotPointsMax = 5000;
        supetShotPointsMaxIncreaseInterval = 6000;
        superShotLevel = 0;
        superShotActive = false;
    }

    private void initBackground() {
        //create three layers with own scrolling speeds
        ParallaxLayer layers[] = new ParallaxLayer[3];
        layers[0] = new ParallaxLayer(0.15f, 0.0f); //background
        layers[1] = new ParallaxLayer(0.5f, 0.0f); //mountains
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
        //set guns
        player.setGunLevel(0);
        //set health
        player.setMaxHealth(1000);
    }

    private void initSpawnPool() {
        spawnPool = new SpawnPool();
        spawnPool.addPool(SpawnType.MissileEnemy, missilesEnemies);
        spawnPool.addPool(SpawnType.MissilePlayer, missilesPlayer);
        spawnPool.addPool(SpawnType.Enemy, enemies);
        spawnPool.addPool(SpawnType.Explosion, explosions);
        spawnPool.addPool(SpawnType.Obstacle, obstacles);
        spawnPool.addPool(SpawnType.Item, items);
    }

    public void restart() {
        //clear spawns
        missilesEnemies.clear();
        missilesPlayer.clear();
        enemies.clear();
        explosions.clear();
        obstacles.clear();
        items.clear();
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
            parallaxBackground.move(delta, speedUpFactor * bgSpeed, 0.0f);
            if (started) {
                player.update(delta);
                if (player.isDead()) {
                    processGameover(delta);
                }
                //calculate spawn level
                calcLevel(speedUpFactor * delta);
                //spawn new enemies
                if (!gameoverSequence)
                    spawnObjects(speedUpFactor * delta);
                //update spawn objects
                updateSpawns(delta, speedUpFactor);
                //do collisions
                calcCollisions();
                //boost effect
                calcBoostEffect(delta);
                //supershot
                updateSuperShot(delta);
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
            boostActive = false;
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
            if (!spawnObstacles) {
                spawnLevel++;
                if (spawnLevel > spawnLevelMax) {
                    spawnLevel = 0;
                    spawnInterval -= spawnIntervalDecreaseStep;
                    if (spawnInterval <= spawnIntervalMinimum) {
                        spawnInterval = spawnIntervalMinimum;
                    }
                }
                //after every 2 levels spawn an obstacle level
                if (spawnLevel % 2 == 0) {
                    spawnObstacles = true;
                    levelDuration = levelDurationObstacles;
                    System.out.println("SPAWNING OBSTACLES");
                    levelDurationObstacles += levelDurationObstaclesIncreaseStep;
                }
                System.out.println("LEVEL: " + spawnLevel);

            }
            else {
                //finish obstacle level after one "levelDuration"
                levelDuration = levelDurationEnemies;
                spawnObstacles = false;
            }
            levelTimer = 0.0f;
        }
    }

    private void spawnObjects(float delta) {
        spawnTimer += delta;
        if (!spawnObstacles) {
            //spawn enemies
            if (spawnTimer >= spawnInterval) {
                spawnEnemies();
                spawnTimer = 0.0f;
            }
            //spawn items
            spawnTimerItems += delta;
            if (spawnTimerItems >= spawnIntervalItems) {
                spawnItems();
                spawnTimerItems = 0.0f;
            }
        } else {
            //spawn obstacles
            if (spawnTimer >= spawnIntervalObstacles) {
                spawnObstacles(0);
                spawnTimer = 0.0f;
            }
        }

    }

    private void spawnEnemies() {
        if (spawnLevel <= 3)
            //spawn one enemy with random y-position
            spawnEnemy(spawnLevel,
                    Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        else if (spawnLevel <= 7) {
            //spawn two enemies of two different levels at random y-position
            spawnEnemy(spawnLevel - 4,
                    Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
            spawnEnemy(spawnLevel - 3,
                    Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        }
    }

    private void spawnEnemy(int type, float posX, float posY) {
        //get enemy from pool
        Enemy e = (Enemy) spawnPool.getFromPool(SpawnType.Enemy);
        //initialize with given enemy type and random y-position
        e.init(type, posX, posY);
    }

    private void spawnObstacles(int type) {
        //Spawn obstacle sequence
        float pos = random.nextFloat();
        float posY = random.nextInt(110);
        //reduce propability that obstacle spawns at same pos as the previous one
        float threshold = obstaclePreviousPosUp ? 0.2f : 0.8f;
        if (pos <= threshold)
            spawnObstacle(type, Spacegame.screenWidth + 500, 470 - posY);
        else
            spawnObstacle(type, Spacegame.screenWidth + 500, 200 - posY);

        obstaclePreviousPosUp = pos <= threshold;
    }

    private void spawnObstacle(int type, float posX, float posY) {
        //get obstacle from pool
        Obstacle o = (Obstacle) spawnPool.getFromPool(SpawnType.Obstacle);
        o.init(type, posX, posY);
    }

    private void spawnItems() {
        Item i = (Item) spawnPool.getFromPool(SpawnType.Item);
        float rand = random.nextFloat();
        if (rand < 0.2f) //repair tool
            i.init(0, Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        else if (rand < 0.4f) //shield
            i.init(2, Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        else if (rand < 0.6f) //boost
            i.init(3, Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        else if (rand <= 0.8f && player.getGunLevel() < player.getGunLevelMax()) //gun upgrade
            i.init(10 + player.getGunLevel() + 1, Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        else { //random gun upgrade/downgrade
            i.init(1, Spacegame.screenWidth + 150, 20 + Gameplay.random.nextInt(600));
        }
    }

    private void calcBoostEffect(float delta) {
        if (boostActive) {
            boostTimer += delta;
            if (boostTimer >= boostTime - boostStoppingTime) {
                speedUpFactor = 1.0f;
                player.setShootingActive(true);
            }
            if (boostTimer >= boostTime) {
                boostActive = false;
                boostTimer = 0.0f;
                speedUpFactor = 1.0f;
            }
        }
    }

    private void setBoost(float factor, float time) {
        speedUpFactor = factor;
        boostTimer = 0.0f;
        boostTime = time + boostStoppingTime;
        boostActive = true;
    }

    private void setSuperShot(boolean active) {
        superShotActive = active;
        if (active) {
            player.setSuperGun(0);
            superShotTimer = 0.0f;
        } else {
            player.setSuperGun(-1);
            superShotPoints = 0.0f;
        }
    }

    private void increaseSuperShotPoints(float amount) {
        superShotPoints += amount;
        if (superShotPoints >= superShotPointsMax) {
            superShotPoints = superShotPointsMax;
            setSuperShot(true);
        }
    }

    public float getSuperShotPoints() {
        return superShotPoints / superShotPointsMax;
    }

    private void updateSuperShot(float delta) {
        if (superShotActive) {
            superShotTimer += delta;
            superShotPoints = (1.0f - superShotTimer / superShotTime) * superShotPointsMax;
            if (superShotTimer >= superShotTime) {
                setSuperShot(false);
                superShotLevel++;
                superShotPoints = 0.0f;
                superShotPointsMax += supetShotPointsMaxIncreaseInterval;
                superShotTimer = 0.0f;
            }
        }
    }

    private void collisionItem(Item item) {
        if (item.getType() == 0) { //Repair tool
            player.heal(0.25f);
            player.showSparkles();
        }
        else if (item.getType() >= 10 && item.getType() <= 10 + player.getGunLevelMax()
            && !superShotActive) { //gun upgrades
            player.setGunLevel(item.getType() - 10);
        }
        else if (item.getType() == 1 && !superShotActive) { //random gun
            int rand = player.getGunLevel();
            while( rand == player.getGunLevel()) {
                rand = random.nextInt(player.getGunLevel() + 4);
            }
            System.out.println("Got random gun level: " + rand);
            player.setGunLevel(rand);
        }
        else if (item.getType() == 2) { //shield
            player.setShield(10, 0);
        }
        else if (item.getType() == 3) { //boost
            float boostTime = boostTimeMax;
            float boostSpeed = boostSpeedMax;
            setBoost(boostSpeed, boostTime);
            player.setShield(boostTime + boostStoppingTime, 1);
            player.setShootingActive(false);
        }

        item.kill(spawnPool);
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
                                score += enemy.getScore();
                                if (!superShotActive)
                                    increaseSuperShotPoints(enemy.getScore());
                                enemy.hit(m.power);
                                m.kill(spawnPool);
                            }
                        }
                    }
                    //collide player with enemy
                    if (!player.isDead()) {
                        if (player.getCollisionRectangle().overlaps(enemy.getCollisionRectangle())) {
                            score += enemy.getScore();
                            if (!superShotActive)
                                increaseSuperShotPoints(enemy.getScore());
                            enemy.kill(spawnPool);
                            player.hit(100000);
                            parallaxBackground.shake();
                        }
                    }
                }
            }
        }
        if (!player.isDead()) {
            //collide enemy missiles with player
            for (SpawnObject me : missilesEnemies) {
                Missile m = (Missile) me;
                if (m.isSpawned() && m.getBoundingRectangle().overlaps(player.getCollisionRectangle())) {
                    //enemy missile hit player
                    Explosion expl = (Explosion) Gameplay.spawnPool.getFromPool(SpawnType.Explosion);
                    expl.init(m.getX(), m.getY());
                    expl.setScale(0.6f);
                    expl.setOriginCenter();
                    expl.setOriginBasedPosition(m.getX(), m.getY());
                    m.kill(spawnPool);
                    player.hit(m.power);
                }
            }
            //collide obstacles with player
            for (SpawnObject o : obstacles) {
                Obstacle ob = (Obstacle) o;
                if (ob.isSpawned() && ob.getCollisionRectangle().overlaps(player.getCollisionRectangle())) {
                    //obstacle hit player
                    player.hit(100000);
                }
            }
            //collide items with player
            for (SpawnObject i : items) {
                Item it = (Item) i;
                if (it.isSpawned() && it.getCollisionRectangle().overlaps(player.getCollisionRectangle())) {
                    //collect item
                    collisionItem(it);
                }
            }
        }
    }

    public void updateSpawns(float delta, float speedUpFactor) {
        // could also be done in SpawnPool class but it
        // would be less performant to iterate through a HashMap
        for (SpawnObject e: enemies) {
            if (e.isSpawned())
                e.update(delta, speedUpFactor);
        }
        for (SpawnObject m: missilesEnemies) {
            if (m.isSpawned())
                m.update(delta, speedUpFactor);
        }
        for (SpawnObject mp: missilesPlayer) {
            if (mp.isSpawned())
                mp.update(delta, 1.0f);
        }
        for (SpawnObject ex: explosions) {
            if (ex.isSpawned())
                ex.update(delta, 1.0f);
        }
        for (SpawnObject i: items) {
            if (i.isSpawned())
                i.update(delta, 1.0f);
        }
        for (SpawnObject o: obstacles) {
            if (o.isSpawned())
                o.update(delta, speedUpFactor);
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
        for (SpawnObject i: items) {
            if (i.isSpawned())
                i.draw(sb);
        }
        for (SpawnObject o: obstacles) {
            if (o.isSpawned())
                o.draw(sb);
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
