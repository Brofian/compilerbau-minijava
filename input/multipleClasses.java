package de.example;

import java.lang.String;

class AClass {
    private int value;

    public int calc(int n) {
        n = 4;
        this.value = 5;

        BClass b = new BClass();
        AClass a = b.createNewInstance();
        a.calc(this.value);

        return n;
    }

    public String getTestString() {
        String text = "Hello world";

        return text.substring(1, 3);
    }
}

class BClass extends AClass {

    public AClass createNewInstance() {
        return new BClass();
    }

}