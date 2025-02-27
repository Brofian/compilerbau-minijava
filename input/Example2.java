package de.example2;

class Example2 {

    public static void main(String[] args) {
        System.out.println(Example2.getMessage());

        for (int i = 0; i < args.length; i += 1) {
            System.out.println("Simon says: ".concat(args[i]));
        }
    }

    private static String getMessage() {
        return "Let's play simon says!";
    }

}