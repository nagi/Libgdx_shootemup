package com.phil.spacegame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Resources extends AssetManager {

    //Paths to assets (Core/Assets/)
    private String fntBangers = "fonts/Bangers_bitmap.fnt";
    public String imgLogo = "img/logo.png";
    public String imgTwitter = "img/twitter_handle.png";
    public String bgLayerFront = "background/ground.png";
    public String bgLayerMid = "background/mountains.png";
    public String bgLayerMid2 = "background/mountains2.png";
    public String bgLayerBack = "background/background.png";
    public String tilesetSpaceships = "anim/spaceship_tileset.png";
    public String tilesetSpaceships2 = "anim/spaceship_tileset2.png";
    public String tilesetExplosion = "anim/explosion1.png";
    public String missile1 = "img/missile1.png";
    public String missile2 = "img/missile2.png";
    public String missile3 = "img/missile3.png";
    public String missile4 = "img/missile4.png";
    public String missile5 = "img/missile5.png";
    public String missile6 = "img/missile6.png";
    public String missile7 = "img/missile7.png";
    public String lifebarInner = "img/lifebar_inner.png";
    public String lifebarBorder = "img/lifebar_border.png";
    public String supershotInner = "img/supershot_inner.png";
    public String supershotBorder = "img/supershot_border.png";
    public String shadow = "img/shadow.png";
    public String animCloud = "anim/cloud.png";
    public String animItemRepair = "anim/item_repair.png";
    public String itemBubble = "img/item_border.png";
    public String tilesetGunUpgrades = "img/item_gun_upgrade.png";
    public String animItemCollect = "anim/anim_item_collect.png";
    public String shield = "img/shield.png";
    public String shieldBoost = "img/shield_boost.png";
    public String superShot = "img/supershot.png";

    //font instance for global usage
    public BitmapFont font1;

    public void loadAssets() {
        //set BitmapFont Filter
        BitmapFontParameter fontParam = new BitmapFontParameter();
        fontParam.magFilter = Texture.TextureFilter.Linear;
        fontParam.minFilter = Texture.TextureFilter.Linear;
        //load font
        load(fntBangers, BitmapFont.class, fontParam);
        //load images
        load(imgLogo, Texture.class);
        load(imgTwitter, Texture.class);
        load(bgLayerFront, Texture.class);
        load(bgLayerMid, Texture.class);
        load(bgLayerMid2, Texture.class);
        load(bgLayerBack, Texture.class);
        load(tilesetSpaceships, Texture.class);
        load(tilesetSpaceships2, Texture.class);
        load(missile1, Texture.class);
        load(missile2, Texture.class);
        load(missile3, Texture.class);
        load(missile4, Texture.class);
        load(missile5, Texture.class);
        load(missile6, Texture.class);
        load(missile7, Texture.class);
        load(tilesetExplosion, Texture.class);
        load(lifebarBorder, Texture.class);
        load(lifebarInner, Texture.class);
        load(supershotBorder, Texture.class);
        load(supershotInner, Texture.class);
        load(shadow, Texture.class);
        load(animCloud, Texture.class);
        load(animItemRepair, Texture.class);
        load(itemBubble, Texture.class);
        load(tilesetGunUpgrades, Texture.class);
        load(animItemCollect, Texture.class);
        load(shield, Texture.class);
        load(shieldBoost, Texture.class);
        load(superShot, Texture.class);

        System.out.println("FINISHED LOADING ASSETS!");
    }

    //initializations that have to be done after loadAssets() is finished
    public void initLoadedAssets() {
        //create BitmapFont
        font1 = get(fntBangers, BitmapFont.class);
        font1.setColor(Color.WHITE);
    }
}
