package bat.mario.tob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

/**
 * Created by mariobat on 19/02/15.
 */
public class MapBodyManager {

    private World world;
    private float units;
    private Array<Body> bodies = new Array<Body>();

    public MapBodyManager(World world, int unitsPerPixel){

        this.world = world;
        this.units = unitsPerPixel;
    }

    public void createPhysics(Map map){
        createPhysics(map, "Collision");
    }

    public void createPhysics(Map map, String layerName){

        MapLayer layer = map.getLayers().get(layerName);

        if(layer == null){
            return;
        }

        MapObjects mapObjects = layer.getObjects();
        Iterator<MapObject> objectIt = mapObjects.iterator();

        while (objectIt.hasNext()){
            MapObject mapObject = objectIt.next();

            if(mapObject instanceof TextureMapObject){
                continue;
            }

            Shape shape = null;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if(mapObject instanceof RectangleMapObject){
                RectangleMapObject rectangle = (RectangleMapObject) mapObject;
                shape = getRectangle(rectangle);
            }

            Body body = world.createBody(bodyDef);
            body.createFixture(shape, 1);
            bodies.add(body);

            if(shape != null){
                shape.dispose();
            }

        }


    }

    public void destroyPhysics(){
        for(Body body: bodies){
            world.destroyBody(body);
        }

        bodies.clear();
    }

    private Shape getRectangle(RectangleMapObject rectangleObject){
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Gdx.app.log("House pos X", String.valueOf((rectangle.x + rectangle.width * 0.5f) / units));
        Gdx.app.log("House pos Y", String.valueOf((rectangle.y + rectangle.height * 0.5f ) / units));

        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / units,
                (rectangle.y + rectangle.height * 0.5f ) / units);
        polygon.setAsBox(rectangle.width * 0.5f / units,
                rectangle.height * 0.5f / units,
                size,
                0.0f);
        return polygon;
    }

}
