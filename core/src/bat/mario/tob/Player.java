package bat.mario.tob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Tooroop on 16.2.2015..
 */
public class Player{

    public static float MAX_VELOCITY = 2f;
    public static float WIDTH;
    public static float HEIGHT;

    public Vector2 position = new Vector2();

    public enum State{
        LEFT, RIGHT, BOTTOM, TOP, NORMAL
    }

    private State lastState;
    private State state;
    public float stateTime;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getLastState() {
        return lastState;
    }

    public void setLastState(State lastState) {
        this.lastState = lastState;
    }
}
