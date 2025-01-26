package de.looptest;

class LoopTest{

    public static void runTests() {
        int a = 0;
        while(a < 10){
            a = a + 1;
        }

        for(int i = 0; i < 42; i = i + 1){
            a = i;
        }

        do {
            a = a - 1;
        } while(a > 1);


        return;
    }
}
