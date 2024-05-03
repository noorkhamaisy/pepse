package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Block class represents a block in the game.
 */
public class Block extends GameObject {

    /**
     * The size of the block.
     */
    public static final int SIZE = 30;

    /**
     * Constructor for the Block class.
     * @param topLeftCorner The top left corner of the block.
     * @param renderable The Renderable instance to render the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable){
        super(topLeftCorner,Vector2.ONES.mult(SIZE),renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
