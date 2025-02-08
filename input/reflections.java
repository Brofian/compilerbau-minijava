package de.reflections;

class ReflectionTest {

    public String getTestString() {
        String text = "Hello world";

        // this requires interfaces, as the parameter of method contains is of type CharSequence
        // boolean b0 = text.contains("world");

        boolean b1 = text.startsWith("world");
        boolean b2 = text.startsWith("world", 2);

        return text.substring(1, 3);
    }

}