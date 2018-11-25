import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Util {
    public static Rectangle circleToRect(Circle circle) {
        double radius = circle.getRadius();
        Rectangle rect = new Rectangle(circle.getCenterX() - radius, circle.getCenterY() - radius, radius * 2, radius * 2);
        rect.setTranslateX(circle.getTranslateX());
        rect.setTranslateY(circle.getTranslateY());
        return rect;
    }

    public static Circle duplicate(Circle original) {
        Circle duplicated = new Circle(original.getCenterX(), original.getCenterY(), original.getRadius());
        duplicated.setTranslateX(original.getTranslateX());
        duplicated.setTranslateY(original.getTranslateY());
        return duplicated;
    }
}
