public class Racecar {
    private double speed;
    private double distance;

    public Racecar() {
        this.speed = 0.0;
        this.distance = 0.0;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public void accelerate(double speedDelta) {
        if (speedDelta >= 0) {
            speed += speedDelta;
        }
    }

    public void decelerate(double speedDelta) {
        if (speedDelta >= 0 && speedDelta <= speed) {
            speed -= speedDelta;
        }
    }

    public void drive(double seconds) {
        distance += speed * seconds;
    }

    @Override
    public String toString() {
        return String.format("Speed: %.3fm/s, Distance: %.3fm", speed, distance);
    }
}
