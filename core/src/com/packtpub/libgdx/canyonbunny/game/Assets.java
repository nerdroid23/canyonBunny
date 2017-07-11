package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetBunny bunny;
    public AssetRock rock;
    public AssetGoldCoin goldCoin;
    public AssetFeather feather;
    public AssetLevelDecoration levelDecoration;
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;

    //singleton: prevent instantiation from other classes
    private Assets() {}

    public class AssetSounds {
        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;

        public AssetSounds (AssetManager am) {
            jump = am.get("android/assets/sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("android/assets/sounds/jump_with_feather.wav", Sound.class);
            pickupCoin = am.get("android/assets/sounds/pickup_coin.wav", Sound.class);
            pickupFeather = am.get("android/assets/sounds/pickup_feather.wav", Sound.class);
            liveLost = am.get("android/assets/sounds/live_lost.wav", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;
        public AssetMusic (AssetManager am) {
            song01 = am.get("android/assets/music/keith303_-_brand_new_highscore.mp3",
                    Music.class);
        }
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            //create three fonts using libgdx's 15px bitmap font
            String path = "android/assets/images/arial-15.fnt";
            defaultSmall = new BitmapFont(Gdx.files.internal(path), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(path), true);
            defaultBig = new BitmapFont(Gdx.files.internal(path), true);
            //set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            //enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
        }
    }

    public void init (AssetManager assetManager) {
        this.assetManager = assetManager;
        //set asset manager error handler
        assetManager.setErrorListener(this);
        //load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
                TextureAtlas.class);
        //load sounds
        assetManager.load("android/assets/sounds/jump.wav", Sound.class);
        assetManager.load("android/assets/sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("android/assets/sounds/pickup_coin.wav", Sound.class);
        assetManager.load("android/assets/sounds/pickup_feather.wav", Sound.class);
        assetManager.load("android/assets/sounds/live_lost.wav", Sound.class);
        //load music
        assetManager.load("android/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
        //start loading assets and wait until finished
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: " +
                assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        //enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        //create game resource objects
        fonts = new AssetFonts();
        bunny = new AssetBunny(atlas);
        rock = new AssetRock(atlas);
        goldCoin = new AssetGoldCoin(atlas);
        feather = new AssetFeather(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override public void error (AssetDescriptor asset,
                                 Throwable throwable) {
        Gdx.app.debug(TAG, "Couldn't load asset '" +
                asset.fileName + "'", (Exception)throwable);
    }

    public class AssetBunny {
        public final AtlasRegion head;
        public final Animation animNormal;
        public final Animation animCopterTransform;
        public final Animation animCopterTransformBack;
        public final Animation animCopterRotate;

        public AssetBunny (TextureAtlas atlas) {
            head = atlas.findRegion("bunny_head");

            Array<AtlasRegion> regions = null;
            AtlasRegion region = null;

            //animation: bunny normal
            regions = atlas.findRegions("anim_bunny_normal");
            animNormal = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP_PINGPONG);

            //animation: bunny copter - knot ears
            regions = atlas.findRegions("anim_bunny_copter");
            animCopterTransform = new Animation(1.0f / 10.0f, regions);

            //animation: bunny copter - unknot ears
            regions = atlas.findRegions("anim_bunny_copter");
            animCopterTransformBack = new Animation(1.0f / 10.0f, regions,
                    Animation.PlayMode.REVERSED);

            //animation: bunny copter - rotate ears
            regions = new Array<AtlasRegion>();
            regions.add(atlas.findRegion("anim_bunny_copter", 4));
            regions.add(atlas.findRegion("anim_bunny_copter", 5));
            animCopterRotate = new Animation(1.0f / 15.0f, regions);
        }
    }

    public class AssetRock {
        public final AtlasRegion edge;
        public final AtlasRegion middle;
        public AssetRock (TextureAtlas atlas) {
            edge = atlas.findRegion("rock_edge");
            middle = atlas.findRegion("rock_middle");
        }
    }

    public class AssetGoldCoin {
        public final AtlasRegion goldCoin;
        public final Animation animGoldCoin;
        public AssetGoldCoin (TextureAtlas atlas) {
            goldCoin = atlas.findRegion("item_gold_coin");

            //animation
            Array<AtlasRegion> regions = atlas.findRegions("anim_gold_coin");
            AtlasRegion region = regions.first();
            for (int i = 0; i < 10; i++) regions.insert(0,region);
            animGoldCoin = new Animation(1.0f / 20.0f, regions, Animation.PlayMode.LOOP_PINGPONG);
        }
    }

    public class AssetFeather {
        public final AtlasRegion feather;
        public AssetFeather (TextureAtlas atlas) {
            feather = atlas.findRegion("item_feather");
        }
    }

    public class AssetLevelDecoration {
        public final AtlasRegion cloud01;
        public final AtlasRegion cloud02;
        public final AtlasRegion cloud03;
        public final AtlasRegion mountainLeft;
        public final AtlasRegion mountainRight;
        public final AtlasRegion waterOverlay;
        public final AtlasRegion carrot;
        public final AtlasRegion goal;

        public AssetLevelDecoration (TextureAtlas atlas) {
            cloud01 = atlas.findRegion("cloud01");
            cloud02 = atlas.findRegion("cloud01");
            cloud03 = atlas.findRegion("cloud03");
            mountainLeft = atlas.findRegion("mountain_left");
            mountainRight = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_overlay");
            carrot = atlas.findRegion("carrot");
            goal = atlas.findRegion("goal");
        }
    }
}
