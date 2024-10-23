
public class Test {

    public static void main(String[] args) {
        List list = new List();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println(list);
        System.out.println(list.length());
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        System.out.println(list.get(3));

        int[] elements = { 4, 5, 6, 4, 5, 6 };
        list.addAll(elements);
        System.out.println(list);
        System.out.println(list.find(4));
        list.remove(list.find(1));
        list.remove(list.find(2));
        list.remove(list.find(3));

        System.out.println(list);

    }
}
