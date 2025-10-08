public class calculator {
    int add(int a, int b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }   

    double add(double a, double b) {
        return a + b;
    }

    int sub(int a, int b) {
        return a - b;
    }

    int sub(int a, int b, int c) {
        return a - b - c;
    }

    double sub(double a, double b) {
        return a - b;
    }

    int multiply(int a, int b) {
        return a * b;
    }

    int multiply(int a, int b, int c) {
        return a * b * c;
    }

    double multiply(double a, double b) {
        return a * b;
    }

    int divide(int a, int b) {
        if (b == 0) {
            System.out.println("Denominator should be greater than zero");
            return 0;
        }
        return a / b;
    }

    double divide(double a, double b) {
        if (b == 0.0) {
            System.out.println("Denominator should be greater than zero");
            return 0.0;
        }
        return a / b;
    }

    public static void main(String[] args) {
        calculator obj = new calculator();

        System.out.println("Add two ints: " + obj.add(5, 10));
        System.out.println("Add three ints: " + obj.add(5, 10, 15));
        System.out.println("Add two doubles: " + obj.add(5.0, 10.0));
        System.out.println("Subtract two ints: " + obj.sub(20, 12));
        System.out.println("Subtract two doubles: " + obj.sub(20.5, 10.5));
        System.out.println("Multiply two ints: " + obj.multiply(2, 3));
        System.out.println("Multiply three ints: " + obj.multiply(2, 3, 4));
        System.out.println("Divide two ints: " + obj.divide(10, 2));
        System.out.println("Divide two doubles: " + obj.divide(10.0, 2.0));
    }
}
