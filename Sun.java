package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * The sun GameObject.
 */
public class Sun extends GameObject{

    /*
     * The tag of the sun object.
     */
    private static final String SUN_TAG = "sun";

    /*
     * The radius of the sun.
     */
    private static final int SUN_RADIUS = 120;

    /*
     * The initial angle of the sun rotation.
     */
    private static final float INITIAL_ANGLE = 0f;

    /*
     * The final angle of the sun rotation.
     */
    private static final float FINAL_ANGLE = 360f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Create a new sun GameObject.
     * @param windowDimension The dimensions of the window.
     * @param cycleLength The length of the day-night cycle.
     * @return The sun GameObject.
     */
    public static GameObject create(Vector2 windowDimension, float cycleLength){
        Vector2 initialSunCenter = new Vector2(
                windowDimension.x()/2 - SUN_RADIUS,
                windowDimension.y()/3 - SUN_RADIUS
        );

        GameObject sun = new Sun(
                initialSunCenter,
                new Vector2(SUN_RADIUS*2,SUN_RADIUS*2),
                new OvalRenderable(Color.YELLOW)
        );
        sun.setTag(SUN_TAG);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Vector2 cycleCenter = new Vector2(
                windowDimension.x()/2,
                windowDimension.y() * Terrain.INITIAL_GROUND_HEIGHT_AT_X0_FACTOR
        );

        new Transition<Float>(
                sun,
                (Float angle) -> sun.setCenter (initialSunCenter.subtract(cycleCenter)
                        .rotated(angle)
                        .add(cycleCenter)),
                INITIAL_ANGLE,
                FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return sun;
    }
}
