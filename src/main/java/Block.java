import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Block implements ICollidable {

    // Constants
    /**
     * The width of the block.
     */
    public static final int BLOCK_WIDTH = 48;
    /**
     * The height of the block.
     */
    public static final int BLOCK_HEIGHT = 43;

    private final String[] POTENTIAL_IMAGES = {"duck.jpg", "goat.jpg", "horse.jpg"};

    // Instance variables
    private final Label imageLabel;

    /**
     * Constructs a new Block which is centered within the Rectangle{x, y, BLOCK_WIDTH, BLOCK_HEIGHT}
     *
     * @param x X coordinate representing the top corner of the placement box
     * @param y Y coordinate representing the top corner of the placement box
     */
    Block(double x, double y) {
        // Load a random animal image from file
        final Image image = new Image(getClass().getResourceAsStream(getFilename()));
        imageLabel = new Label("", new ImageView(image));

        // Apply a small coordinate offset to ensure that all images look centered relative to one
        // another, since the coordinate parameters represent the top corner of a BLOCK_WIDTH wide,
        // BLOCK_HEIGHT tall placement position
        imageLabel.setLayoutX(x + (BLOCK_WIDTH - image.getWidth()) / 2);
        imageLabel.setLayoutY(y + (BLOCK_HEIGHT - image.getHeight()) / 2);
    }

    /**
     * @return a random filename for a potential animal image
     */
    String getFilename() {
        return POTENTIAL_IMAGES[new Random().nextInt(POTENTIAL_IMAGES.length)];
    }

    /**
     * @return the Rectangle object that represents the block on the game board
     */
    Label getImage() {
        return imageLabel;
    }

    /**
     * Get the bounding box that encapsulates the ICollidable object, in game space
     *
     * @return Bounding box encapsulating the object
     */
    @Override
    public Bounds getBoundingBox() {
        return imageLabel.getBoundsInParent();
    }
}
