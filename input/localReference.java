package de.localreference;

class LocalReference {

    public int dynamicProperty = 1;

    public static int staticMethod() {
        return 2;
    }

    public int dynamicMethod() {
        return 3;
    }

    public int main() {
        // use implicit this
        int a = dynamicProperty + dynamicMethod() + staticMethod();
        // use explicit this
        int b = this.dynamicProperty + this.dynamicProperty + this.staticMethod();
        // call static method via class name
        return a + b + LocalReference.staticMethod();
    }

}