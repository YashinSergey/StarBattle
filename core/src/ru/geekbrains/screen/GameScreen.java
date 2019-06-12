package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import java.util.List;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.MyFont;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.MessageGameOver;
import ru.geekbrains.sprite.MyShip;
import ru.geekbrains.sprite.buttons.NewGameButton;
import ru.geekbrains.sprite.buttons.SoundButton;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    public enum State {PLAYING, PAUSE, GAME_OVER}
    private State state;
    public static long timer;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private int STAR_COUNT = 128;

    private Texture backgroundTexture;
    private Background background;

    private TextureAtlas mainAtlas;

    private Star[] stars;

    private MyShip myShip;
    private BulletPool bulletPool;

    private EnemyPool enemyPool;
    private Sound enemyBulletSound;
    private EnemyGenerator enemyGenerator;

    private ExplosionPool explosionPool;
    private Sound explosionSound;

    private Music gameMusic;

    private TextureAtlas soundControlAtlas;
    private SoundButton soundControlButton;
    private MessageGameOver messageGameOver;
    private NewGameButton newGameButton;

    private int frags;

    private MyFont myFont;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();
        myFont = new MyFont("font/font.fnt", "font/font.png");
        myFont.setSize(0.02f);
        backgroundTexture = new Texture("textures/space1.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        mainAtlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(mainAtlas);
        }
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(new TextureAtlas("textures/mainAtlas.tpack"), explosionSound);
        myShip = new MyShip(mainAtlas, bulletPool, explosionPool);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/StellardroneBreatheInTheLigh.mp3"));
        gameMusic.setVolume(0.3f);
        gameMusic.play();
        gameMusic.setLooping(true);
        soundControlAtlas = new TextureAtlas("myTextures/soundControlAtlas.tpack");
        soundControlButton = new SoundButton(soundControlAtlas);
        enemyBulletSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/laser1.mp3"));
        enemyPool = new EnemyPool(bulletPool, explosionPool, enemyBulletSound, worldBounds, myShip);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, mainAtlas);
        messageGameOver = new MessageGameOver(mainAtlas, explosionPool);
        newGameButton = new NewGameButton(mainAtlas, this);
        frags = 0;
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        state = State.PLAYING;
    }

    public void restart(){
        state = State.PLAYING;
        myShip.restart();
        frags = 0;
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        gameMusic.stop();
        gameMusic.play();
        gameMusic.setLooping(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyedActiveObjects();
        draw();
    }

    private void update(float delta){
        for (Star star: stars) {
            star.update(delta);
        }
        checkMusic();
        if (state != State.PAUSE) {
            soundControlButton.update(delta);
            explosionPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
        }
        if (state == State.PLAYING) {
            myShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta, frags);
        }
        if (state == State.GAME_OVER){
            newGameButton.update(delta);
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star: stars) {
            star.draw(batch);
        }
            enemyPool.drawActiveSprites(batch);
            explosionPool.drawActiveSprites(batch);
            soundControlButton.draw(batch);
        switch (state) {
            case PLAYING:
                myShip.draw(batch);
                bulletPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                if(System.currentTimeMillis() - timer < 5000) {
                    messageGameOver.draw(batch);
                } else {
                    messageGameOver.boom();
                    newGameButton.draw(batch);
                }
                break;
        }
        printInfo();
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star: stars) {
            star.resize(worldBounds);
        }
        myShip.resize(worldBounds);
        soundControlButton.resize(worldBounds);
        messageGameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
        super.resize(worldBounds);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        enemyBulletSound.dispose();
        gameMusic.dispose();
        myShip.dispose();
        explosionSound.dispose();
        soundControlAtlas.dispose();
        myFont.dispose();
        super.dispose();
    }

    private void checkCollisions(){
        if(state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + myShip.getHalfWidth();
            if (enemy.pos.dst(myShip.pos) < minDist) {
                enemy.destroy();
                myShip.damage(myShip.getHp());
                state = State.GAME_OVER;
            }
            for(Bullet bullet : bulletList){
                if (bullet.getOwner() != myShip || bullet.isDestroyed()){
                    continue;
                }
                if(enemy.isBulletCollision(bullet)){
                    enemy.damage(bullet.getDamage());
                    if(enemy.isDestroyed()){
                        frags++;
                    }
                    bullet.destroy();
                }
            }
        }
        for (Bullet bullet : bulletList){
            if(bullet.getOwner() == myShip || bullet.isDestroyed()){
                continue;
            }
            if(myShip.isBulletCollision(bullet)){
                myShip.damage(bullet.getDamage());
                bullet.destroy();
                if(myShip.isDestroyed()){
                    state = State.GAME_OVER;
                }
            }
        }
    }

    public void printInfo(){
        sbFrags.setLength(0);
        myFont.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + 0.004f, +
                worldBounds.getTop() - 0.005f);

        sbHp.setLength(0);
        myFont.draw(batch, sbHp.append(HP).append(myShip.getHp()), worldBounds.pos.x,
                worldBounds.getTop() - 0.004f, Align.center);

        sbLevel.setLength(0);
        myFont.draw(batch,sbLevel.append(LEVEL).append(enemyGenerator.getLevel()),soundControlButton.pos.x - soundControlButton.getHalfHeight(),
                worldBounds.getTop() - 0.004f, Align.right);
    }

    private void freeAllDestroyedActiveObjects(){
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    @Override
    public void checkMusic(){
        if(SoundButton.soundOn && !gameMusic.isPlaying() && state != State.PAUSE) {
            gameMusic.play();
            gameMusic.setLooping(true);
        } else if(!SoundButton.soundOn && gameMusic.isPlaying()){
            gameMusic.stop();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        switch (state){
            case PLAYING:
                myShip.keyDown(keycode);
            break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (state){
            case PLAYING:
                myShip.keyUp(keycode);
            break;
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        switch (state){
            case PLAYING:
                myShip.touchDown(touch, pointer);
                break;
            case GAME_OVER:
                newGameButton.touchDown(touch, pointer);
                break;
        }
        soundControlButton.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        switch (state){
            case PLAYING:
                myShip.touchUp(touch, pointer);
                break;
            case GAME_OVER:
                newGameButton.touchUp(touch, pointer);
                break;
        }
        soundControlButton.touchUp(touch, pointer);
        return false;
    }

    @Override
    public void pause() {
        super.pause();
        if(state != State.GAME_OVER) {
            state = State.PAUSE;
            gameMusic.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        if(state != State.GAME_OVER) {
            state = State.PLAYING;
            gameMusic.play();
            gameMusic.setLooping(true);
        }
    }
}
