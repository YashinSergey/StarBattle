package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.buttons.ExitButton;
import ru.geekbrains.sprite.buttons.PlayButton;
import ru.geekbrains.sprite.buttons.SoundButton;

public class MenuScreen extends BaseScreen {


    private Game game;
    private Texture backgroundTexture;
    private Background background;

    private TextureAtlas menuAtlas;

    private ExitButton exitButton;
    private PlayButton playButton;
    private static Music menuMusic;

    private TextureAtlas soundControlAtlas;
    private SoundButton soundControlButton;


    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("myTextures/spaceBattle.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        menuAtlas = new TextureAtlas("myTextures/myMenuAtlas.tpack");
        exitButton = new ExitButton(menuAtlas);
        playButton = new PlayButton(menuAtlas, game);
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/StellardroneBreatheInTheLigh.mp3"));
        menuMusic.setVolume(0.2f);
        menuMusic.play();
        menuMusic.setLooping(true);
        soundControlAtlas = new TextureAtlas("myTextures/soundControlAtlas.tpack");
        soundControlButton = new SoundButton(soundControlAtlas);
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta){
        checkMusic();
        soundControlButton.update(delta);
    }

    public void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        exitButton.draw(batch);
        playButton.draw(batch);
        soundControlButton.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        exitButton.resize(worldBounds);
        playButton.resize(worldBounds);
        soundControlButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        menuAtlas.dispose();
        menuMusic.dispose();
        soundControlAtlas.dispose();
        super.dispose();
    }

    @Override
    public void checkMusic(){
        if(SoundButton.soundOn && !menuMusic.isPlaying()) {
            menuMusic.play();
            menuMusic.setLooping(true);
        } else if(!SoundButton.soundOn && menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        exitButton.touchDown(touch,pointer);
        playButton.touchDown(touch, pointer);
        soundControlButton.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        exitButton.touchUp(touch,pointer);
        playButton.touchUp(touch, pointer);
        soundControlButton.touchUp(touch,pointer);
        return false;
    }
}
