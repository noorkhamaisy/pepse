package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The main class of the game, responsible for initializing and running the game.
 */
public class
PepseGameManager extends GameManager {

    /*
     * The length of the day-night cycle.
     */
    private static final int DAY_NIGHT_CYCLE_LENGTH = 30;


    /*
     * The seed for the random number generator.
     */
    private final Random random;
    /**
     * Constructs a new PepseGameManager object.
     */
    public PepseGameManager() {
        super();
        this.random = new Random();
    }

    /**
     * The main entry point for the game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game, setting up all the necessary components.
     *
     * @param imageReader      The image reader for loading images.
     * @param soundReader      The sound reader for loading sounds.
     * @param inputListener    The user input listener for handling input.
     * @param windowController The window controller for managing the game window.
     */
    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController
    ) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // create sky
        GameObject sky = Sky.create(windowController.getWindowDimensions(), imageReader);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // create terrain
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), random.nextInt());
        List<Block> blocks = terrain.createInRange(
                0,
                (int) windowController.getWindowDimensions().x()
        );
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        // create night
        GameObject night = Night.create(windowController.getWindowDimensions(), DAY_NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        // create sun
        GameObject sun = Sun.create(windowController.getWindowDimensions(), DAY_NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        // create sun halo
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        // create avatar
        Avatar avatar = new Avatar(
                new Vector2(
                        Block.SIZE,
                        windowController.getWindowDimensions().y() *
                                Terrain.INITIAL_GROUND_HEIGHT_AT_X0_FACTOR
                ),
                inputListener,
                imageReader
        );
        gameObjects().addGameObject(avatar);
        gameObjects().addGameObject(avatar.getEnergyDisplayer(), Layer.UI);

        // create trees with leaves and fruits.
        Flora flora = new Flora(terrain, DAY_NIGHT_CYCLE_LENGTH);
        List<Tree> trees = flora.createInRange(
                Avatar.AVATAR_SIZE + Block.SIZE,
                (int) windowController.getWindowDimensions().x()
        );
        for (Tree tree : trees) {
            gameObjects().addGameObject(tree.getTrunk(), Layer.STATIC_OBJECTS);
            for (GameObject leaf : tree.getLeafs()) {
                gameObjects().addGameObject(leaf, Layer.STATIC_OBJECTS);
            }
            for (GameObject fruit : tree.getFruits()) {
                gameObjects().addGameObject(fruit);
            }
            avatar.registerObserver(tree);
        }
    }
}

//class Main {
//
//    public static void main(String[] args) {
//        int loc0 = 64;
//        int loc1 = 0;
//        int loc2 = 0;
//        while (loc0>0){
//            loc2=loc0+loc1;
//            if(loc2*loc2 > 81){
//                loc0 = loc0*2;
//            }else {
//                loc1 = loc2;
//            }
//        }
//        System.out.println( loc1);
//    }
//
//}