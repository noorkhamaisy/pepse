package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Observer;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Tree class represents a tree object in the game world.
 * It handles the behavior and properties of individual tree objects.
 */
public class Tree implements Observer {

    /**
     * leafs foliage size.
     */
    public static final int LEAFS_FOLIAGE_SIZE = 7;

    /*
     * tree trunk color.
     */
    private static final Color TREE_TRUNK_COLOR = new Color(100, 50, 20);

    /*
     * tree leaf color.
     */
    private static final Color TREE_LEAF_COLOR = new Color(50, 200, 30);

    /*
     * tree fruits colors.
     */
    private static final Color[] FRUIT_COLORS = new Color[] {Color.red,Color.yellow,Color.orange,Color.green};

    /*
     * creating leaf probability.
     */
    private static final float CREATE_LEAF_PROBABILITY = 0.55f;

    /*
     * creating fruit probability.
     */
    private static final float CREATE_FRUIT_PROBABILITY = 0.12f;

    /*
     * leaf cycle length.
     */
    private static final float LEAFS_CYCLE_LENGTH = 10;

    /*
     * leafs angle change when avatar jumps.
     */
    private static final float LEAFS_ANGLE_CHANGE_WHEN_AVATAR_JUMPS = 90f;

    /*
     * leaf angle change cycle length when avatar jumps.
     */
    private static final float LEAF_ANGLE_CHANGE_CYCLE_LENGTH_WHEN_AVATAR_JUMPS = 1f;

    /*
     * trunk field.
     */
    private final Trunk trunk;

    /*
     * leafs list.
     */
    private final ArrayList<Leaf> leafs;

    /*
     * fruits list.
     */
    private final ArrayList<Fruit> fruits;

    /*
     * random.
     */
    private final Random random;

    /*
     * current fruit color index.
     */
    private int fruitColorIndex;

    /**
     * Constructs a Tree object with the specified top-left corner, dimensions, and day-night cycle length.
     *
     * @param topLeftCorner        The top-left corner of the tree object.
     * @param dimensions           The dimensions of the tree object.
     * @param dayNightCycleLength  The length of the day-night cycle in the game world.
     */
    public Tree(Vector2 topLeftCorner, Vector2 dimensions, float dayNightCycleLength) {
        this.trunk = new Trunk(
                topLeftCorner,
                dimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(TREE_TRUNK_COLOR))
        );
        this.leafs = new ArrayList<>();
        this.fruits = new ArrayList<>();
        this.random = new Random();
        this.fruitColorIndex = 0;
        createLeafsAndFruits(topLeftCorner, dayNightCycleLength);
    }

    /**
     * Retrieves the trunk of the tree.
     *
     * @return The trunk of the tree.
     */
    public GameObject getTrunk() {
        return trunk;
    }

    /**
     * Retrieves the list of leafs belonging to the tree.
     *
     * @return The list of leafs.
     */
    public ArrayList<Leaf> getLeafs() {
        return leafs;
    }

    /**
     * Retrieves the list of fruits belonging to the tree.
     *
     * @return The list of fruits.
     */
    public ArrayList<Fruit> getFruits(){return fruits;}

    /**
     * Updates the state of the tree object when an observer event occurs.
     * This method is called periodically to update the behavior of the tree components,
     * such as leafs and fruits.
     * It iterates through the list of leafs and adjusts their angles to simulate movement
     * or response to external events,
     * such as the avatar jumping.
     * Additionally, it updates the appearance of fruits by
     * changing their colors based on a predefined color array.
     * Finally, it updates the appearance of the tree trunk to ensure consistency with the rest of the tree.
     */
    @Override
    public void update() {
        for (Leaf leaf : leafs) {
            new Transition<Float>(
                    leaf,
                    leaf.renderer()::setRenderableAngle,
                    leaf.renderer().getRenderableAngle(),
                    leaf.renderer().getRenderableAngle() + LEAFS_ANGLE_CHANGE_WHEN_AVATAR_JUMPS,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    LEAF_ANGLE_CHANGE_CYCLE_LENGTH_WHEN_AVATAR_JUMPS,
                    Transition.TransitionType.TRANSITION_ONCE,
                    null
            );
        }
        for (Fruit fruit : fruits) {
            fruit.renderer().setRenderable(
                    new OvalRenderable(FRUIT_COLORS[(fruitColorIndex+1) % FRUIT_COLORS.length])
            );
        }
        trunk.renderer().setRenderable(
                new RectangleRenderable(ColorSupplier.approximateColor(TREE_TRUNK_COLOR))
        );
        fruitColorIndex = (fruitColorIndex + 1) % FRUIT_COLORS.length;
    }

    /*
     * Creates leafs and fruits around the tree's top-left corner.
     *
     * @param topLeftCorner        The top-left corner of the tree.
     * @param dayNightCycleLength  The length of the day-night cycle in the game world.
     */
    private void createLeafsAndFruits(Vector2 topLeftCorner, float dayNightCycleLength) {
        {
            for (int i = -LEAFS_FOLIAGE_SIZE / 2; i < LEAFS_FOLIAGE_SIZE / 2; i++) {
                for (int j = -LEAFS_FOLIAGE_SIZE / 2; j < LEAFS_FOLIAGE_SIZE / 2; j++) {
                    createLeafAndFruit(topLeftCorner, dayNightCycleLength, i, j);
                }
            }
        }
    }

    /*
     * Creates a leaf and fruit at a specified position around the tree.
     *
     * @param topLeftCorner        The top-left corner of the tree.
     * @param dayNightCycleLength  The length of the day-night cycle in the game world.
     * @param i                    The x-coordinate offset for the leaf/fruit position.
     * @param j                    The y-coordinate offset for the leaf/fruit position.
     */
    private void createLeafAndFruit(Vector2 topLeftCorner, float dayNightCycleLength, int i, int j) {
        float createLeaf = random.nextFloat();
        float createFruit = random.nextFloat();
        Vector2 position = new Vector2(
                topLeftCorner.x() + i * Block.SIZE,
                topLeftCorner.y() + j * Block.SIZE
        );
        Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
        if (createLeaf <= CREATE_LEAF_PROBABILITY) {
            leafs.add(
                    Leaf.create(
                            position,
                            dimensions,
                            new RectangleRenderable(ColorSupplier.approximateColor(TREE_LEAF_COLOR)),
                            LEAFS_CYCLE_LENGTH
                    )
            );
        }
        if (createFruit <= CREATE_FRUIT_PROBABILITY && i != 0) {
            fruits.add(
                    new Fruit(
                            position,
                            dimensions,
                            new OvalRenderable(FRUIT_COLORS[fruitColorIndex]),
                            dayNightCycleLength
                    )
            );
        }
    }
}
