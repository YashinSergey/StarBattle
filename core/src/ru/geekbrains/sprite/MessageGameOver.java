package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.ExplosionPool;

public class MessageGameOver extends Sprite {

    private ExplosionPool explosionPool;
    private static boolean isBoom;

    public MessageGameOver(TextureAtlas atlas, ExplosionPool explosionPool) {
        super(atlas.findRegion("message_game_over"));
        this.explosionPool = explosionPool;
        isBoom = false;
    }

    public void boom(){
        if(!isBoom) {
            Explosion explosion = explosionPool.obtain();
            explosion.set(getWidth(), pos);
            isBoom = true;
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.07f);
        setBottom(0.009f);
    }

    public static void setIsBoom(boolean isBoom) {
        MessageGameOver.isBoom = isBoom;
    }
}
