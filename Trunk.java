package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Trunk class represents a trunk object in the game world.
 * Trunk objects are part of tree structures and provide the foundational support for branches,
 * leaves, and fruits.
 */
public class Trunk extends GameObject {

    /*
     * trunk tag.
     */
    private static final String TRUNK_TAG = "trunk";

    /**
     * Constructs a Trunk object with the specified parameters.
     *
     * @param topLeftCorner The top-left corner of the trunk.
     * @param dimensions    The dimensions of the trunk.
     * @param renderable    The renderable component defining the visual representation of the trunk.
     */
    public Trunk(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TRUNK_TAG);
    }
}
