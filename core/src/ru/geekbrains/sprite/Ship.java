package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.buttons.SoundButton;

public abstract class Ship  extends Sprite {

    protected BulletPool bulletPool;
    protected Vector2 bulletPos;
    protected TextureRegion bulletRegion;
    protected ExplosionPool explosionPool;

    protected Vector2 v;
    protected Vector2 v0;

    protected Vector2 bulletV;
    protected float bulletHeight;

    protected Rect worldBounds;

    protected float reloadInterval;
    protected float reloadTimer;

    protected Sound bulletSound;

    protected  int damage;
    protected int hp;

    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer = damageAnimateInterval;

    public Ship(){
    }

    public Ship(TextureRegion region, int rows, int cols, int frame) {
        super(region, rows, cols, frame);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if(damageAnimateTimer >= damageAnimateInterval){
            frame = 0;
        }
    }

    public void damage(int damage){
        hp -= damage;
        if(hp <= 0){
            hp = 0;
            destroy();
        }
        frame = 1;
        damageAnimateTimer = 0f;
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, this.worldBounds, damage);
        if(SoundButton.soundOn) {
            bulletSound.play(0.2f);
        }
    }

    public abstract boolean isBulletCollision(Rect bullet);

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public int getHp() {
        return hp;
    }
}
