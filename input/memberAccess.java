package de.memberaccess;

class MemberAccess {
    static void main(String[] args) {

        // instance member access
        ClassA a = new ClassA();
        a.someMethod();

        // static method access
        ClassA.someStaticMethod();

        // method call of class access
        System.out.println("Hello world");

    }
}

class ClassA {

    static int someStaticMethod() {
        return 1;
    }

    int someMethod() {
        return 2;
    }
}