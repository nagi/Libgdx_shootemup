package com.phil.spacegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class GUIStage {

    public Group grpIngameUI;

    private Stage stage;
    private boolean active;

    private Label lblPause;
    private Label lblStart;
    private String txtPause = "Pause";
    private String txtStart = "Press to start";

    private BitmapFont fntCenter = Spacegame.resources.font1;

    public GUIStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        active = true;
        grpIngameUI.setVisible(true);
    }

    public void hide() {
        active = false;
        grpIngameUI.setVisible(false);
    }

    public void init() {
        grpIngameUI = new Group();

        //init gui elements
        lblStart = new Label(txtStart,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblStart.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2, Align.center);
        lblStart.setVisible(false);
        grpIngameUI.addActor(lblStart);

        lblPause = new Label(txtPause,
                new Label.LabelStyle(Spacegame.resources.font1, Color.WHITE));
        lblPause.setPosition(
                Spacegame.screenWidth / 2, Spacegame.screenHeight / 2, Align.center);
        lblPause.setVisible(false);
        grpIngameUI.addActor(lblPause);

        stage.addActor(grpIngameUI);
    }

    public void draw(float delta){
        if (active) {
            if (stage != null) {
                stage.act(delta);
                stage.draw();
            }
        }
    }

    public void showStart(boolean show) {
        lblStart.setVisible(show);
    }

    public void showPause(boolean show) {
        lblPause.setVisible(show);
    }
}
