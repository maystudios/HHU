public class Matrix {

    public static void main(String[] args) {
        // Matrix m1 = new Matrix(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6, 7 } });
        // Matrix m2 = new Matrix(new int[][] { { 1, 2 }, null, { 5, 6 } });
        Matrix m3 = new Matrix(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        System.out.println(m3.get(0, 0));

        Matrix m4 = new Matrix(new int[][] { { 1, 2, 7, 8 }, { 2, 6, 5, 3 } });
        Matrix m5 = new Matrix(new int[][] { { 2, 8, 9 }, { 1, 12, 15 }, { 42, 3, 6 }, { 50, 21, 4 } });
        System.out.println(m4);
        System.out.println(m5);
        System.out.println(m4.multiply(m5));
    }

    public static Matrix createBigMatrix(int size) {
        int[][] matrix = new int[size][size];
        int value = 1;
        for (int i = 0; i < size; i++) {
            int[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                row[j] = value++;
            }
        }
        return new Matrix(matrix);
    }

    // Matrix class below here
    // ==========================================================================

    /**
     * A class representing a matrix of integers.
     */
    private int[][] matrix;

    /**
     * Constructs a Matrix object based on a two-dimensional array.
     *
     * @param matrix A two-dimensional array of integers representing the matrix.
     * @throws IllegalArgumentException if the input array is null, empty, or not
     *                                  rectangular.
     */
    public Matrix(int[][] matrix) {
        // Check if the passed matrix is null or has no rows, which is considered
        // invalid
        if (matrix == null || matrix.length == 0) {
            throw new IllegalArgumentException("Matrix must not be null or empty");
        }

        // Iterate through each row of the matrix to perform additional checks
        for (int i = 0; i < matrix.length; i++) {
            // Check if any row of the matrix is null or has no columns, which is also
            // considered invalid
            if (matrix[i] == null || matrix[i].length == 0) {
                throw new IllegalArgumentException("Matrix rows must not be null or empty");
            }

            int currentLength = matrix[i].length;
            // If this is not the first row, check that it has the same number of columns as
            // the previous row
            if (i > 0 && currentLength != matrix[i - 1].length) {
                throw new IllegalArgumentException("Matrix must be rectangular");
            }
        }
        this.matrix = matrix;
    }

    /**
     * Retrieves the matrix as a two-dimensional array of integers.
     *
     * @return The matrix as a two-dimensional array of integers.
     */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Retrieves the element at the specified position in a matrix.
     *
     * @param row The zero-based row index of the matrix element to retrieve.
     * @param col The zero-based column index of the matrix element to retrieve.
     * @return The value of the element at the specified row and column.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds of
     *                                   the matrix.
     */
    public int get(int row, int col) {
        // Check if either row or column index is out of the acceptable range of the
        // matrix dimensions
        if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[row].length) {
            throw new IndexOutOfBoundsException("Row and column must be within the matrix");
        }
        return matrix[row][col];
    }

    /**
     * Sets the element at the specified position in a matrix to the specified
     * value.
     *
     * @param row   The zero-based row index of the matrix element to set.
     * @param col   The zero-based column index of the matrix element to set.
     * @param value The value to set the element to.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds of
     *                                   the matrix.
     */
    public void set(int row, int col, int value) {
        // Check if either row or column index is out of the acceptable range of the
        // matrix dimensions
        if (row < 0 || row >= matrix.length || col < 0 || col >= matrix.length) {
            throw new IndexOutOfBoundsException("Row and column must be within the matrix");
        }
        matrix[row][col] = value;
    }

    /**
     * Returns a string representation of the matrix.
     *
     * @return A string representation of the matrix.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            sb.append("[");
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i != row.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    /**
     * Multiplies this matrix by another matrix.
     *
     * @param other The matrix to multiply this matrix by.
     * @return The result of multiplying this matrix by the other matrix.
     * @throws IllegalArgumentException if the other matrix is null, empty, or not
     *                                  rectangular, or if the number of columns in
     *                                  this matrix does not match the number of
     *                                  rows in the other matrix.
     */
    public Matrix multiply(Matrix other) {
        // Check if the passed matrix is null before using it
        if (other == null) {
            throw new IllegalArgumentException("The passed matrix cannot be null");
        }

        // Further checks to ensure proper dimensions for matrix multiplication
        if (matrix[0].length != other.getMatrix().length) {
            throw new IllegalArgumentException("Matrix column count must match other matrix row count");
        }

        for (int i = 0; i < other.getMatrix().length; i++) {
            if (other.getMatrix()[i] == null || other.getMatrix()[i].length == 0) {
                throw new IllegalArgumentException("Matrix rows must not be null or empty");
            }

            int currentLength = other.getMatrix()[i].length;
            if (i > 0 && currentLength != other.getMatrix()[i - 1].length) {
                throw new IllegalArgumentException("Matrix must be rectangular");
            }
        }

        int[][] result = new int[matrix.length][other.getMatrix()[0].length];
        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];
            for (int j = 0; j < other.getMatrix()[0].length; j++) {
                int[] otherCol = new int[other.getMatrix().length];
                for (int k = 0; k < other.getMatrix().length; k++) {
                    otherCol[k] = other.getMatrix()[k][j];
                }
                int sum = 0;
                for (int l = 0; l < row.length; l++) {
                    sum += row[l] * otherCol[l];
                }
                result[i][j] = sum;
            }
        }

        return new Matrix(result);
    }
}
