package com.phil.spacegame;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class GUIStage {

    public Group grpIngameUI;

    private Stage stage;
    private boolean active;
    //top Item Labels
    private Image iconTreasure;
    private Label lblItemGold;
    //life Bar
    private Image imgLifeBar;
    private Image imgLifeBarInner;
    private Group grpLifeBar;
    private int lifeBarInnerOffsetY;
    private int lifeBarInnerOffsetX;
    private int lifeBarInnerLength;
    private int lifeBarInnerLengthMax;
    private int lifeBarInnerHeight;
    private int lifeBarYPos = 1000;
    private int lifeBarYPosFight = 800;
    private SequenceAction actionSequenceHideLifeBar;

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

        initTopLabels();
        initLifeBar();

        stage.addActor(grpIngameUI);
    }

    public void setLifeBarLength(float percent) {
        imgLifeBarInner.setWidth(lifeBarInnerLengthMax * percent);
    }

    public void showLifeBar(boolean fightMode) {
        grpLifeBar.addAction(Actions.alpha(1.0f));
        grpLifeBar.removeAction(actionSequenceHideLifeBar);
        if (fightMode)
            grpLifeBar.setY(lifeBarYPosFight);
        else
            grpLifeBar.setY(lifeBarYPos);
        grpLifeBar.setVisible(true);
    }

    public void hideLifeBar(float delay) {
        if (grpLifeBar.isVisible()) {
            if (delay > 0.0f) {
                grpLifeBar.removeAction(actionSequenceHideLifeBar);
                VisibleAction visibleAction = new VisibleAction();
                visibleAction.setVisible(true);

                DelayAction delayAction = new DelayAction(delay / 2);

                VisibleAction unVisibleAction = new VisibleAction();
                unVisibleAction.setVisible(false);

                actionSequenceHideLifeBar = new SequenceAction();
                actionSequenceHideLifeBar.addAction(visibleAction);
                actionSequenceHideLifeBar.addAction(delayAction);
                actionSequenceHideLifeBar.addAction(Actions.fadeOut(delay / 2));
                actionSequenceHideLifeBar.addAction(unVisibleAction);
                actionSequenceHideLifeBar.addAction(Actions.fadeIn(0.0f));

                grpLifeBar.addAction(actionSequenceHideLifeBar);
            } else
                grpLifeBar.setVisible(false);
        }
    }

    public void updateGoldStats() {
        String text = Integer.toString(999);
        lblItemGold.setText(text);
        pulseActor(iconTreasure);
    }


    private void initTopLabels() {
//        //Icon attributes
//        int iconOffsetX = 30;
//        int iconOffsetY = 0;
//        int iconWidth= 256;
//        int iconHeight = 128;
//        int iconMarginRight = 20;
//        int iconLabelOffsetLeft = 137;
//
//        //ICON TREASURE
//        TextureRegion iconTxtTreasure = new TextureRegion(
//                GdxGame.ressources.get(GdxGame.ressources.assets1, Texture.class), 512, 512, iconWidth, iconHeight);
////        iconTxt.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        iconTreasure = new Image(iconTxtTreasure);
//        iconTreasure.setBounds(
//                iconOffsetX,
//                GdxGame.screenHeight - iconOffsetY - iconHeight,
//                iconWidth, iconHeight);
//
//        grpIngameUI.addActor(iconTreasure);
//
//        //ICON TREASURE LABEL
//        String text = Integer.toString(gameControl.ship.getInventorySize(SpawnObject.SpawnType.ItemGold));
//        lblItemGold = new Label(text,
//                new Label.LabelStyle(GdxGame.ressources.fontPoints, Color.BLACK));
//        lblItemGold.setPosition(
//                iconOffsetX + iconLabelOffsetLeft, GdxGame.screenHeight - iconOffsetY - iconHeight + 50, Align.center);
//        grpIngameUI.addActor(lblItemGold);
    }


    private void initLifeBar() {
//        int textureXLifeBar = 640;
//        int textureYLifeBar = 384;
//        int textureWidthLifeBar = 384;
//        int textureHeightLifeBar = 64;
//        int textureYLifeBarInner = 500;
//
//        lifeBarInnerOffsetY = 18;
//        lifeBarInnerOffsetX = 30;
//        lifeBarInnerLengthMax = textureWidthLifeBar - lifeBarInnerOffsetX;// - (2 * lifeBarInnerOffsetX);
//        lifeBarInnerHeight = textureHeightLifeBar - (2 * lifeBarInnerOffsetY);
//        lifeBarInnerOffsetX = (int)(lifeBarInnerOffsetX / Spacegame.ratioX);
//
//        //Life bar border
//        TextureRegion imgLifeBarTexture = new TextureRegion(
//                Spacegame.ressources.get(Spacegame.ressources.assets1, Texture.class),
//                textureXLifeBar, textureYLifeBar, textureWidthLifeBar, textureHeightLifeBar);
//        imgLifeBar = new Image(imgLifeBarTexture);
//        imgLifeBar.setBounds(0, 0, textureWidthLifeBar, textureHeightLifeBar);
//
//        //life bar inner
//        TextureRegion imgLifeBarInnerTexture = new TextureRegion(
//                Spacegame.ressources.get(Spacegame.ressources.assets1, Texture.class),
//                textureXLifeBar + 10,textureYLifeBarInner, 10, 10);
//        imgLifeBarInner = new Image(imgLifeBarInnerTexture);
//        imgLifeBarInner.setBounds(lifeBarInnerOffsetX, 0 + lifeBarInnerOffsetY, lifeBarInnerLengthMax, lifeBarInnerHeight);
//
//        grpLifeBar = new Group();
//        grpLifeBar.addActor(imgLifeBarInner);
//        grpLifeBar.addActor(imgLifeBar);
//
//        grpLifeBar.setPosition(ship.getX() + (ship.getWidth() / 2) - (textureWidthLifeBar / 2) - 30, lifeBarYPos);
//
//        grpLifeBar.setVisible(false);
//
//        grpIngameUI.addActor(grpLifeBar);
    }


    // Helper for animating actors -------------------------------- //

    private void showAndPulse(Actor actor, float delay) {
        float scaleUpDelay = 0.13f;
        float scaleDownDelay = 0.07f;
        delay -= (scaleUpDelay + scaleDownDelay);

        actor.setOrigin(actor.getWidth() / 2.0f, actor.getHeight() / 2.0f);

        VisibleAction unVisibleAction = new VisibleAction();
        unVisibleAction.setVisible(false);

        VisibleAction visibleAction = new VisibleAction();
        visibleAction.setVisible(true);

        DelayAction delayAction = new DelayAction(delay);

        ScaleToAction scaleActionInit = new ScaleToAction();
        scaleActionInit.setScale(0.01f);
        scaleActionInit.setDuration(0.0f);

        ScaleToAction scaleUpAction = new ScaleToAction();
        scaleUpAction.setScale(1.3f);
        scaleUpAction.setDuration(scaleUpDelay);

        ScaleToAction scaleDownAction = new ScaleToAction();
        scaleDownAction.setScale(1.0f);
        scaleDownAction.setDuration(scaleDownDelay);

        SequenceAction sequence = new SequenceAction();
        sequence.addAction(unVisibleAction);
        sequence.addAction(scaleActionInit);
        sequence.addAction(delayAction);
        sequence.addAction(visibleAction);
        sequence.addAction(scaleUpAction);
        sequence.addAction(scaleDownAction);
        actor.addAction(sequence);
    }

    public void scaleUpActor(Actor img) {
        img.setOrigin(Align.center);
        img.setScale(1.0f);

        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(1.1f);
        scaleAction.setDuration(0.05f);
        img.addAction(scaleAction);
    }

    public void scaleDownActor(Actor img){
        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(1.0f);
        scaleAction.setDuration(0.05f);
        img.addAction(scaleAction);
    }

    public void scaleActor(Actor actor, float scale, float duration) {
        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(scale);
        scaleAction.setDuration(duration);
        actor.addAction(scaleAction);
    }

    public void pulseActor(Actor actor) {
        actor.setOrigin(Align.center);
        actor.setScale(1.0f);

        DelayAction delayAction = new DelayAction(0.1f);

        ScaleToAction scaleUpAction = new ScaleToAction();
        scaleUpAction.setScale(1.2f);
        scaleUpAction.setDuration(0.1f);

        ScaleToAction scaleDownAction = new ScaleToAction();
        scaleDownAction.setScale(1.0f);
        scaleDownAction.setDuration(0.1f);

        SequenceAction sequence = new SequenceAction();
        sequence.addAction(delayAction);
        sequence.addAction(scaleUpAction);
        sequence.addAction(scaleDownAction);
        actor.addAction(sequence);
    }

    public void draw(float delta){
        if (active) {
            if (stage != null) {
                stage.act(delta);
                stage.draw();
            }
        }
    }
}
