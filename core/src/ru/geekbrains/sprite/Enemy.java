package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class Enemy extends Ship {

    private enum State {DESCENT, FIGHT}
    private State state;
    private Vector2 descentV = new Vector2(0,-0.15f);
    private MyShip myShip;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound, Rect worldBounds, MyShip myShip){
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.myShip = myShip;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state){
            case DESCENT:
                if(getTop() <= worldBounds.getTop()){
                    v.set(v0);
                    state = State.FIGHT;
                    break;
                }
            case FIGHT:
                reloadTimer += delta;
                if(reloadTimer >= reloadInterval && !myShip.isDestroyed()) {
                    shoot();
                    reloadTimer = 0;
                }
                if(getBottom() < worldBounds.getBottom() - 0.03f){
                    destroy();
                    if(myShip.hp > 0) {
                        myShip.damage(damage);
                    }
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float billetHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0 = v0;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = billetHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        this.v.set(descentV);
        this.state = State.DESCENT;
    }

    @Override
    public boolean isBulletCollision(Rect bullet){
        return !(
                bullet.getRight() < getLeft() ||
                bullet.getLeft() > getRight() ||
                bullet.getBottom() > getTop() ||
                bullet.getTop() < pos.y
                );
    }
}
