import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;

import java.util.*;
import java.util.stream.Collectors;

public class GameImpl extends Pane implements Game {
    /**
     * Defines different states of the game.
     */
    public enum GameState {
        WON, LOST, ACTIVE, NEW
    }

    // Constants
    /**
     * The width of the game board.
     */
    public static final int WIDTH = 400;
    /**
     * The height of the game board.
     */
    public static final int HEIGHT = 600;

    /**
     * The amount of rows of animals to generate.
     */
    public static final int ANIMAL_ROWS = 4;
    /**
     * The amount of columns of animals to generate.
     */
    public static final int ANIMAL_COLUMNS = 4;

    /**
     * The padding on the leftmost and rightmost sides of the game bounding box.
     */
    public static final int ANIMAL_PADDING_X = 60;
    /**
     * The padding on the top side of the game bounding box.
     */
    public static final int ANIMAL_PADDING_Y = 30;

    /**
     * Spacing between each column of animals, computed based on the desired edge padding and block width
     */
    public static final int ANIMAL_SPACING_X = Block.BLOCK_WIDTH + (WIDTH - (ANIMAL_COLUMNS * Block.BLOCK_WIDTH) - (ANIMAL_PADDING_X * 2)) / (ANIMAL_COLUMNS - 1);
    /**
     * Spacing between each row of animals. Arbitrary value that looked good.
     */
    public static final int ANIMAL_SPACING_Y = 60;

    /**
     * Amount of times that the ball can collide with the bottom of the game bounding box before the game is lost.
     */
    public static final int LOWER_BOUNDARY_COLLISION_LIMIT = 5;


    // Instance variables
    private Ball ball;
    private Paddle paddle;
    private List<Block> animals;
    private GameState state;
    private int lowerBoundaryCollisions = 0;

    /**
     * Constructs a new GameImpl.
     */
    public GameImpl() {
        setStyle("-fx-background-color: white;");

        restartGame(GameState.NEW);
    }

    public String getName() {
        return "Zutopia";
    }

    public Pane getPane() {
        return this;
    }

    /**
     * Initialize or re-initialize the game state to play again, and show a start message
     * based on the last game state.
     *
     * @param state The last game state before restarting
     */
    private void restartGame(GameState state) {
        getChildren().clear();  // remove all components from the game

        // Create and add ball
        ball = new Ball();
        getChildren().add(ball.getCircle());  // Add the ball to the game board

        // Create and add animals ...
        animals = new ArrayList<Block>() {{
            for (int i = 0; i < ANIMAL_ROWS; i++) {
                for (int j = 0; j < ANIMAL_COLUMNS; j++) {
                    add(new Block(
                            ANIMAL_PADDING_X + i * ANIMAL_SPACING_X,
                            ANIMAL_PADDING_Y + j * ANIMAL_SPACING_Y
                    ));
                }
            }
        }};
        getChildren().addAll(animals.stream().map(Block::getImage).collect(Collectors.toList()));

        // Create and add paddle
        paddle = new Paddle();
        getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

        lowerBoundaryCollisions = 0;

        // Add start message
        final String message;
        if (state == GameState.LOST) {
            message = "Game Over\n";
        } else if (state == GameState.WON) {
            message = "You won!\n";
        } else {
            message = "";
        }
        final Label startLabel = new Label(message + "Click mouse to start");
        startLabel.setLayoutX(WIDTH / 2 - 50);
        startLabel.setLayoutY(HEIGHT / 2 + 100);
        getChildren().add(startLabel);

        // Add event handler to start the game
        setOnMouseClicked(e -> {
            GameImpl.this.setOnMouseClicked(null);

            // As soon as the mouse is clicked, remove the startLabel from the game board
            getChildren().remove(startLabel);
            run();
        });

        // Add event handler to steer paddle
        setOnMouseMoved(e -> {
            if (state == GameState.ACTIVE) paddle.moveTo(e.getX(), e.getY());
        });

    }

    /**
     * Begins the game-play by creating and starting an AnimationTimer.
     */
    public void run() {
        // Instantiate and start an AnimationTimer to update the component of the game.
        new AnimationTimer() {
            private long lastNanoTime = -1;

            public void handle(long currentNanoTime) {
                if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
                    if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
                        // Once the game is no longer ACTIVE, stop the AnimationTimer.
                        stop();
                        // Restart the game, with a message that depends on whether
                        // the user won or lost the game.
                        restartGame(state);
                    }
                }
                // Keep track of how much time actually transpired since the last clock-tick.
                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    /**
     * Updates the state of the game at each timestep. In particular, this method should
     * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
     *
     * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
     * @return the current game state
     */
    public GameState runOneTimestep(long deltaNanoTime) {

        // Look ahead one frame to check if the ball and paddle will collide. This lookahead collision
        // prevents the ball from colliding multiple times with the paddle, since it could otherwise
        // get "stuck" inside.
        if (paddle.checkCollision(ball.peekUpdatedPosition(deltaNanoTime))) {
            ball.flipYDirection();
        }

        Iterator<Block> it = animals.iterator();
        while (it.hasNext()) {
            Block animal = it.next();
            if (!animal.checkCollision(ball))
                continue;
            getChildren().remove(animal.getImage());
            ball.increaseSpeed();
            ball.flipYDirection();
            it.remove();
        }

        boolean collidedLower = ball.updatePosition(deltaNanoTime);
        if (collidedLower)
            lowerBoundaryCollisions++;

        if (animals.size() == 0)
            return GameState.WON;
        if (lowerBoundaryCollisions >= LOWER_BOUNDARY_COLLISION_LIMIT)
            return GameState.LOST;

        return GameState.ACTIVE;
    }
}
