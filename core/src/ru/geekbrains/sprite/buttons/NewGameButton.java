package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.ScaledTouchUpButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.MessageGameOver;

public class NewGameButton extends ScaledTouchUpButton {

    GameScreen gameScreen;

    public NewGameButton(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void action() {
        gameScreen.restart();
        MessageGameOver.setIsBoom(false);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.05f);
        setBottom(-0.25f);
    }
}
