package com.phil.spacegame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Resources extends AssetManager {

    //Paths to assets (Core/Assets/)
    private String fntBangers = "fonts/Bangers_bitmap.fnt";
    public String bgLayerFront = "background/ground.png";
    public String bgLayerMid = "background/mountains.png";
    public String bgLayerBack = "background/background.png";
    public String tilesetSpaceships = "spaceship_tileset.png";
    public String tilesetExplosion = "explosion1.png";
    public String missile1 = "missile1.png";
    public String missile2 = "missile2.png";
    public String missile3 = "missile3.png";
    public String missile4 = "missile4.png";
    public String missile5 = "missile5.png";
    public String missile6 = "missile6.png";
    public String missile7 = "missile7.png";
    public String lifebarInner = "lifebar_inner.png";
    public String lifebarBorder = "lifebar_border.png";
    public String shadow = "shadow.png";
    public String animCloud = "cloud.png";
    public String animItemRepair = "item_repair.png";
    public String itemBubble = "item_border.png";
    public String tilesetGunUpgrades = "item_gun_upgrade.png";
    public String animItemCollect = "anim_item_collect.png";
    public String shield = "shield.png";

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
        load(bgLayerFront, Texture.class);
        load(bgLayerMid, Texture.class);
        load(bgLayerBack, Texture.class);
        load(tilesetSpaceships, Texture.class);
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
        load(shadow, Texture.class);
        load(animCloud, Texture.class);
        load(animItemRepair, Texture.class);
        load(itemBubble, Texture.class);
        load(tilesetGunUpgrades, Texture.class);
        load(animItemCollect, Texture.class);
        load(shield, Texture.class);

        System.out.println("FINISHED LOADING ASSETS!");
    }

    //initializations that have to be done after loadAssets() is finished
    public void initLoadedAssets() {
        //create BitmapFont
        font1 = get(fntBangers, BitmapFont.class);
        font1.setColor(Color.WHITE);
    }
}
