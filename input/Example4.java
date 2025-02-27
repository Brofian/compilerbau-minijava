package de.example3;

import de.example3.Example3Helper;

class Example4 {

    public static void main(String[] args) {

        Example3Helper helper = new Example3Helper();

        int fib;
        fib = helper.fibonacci(8);

        System.out.println(fib);

    }

}