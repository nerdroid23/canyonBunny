package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.screens.DirectedGame;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.libgdx.canyonbunny.screens.transitions.ScreenTransitionSlice;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

public class CanyonBunnyMain extends DirectedGame {
    @Override public void create() {
        //set libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //load assets
        Assets.instance.init(new AssetManager());

        //load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);

        //start game at menu screen
        ScreenTransition transition = ScreenTransitionSlice.init(2,
                ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
        this.setScreen(new MenuScreen(this), transition);
    }
}
