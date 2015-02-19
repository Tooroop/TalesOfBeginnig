package bat.mario.tob;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Tooroop on 16.2.2015..
 */
public class Player extends Sprite{

    public static float MAX_VELOCITY = 2f;

    private Body body;
    private int units;

    public enum State{
        LEFT, RIGHT, BOTTOM, TOP, NORMAL
    }

    public Player(World world, int unitsPerPixel) {
        this.units = unitsPerPixel;

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
