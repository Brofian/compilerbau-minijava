package mypackage;


public class ObjectTest extends Object{

    int sum = 0;

    public class A{
        int a = 5;
    }

    public class B{
        int b = 2;
    }

    private A objA = new A();
    private B objB = new B();

    public ObjectTest(){
        this.sum = this.objA.a + this.objB.b;
    }

}