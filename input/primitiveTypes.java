package de.primitivetypes;

public class primitiveTypes {
    public void testTypes() {
        // Integer type
        int a = 5;
        int b = 10;
        int c = a + b;
        int negative = -4;

        // Boolean type
        boolean flag1 = true;
        boolean flag2 = false;
        boolean flag3 = flag1 && flag2;

        // Byte type
        byte byteVar1 = 127b;
        byte byteVar2 = 0b;
        byte byteVar3 = 12b + 24b;

        // Short type
        short shortVar1 = 32767s;
        short shortVar2 = 0s;
        short shortVar3 = 15s % 4s;

        // Long type
        long longVar1 = 100000L;
        long longVar2 = 0L;
        long longVar3 = 36l / 4L;

        // Float type
        float floatVar1 = 3.14f;
        float floatVar2 = 0.0f;
        float floatVar3 = 100.1f - 32.4f;

        // Double type
        double doubleVar1 = 3.141592653589793;
        double doubleVar2 = 0.0;
        double doubleVar3 = 123.456 * 78.9;

        // Char type
        char charVar1 = 'A';
        char charVar2 = 'B';
        char charVar3 = 'C' + 'D';
        return;
    }
}