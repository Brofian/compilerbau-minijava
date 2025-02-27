package de.operands;

public class Operands {
    int count;

    public void test() {

        while (true) {
            this.count = 3;
            this.count = (this.count + 1);
            int[] arr = new int[3];

            int a = 0;
            if (this.count < 2) {
                arr[0] = this.count;
            } else if (this.count > 10) {
                arr[1] = this.count;
            }
            else {
                arr[2] = this.count;
            }
        }
    }

    public static void main(String[] args) {

    }
}