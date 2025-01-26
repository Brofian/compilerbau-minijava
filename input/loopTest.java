package de.looptest;

class LoopTest{

    public static void runTests() {
        int a = 0;
        while(2 > 1){
            a = 1 + 2;
        }

        for(int i = 0; i < 45; i = i + 1){
            a = 2 + 1;
        }

        do {
            a = 3;
        } while(2 > 1);


        return;
    }
}
