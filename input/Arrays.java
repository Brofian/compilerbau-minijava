package pck;

class Arrays {
    public static void main(String[] args) {
        int[] a;
        a = new int[10];
        System.out.println(a.length);
        System.out.println(a[2]);
        a[2] = 42;
        System.out.println(a[2]);
        int b = a[3];
    }
}