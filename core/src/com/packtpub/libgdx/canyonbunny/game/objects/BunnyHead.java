package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

public class BunnyHead extends AbstractGameObject {
    public static final String TAG = BunnyHead.class.getName();

    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING =
            JUMP_TIME_MAX - 0.018f;

    public enum VIEW_DIRECTION {LEFT, RIGHT}
    public enum JUMP_STATE {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }

    private TextureRegion regHead;
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasFeatherPowerUp;
    public float timeLeftFeatherPowerUp;

    public ParticleEffect dustParticles = new ParticleEffect();

    private Animation animNormal;
    private Animation animCopterTransform;
    private Animation animCopterTransformBack;
    private Animation animCopterRotate;

    public BunnyHead() {
        init();
    }

    public void init() {
        dimension.set(1, 1);

        animNormal = Assets.instance.bunny.animNormal;
        animCopterTransform = Assets.instance.bunny.animCopterTransform;
        animCopterTransformBack = Assets.instance.bunny.animCopterTransformBack;
        animCopterRotate = Assets.instance.bunny.animCopterRotate;
        setAnimation(animNormal);

        //center image on game
        origin.set(dimension.x / 2, dimension.y / 2);
        //bounding box for collision
        bounds.set(0, 0, dimension.x, dimension.y);
        //set physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        //view direction
        viewDirection = VIEW_DIRECTION.RIGHT;
        //Jump state
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        //power-ups
        hasFeatherPowerUp = false;
        timeLeftFeatherPowerUp = 0;

        //particles
        dustParticles.load(Gdx.files.internal(
                "android/assets/particles/dust.pfx"),
                Gdx.files.internal("android/assets/particles"));
    }

    public void setJumping (boolean jumpKeyPressed) {
        switch (jumpState) {
            case GROUNDED: //character is standing on a platform
                if (jumpKeyPressed) {
                    AudioManager.instance.play(Assets.instance.sounds.jump);
                    //start counting jump time from the beginning
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING: //rising in the air
                if (!jumpKeyPressed)
                    jumpState = JUMP_STATE.JUMP_FALLING;
                break;
            case FALLING: //falling down
            case JUMP_FALLING: //falling down after jump
                if (jumpKeyPressed && hasFeatherPowerUp) {
                    AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather,
                            1, MathUtils.random(1.0f, 1.1f));
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    public void setFeatherPowerUp (boolean pickedUp) {
        hasFeatherPowerUp = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerUp() {
        return hasFeatherPowerUp && timeLeftFeatherPowerUp > 0;
    }

    @Override public void update (float deltaTime) {
        super.update(deltaTime);
        if (velocity.x != 0) {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerUp > 0) {
            if (animation == animCopterTransformBack) {
                //restart "transform" animation if another feather power up
                //was picked during "TransformBack" animation. otherwise, the
                //"TransformBack" animation would be stuck while the power up is still active
                setAnimation(animCopterTransform);
            }
            timeLeftFeatherPowerUp -= deltaTime;
            if (timeLeftFeatherPowerUp < 0) {
                //disable power-up
                timeLeftFeatherPowerUp = 0;
                setFeatherPowerUp(false);
                setAnimation(animCopterTransformBack);
            }
        }
        dustParticles.update(deltaTime);

        //change animation state according to feather power up
        if (hasFeatherPowerUp) {
            if (animation == animNormal) {
                setAnimation(animCopterTransform);
            } else if (animation == animCopterTransform) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(animCopterRotate);
            }
        } else {
            if (animation == animCopterRotate) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(animCopterTransformBack);
            } else if (animation == animCopterTransformBack) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(animNormal);
            }
        }
    }

    @Override protected void updateMotionY (float deltaTime) {
        switch(jumpState) {
            case GROUNDED:
                jumpState = JUMP_STATE.JUMP_FALLING;
                if (velocity.x != 0) {
                    dustParticles.setPosition(position.x + dimension.x / 2, position.y);
                    dustParticles.start();
                }
                break;
            case JUMP_RISING:
                //keep track of jump time
                timeJumping += deltaTime;
                //jump time left?
                if (timeJumping <= JUMP_TIME_MAX) {
                    //still jumping
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
                //add delta times to track jump time
                timeJumping += deltaTime;
                //jump to minimal height if jump key was pressed
                //too short
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
                    //still jumping
                    velocity.y = terminalVelocity.y;
                }
        }
        if (jumpState != JUMP_STATE.GROUNDED) {
            dustParticles.allowCompletion();
            super.updateMotionY(deltaTime);
        }
    }

    @Override public void render (SpriteBatch batch) {
        TextureRegion reg = null;

        //draw particles
        dustParticles.draw(batch);

        //apply skin color
        batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());

        float dimCorrectionX = 0;
        float dimCorrectionY = 0;
        if (animation != animNormal) {
            dimCorrectionX = 0.05f;
            dimCorrectionY = 0.2f;
        }

        //draw image
        reg = (TextureRegion) animation.getKeyFrame(stateTime, true);
        batch.draw(reg.getTexture(),
                position.x, position.y,
                origin.x, origin.y,
                dimension.x + dimCorrectionX, dimension.y + dimCorrectionY,
                scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(),
                viewDirection == VIEW_DIRECTION.LEFT, false);
        //reset color to white
        batch.setColor(1, 1, 1, 1);
    }
}
