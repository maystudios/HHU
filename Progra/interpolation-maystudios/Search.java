public class Search {
    public static void main(String[] args) {

        int needle = Integer.parseInt(args[0]);
        int[] haystack = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            haystack[i - 1] = Integer.parseInt(args[i]);
        }

        int index = search(haystack, needle);
        System.out.println(index);
    }

    private static int search(int[] haystack, int needle) {
        int left = 0;
        int right = haystack.length - 1;

        while (left <= right && needle >= haystack[left] && needle <= haystack[right]) {
            int index = split(haystack, needle, left, right);

            if (index < left || index > right) {
                return -1;
            }

            if (haystack[index] == needle) {
                return index;
            } else if (haystack[index] < needle) {
                left = index + 1;
            } else {
                right = index - 1;
            }
        }

        return -1;
    }

    private static int split(int[] haystack, int needle, int left, int right) {
        return (haystack[left] == haystack[right]) ? left : (int) interpolate(haystack, needle, left, right);
    }

    private static double interpolate(int[] haystack, int needle, int left, int right) {
        return left + ((needle - haystack[left]) * (right - left)) / (haystack[right] - haystack[left]);
    }

}
