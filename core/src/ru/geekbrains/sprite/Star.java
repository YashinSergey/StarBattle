package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    private Vector2 velocity;
    private Rect worldBounds;
    private float height;


    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        float vX = Rnd.nextFloat(-0.01f,  0.01f);
        float vY = Rnd.nextFloat(-0.06f, -0.2f);
        velocity = new Vector2(vX,vY);
        height = Rnd.nextFloat(0.0031f, 0.005f);
        setHeightProportion(height);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velocity,delta);
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()){
            setRight(worldBounds.getLeft());
        }
        if(getTop() < worldBounds.getBottom()){
            setBottom(worldBounds.getTop());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }
}
