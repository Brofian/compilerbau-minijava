package superTest;
class Parent {
    String message = "Hello from Parent";

    // Parent constructor
    Parent(String name) {
        System.out.println("Parent Constructor: " + name);
    }

    void showMessage() {
        System.out.println("Parent method: " + message);
    }
}

class Child extends Parent {
    String message = "Hello from Child";

    // Child constructor
    Child(String name) {
        super(name); // Super constructor call
        System.out.println("Child Constructor: " + name);
    }

    void showMessage() {
        super.showMessage(); // Super method call
        System.out.println("Child method: " + message);
    }

    void printParentMessage() {
        System.out.println("Accessing Parent field: " + super.message); // Super field access
    }
}