package de.operations;

class Operations{
        public static void main(String[] args) {
            // Arithmetic operators
            int a = 10;
            int b = 3;
            int sum = a + b; // Addition
            int difference = a - b; // Subtraction
            int product = a * b; // Multiplication
            int quotient = a / b; // Division
            int remainder = a % b; // Modulus

            // Comparison operators
            boolean isEqual = (a == b); // Equal to
            boolean isNotEqual = (a != b); // Not equal to
            boolean isLessThan = (a < b); // Less thans
            boolean isLessThanOrEqual = (a <= b); // Less than or equal to
            boolean isGreaterThan = (a > b); // Greater than
            boolean isGreaterThanOrEqual = (a >= b); // Greater than or equal to

            // Logical operators
            boolean x = true;
            boolean y = false;
            boolean andResult = x && y; // Logical AND
            boolean orResult = x || y; // Logical OR

            // Assignment operators
            int c = 5;
            c += 2; // Equivalent to c = c + 2
            c -= 1; // Equivalent to c = c - 1
            c *= 3; // Equivalent to c = c * 3
            c /= 2; // Equivalent to c = c / 2
            c %= 2; // Equivalent to c = c % 2

            // unary operatos
            // int inverse = -a;
            // boolean negation = !x;


            // Mixed example
            int result = (a + b) * (c - 1); // Using arithmetic operators
            boolean isResultValid = (result > 10) && (result < 50); // Using comparison and logical operators

        }

}