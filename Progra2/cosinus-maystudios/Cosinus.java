public class Cosinus {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("ERROR: Bitte übergib eine Zahl!");
            return;
        }

        try {
            System.out.println(MayMath.Cosine(Double.parseDouble(args[0]), 11));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Bitte übergib eine Gleitkommazahl!");
        }
    }

    /**
     * {@code MayMath} is a standard Math lib to calcualte several Math operations
     * like:
     * 
     * <blockquote>
     * 
     * <pre>
     * 
     *double pow = MayMath.Pow(x, n);
     *long   fact = MayMath.Fact(n);
     *double taylor = MayMath.Taylor(x, n)
     * 
     *double cos = MayMath.Cosine(x, aproximation);
     * 
     * </pre>
     * 
     * </blockquote>
     * <p>
     *
     * @author MayStudios
     */
    public static class MayMath {

        /**
         * Calculates the power of a number.
         * 
         * @param x The base number.
         * @param n The exponent.
         * 
         * @return Returns the result of raising x to the power of n.
         */
        public static double Pow(double x, int n) {
        	return n == 0 ? 1 : n > 0 ? (n % 2 == 0 ? Pow(x * x, n / 2) : x * Pow(x, n - 1)) : 1 / Pow(x, -n);
		}

        /**
         * Calculates the factorial of a number.
         * 
         * @param n The number to find the factorial of.
         * 
         * @return Returns the factorial of n.
         */
        public static long Fact(int n) {
            return (n <= 1) ? 1 : n * Fact(n - 1);
		}

        /**
         * Calculates a term in the Taylor series.
         * 
         * @param x The value to calculate the term for.
         * @param n The term number.
         * 
         * @return Returns the n-th term of the Taylor series for value x.
         */
        public static double Taylor(double x, int n) {
            return (Pow(x, n) / Fact(n));
        }

        /**
         * Calculates the Cosine of the radian by using the Taylor aproximation.
         * 
         * @param x            The value to approximate.
         * @param aproximation The number of terms in the Taylor series to
         *                     use for the approximation.
         * 
         * @return Returns the aproximated value of Cosine.
         */
        public static double Cosine(double x, int aproximation) {
            double res = 0;
            x = x % (2 * Math.PI);
            for (int i = 0; i < aproximation; i++) {
                res += Pow(-1, i) * Taylor(x, 2 * i);
            }
            return res;
        }

        /**
         * Calculates the Cosine of the radian by using the Taylor aproximation.
         * 
         * @param x The value to approximate.
         * 
         * @return Returns the aproximated value of Cosine.
         */
        public static double Cosine(double x) {
            return Cosine(x, 11);
        }
    }
}
