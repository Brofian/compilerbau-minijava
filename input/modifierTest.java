package de.modifiertest;

class A {
    final int a = 3;
    final int b;
    public int c;
    protected int d;
    private final int e;

    public static void main(String[] args) { return; }
    private void foo() { return; }
    private static void bar() { return; }
    final void theend() { return; }
    protected void safe() { return; }
}

abstract class B {
    abstract void foo()
}