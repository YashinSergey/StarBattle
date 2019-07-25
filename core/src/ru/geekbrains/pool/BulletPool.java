package ru.geekbrains.pool;

import ru.geekbrains.base.SpritePool;
import ru.geekbrains.sprite.Bullet;

public class BulletPool extends SpritePool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
