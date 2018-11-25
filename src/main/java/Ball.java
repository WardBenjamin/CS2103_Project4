import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball implements ICollidable {
    // Constants
    /**
     * The radius of the ball.
     */
    public static final int BALL_RADIUS = 8;
    /**
     * The initial velocity of the ball in the x direction.
     */
    public static final double INITIAL_VX = 1e-7;
    /**
     * The initial velocity of the ball in the y direction.
     */
    public static final double INITIAL_VY = 1e-7;

    /**
     * The factor which to multiply the speed by when speed is increased.
     */
    public static final double SPEED_INCREASE_FACTOR = 1.1;

    // Instance variables
    // (x,y) is the position of the center of the ball.
    private double x, y;
    private double vx, vy;
    private final Circle circle;

    /**
     * @return the Circle object that represents the ball on the game board.
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * Constructs a new Ball object at the centroid of the game board
     * with a default velocity that points down and right.
     */
    public Ball() {
        x = GameImpl.WIDTH / 2;
        y = GameImpl.HEIGHT / 2;
        vx = INITIAL_VX;
        vy = INITIAL_VY;

        circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
        circle.setLayoutX(x - BALL_RADIUS);
        circle.setLayoutY(y - BALL_RADIUS);
        circle.setFill(Color.BLACK);
    }

    /**
     * Updates the position of the ball, given its current position and velocity,
     * based on the specified elapsed time since the last update.
     *
     * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
     * @return Whether the ball has collided with the lower wall of the game area
     */
    public boolean updatePosition(long deltaNanoTime) {
        Bounds peek = peekUpdatedPosition(deltaNanoTime);

        // Ensure that the ball bounces off the sides of the game bounding box
        if (peek.getMinX() < 0 || peek.getMaxX() > GameImpl.WIDTH)
            flipXDirection();
        if (peek.getMinY() < 0 || peek.getMaxY() > GameImpl.HEIGHT)
            flipYDirection();

        double dx = vx * deltaNanoTime;
        double dy = vy * deltaNanoTime;
        x += dx;
        y += dy;

        circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
        circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));

        return peek.getMaxY() > GameImpl.HEIGHT;
    }

    /**
     * Returns the updated position of the ball, given its current position and velocity,
     * based on the specified elapsed time since the last update.
     *
     * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
     * @return The position at which the ball will be after an update
     */
    public Bounds peekUpdatedPosition(long deltaNanoTime) {
        double dx = vx * deltaNanoTime;
        double dy = vy * deltaNanoTime;
        double x = this.x + dx;
        double y = this.y + dy;

        Circle circle = Util.duplicate(this.circle);
        circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
        circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));

        return circle.getBoundsInParent();
    }

    /**
     * Get the bounding box that encapsulates the ICollidable object, in game space
     *
     * @return Bounding box encapsulating the object
     */
    @Override
    public Bounds getBoundingBox() {
        return circle.getBoundsInParent();
    }

    /**
     * Invert the y direction of motion
     */
    void flipYDirection() {
        vy *= -1;
    }

    /**
     * Invert the x direction of motion
     */
    void flipXDirection() {
        vx *= -1;
    }

    /**
     * Increase the speed of the ball by a factor of SPEED_INCREASE_FACTOR
     */
    void increaseSpeed() {
        vx *= SPEED_INCREASE_FACTOR;
        vy *= SPEED_INCREASE_FACTOR;
    }
}
