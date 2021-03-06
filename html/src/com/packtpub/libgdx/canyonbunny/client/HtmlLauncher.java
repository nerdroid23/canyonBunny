package com.packtpub.libgdx.canyonbunny.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(800, 480);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CanyonBunnyMain();
        }
}