package de.example5;

class Example5 {

    public int getNumber() {
        return 42;
    }

    public static void main(String[] args) {

        Example5 extension = new Example5Extension();

        int num;
        num = extension.getNumber();

        System.out.println(num);
    }


}

class Example5Extension extends Example5 {


    /*
    public int getNumber() {
        return 21;
    }
    */



}