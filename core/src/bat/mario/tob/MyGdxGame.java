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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends ApplicationAdapter {

    private static final int UNITS_PER_PIXEL = 16;
    private static final float WORLD_TO_BOX = 0.01f;
    private static final float BOX_TO_WORLD = 100f;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private MapBodyManager mapBodyManager;

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
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

		tiledMap = new TmxMapLoader().load("cresta.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        camera = new OrthographicCamera();

        mapBodyManager = new MapBodyManager(world, UNITS_PER_PIXEL);
        mapBodyManager.createPhysics(tiledMap);


        playerTexture = new Texture("spritesheet_leon_walk.png");
        TextureRegion[][] regions = TextureRegion.split(playerTexture, 16, 29);
        bottomWalk = new Animation(0.05f, regions[0][0], regions[0][1], regions[0][2], regions[0][3], regions[0][4]);
        topWalk = new Animation(0.05f, regions[0][5], regions[0][6], regions[0][7], regions[0][8], regions[0][9]);
        sideWalk = new Animation(0.05f, regions[0][10], regions[0][11], regions[0][12], regions[0][13], regions[0][14]);

        bottomWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        topWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        sideWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        player = new Player(world, UNITS_PER_PIXEL);
        player.setPosition(0, 0);
        player.setSize(16, 29);
        player.setRegion(regions[0][0]);
        player.setState(Player.State.NORMAL);


        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        final PolygonShape shape = new PolygonShape();

        Vector2 size = new Vector2(((player.getX() + player.getWidth()) * 0.5f) / UNITS_PER_PIXEL, ((player.getY() + player.getHeight()) * 0.5f) / UNITS_PER_PIXEL);

        final float halfWidth = (player.getWidth() * 0.5f) / UNITS_PER_PIXEL;
        final float halfHeight = (player.getHeight() * 0.5f) / UNITS_PER_PIXEL;
        shape.setAsBox(halfWidth, halfHeight, size, 0.0f);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, 1);

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

//        Matrix4 cameraCopy = camera.combined.cpy();
        debugRenderer.render(world, camera.combined);

        world.step(1/60f, 6, 2);

	}

    @Override
    public void dispose() {
        this.tiledMap.dispose();
        this.mapRenderer.dispose();
        this.mapBodyManager.destroyPhysics();
        super.dispose();
    }

    private void updatePlayer(float deltaTime){
        if (deltaTime == 0) return;
        player.stateTime += deltaTime;

        switch (player.getState()){
            case LEFT:
                player.setPosition(player.getX()- Player.MAX_VELOCITY, player.getY());
                break;
            case RIGHT:
                player.setPosition(player.getX() + Player.MAX_VELOCITY, player.getY());
                break;
            case BOTTOM:
                player.setPosition(player.getX(), player.getY() - Player.MAX_VELOCITY);
                break;
            case TOP:
                player.setPosition(player.getX(), player.getY() + Player.MAX_VELOCITY);
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
            player.setRegion(frame);
            player.draw(mapRenderer.getBatch());
            mapRenderer.getBatch().end();
        }
    }
}
