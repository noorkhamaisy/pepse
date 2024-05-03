package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Flora class is responsible for generating and managing the vegetation in the game world.
 * It includes methods for creating trees within a specified range and controlling their properties.
 */
public class Flora {

    /*
     * plant probability.
     */
    private static final float PLANT_PROBABILITY = 0.1f;

    /*
     * tree maximum
     */
    private static final int TREE_MAX_HEIGHT_IN_BLOCKS = 10;

    /*
     * random
     */
    private final Random random;

    /*
     * terrain field.
     */
    private final Terrain terrain;

    /*
     * day night cycle length.
     */
    private final float dayNightCycleLength;

    /**
     * Constructs a new Flora object with the specified terrain and day-night cycle length.
     *
     * @param terrain The terrain object representing the game world's landscape.
     * @param dayNightCycleLength The length of the day-night cycle in the game world.
     */
    public Flora(Terrain terrain, float dayNightCycleLength) {
        this.random = new Random();
        this.terrain = terrain;
        this.dayNightCycleLength = dayNightCycleLength;
    }

    /**
     * Creates trees within the specified range of x-coordinates on the terrain.
     *
     * @param minX The minimum x-coordinate for tree generation.
     * @param maxX The maximum x-coordinate for tree generation.
     * @return A list of Tree objects generated within the specified range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        int startX = (minX / Block.SIZE) * Block.SIZE;
        int endX = (int) Math.ceil((float) maxX / Block.SIZE) * Block.SIZE;
        List<Tree> trees = new ArrayList<>();
        for (int i = startX; i < endX; i += Block.SIZE) {
            if (random.nextFloat() < PLANT_PROBABILITY) {
                trees.add(createTree(i));
            }
        }
        return trees;
    }

    /*
     * Creates a tree at the specified x-coordinate based on the terrain's ground height.
     *
     * @param x The x-coordinate at which the tree will be created.
     * @return A Tree object representing the generated tree.
     */
    private Tree createTree(int x){
        int groundHeight = ((int) Math.floor(terrain.groundHeightAt(x)) / Block.SIZE) * Block.SIZE;
        int treeMaxHeightInBlocks = Math.min(groundHeight/Block.SIZE, TREE_MAX_HEIGHT_IN_BLOCKS);
        int treeMinHeightInBlocks = Tree.LEAFS_FOLIAGE_SIZE - 1;
        int treeHeight = (
                random.nextInt(
                        (treeMaxHeightInBlocks - treeMinHeightInBlocks) + 1
                ) + treeMinHeightInBlocks) * Block.SIZE;
        return new Tree(
                new Vector2(x, groundHeight - treeHeight),
                new Vector2(Block.SIZE, treeHeight),
                dayNightCycleLength
        );
    }


}
