package ru.geekbrains.pool;

import com.badlogic.gdx.audio.Sound;
import ru.geekbrains.base.SpritePool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.MyShip;

public class EnemyPool extends SpritePool<Enemy> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Sound bulletSound;
    private Rect worldBounds;
    private MyShip myShip;


    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound, Rect worldBounds, MyShip myShip) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.myShip = myShip;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, explosionPool, bulletSound, worldBounds, myShip);
    }

}
