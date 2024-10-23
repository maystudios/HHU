import java.util.NoSuchElementException;

public class BinarySearchTree {

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(8);
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(2);
        tree.insert(4);
        tree.insert(6);
        tree.insert(1);
        tree.insert(9);
        tree.insert(10);
        tree.insert(11);
        tree.insert(12);
        tree.insert(13);
        tree.insert(14);

        System.out.println(tree);
        System.out.println(tree.printAsTreeWithLines());
        System.out.println(tree.height());
        System.out.println(tree.sum());
        System.out.println(tree.maximumRecursive());
        System.out.println(tree.maximumIterative());
        System.out.println(tree.reverseOrder());
    }

    private class BinaryNode {
        private int element;
        private BinaryNode left;
        private BinaryNode right;

        private BinaryNode(int element) {
            this.element = element;
        }
    }

    private BinaryNode root;

    public void insert(int newNumber) {
        // Sonderfall: leerer Baum
        if (root == null) {
            root = new BinaryNode(newNumber);
            return;
        }

        BinaryNode parent = null;
        BinaryNode child = root;
        while (child != null) {
            parent = child;
            if (newNumber == child.element) {
                // Zahl bereits im Baum vorhanden
                return;
            } else if (newNumber < child.element) {
                child = child.left;
            } else {
                child = child.right;
            }
        }

        if (newNumber < parent.element) {
            parent.left = new BinaryNode(newNumber);
        } else {
            parent.right = new BinaryNode(newNumber);
        }
    }

    @Override
    public String toString() {
        return getNodeAsString(root);
    }

    private String getNodeAsString(BinaryNode node) {
        if (node == null) {
            return "";
        }
        return getNodeAsString(node.left) + node.element + " " + getNodeAsString(node.right);
    }

    public String printAsTreeWithLines() {
        return printAsTreeWithLines(root, "");
    }

    private String printAsTreeWithLines(BinaryNode node, String prefix) {
        if (node == null) {
            return "";
        }
        String result = "";
        result += printAsTreeWithLines(node.right, prefix + "  ");
        result += prefix + node.element + "\n";
        result += printAsTreeWithLines(node.left, prefix + "  ");
        return result;
    }

    public String reverseOrder() {
        return reverseOrder(root);
    }

    private String reverseOrder(BinaryNode node) {
        if (node == null) {
            return "";
        }
        return reverseOrder(node.right) + node.element + ", " + reverseOrder(node.left);
    }

    public int height() {
        return height(root);
    }

    private int height(BinaryNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int sum() {
        return sum(root);
    }

    private int sum(BinaryNode node) {
        if (node == null) {
            return 0;
        }
        return node.element + sum(node.left) + sum(node.right);
    }

    public int maximumRecursive() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty");
        }
        return maximumRecursive(root);
    }

    private int maximumRecursive(BinaryNode node) {
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        return Math.max(node.element, Math.max(maximumRecursive(node.left), maximumRecursive(node.right)));
    }

    public int maximumIterative() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty");
        }
        BinaryNode node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.element;
    }

}
