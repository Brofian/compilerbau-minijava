package de.example1;

class Example1 {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i += 1) {
            System.out.println(args[i]);
        }

        Example1 e = new Example1();
        int num = e.addOne(args.length);

        StringBuilder sb = new StringBuilder();
        sb.append("This message included, i responded with ");
        sb.append(num);
        sb.append(" output texts");

        System.out.println(sb.toString());
    }

    public int addOne(int n) {
        return n + 1;
    }

}