package de.example3;

class Example3 {

    public static void main(String[] args) {

        Example3Helper helper = new Example3Helper();

        int fib;

        for (int n = 0; n < 10; n += 1) {
            fib = helper.fibonacci(n);
            System.out.println(fib);
        }

    }

}

class Example3Helper {

    public int fibonacci(int n) {

        int tmpA = 1;
        int tmpB = 1;
        int fibNumber = 1;

        for (int i = 0; i < n; i += 1) {
            fibNumber = tmpA + tmpB;
            tmpA = tmpB;
            tmpB = fibNumber;
        }

        return fibNumber;
    }


    public int fibonacciRecursive(int n) {

        if (n <= 2) {
            return 1;
        }

        return this.fibonacciRecursive(n - 1) + this.fibonacciRecursive(n - 2);
    }

}