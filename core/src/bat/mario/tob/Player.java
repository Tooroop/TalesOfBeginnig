package bat.mario.tob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Tooroop on 16.2.2015..
 */
public class Player extends Sprite {

    public static float MAX_VELOCITY = 1.6f;
    public static float WIDTH;
    public static float HEIGTH;

    public Vector2 velocity = new Vector2();

    public enum State{
        LEFT, RIGHT, BOTTOM, TOP
    }

    private State state;
    public float stateTime;

    public Player(Sprite sprite) {
        super(sprite);
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    private void update(float delta){
        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
