import javafx.geometry.Bounds;

interface ICollidable {
    /**
     * Get the bounding box that encapsulates the ICollidable object, in game space
     *
     * @return Bounding box encapsulating the object
     */
    Bounds getBoundingBox();

    /**
     * Check if there's a collision between this collidable object and another.
     *
     * @param other The object to check for collision with
     * @return Whether the two objects are colliding
     */
    default boolean checkCollision(ICollidable other) {
        return getBoundingBox().intersects(other.getBoundingBox());
    }

    /**
     * Check if there's a collision between this collidable object and another.
     *
     * @param other The object to check for collision with
     * @return Whether the two objects are colliding
     */
    default boolean checkCollision(Bounds other) {
        return getBoundingBox().intersects(other);
    }
}