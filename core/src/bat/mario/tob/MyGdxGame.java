package bat.mario.tob;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends ApplicationAdapter {

	private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private Texture playerTexture;
    private Animation bottomWalk;
    private Player player;
	
	@Override
	public void create () {
		tiledMap = new TmxMapLoader().load("cresta.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();

        playerTexture = new Texture("spritesheet_leon.png");
        TextureRegion[][] regions = TextureRegion.split(playerTexture, 17, 30);
        bottomWalk = new Animation(0, regions[3][0], regions[3][1], regions[3][2], regions[0][3]);
        bottomWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        player = new Player(new Sprite(regions[2][0]));
        player.setState(Player.State.BOTTOM);
        player.setPosition(0, 0);
	}

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.position.set(width/2, height/2, 0);
        camera.update();
    }

    @Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // get the delta time
        float deltaTime = Gdx.graphics.getDeltaTime();

        mapRenderer.setView(camera);
        mapRenderer.render();

        updatePlayer(deltaTime);
        renderPlayer();

	}

    @Override
    public void dispose() {
        this.tiledMap.dispose();
        this.mapRenderer.dispose();
        this.player.getTexture().dispose();
        super.dispose();
    }

    private void updatePlayer(float deltaTime){

        player.stateTime += deltaTime;

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            player.setPosition(player.getX() - Player.MAX_VELOCITY, player.getY());
            player.setState(Player.State.LEFT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            player.setPosition(player.getX() + Player.MAX_VELOCITY, player.getY());
            player.setState(Player.State.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            player.setPosition(player.getX(), player.getY() + Player.MAX_VELOCITY);
            player.setState(Player.State.TOP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            player.setPosition(player.getX(), player.getY() - Player.MAX_VELOCITY);
            player.setState(Player.State.BOTTOM);
        }

    }

    private void renderPlayer(){

        TextureRegion frame = null;

        switch (player.getState()) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case BOTTOM:
                frame = bottomWalk.getKeyFrame(0);
                break;
            case TOP:
                break;
        }

        if(frame != null)
        player.setTexture(frame.getTexture());

        mapRenderer.getBatch().begin();
        player.draw(mapRenderer.getBatch());
        mapRenderer.getBatch().end();
    }
}
