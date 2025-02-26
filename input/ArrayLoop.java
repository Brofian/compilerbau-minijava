package de.arrayloop;

public class ArrayLoop {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i += 1) {
            System.out.println(args[i]);
        }
    }
}