package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.buttons.SoundButton;

public class MyShip extends Ship {

    private static final int INVALID_POINTER = -1;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private boolean fire;

    public MyShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2,2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.explosionPool = explosionPool;
        v = new Vector2();
        v0 = new Vector2(0.5f,0);
        bulletV = new Vector2(0f, 0.5f);
        this.bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/blasterSound.mp3"));
        bulletPos = new Vector2();
        fire = false;
        this.reloadInterval = 0.2f;
        this.bulletHeight = 0.01f;
        this.damage = 1;
        this.hp = 100;
    }

    public void restart(){
        this.hp = 100;
        this.pos.x = worldBounds.pos.x;
        flushDestroy();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        reloadTimer += delta;
        if(reloadTimer >= reloadInterval) {
            shoot();
            reloadTimer = 0;
        }
        if(getRight() > worldBounds.getRight()){
            setRight(worldBounds.getRight());
            stop();
        }
        if(getLeft() < worldBounds.getLeft() - 0.02f){ // - 0.02f потому что прозрачная часть текстуры отходит
            setLeft(worldBounds.getLeft() - 0.02f);    // от корабля на 0.02f
            stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.15f);
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    public void dispose() {
        bulletSound.dispose();
    }

    @Override
    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos);
        bulletPos.y += getHalfHeight();
        if(!fire) {
            bulletPos.x -= 0.015f;
            bulletPos.y -= 0.03f;
            bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, this.worldBounds, damage);
            if(SoundButton.soundOn) {
                bulletSound.play(0.09f);
            }
            fire = true;
        } else {
            bulletPos.x += 0.015f;
            bulletPos.y -= 0.03f;
            bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, this.worldBounds, damage);
            if(SoundButton.soundOn) {
                bulletSound.play(0.09f);
            }
            fire = false;
        }
    }

    @Override
    public boolean isBulletCollision(Rect bullet){
        return !(
                bullet.getRight() < getLeft() ||
                bullet.getLeft() > getRight() ||
                bullet.getBottom() > pos.y ||
                bullet.getTop() < getBottom()
        );
    }

    @Override
    public void destroy() {
        super.destroy();
        GameScreen.timer = System.currentTimeMillis();
        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        hp = 0;
        stop();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(touch.x < worldBounds.pos.x){
            if(leftPointer != INVALID_POINTER){
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else{
            if(rightPointer != INVALID_POINTER){
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(pointer == leftPointer){
            leftPointer = INVALID_POINTER;
            if(rightPointer != INVALID_POINTER){
                moveRight();
            }else {
                stop();
            }
        }
        if(pointer == rightPointer){
            rightPointer = INVALID_POINTER;
            if(leftPointer != INVALID_POINTER){
                moveLeft();
            }else {
                stop();
            }
        }
        return false;
    }


    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if(pressedRight){
                    moveRight();
                }else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if(pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    private void moveRight(){
        v.set(v0);
    }

    private void moveLeft(){
        v.set(v0).rotate(180);
    }

    private void stop(){
        v.setZero();
    }

}
