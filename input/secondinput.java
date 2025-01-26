package de.secondinput;

public class MyProgram {

      private int count = 10;

      public MyProgram() {
            this.count = 0;
      }

      public int calculate(int a, int b) {
            if (a > b) {
                  return a - b;
            } else {
                  return a + b;
            }
      }

      public static void main(String[] args) {
            MyProgram prog = new MyProgram();
            // (new A()).getB().getNumber(); // this is valid if class A is defined

            int result = prog.calculate(5, 3);

            while(true){
                  this.count = this.count + 1;
                  int[] arr = new int[2];

                  /* TODO: BytecodeGenerator does not accept indexed array as lvalue
                  if(this.count < 2) {
                        arr[0] = this.count;
                  }else if(this.count > 10){
                        arr[1] = this.count;
                  }
                   */
            }
      }
}