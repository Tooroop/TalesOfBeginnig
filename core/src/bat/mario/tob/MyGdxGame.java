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
    private Animation sideWalk;
    private Animation topWalk;
    private Player player;
	
	@Override
	public void create () {
		tiledMap = new TmxMapLoader().load("cresta.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();

        playerTexture = new Texture("spritesheet_leon_walk.png");
        TextureRegion[][] regions = TextureRegion.split(playerTexture, 16, 29);
        bottomWalk = new Animation(0.05f, regions[0][0], regions[0][1], regions[0][2], regions[0][3], regions[0][4]);
        topWalk = new Animation(0.05f, regions[0][5], regions[0][6], regions[0][7], regions[0][8], regions[0][9]);
        sideWalk = new Animation(0.05f, regions[0][10], regions[0][11], regions[0][12], regions[0][13], regions[0][14]);

        bottomWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        topWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        sideWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Player.WIDTH = 1 / 16f * regions[0][0].getRegionWidth();
        Player.HEIGHT = 1 / 16f * regions[0][0].getRegionHeight();

        player = new Player();
        player.setState(Player.State.NORMAL);

        Gdx.input.setInputProcessor(new PlayerInputProcessor(player));
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
        super.dispose();
    }

    private void updatePlayer(float deltaTime){
        if (deltaTime == 0) return;
        player.stateTime += deltaTime;

        switch (player.getState()){
            case LEFT:
                player.position.x -= Player.MAX_VELOCITY;
                break;
            case RIGHT:
                player.position.x += Player.MAX_VELOCITY;
                break;
            case BOTTOM:
                player.position.y -= Player.MAX_VELOCITY;
                break;
            case TOP:
                player.position.y += Player.MAX_VELOCITY;
                break;
            case NORMAL:
                break;
        }
    }

    private void renderPlayer() {

        TextureRegion frame = null;

        switch (player.getState()) {
            case LEFT:
                if(sideWalk.getKeyFrame(player.stateTime).isFlipX()){
                    sideWalk.getKeyFrame(player.stateTime).flip(true, false);
                }
                frame = sideWalk.getKeyFrame(player.stateTime);
                break;
            case RIGHT:
                if(!sideWalk.getKeyFrame(player.stateTime).isFlipX()){
                    sideWalk.getKeyFrame(player.stateTime).flip(true, false);
                }
                frame = sideWalk.getKeyFrame(player.stateTime);
                break;
            case BOTTOM:
                frame = bottomWalk.getKeyFrame(player.stateTime);
                break;
            case TOP:
                frame = topWalk.getKeyFrame(player.stateTime);
                break;
            case NORMAL:
                if(player.getLastState() == Player.State.TOP){
                    frame = topWalk.getKeyFrames()[2];
                } else if(player.getLastState() == Player.State.BOTTOM){
                    frame = bottomWalk.getKeyFrames()[2];
                } else if(player.getLastState() == Player.State.LEFT || player.getLastState() == Player.State.RIGHT){
                    frame = sideWalk.getKeyFrames()[2];
                } else{
                    frame = bottomWalk.getKeyFrames()[2];
                }

                break;
        }

        if (frame != null) {
            mapRenderer.getBatch().begin();
            mapRenderer.getBatch().draw(frame, player.position.x, player.position.y);
            mapRenderer.getBatch().end();
        }
    }
}
