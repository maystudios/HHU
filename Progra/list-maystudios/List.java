public class List {

    private class Node {
        private int value;
        private Node next;

        private Node(int element, Node next) {
            this.value = element;
            this.next = next;
        }
    }

    private Node head;
    private int length = 0;

    public List() {
        head = null;
    }

    public List(int[] elements) {
        addAll(elements);
    }

    public void add(int element) {
        if (head == null) {
            head = new Node(element, null);
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node(element, null);
        }
        length++;
    }

    public int length() {
        return length;
    }

    public int get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        } else if (index >= length) {
            return get(length - 1);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public void set(int index, int element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        } else if (index >= length) {
            set(length - 1, element);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.value = element;
    }

    public void addAll(int[] elements) {
        for (int element : elements) {
            add(element);
        }
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += get(i) + (i == length - 1 ? "" : ",");
        }
        return result;
    }

    public int find(int element) {
        for (int i = 0; i < length; i++) {
            if (get(i) == element) {
                return i;
            }
        }
        return -1;
    }

    public int findLast(int element) {
        for (int i = length - 1; i >= 0; i--) {
            if (get(i) == element) {
                return i;
            }
        }
        return -1;
    }

    public void remove(int index) {
        if (index < 0 || index >= length) {
            return;
        }

        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }

        length--;
    }

    public void removeFirstOccurrence(int element) {
        remove(find(element));
    }

    public void removeLastOccurrence(int element) {
        remove(findLast(element));
    }

    public void sort() {
        for (int i = 0; i < length; i++) {
            int min = get(i);
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (get(j) < min) {
                    min = get(j);
                    minIndex = j;
                }
            }
            swap(i, minIndex);
        }
    }

    public void swap(int i, int j) {
        int temp = get(i);
        set(i, get(j));
        set(j, temp);
    }
}
