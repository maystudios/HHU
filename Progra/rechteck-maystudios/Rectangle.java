public class Rectangle {
    private int width;
    private int height;

    // Constructor with width and height
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // No-argument constructor
    public Rectangle() {
        this.width = 0;
        this.height = 0;
    }

    // Method to calculate area
    public int area() {
        return width * height;
    }

    // Method to calculate perimeter
    public int perimeter() {
        return 2 * (width + height);
    }

    // Method to check if it's a square
    public boolean isSquare() {
        return width == height;
    }

    // Method to return string representation
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                sb.append("* ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Main method for testing
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(5, 3);
        Rectangle square = new Rectangle(2, 2);

        System.out.println(rectangle.isSquare());
        System.out.println(rectangle.perimeter());
        System.out.println(rectangle.area());
        System.out.println(rectangle);

        System.out.println(square.isSquare());
        System.out.println(square);
    }
}
