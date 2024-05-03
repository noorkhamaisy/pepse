package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * The Terrain class represents the terrain in the game.
 */
public class Terrain {

    /**
     * The initial ground height at x=0 factor.
     */
    public static final float INITIAL_GROUND_HEIGHT_AT_X0_FACTOR = (float) 2 / 3;

    /*
     * The block tag.
     */
    private static final String BLOCK_TAG = "block";

    /*se
     * The base ground color.
     */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    /*
     * The ground height at x=0.
     */
    private final int groundHeightAtX0;

    /*
     * The NoiseGenerator instance to generate the ground height.
     */
    private final NoiseGenerator noiseGenerator;

    /*
     * The window dimensions.
     */
    private final Vector2 windowDimensions;

    /**
     * Constructor for the Terrain class.
     * @param windowDimensions The dimensions of the window.
     * @param seed The seed for the noise generator.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = (int) (windowDimensions.y() * INITIAL_GROUND_HEIGHT_AT_X0_FACTOR);
        this.noiseGenerator = new NoiseGenerator(seed, this.groundHeightAtX0);
    }

    /**
     * Returns the ground height at the given x coordinate.
     * @param x The x coordinate.
     * @return The ground height.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }


    /**
     * Creates a list of blocks in the given x range.
     * @param minX The minimum x coordinate.
     * @param maxX The maximum x coordinate.
     * @return The list of blocks.
     */
    public List<Block> createInRange(int minX,int maxX) {
        int startX = (minX / Block.SIZE) * Block.SIZE;
        int endX = (int) Math.ceil( (float) maxX / Block.SIZE) * Block.SIZE;
        List<Block> blocks =  new ArrayList<>();
        for (int i = startX; i < endX; i+=Block.SIZE) {
            int startY = ((int) Math.floor(groundHeightAt(i)) / Block.SIZE) * Block.SIZE;
            for (int j = startY; j < (int) windowDimensions.y(); j+=Block.SIZE) {
                blocks.add(createBlockAt(i, j));
            }
        }
        return blocks;
    }

    /*
     * Creates a block at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The block.
     */
    private Block createBlockAt(int x, int y) {
        Block block = new Block(
                new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)
                )
        );
        block.setTag(BLOCK_TAG);
        return block;
    }
}
