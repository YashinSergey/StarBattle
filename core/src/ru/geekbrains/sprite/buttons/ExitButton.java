package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;

public class ExitButton extends ScaledTouchUpButton {

    public ExitButton(TextureAtlas atlas) {
        super(atlas.findRegion("exit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.15f);
        setLeft(worldBounds.getLeft() + 0.005f);
        setBottom(worldBounds.getBottom() + 0.005f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
