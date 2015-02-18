package bat.mario.tob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by mariobat on 18/02/15.
 */
public class PlayerInputProcessor implements InputProcessor {


    private Player player;


    public PlayerInputProcessor(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode){
            case Input.Keys.LEFT:
                player.setState(Player.State.LEFT);
                break;
            case Input.Keys.RIGHT:
                player.setState(Player.State.RIGHT);
                break;
            case Input.Keys.UP:
                player.setState(Player.State.TOP);
                break;
            case Input.Keys.DOWN:
                player.setState(Player.State.BOTTOM);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        boolean normalState = true;
        switch (keycode){
            case Input.Keys.LEFT:
                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    player.setState(Player.State.RIGHT);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                    player.setState(Player.State.TOP);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    player.setState(Player.State.BOTTOM);
                    normalState = false;
                }

                if(normalState) {
                    player.setLastState(player.getState());
                    player.setState(Player.State.NORMAL);
                }

                break;
            case Input.Keys.RIGHT:
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    player.setState(Player.State.LEFT);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                    player.setState(Player.State.TOP);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    player.setState(Player.State.BOTTOM);
                    normalState = false;
                }

                if(normalState) {
                    player.setLastState(player.getState());
                    player.setState(Player.State.NORMAL);
                }
                break;
            case Input.Keys.UP:
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    player.setState(Player.State.LEFT);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    player.setState(Player.State.RIGHT);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    player.setState(Player.State.BOTTOM);
                    normalState = false;
                }

                if(normalState) {
                    player.setLastState(player.getState());
                    player.setState(Player.State.NORMAL);
                }
                break;
            case Input.Keys.DOWN:
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    player.setState(Player.State.LEFT);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                    player.setState(Player.State.TOP);
                    normalState = false;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    player.setState(Player.State.RIGHT);
                    normalState = false;
                }

                if(normalState) {
                    player.setLastState(player.getState());
                    player.setState(Player.State.NORMAL);
                }
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
