package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.libgdx.canyonbunny.game.WorldController;
import com.packtpub.libgdx.canyonbunny.game.WorldRenderer;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private boolean paused;

    public GameScreen (DirectedGame game) {
        super(game);
    }

    @Override public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override public void render (float deltaTime) {
        //do not update game world when paused
        if (!paused) {
            //update game world by the time that has passed
            //since last rendered frame
            worldController.update(deltaTime);
        }
        //sets the clear screen color to cornflower blue
        Gdx.gl.glClearColor(0x64 / 2550f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        //clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game world to screen
        worldRenderer.render();
    }

    @Override public void resize (int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override public void show() {
        GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override public void hide() {
        worldController.dispose();
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override public void pause() {
        paused = true;
    }

    @Override public void resume() {
        super.resume();
        //only called on android
        paused = false;
    }
}
