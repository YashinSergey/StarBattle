package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class PlayButton extends ScaledTouchUpButton {

    private Game game;

    public PlayButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("play"));
        this.game = game;
        setHeightProportion(0.20f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setRight(worldBounds.getRight() - 0.001f);
        setBottom(worldBounds.getBottom() - 0.02f);

    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }


}
