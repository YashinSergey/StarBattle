package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Background  extends Sprite {

    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(1f);
        pos.set(worldBounds.pos);
    }
}
