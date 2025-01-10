package mypackage;

public class ArrayTest{

    private int[] intArray = new int[5];
    private int[] expressionIntArray = new int[5 + 5 * 2];

    public ArrayTest(int index,int input){
        this.intArray[index] = input;
    }

    public int[] returnIntArr(){
        int[] temp = new int[1];
        temp[0] = 1;
        int[] res = new int[2];
        res[0] = temp[0];
        return temp;
    }

}