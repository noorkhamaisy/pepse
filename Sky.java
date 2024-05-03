package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.Color;

/**
 * The sky GameObject.
 */
public class Sky extends GameObject{

    /*
     * The basic color of the sky.
     */
    private static final String SKY_IMAGE_PATH = "assets/sky.png";

    /*
     * The tag of the sky object.
     */
    private static final String SKY_TAG = "sky";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Sky(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Create a new sky GameObject.
     * @param windowDimensions The dimensions of the window.
     * @return The sky GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, ImageReader imageReader) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                imageReader.readImage(SKY_IMAGE_PATH, false)
        );
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
