package de.mypackage;

import example.other.CClass;

class AClass {
    private int value;

    public int calc(int n) {
        this.value = 5;

        BClass b = new BClass();
        AClass a = b.createNewInstance();
        a.calc(this.value);

        return n;
    }
}

class BClass extends AClass {

    public AClass createNewInstance() {
        return new BClass();
    }

}