package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.sprite.buttons.SoundButton;

public class Explosion extends Sprite {

    private float animateInterval = 0.017f;
    private float animateTimer;
    private Sound explosionSound;

    public Explosion(TextureAtlas atlas, Sound sound) {
        super(atlas.findRegion("explosion"), 9,9,74);
        this.explosionSound = sound;
    }

    public void set(float height, Vector2 pos){
        this.pos.set(pos);
        setHeightProportion(height);
        if(SoundButton.soundOn) {
            explosionSound.play(0.09f);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animateTimer += delta;
        if(animateTimer >= animateInterval){
            animateTimer = 0f;
            if(++frame == regions.length){
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
