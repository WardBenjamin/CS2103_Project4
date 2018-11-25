import javafx.scene.shape.Circle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameImplTest {
    Circle circle;

    final double x = GameImpl.WIDTH / 2;
    final double y = GameImpl.HEIGHT / 2;
    final double vx = Ball.INITIAL_VX;
    final double vy = Ball.INITIAL_VY;

    private void resetCircle() {
        circle = new Circle(Ball.BALL_RADIUS, Ball.BALL_RADIUS, Ball.BALL_RADIUS);
        circle.setLayoutX(x - Ball.BALL_RADIUS);
        circle.setLayoutY(y - Ball.BALL_RADIUS);
    }

    @Test
    public void testUtilCircleDuplication1() {
        resetCircle();

        Circle duplicated = Util.duplicate(circle);

        assertEquals(circle.getCenterX(), duplicated.getCenterX(), 1E-7);
        assertEquals(circle.getCenterY(), duplicated.getCenterY(), 1E-7);
        assertEquals(circle.getRadius(), duplicated.getRadius(), 1E-7);
        assertEquals(circle.getTranslateX(), duplicated.getTranslateX(), 1E-7);
        assertEquals(circle.getTranslateY(), duplicated.getTranslateY(), 1E-7);
    }

    @Test
    public void testUtilCircleDuplication2() {
        resetCircle();

        Circle duplicated = Util.duplicate(circle);

        double deltaNanoTime = 16064914; // Reasonable number, found via opening game. Equivalent to about 60 hz

        double dx = vx * deltaNanoTime;
        double dy = vy * deltaNanoTime;
        // GameImpl.CONST / 2 represents the initial (x, y) position from the Ball ctor
        double x = GameImpl.WIDTH / 2 + dx;
        double y = GameImpl.HEIGHT / 2 + dy;

        // translate = x - layout + radius
        // x = translate + layout - radius

        circle.setTranslateX(x - (circle.getLayoutX() + Ball.BALL_RADIUS));
        circle.setTranslateY(y - (circle.getLayoutY() + Ball.BALL_RADIUS));

        assertEquals(circle.getCenterX(), duplicated.getCenterX(), 1E-7);
        assertEquals(circle.getCenterY(), duplicated.getCenterY(), 1E-7);
        assertEquals(circle.getRadius(), duplicated.getRadius(), 1E-7);
        assertNotEquals(circle.getTranslateX(), duplicated.getTranslateX(), 1E-7);
        assertNotEquals(circle.getTranslateY(), duplicated.getTranslateY(), 1E-7);
    }

    @Test
    public void testUtilCircleDuplication3() {
        resetCircle();

        double deltaNanoTime = 16064914; // Reasonable number, found via opening game. Equivalent to about 60 hz

        double dx = vx * deltaNanoTime;
        double dy = vy * deltaNanoTime;
        // GameImpl.CONST / 2 represents the initial (x, y) position from the Ball ctor
        double x = GameImpl.WIDTH / 2 + dx;
        double y = GameImpl.HEIGHT / 2 + dy;

        // translate = x - layout + radius
        // x = translate + layout - radius

        circle.setTranslateX(x - (circle.getLayoutX() + Ball.BALL_RADIUS));
        circle.setTranslateY(y - (circle.getLayoutY() + Ball.BALL_RADIUS));

        Circle duplicated = Util.duplicate(circle);

        assertEquals(circle.getCenterX(), duplicated.getCenterX(), 1E-7);
        assertEquals(circle.getCenterY(), duplicated.getCenterY(), 1E-7);
        assertEquals(circle.getRadius(), duplicated.getRadius(), 1E-7);
        assertEquals(circle.getTranslateX(), duplicated.getTranslateX(), 1E-7);
        assertEquals(circle.getTranslateY(), duplicated.getTranslateY(), 1E-7);
    }

    /**
     * Test that translateX == x - (layoutX + radius)
     * Formula deduced from Ball.updatePosition
     */
    @Test
    public void testCirclePositionDeduction1() {
        resetCircle();

        // Formula: translate = x - (layout + radius)
        // Deduced from Ball.updatePosition

        double translateX = circle.getTranslateX(), translateY = circle.getTranslateY();

        circle.setTranslateX(x - (circle.getLayoutX() + Ball.BALL_RADIUS));
        circle.setTranslateY(y - (circle.getLayoutY() + Ball.BALL_RADIUS));

        assertEquals(translateX, circle.getTranslateX(), 1E-7);
        assertEquals(translateY, circle.getTranslateY(), 1E-7);
    }

    /**
     * Test that x == translateX + layoutX + radius
     * Formula deduced from Ball.updatePosition and testCirclePositionDeduction1
     */
    @Test
    public void testCirclePositionDeduction2() {
        resetCircle();

        // Formula: x = translate + layout - radius
        // Deduced from Ball.updatePosition and testCirclePositionDeduction2

        double translateX = circle.getTranslateX(), translateY = circle.getTranslateY();

        assertEquals(x, circle.getTranslateX() + circle.getLayoutX() + Ball.BALL_RADIUS, 1E-7);
        assertEquals(y, circle.getTranslateY() + circle.getLayoutY() + Ball.BALL_RADIUS, 1E-7);
    }
}