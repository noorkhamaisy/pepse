package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * SunHalo class represents the sun halo game object in the game.
 * It extends the GameObject class and has a Renderable instance to render the sun halo.
 */
public class SunHalo extends GameObject {

    /*
     * The radius of the sun halo.
     */
    private static final int HALO_RADIUS = 200;

    /*
     * The color of the sun halo.
     */
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    /*
     * The tag of the sun halo.
     */
    private static final String SUN_HALO_TAG = "sunHalo";

    /**
     * Constructor for the SunHalo class.
     * @param topLeftCorner The top left corner of the sun halo.
     * @param dimensions The dimensions of the sun halo.
     * @param renderable The Renderable instance to render the sun halo.
     */
    public SunHalo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * This method creates a sun halo game object and returns it.
     * @param sun The sun game object.
     * @return The sun halo game object.
     */
    public static GameObject create(GameObject sun){
        GameObject sunHalo = new SunHalo(
                new Vector2(sun.getCenter().x()-HALO_RADIUS, sun.getCenter().y()-HALO_RADIUS),
                new Vector2(HALO_RADIUS*2,HALO_RADIUS*2),
                new OvalRenderable(HALO_COLOR)
        );
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent((float deltaTime) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
