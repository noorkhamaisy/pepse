package pepse.world.daynight;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import danogl.components.Transition;

import java.awt.*;

/**
 * The night GameObject.
 */
public class Night extends GameObject{

    /*
     * The tag of the night object.
     */
    private static final String NIGHT_TAG = "night";

    /*
     * The opacity of the day.
     */
    private static final float DAY_OPACITY = 0f;

    /*
     * The opacity of the night.
     */
    private static final float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Night(Vector2 topLeftCorner, Vector2 dimensions, RectangleRenderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Create a new night object with a transition from day to night.
     * @param windowDimension The window dimensions.
     * @param cycleLength The cycle length.
     * @return The night object.
     */
    public static GameObject create(Vector2 windowDimension, float cycleLength) {
        GameObject night = new Night(
                new Vector2(0, 0),
                windowDimension,
                new RectangleRenderable(Color.BLACK)
        );
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);

        new Transition<Float>(
                night,
                night.renderer()::setOpaqueness,
                DAY_OPACITY,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );

        return night;
    }
}
