package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

/**
 * The Fruit class represents a fruit object in the game world.
 * It extends the GameObject class and includes methods for collision handling and visibility control.
 */
public class Fruit extends GameObject {
    /**
     * fruit tag.
     */
    public static final String FRUIT_TAG = "fruit";

    /*
     * day-night cycle length.
     */
    private final float dayNightCycleLength;

    /**
     * Constructs a new Fruit object with the specified parameters.
     *
     * @param topLeftCorner The position of the top-left corner of the fruit.
     * @param dimensions The dimensions of the fruit.
     * @param renderable The renderable component representing the visual appearance of the fruit.
     * @param dayNightCycleLength The length of the day-night cycle in the game world.
     */
    public Fruit(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            float dayNightCycleLength
    ) {
        super(topLeftCorner, dimensions, renderable);
        setTag(FRUIT_TAG);
        this.dayNightCycleLength = dayNightCycleLength;
    }

    /**
     * Determines whether the fruit should collide with the specified game object.
     *
     * @param other The game object to check collision with.
     * @return True if the fruit should collide with the specified object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Avatar.AVATAR_TAG) && renderer().getOpaqueness() != 0;
    }

    /**
     * Handles the action to be performed when a collision occurs with another game object.
     *
     * @param other The game object collided with.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (renderer().getOpaqueness() != 0) {
            renderer().setOpaqueness(0);
            new ScheduledTask(
                    this,
                    dayNightCycleLength,
                    false,
                    () -> renderer().setOpaqueness(1)
                    );
        }
    }
}
