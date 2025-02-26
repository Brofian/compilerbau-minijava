package de.multipleclasses;

class AClass {
    private int value;

    public int calc(int n) {
        // wrong precedence for assignment operators
        // n = n + 4
        n = (n + 4);
        this.value = 5;

        BClass b = new BClass();
        AClass a = b.createNewInstance();
        a.calc(this.value);
        b.calc(this.value);

        return n;
    }
}

class BClass extends AClass {

    public AClass createNewInstance() {
        //System.out.println("Im creating a new Instance!!");
        return new BClass();

    }

    /*
    // Method calls still runs into problems with the parser
    public int heavyCalc() {
        this.calc(10);
        return 1;
    }
     */

}