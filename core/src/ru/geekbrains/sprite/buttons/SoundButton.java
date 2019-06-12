package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;

public class SoundButton extends ScaledTouchUpButton {

    public static boolean soundOn = true;

    public SoundButton(TextureAtlas atlas) {
        super(atlas.findRegion("soundOn"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        pos.set(worldBounds.getRight() - 0.03f, worldBounds.getTop() - 0.02f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        return super.touchUp(touch, pointer);
    }

    @Override
    public void action() {
        if(SoundButton.soundOn) {
            SoundButton.soundOn = false;
        } else {
            SoundButton.soundOn = true;
        }
    }
}
