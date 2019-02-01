package com.phil.spacegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GUIStage {
    //groups for better management of GUI objects
    public Group grpMenuUI;
    public Group grpIngameUI;
    //the stage for all GUI objects
    private Stage stage;
    //menu gui objects
    private Label lblStart;
    private String txtStart = "Press \" W \" to start!";
    private Image imgLogo;
    private Image imgTwitter;
    //ingame gui objects
    private String txtPause = "Pause";
    private String txtScore = "0";
    private String txtGameOver = "Gameover!";
    private String txtHighscore = "Highscore: ";
    private String txtHighscorePoints = "0";
    private String txtPressForSuperShot = "Super shot!  (Press \" L \")";
    private int offsetGameover = 100;
    private int offsetHighscore = 100;
    private Label lblPause;
    private Label lblScore;
    private Label lblGameOver;
    private Label lblHighscore;
    private Label lblSuperShot;
    private Image imgLifeBarBorder;
    private Image imgLifeBarInner;
    private float lifeBarInnerWidthMax = 374;
    private Image imgSuperShotBarBorder;
    private Image imgSuperShotBarInner;
    private float superShotBarInnerWidthMax = 378;
    private float offsetSuperShotBar = 42;
    private float offsetLifeBar = 20;
    private Image superShotCaption;
    //super shot pulse cooldown
    private boolean justPulsedSuperShot;
    private float pulseCooldown;
    private float pulseCooldownTime = 1.0f;
    //super shot loaded
    private boolean superShotLoadedAction;
    private float superShotLoadedBlinkTimer;
    private float superShotLoadedBlinkInterval = 0.6f;
    //life bar blinking
    private float playerHealth;
    private float lifeBarBlinkTimer;
    private float lifeBarBlinkTime = 0.5f;
    private float lifeBarBlinkThreshold = 0.2f;


    public GUIStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void init() {
        initMenuGUI();
        initIngameGUI();
    }

    private void initMenuGUI() {
        grpMenuUI = new Group();

        //init menu gui elements
        //logo
        TextureRegion txLogo = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.imgLogo, Texture.class));
        imgLogo = new Image(txLogo);
        imgLogo.setPosition(Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 + 550, Align.center);
        imgLogo.setVisible(false);
        grpMenuUI.addActor(imgLogo);

        //twitter handle
        TextureRegion txTwitter = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.imgTwitter, Texture.class));
        imgTwitter = new Image(txTwitter);
        imgTwitter.setPosition(30, 20);
        imgTwitter.setVisible(false);
        grpMenuUI.addActor(imgTwitter);

        //label "Start"
        lblStart = new Label(txtStart,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblStart.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 - 150, Align.center);
        lblStart.setVisible(false);
        grpMenuUI.addActor(lblStart);

        //Add GUI to stage
        stage.addActor(grpMenuUI);
    }

    private void initIngameGUI() {
        grpIngameUI = new Group();
        grpIngameUI.setSize(Spacegame.screenWidth, Spacegame.screenHeight);
//        stage.setDebugAll(true);

        //Table for managing scene objects
        //currently only used for aligning text of lblScore to right
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        grpIngameUI.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE);

        //label for score
        //to achieve right alignment of the text, a table is needed
        lblScore = new Label(txtScore,labelStyle);
        lblScore.setVisible(false);
        lblScore.setAlignment(Align.topRight);
        int lblScoreWidth = 150;
        table.add(lblScore).size(lblScoreWidth, 50).padLeft(Spacegame.screenWidth - lblScoreWidth - 40);

        //life bar border
        TextureRegion txBorder = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.lifebarBorder, Texture.class), 0, 0, 384, 21);
        imgLifeBarBorder = new Image(txBorder);
        imgLifeBarBorder.setPosition(Spacegame.screenWidth / 2, Spacegame.screenHeight - offsetLifeBar, Align.center);
        imgLifeBarBorder.setVisible(false);

        //life bar inner
        TextureRegion txInner = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.lifebarInner, Texture.class), 0, 0, 378, 12);
        imgLifeBarInner = new Image(txInner);
        imgLifeBarInner.setPosition(
                Spacegame.screenWidth / 2 + 2, Spacegame.screenHeight - offsetLifeBar, Align.center);
        imgLifeBarInner.setVisible(false);

        // add "inner" before "border" to render it behind
        grpIngameUI.addActor(imgLifeBarInner);
        grpIngameUI.addActor(imgLifeBarBorder);

        //supershot bar border
        TextureRegion txSuperShotBorder = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.supershotBorder, Texture.class), 0, 0, 384, 21);
        imgSuperShotBarBorder = new Image(txSuperShotBorder);
        imgSuperShotBarBorder.setPosition(Spacegame.screenWidth / 2, Spacegame.screenHeight - offsetSuperShotBar, Align.center);
        imgSuperShotBarBorder.setVisible(false);

        //supershot bar inner
        TextureRegion txSuperShotInner = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.supershotInner, Texture.class), 0, 0, 378, 12);
        imgSuperShotBarInner = new Image(txSuperShotInner);
        imgSuperShotBarInner.setPosition(
                Spacegame.screenWidth / 2 + 2, Spacegame.screenHeight - offsetSuperShotBar, Align.center);
        imgSuperShotBarInner.setVisible(false);

        // add "inner" before "border" to render it behind
        grpIngameUI.addActor(imgSuperShotBarInner);
        grpIngameUI.addActor(imgSuperShotBarBorder);

        //super shot caption
        TextureRegion txSuperShotCaption = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.superShot, Texture.class), 0, 0, 464, 200);
        superShotCaption = new Image(txSuperShotCaption);
        superShotCaption.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 + 50, Align.center);
        superShotCaption.setVisible(false);

        grpIngameUI.addActor(superShotCaption);

        //label for "Press Key for Super Shot"
        lblSuperShot = new Label(txtPressForSuperShot, labelStyle);
        lblSuperShot.setScale(0.7f);
        lblSuperShot.setFontScale(0.7f);
        lblSuperShot.setAlignment(Align.center);
        lblSuperShot.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 + 290, Align.center);
        lblSuperShot.setVisible(false);
        grpIngameUI.addActor(lblSuperShot);

        //label for "Pause"
        lblPause = new Label(txtPause, labelStyle);
        lblPause.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2, Align.center);
        lblPause.setVisible(false);
        grpIngameUI.addActor(lblPause);

        //label for "Gameover!"
        lblGameOver = new Label(txtGameOver, labelStyle);
        lblGameOver.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 + offsetGameover, Align.center);
        lblGameOver.setVisible(false);
        grpIngameUI.addActor(lblGameOver);

        lblHighscore = new Label(txtHighscore, labelStyle);
        lblHighscore.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 - offsetHighscore, Align.center);
        lblHighscore.setVisible(false);
        grpIngameUI.addActor(lblHighscore);

        //Add GUI to stage
        stage.addActor(grpIngameUI);
    }

    public void draw(float delta){
        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
        if (justPulsedSuperShot) {
            pulseCooldown += delta;
            if (pulseCooldown >= pulseCooldownTime) {
                pulseCooldown = 0.0f;
                justPulsedSuperShot = false;
                superShotCaption.setVisible(false);
            }
        }

        if (superShotLoadedAction) {
            superShotLoadedBlinkTimer += delta;
            if (superShotLoadedBlinkTimer >= superShotLoadedBlinkInterval) {
                superShotLoadedBlinkTimer = 0.0f;
                lblSuperShot.setVisible(!lblSuperShot.isVisible());
                imgSuperShotBarInner.setVisible(!imgSuperShotBarInner.isVisible());
            }
        }

        if (playerHealth <= lifeBarBlinkThreshold) {
            lifeBarBlinkTimer += delta;
            if (lifeBarBlinkTimer >= lifeBarBlinkTime) {
                imgLifeBarInner.setVisible(!imgLifeBarInner.isVisible());
                lifeBarBlinkTimer = 0.0f;
            }
        }
    }

    //update score label
    public void updateScore(int score) {
        txtScore = Integer.toString(score);
        lblScore.setText(txtScore);
    }

    //update health bar.
    //health between 0.0-1.0
    public void updateHealth(float health) {
        this.playerHealth = health;
        float oldWidth = imgLifeBarInner.getWidth();
        imgLifeBarInner.setWidth(lifeBarInnerWidthMax * health);
        if (oldWidth < imgLifeBarInner.getWidth()) {
            imgLifeBarInner.setVisible(true);
            imgLifeBarBorder.setVisible(true);
            pulseActor(imgLifeBarBorder, 1.2f);
            pulseActor(imgLifeBarInner, 1.22f);

        }
    }

    public void updateSuperShot(float percent, boolean isLoaded) {
        imgSuperShotBarInner.setWidth(superShotBarInnerWidthMax * percent);

//        if (percent >= 0.99f && !justPulsedSuperShot) {
//            justPulsedSuperShot = true;
//            pulseActor(imgSuperShotBarBorder, 1.3f);
//            pulseActor(imgSuperShotBarInner, 1.32f);
//
//            blinkAndhideActor(superShotCaption, 6);
//        }

        if (isLoaded && !superShotLoadedAction) {
            superShotLoadedAction = true;
            imgSuperShotBarInner.setVisible(false);
        }
    }

    public void setSuperShotActive() {
        superShotLoadedAction = false;
        lblSuperShot.setVisible(false);
        imgSuperShotBarInner.setVisible(true);
    }

    public void showMenuGUI(boolean show) {
        imgLogo.setVisible(show);
        if (show == true) {
            imgLogo.addAction(Actions.moveToAligned(Spacegame.screenWidth / 2, Spacegame.screenHeight / 2 + 150, Align.center, 0.4f));
            lblStart.addAction(sequence(hide(), delay(0.7f), show()));
        }
        else {
            imgLogo.setVisible(false);
            lblStart.setVisible(false);
        }
        imgTwitter.setVisible(show);
    }

    public void showGameGUI(boolean show) {
        lblStart.clearActions();
        lblPause.setVisible(false);
        lblGameOver.setVisible(false);
        lblHighscore.setVisible(false);
        superShotCaption.setVisible(false);
        lblSuperShot.setVisible(false);
        lblScore.setVisible(show);
        imgLifeBarInner.setVisible(show);
        imgLifeBarBorder.setVisible(show);
        imgSuperShotBarInner.setVisible(show);
        imgSuperShotBarBorder.setVisible(show);
    }

    public void showPause(boolean show) {
        lblPause.setVisible(show);
    }

    public void showGameOver(int highscore) {
        lblGameOver.setVisible(true);
        lblHighscore.setText(txtHighscore + "\n" + Integer.toString(highscore));
        lblHighscore.setAlignment(Align.center);
        lblHighscore.setVisible(true);
        imgSuperShotBarInner.setVisible(false);
        imgSuperShotBarBorder.setVisible(false);
    }

    public void pulseActor(Actor actor, float scale) {
        actor.setOrigin(Align.center);
        actor.setScale(1.0f);

        DelayAction delayAction = new DelayAction(0.1f);

        ScaleToAction scaleUpAction = new ScaleToAction();
        scaleUpAction.setScale(scale);
        scaleUpAction.setDuration(0.2f);

        ScaleToAction scaleDownAction = new ScaleToAction();
        scaleDownAction.setScale(1.0f);
        scaleDownAction.setDuration(0.2f);

        SequenceAction sequence = new SequenceAction();
        sequence.addAction(delayAction);
        sequence.addAction(scaleUpAction);
        sequence.addAction(scaleDownAction);
        actor.addAction(sequence);
    }

    public void blinkAndhideActor(Actor actor, float blinkAmount) {
        SequenceAction sequence = new SequenceAction();
        for (int i=0; i<blinkAmount; ++i) {
            DelayAction delayAction1 = new DelayAction(0.2f);
            DelayAction delayAction2 = new DelayAction(0.2f);

            VisibleAction unvisible = new VisibleAction();
            unvisible.setVisible(false);

            VisibleAction visible = new VisibleAction();
            visible.setVisible(true);

            sequence.addAction(visible);
            sequence.addAction(delayAction1);
            sequence.addAction(unvisible);
            sequence.addAction(delayAction2);
        }
        actor.addAction(sequence);
    }

}
