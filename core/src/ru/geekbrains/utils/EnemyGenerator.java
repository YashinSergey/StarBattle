package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Enemy;

public class EnemyGenerator {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1.5f;
    private static final int ENEMY_SMALL_HP = 2;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.2f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.03f;
    private static final float ENEMY_MEDIUM_BULLET_VY = -0.3f;
    private static final int ENEMY_MEDIUM_BULLET_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 2.5f;
    private static final int ENEMY_MEDIUM_HP = 10;

    private static final float ENEMY_BIG_HEIGHT = 0.25f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.07f;
    private static final float ENEMY_BIG_BULLET_VY = -0.3f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 3.5f;
    private static final int ENEMY_BIG_HP = 20;

    private Rect worldBounds;
    private float generateInterval = 4f;
    private float generateTimer;

    private final TextureRegion[] enemySmallRegion;
    private final TextureRegion[] enemyMediumRegion;
    private final TextureRegion[] enemyBigRegion;

    private final Vector2 enemySmallV = new Vector2(0f,-0.12f);
    private final Vector2 enemyMediumV = new Vector2(0f,-0.08f);
    private final Vector2 enemyBigV = new Vector2(0f,-0.04f);

    private TextureRegion bulletRegion;
    private EnemyPool enemyPool;

    private int level;

    public EnemyGenerator(Rect worldBounds, EnemyPool enemyPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        TextureRegion enemy0 = atlas.findRegion("enemy0");
        this.enemySmallRegion = Regions.split(enemy0,1,2,2);
        TextureRegion enemy1 = atlas.findRegion("enemy1");
        this.enemyMediumRegion = Regions.split(enemy1,1,2,2);
        TextureRegion enemy2 = atlas.findRegion("enemy2");
        this.enemyBigRegion = Regions.split(enemy2,1,2,2);
        this.bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta, int frags){
        level = frags / 10 + 1;
        generateTimer += delta;
        if(generateTimer >= generateInterval){
            generateTimer = 0f;
            Enemy enemy = enemyPool.obtain();
            float type = (float)Math.random();
            if(type < 0.65f) {
                enemy.set(
                        enemySmallRegion,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_BULLET_DAMAGE * level,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP
                );
            } else if(type < 0.9f){
                enemy.set(
                        enemyMediumRegion,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_BULLET_DAMAGE * level,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP
                );
            } else {
                    enemy.set(
                        enemyBigRegion,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                            ENEMY_BIG_BULLET_DAMAGE * level,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP
                    );
            }
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(),
                    worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }
}
