package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Observer;
import pepse.world.trees.Fruit;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The Avatar class represents the player character in the game world.
 * It controls the movement, interaction, and energy management of the avatar.
 */
public class Avatar extends GameObject {

    /**
     * Avatar size.
     */
    public static final int AVATAR_SIZE = 50;

    /**
     * avatar tag
     */
    public static final String AVATAR_TAG = "avatar";

    /*
     * avatar image path
     */
    private static final String AVATAR_IMAGE_PATH = "assets/idle_0.png";

    /*
     * x velocity.
     */
    private static final float VELOCITY_X = 400;

    /*
     *y velocity.
     */
    private static final float VELOCITY_Y = -650;

    /*
     * gravity.
     */
    private static final float GRAVITY = 600;

    /*
     * maximum energy value.
     */
    private static final int MAX_ENERGY = 100;

    /*
     * energy to gain when eating fruit.
     */
    private static final int ENERGY_TO_GAIN_WHEN_EATING_FRUIT = 10;

    /*
     * energy to store when idle.
     */
    private static final float ENERGY_TO_RESTORE_WHEN_IDLE = 1;

    /*
     * energy to lose when moving.
     */
    private static final float ENERGY_TO_LOSE_WHEN_MOVING = (float) 1/2;

    /*
     * energy to lose when jumping.
     */
    private static final float ENERGY_TO_LOSE_WHEN_JUMPING = 10;

    /*
     * energy position padding.
     */
    private static final int ENERGY_POS_PADDING = 10;

    /*
     * energy display size.
     */
    private static final int ENERGY_DISPLAY_SIZE = 30;

    /*
     * time between clips.
     */
    private static final double TIME_BETWEEN_CLIPS = 0.25;

    /*
     * idle animation.
     */
    private static final String[] IDLE_ANIMATION = new String[]{
            "assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"
    };

    /*
     * run animation.
     */
    private static final String[] RUN_ANIMATION = new String[]{
            "assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png",
            "assets/run_5.png"
    };

    /*
     * jump animation.
     */
    private static final String[] JUMP_ANIMATION = new String[]{
            "assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png",
    };

    /*
     * user input listener.
     */
    private final UserInputListener inputListener;

    /*
     * current energy.
     */
    private float curEnergy;

    /*
     * energy disPlayer.
     */
    private final EnergyDisplayer energyDisplayer;

    /*
     * observers
     */
    private final ArrayList<Observer> observers = new ArrayList<>();

    /*
     * animation renderable :
     * idleRenderable
     * runRenderable
     * jumpRenderable
     */
    private final AnimationRenderable idleRenderable, runRenderable, jumpRenderable;

    /**
     * Constructs an Avatar object with the specified parameters.
     *
     * @param pos         The initial position of the avatar.
     * @param inputListener The user input listener for controlling the avatar.
     * @param imageReader   The image reader for loading avatar images.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(
                new Vector2(pos.x() - AVATAR_SIZE, pos.y() - AVATAR_SIZE),
                Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(AVATAR_IMAGE_PATH, true)
        );
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.curEnergy = MAX_ENERGY;
        this.energyDisplayer = new EnergyDisplayer(new TextRenderable(String.valueOf(curEnergy)));
        this.idleRenderable = new AnimationRenderable(
                IDLE_ANIMATION,
                imageReader,
                true,
                TIME_BETWEEN_CLIPS
        );
        this.runRenderable = new AnimationRenderable(
                RUN_ANIMATION,
                imageReader,
                true,
                TIME_BETWEEN_CLIPS
        );
        this.jumpRenderable = new AnimationRenderable(
                JUMP_ANIMATION,
                imageReader,
                true,
                TIME_BETWEEN_CLIPS
        );
        setTag(AVATAR_TAG);
    }

    /**
     * Updates the avatar's state and behavior.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        if (xVel != 0 && curEnergy >= ENERGY_TO_LOSE_WHEN_MOVING) {
                curEnergy -= ENERGY_TO_LOSE_WHEN_MOVING;
        } else if (xVel != 0){
                xVel = 0;
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            if (curEnergy >= ENERGY_TO_LOSE_WHEN_JUMPING) {
                curEnergy -= ENERGY_TO_LOSE_WHEN_JUMPING;
                notifyObservers();
                transform().setVelocityY(VELOCITY_Y);
            } else {
                transform().setVelocityY(0);
            }
        if ((transform().getVelocity().equals(Vector2.ZERO)) && (curEnergy < MAX_ENERGY)){
            curEnergy += Math.min(ENERGY_TO_RESTORE_WHEN_IDLE, MAX_ENERGY - curEnergy);
        }
        chooseRenderable();
        energyDisplayer.updateEnergy(curEnergy);
    }

    /**
     * Retrieves the energyDisplayer
     *
     * @return energyDisplayer.
     */
    public GameObject getEnergyDisplayer() {
        return energyDisplayer;
    }

    /**
     * Registers an observer to be notified of avatar events.
     *
     * @param observer The observer to be registered.
     */
    public void registerObserver(Observer observer){
        observers.add(observer);
    }

    /**
     * Handles collision events with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Fruit.FRUIT_TAG)){
            setEnergy(Math.min(curEnergy + ENERGY_TO_GAIN_WHEN_EATING_FRUIT, MAX_ENERGY));
        }
    }

    /*
     * Notifies all registered observers of avatar events.
     */
    private void notifyObservers(){
        for (Observer observer : observers){
            observer.update();
        }
    }

    /*
     * Chooses the appropriate renderable based on the avatar's movements.
     * If the avatar is moving horizontally, sets the run renderable and flips it horizontally
     *  based on the direction of movement.
     * If the avatar is moving vertically (jumping), sets the jump renderable.
     * If the avatar is not moving, sets the idle renderable.
     */
    private void chooseRenderable(){
        if (getVelocity().x() != 0){
            renderer().setRenderable(runRenderable);
            renderer().setIsFlippedHorizontally(!(getVelocity().x() > 0));
        } else if (getVelocity().y() != 0){
            renderer().setRenderable(jumpRenderable);
        } else {
            renderer().setRenderable(idleRenderable);
        }
    }

    /*
     * Sets the current energy level of the avatar.
     *
     * @param curEnergy The current energy level to set.
     */
    private void setEnergy(float curEnergy) {
        this.curEnergy = curEnergy;
        energyDisplayer.updateEnergy(curEnergy);
    }

    /*
     * Inner class representing the energy displayer for the avatar.
     * Displays the current energy level as text.
     */
    private class EnergyDisplayer extends GameObject{
        /*
         * text renderable.
         */
        private final TextRenderable renderable;

        /**
         * Constructs an EnergyDisplayer object with the specified parameters.
         *
         * @param renderable The text renderable used to display energy.
         */
        public EnergyDisplayer(TextRenderable renderable){
            super(
                    Vector2.ONES.mult(ENERGY_POS_PADDING),
                    Vector2.ONES.mult(ENERGY_DISPLAY_SIZE),
                    renderable);
            this.renderable = renderable;
        }

        /**
         * Updates the energy displayed by the displayer.
         *
         * @param energy The current energy level to display.
         */
        public void updateEnergy(float energy){
            renderable.setString(String.valueOf(energy));
        }
    }
}
