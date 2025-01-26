package de.example;

import java.lang.String;

class HelloWorld {
	int value;

    public static void test() {
		HelloWorld obj = new HelloWorld();
		obj.foo();
		// obj.value; // TODO: parser confuses MethodCall with ClassAccess
		return;
    }

	public void foo() {
		return;
	}
}