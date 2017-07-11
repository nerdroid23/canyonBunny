package com.packtpub.libgdx.canyonbunny.util;

public class Constants {
    //visible game world is 5 meters wide and tall
    public static final float VIEWPORT_WIDTH = 5.0f;
    public static final float VIEWPORT_HEIGHT = 5.0f;

    //GUI width and height
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    //location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS =
            "android/assets/images/canyonbunny.pack.atlas";
    public static final String TEXTURE_ATLAS_UI =
            "android/assets/images/canyonbunny-ui.pack.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI =
            "android/assets/images/uiskin.atlas";

    //location of description file for skins
    public static final String SKIN_LIBGDX_UI =
            "android/assets/images/uiskin.json";
    public static final String SKIN_CANYONBUNNY_UI =
            "android/assets/images/canyonbunny-ui.json";

    //location of image file for level 01
    public static final String LEVEL_01 =
            "android/assets/levels/level-01.png";

    //shader location
    public static final String shaderMonochromeVertex =
            "android/assets/shaders/monochrome.vs";
    public static final String ShaderMonochromeFragment =
            "android/assets/shaders/monochrome.fs";

    //amount of extra lives at level start
    public static final int LIVES_START = 3;

    //duration of feather powerup
    public static final float ITEM_FEATHER_POWERUP_DURATION = 9;

    //number of carrots to spawn
    public static final int CARROTS_SPAWN_MAX = 100;

    //spawn radius for carrots
    public static final float CARROTS_SPAWN_RADIUS = 3.5f;

    //delay after game finished
    public static final float TIME_DELAY_GAME_FINISHED = 6;

    //delay after game over
    public static final float TIME_DELAY_GAME_OVER = 3;

    //angle of rotation for dead zone
    public static final float ACCEL_ANGLE_DEAD_ZONE = 5.0f;

    //max angle of rotation needed to gain max movement velocity
    public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;

    // Game preferences file
    public static final String PREFERENCES = "canyonbunny.prefs";
}
