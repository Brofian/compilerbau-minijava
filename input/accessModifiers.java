package de.accessmodifiers;

class AccessModifiers {

    public void test() {
        B b = new B();
        //b.propA; // not possible, because property is private
        b.propB;
        b.propC;
        b.propD;
    }

}

class B {

    private int propA = 1;
    protected int propB = 2;
    public int propC = 3;
    /* package */ int propD = 4;

}