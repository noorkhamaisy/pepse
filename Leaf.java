package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

/**
 * The Leaf class represents a leaf object in the game world.
 * It handles the behavior and properties of individual leaf objects.
 */
public class Leaf extends GameObject {

    /*
     * the leaf tag.
     */
    private static final String LEAF_TAG = "leaf";

    /*
     * The final angle of the sun rotation.
     */
    private static final float MAX_ANGLE_CHANGE = 50f;

    /*
     * The leaf initial dimensions.
     */
    private static final float LEAF_INITIAL_DIMENSION_FACTOR = 0.6f;

    /*
     * The leaf initial dimensions.
     */
    private static final float LEAF_FINAL_DIMENSION_FACTOR = 1.4f;

    /*
     * The leaf initial dimensions.
     */
    private static final float LEAF_MIN_CYCLE_FACTOR = 0.4f;

    /*
     * The leaf initial dimensions.
     */
    private static final float LEAF_MAX_CYCLE_FACTOR = 1.6f;

    /*
     * The max delay time.
     */
    private static final float MAX_DELAY_TIME = 0.7f;

    /*
     * random number.
     */
    private static final Random random = new Random();

    /**
     * Constructs a Leaf object with the specified top-left corner, dimensions, and renderable.
     *
     * @param topLeftCorner The top-left corner of the leaf object.
     * @param dimensions    The dimensions of the leaf object.
     * @param renderable    The renderable representing the visual aspect of the leaf.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        setTag(LEAF_TAG);
    }

    /**
     * Creates a new Leaf object with the specified parameters and schedules its behavior.
     *
     * @param topLeftCorner   The top-left corner of the leaf object.
     * @param dimensions      The dimensions of the leaf object.
     * @param renderable      The renderable representing the visual aspect of the leaf.
     * @param cycleLength     The length of the cycle for the leaf's behavior.
     * @return                The newly created Leaf object.
     */
    public static Leaf create(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            float cycleLength
    ){
        Leaf leaf = new Leaf(topLeftCorner, dimensions, renderable);
        createScheduleTask(leaf, cycleLength);
        return leaf;
    }

    /**
     * Determines whether the leaf should collide with the specified game object.
     *
     * @param other The game object to check collision with.
     * @return false
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    /*
     * Creates a scheduled task for the leaf object, controlling its behavior over time.
     *
     * @param leaf          The leaf object for which the task is created.
     * @param cycleLength   The length of the cycle for the leaf's behavior.
     */
    private static void createScheduleTask(Leaf leaf, float cycleLength){
        float initialAngle = random.nextFloat() * MAX_ANGLE_CHANGE - MAX_ANGLE_CHANGE / 2;
        float initialDimensionFactor = random.nextFloat() *
                (LEAF_FINAL_DIMENSION_FACTOR - LEAF_INITIAL_DIMENSION_FACTOR) + LEAF_INITIAL_DIMENSION_FACTOR;
        float randomCycleFactor = random.nextFloat() *
                (LEAF_MAX_CYCLE_FACTOR - LEAF_MIN_CYCLE_FACTOR) + LEAF_MIN_CYCLE_FACTOR;
        new ScheduledTask (
                leaf,
                random.nextFloat() * MAX_DELAY_TIME,
                false,
                () -> {
                    new Transition<Float>(
                            leaf,
                            leaf.renderer()::setRenderableAngle,
                            initialAngle,
                            - initialAngle,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            cycleLength * randomCycleFactor,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null
                    );
                    new Transition<Float>(
                            leaf,
                            (xDimension) -> leaf.setDimensions(
                                    new Vector2(xDimension, leaf.getDimensions().y())
                            ),
                            initialDimensionFactor * Block.SIZE,
                            (1 + (1 - initialDimensionFactor)) * Block.SIZE,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            cycleLength * randomCycleFactor,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null
                    );
                }
        );
    }
}
