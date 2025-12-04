import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Cleaned and improved version of the handwritten LibrarySystem.
 * - Uses try-with-resources for file I/O
 * - Performs safe parsing of file lines
 * - Uses a single Scanner instance (avoids resource leak warnings)
 * - Auto-generates unique book IDs
 * - Better error messages and simple validation
 */
public class LibrarySystem {

    private static final Path BOOK_FILE = Paths.get("books.txt");
    private static final Path MEMBER_FILE = Paths.get("members.txt");

    // Domain classes
    public static class Book implements Comparable<Book> {
        final int bookId;
        String title;
        String author;
        String category;
        boolean isIssued;

        public Book(int bookId, String title, String author, String category, boolean isIssued) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.category = category;
            this.isIssued = isIssued;
        }

        public Book(int bookId, String title, String author, String category) {
            this(bookId, title, author, category, false);
        }

        public void markIssued() { isIssued = true; }
        public void markReturned() { isIssued = false; }

        public void displayBookDetails() {
            System.out.printf("ID: %d | Title: %s | Author: %s | Category: %s | Issued: %b%n",
                    bookId, title, author, category, isIssued);
        }

        @Override
        public int compareTo(Book o) {
            return this.title.compareToIgnoreCase(o.title);
        }
    }

    public static class Member {
        final int memberId;
        String name;

        public Member(int memberId, String name) {
            this.memberId = memberId;
            this.name = name;
        }
    }

    // In-memory stores
    private final Map<Integer, Book> books = new HashMap<>();
    private final Map<Integer, Member> members = new HashMap<>();

    // Load data from files (if present)
    public void loadFromFile() {
        // Ensure files exist
        try {
            if (!Files.exists(BOOK_FILE)) Files.createFile(BOOK_FILE);
            if (!Files.exists(MEMBER_FILE)) Files.createFile(MEMBER_FILE);
        } catch (IOException e) {
            System.err.println("Unable to create data files: " + e.getMessage());
            return;
        }

        // Load books
        try (BufferedReader br = Files.newBufferedReader(BOOK_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Expect format: id,title,author,category,isIssued
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue; // skip malformed line

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    String category = parts[3].trim();
                    boolean isIssued = Boolean.parseBoolean(parts[4].trim());
                    books.put(id, new Book(id, title, author, category, isIssued));
                } catch (NumberFormatException ignored) {
                    // skip bad line
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }

        // Load members
        try (BufferedReader br = Files.newBufferedReader(MEMBER_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Expect format: id,name
                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    members.put(id, new Member(id, name));
                } catch (NumberFormatException ignored) { }
            }
        } catch (IOException e) {
            System.err.println("Error reading members file: " + e.getMessage());
        }
    }

    // Save data to files
    public void saveToFile() {
        // Save books
        try (BufferedWriter bw = Files.newBufferedWriter(BOOK_FILE, StandardCharsets.UTF_8)) {
            for (Book b : books.values()) {
                // CSV: id,title,author,category,isIssued
                bw.write(String.format("%d,%s,%s,%s,%b",
                        b.bookId,
                        escapeCsv(b.title),
                        escapeCsv(b.author),
                        escapeCsv(b.category),
                        b.isIssued));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }

        // Save members
        try (BufferedWriter bw = Files.newBufferedWriter(MEMBER_FILE, StandardCharsets.UTF_8)) {
            for (Member m : members.values()) {
                bw.write(String.format("%d,%s", m.memberId, escapeCsv(m.name)));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    // Very small CSV escape (commas can break parsing). For the simple format used here,
    // replace commas with spaces. (Alternatively, use a proper CSV library.)
    private static String escapeCsv(String s) {
        return s == null ? "" : s.replace(",", " ");
    }

    // Generate next unique book id (1 + current max id)
    private int nextBookId() {
        return books.keySet().stream().max(Integer::compareTo).map(i -> i + 1).orElse(1);
    }

    // Generate next unique member id (1 + current max id)
    private int nextMemberId() {
        return members.keySet().stream().max(Integer::compareTo).map(i -> i + 1).orElse(1);
    }

    // User operations (all receive the shared Scanner to avoid multiple open scanners)
    public void addBook(Scanner sc) {
        sc.nextLine(); // consume leftover newline if present
        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = sc.nextLine().trim();

        int id = nextBookId();
        Book b = new Book(id, title, author, category);
        books.put(id, b);
        saveToFile();

        System.out.println("Added book with ID " + id);
    }

    public void addMember(Scanner sc) {
        System.out.print("Enter member name: ");
        sc.nextLine(); // consume leftover newline if present
        String name = sc.nextLine().trim();
        int id = nextMemberId();
        members.put(id, new Member(id, name));
        saveToFile();
        System.out.println("Added member with ID " + id);
    }

    public void issueBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        int bid = readInt(sc);
        System.out.print("Enter Member ID: ");
        int mid = readInt(sc);

        if (!books.containsKey(bid)) {
            System.out.println("Book ID not found.");
            return;
        }
        if (!members.containsKey(mid)) {
            System.out.println("Member ID not found.");
            return;
        }

        Book b = books.get(bid);
        if (b.isIssued) {
            System.out.println("Book is already issued.");
            return;
        }

        b.markIssued();
        saveToFile();
        System.out.println("Book issued successfully.");
    }

    public void returnBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        int bid = readInt(sc);
        Book b = books.get(bid);
        if (b == null) {
            System.out.println("Book ID not found.");
            return;
        }
        if (!b.isIssued) {
            System.out.println("Book is not currently issued.");
            return;
        }
        b.markReturned();
        saveToFile();
        System.out.println("Book returned successfully.");
    }

    public void searchBook(Scanner sc) {
        sc.nextLine(); // consume newline
        System.out.print("Enter Title to search: ");
        String title = sc.nextLine().trim();

        boolean found = false;
        for (Book b : books.values()) {
            if (b.title.equalsIgnoreCase(title)) {
                b.displayBookDetails();
                found = true;
            }
        }
        if (!found) System.out.println("Book not found.");
    }

    public void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the system.");
            return;
        }
        List<Book> sorted = new ArrayList<>(books.values());
        Collections.sort(sorted);
        for (Book b : sorted) {
            b.displayBookDetails();
            System.out.println("-------------------------------------------------");
        }
    }

    // Helper to safely read an integer from scanner (returns -1 on bad input)
    private int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next(); // discard bad token
        }
        return sc.nextInt();
    }

    // Main menu
    public static void main(String[] args) {
        LibrarySystem sys = new LibrarySystem();
        sys.loadFromFile();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== City Library Digital Management System ===");
                System.out.println("1. Add Book");
                System.out.println("2. Add Member");
                System.out.println("3. Issue Book");
                System.out.println("4. Return Book");
                System.out.println("5. List Books");
                System.out.println("6. Search Book by Title");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = -1;
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                } else {
                    sc.next(); // discard invalid token
                }

                switch (choice) {
                    case 1 -> sys.addBook(sc);
                    case 2 -> sys.addMember(sc);
                    case 3 -> sys.issueBook(sc);
                    case 4 -> sys.returnBook(sc);
                    case 5 -> sys.listBooks();
                    case 6 -> sys.searchBook(sc);
                    case 7 -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Enter 1-7.");
                }
            }
        }
    }
}
