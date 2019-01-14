package com.phil.spacegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class GUIStage {

    public Group grpMenuUI;
    public Group grpIngameUI;

    private Stage stage;
    private boolean active;

    private Label lblPause;
    private Label lblStart;
    private Label lblScore;
    private String txtPause = "Pause";
    private String txtStart = "Press to start";
    private String txtScore = "0";

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

        //init ingame gui elements
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
                Spacegame.screenWidth - 30, Spacegame.screenHeight -40, Align.right);
        lblScore.setVisible(false);
        grpIngameUI.addActor(lblScore);

        //Add GUI to stage
        stage.addActor(grpIngameUI);
    }

    public void draw(float delta){
        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
    }

    public void updateScore(int score) {
        txtScore = Integer.toString(score);
        lblScore.setText(txtScore);
    }

    public void showMenuGUI(boolean show) {
        lblStart.setVisible(show);
    }

    public void showGameGUI(boolean show) {
        lblPause.setVisible(false);
        lblScore.setVisible(show);
    }

    public void showPause(boolean show) {
        lblPause.setVisible(show);
    }
}
