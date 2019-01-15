package com.phil.spacegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class GUIStage {
    //groups for better management of GUI objects
    public Group grpMenuUI;
    public Group grpIngameUI;
    //the stage for all GUI objects
    private Stage stage;
    //menu gui objects
    private Label lblStart;
    private String txtStart = "Press to start";
    //ingame gui objects
    private String txtPause = "Pause";
    private String txtScore = "0";
    private Label lblPause;
    private Label lblScore;
    private Image imgLifeBarBorder;
    private Image imgLifeBarInner;
    private float lifeBarInnerWidthMax = 378;
    //reference to the font instance
    private BitmapFont fntCenter = Spacegame.resources.font1;

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
        lblStart = new Label(txtStart,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblStart.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2, Align.center);
        lblStart.setVisible(false);
        grpMenuUI.addActor(lblStart);

        //Add GUI to stage
        stage.addActor(grpMenuUI);
    }

    private void initIngameGUI() {
        grpIngameUI = new Group();

        //label for "Pause"
        lblPause = new Label(txtPause,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblPause.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2, Align.center);
        lblPause.setVisible(false);
        grpIngameUI.addActor(lblPause);
        //label for score
        lblScore = new Label(txtScore,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblScore.setPosition(
                Spacegame.screenWidth - 40, Spacegame.screenHeight -35, Align.right);
        lblScore.setVisible(false);
        grpIngameUI.addActor(lblScore);
        //life bar border
        TextureRegion txBorder = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.lifebarBorder, Texture.class), 0, 0, 384, 21);
        imgLifeBarBorder = new Image(txBorder);
        imgLifeBarBorder.setPosition(Spacegame.screenWidth / 2, Spacegame.screenHeight - 30, Align.center);
        imgLifeBarBorder.setVisible(false);
        //life bar inner
        TextureRegion txInner = new TextureRegion(
                Spacegame.resources.get(Spacegame.resources.lifebarInner, Texture.class), 0, 0, 378, 13);
        imgLifeBarInner = new Image(txInner);
        imgLifeBarInner.setPosition(
                Spacegame.screenWidth / 2 - imgLifeBarBorder.getWidth() / 2 + 5, Spacegame.screenHeight - 36);
        imgLifeBarInner.setVisible(false);
        // add "inner" before "border" to render it behind
        grpIngameUI.addActor(imgLifeBarInner);
        grpIngameUI.addActor(imgLifeBarBorder);

        //Add GUI to stage
        stage.addActor(grpIngameUI);
    }

    public void draw(float delta){
        if (stage != null) {
            stage.act(delta);
            stage.draw();
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
        imgLifeBarInner.setWidth(lifeBarInnerWidthMax * health);
    }

    public void showMenuGUI(boolean show) {
        lblStart.setVisible(show);
    }

    public void showGameGUI(boolean show) {
        lblPause.setVisible(false);
        lblScore.setVisible(show);
        imgLifeBarInner.setVisible(show);
        imgLifeBarBorder.setVisible(show);
    }

    public void showPause(boolean show) {
        lblPause.setVisible(show);
    }
}
