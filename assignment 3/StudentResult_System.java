import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// ----------------- CUSTOM EXCEPTION -----------------
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

// ----------------- STUDENT CLASS -----------------
class Student {
    private int rollNumber;
    private String studentName;
    private int[] marks;

    public Student(int rollNumber, String studentName, int[] marks) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.marks = marks;
    }

    // Validate marks (0 to 100)
    public void validateMarks() throws InvalidMarksException {
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] < 0 || marks[i] > 100) {
                throw new InvalidMarksException(
                        "Invalid Marks in Subject " + (i + 1) + " (Allowed: 0–100)"
                );
            }
        }
    }

    public double calculateAverage() {
        int sum = 0;
        for (int m : marks) sum += m;
        return sum / (double) marks.length; // use marks.length instead of hardcoded 3
    }

    public void displayResult() {
        System.out.println("\n----- Student Result -----");
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Name: " + studentName);
        System.out.println("Marks:");
        for (int i = 0; i < marks.length; i++) {
            System.out.println("  Subject " + (i + 1) + ": " + marks[i]);
        }

        String status = (calculateAverage() >= 40) ? "PASS" : "FAIL";
        System.out.printf("Average: %.2f%n", calculateAverage());
        System.out.println("Result: " + status);
        System.out.println("--------------------------\n");
    }

    public int getRollNumber() {
        return rollNumber;
    }
}

// ----------------- RESULT MANAGER -----------------
class ResultManager {
    private static final int SUBJECT_COUNT = 3; // change here if you want more subjects
    private final List<Student> students = new ArrayList<>();

    // Add Student
    public void addStudent(Scanner sc) {
        try {
            System.out.print("Enter Roll Number: ");
            int roll = readInt(sc);

            // Check for duplicate roll number
            if (findStudentByRoll(roll) != null) {
                System.out.println("A student with this roll number already exists. Try again.");
                return;
            }

            sc.nextLine(); // consume endline before reading name
            System.out.print("Enter Student Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Student not added.");
                return;
            }

            int[] marks = new int[SUBJECT_COUNT];
            for (int i = 0; i < SUBJECT_COUNT; i++) {
                while (true) {
                    System.out.print("Enter marks for Subject " + (i + 1) + " (0-100): ");
                    try {
                        int mark = readInt(sc);
                        if (mark < 0 || mark > 100) {
                            System.out.println("Marks must be between 0 and 100. Try again.");
                            continue;
                        }
                        marks[i] = mark;
                        break;
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input. Please enter an integer (0-100).");
                        sc.nextLine(); // clear bad token
                    }
                }
            }

            Student s = new Student(roll, name, marks);
            // extra validation (redundant here but good demonstration)
            s.validateMarks();
            students.add(s);

            System.out.println("Student added successfully!");

        } catch (InvalidMarksException e) {
            // This should not happen because we validated each mark while reading,
            // but keeping it is good for demonstration.
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input! Please enter numeric values where required.");
            sc.nextLine(); // clear buffer
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        } finally {
            System.out.println("addStudent() execution completed.\n");
        }
    }

    // Show details of a student by roll number
    public void showStudentDetails(Scanner sc) {
        try {
            System.out.print("Enter roll number to search: ");
            int roll = readInt(sc);

            Student found = findStudentByRoll(roll);
            if (found != null) {
                found.displayResult();
            } else {
                System.out.println("Student NOT FOUND!");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input! Enter a valid roll number.");
            sc.nextLine();
        } finally {
            System.out.println("showStudentDetails() execution completed.\n");
        }
    }

    private Student findStudentByRoll(int roll) {
        for (Student s : students) {
            if (s.getRollNumber() == roll) return s;
        }
        return null;
    }

    // Utility to read an int safely (throws InputMismatchException on bad token)
    private int readInt(Scanner sc) throws InputMismatchException {
        return sc.nextInt();
    }

    // Menu
    public void mainMenu() {
        int choice = 0;

        // Use try-with-resources to ensure Scanner is closed at the end
        try (Scanner sc = new Scanner(System.in)) {
            do {
                System.out.println("\n======= STUDENT RESULT MANAGEMENT SYSTEM =======");
                System.out.println("1. Add Student");
                System.out.println("2. Show Student Details");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = readInt(sc);

                    switch (choice) {
                        case 1 -> addStudent(sc);
                        case 2 -> showStudentDetails(sc);
                        case 3 -> System.out.println("Exiting...");
                        default -> System.out.println("Invalid Choice! Try again.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Please enter only numeric choices.");
                    sc.nextLine(); // clear buffer
                }

            } while (choice != 3);
        } // scanner closed here
    }

    public static void main(String[] args) {
        ResultManager manager = new ResultManager();
        manager.mainMenu();
    }
}
