import java.io.*;
import java.util.*;

// Interface for Library Items
interface LibraryItem {
    String getTitle();

    int getQuantity();

    void decreaseQuantity();

    void increaseQuantity();
}

// Class representing a Book
class Book implements LibraryItem {
    private String title;
    private int quantity;

    public Book(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    @Override
    public void increaseQuantity() {
        quantity++;
    }
}

// Class representing a Library Member
class LibraryMember {
    private String name;
    private List<Book> issuedBooks;

    public LibraryMember(String name) {
        this.name = name;
        issuedBooks = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public List<Book> getIssuedBooks() {
        return issuedBooks;
    }
    public void issueBook(Book book) {
        issuedBooks.add(book);
    }

    public void returnBook(Book book) {
        issuedBooks.remove(book);
    }
}

// Class representing the Library
class Library {
    private List<LibraryItem> items;
    private List<LibraryMember> members;
    private Scanner scanner;

    public Library() {
        items = new ArrayList<>();
        members = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    // Method for adding a Library Item
    public void addItem(LibraryItem item) {
        items.add(item);
    }

    // Method for adding a Library Member
    public void addMember(LibraryMember member) {
        members.add(member);
    }

    // Method for saving Books to a file
    public void saveBooksToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (LibraryItem item : items) {
                if (item instanceof Book) {
                    Book book = (Book) item;
                    writer.println("Book," + book.getTitle() + "," + book.getQuantity());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for loading Books from a file
    public void loadBooksFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals("Book")) {
                    String title = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    LibraryItem item = new Book(title, quantity);
                    addItem(item);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // Method for saving Members to a file
    public void saveMembersToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (LibraryMember member : members) {
                writer.println("Member," + member.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for loading Members from a file
    public void loadMembersFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals("Member")) {
                    String name = parts[1];
                    LibraryMember member = new LibraryMember(name);
                    addMember(member);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method for adding a Book to the Library
    public void addBook(String title, int quantity) {
        Book newBook = new Book(title, quantity);
        addItem(newBook);
        System.out.println("Book added to the library.");
    }

    // Method for adding a Member to the Library
    public void addMember(String name) {
        LibraryMember newMember = new LibraryMember(name);
        addMember(newMember);
        System.out.println("Member added to the library.");
    }

    // Method for displaying the menu
    public void displayMenu() {
        System.out.println("Welcome to the Library Management System");
        System.out.println("1. Issue a Book");
        System.out.println("2. Return a Book");
        System.out.println("3. Display Members");
        System.out.println("4. Add Book to the Library");
        System.out.println("5. Add Member to the Library");
        System.out.println("6. Display Library Items");
        System.out.println("7. Exit");
    }

    // Method for starting the library management system
    public void start() {
        int choice = 0;
        while (choice != 7) {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the member name: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Enter the book title to issue: ");
                    String issueBookTitle = scanner.nextLine();
                    issueBook(memberName, issueBookTitle);
                    break;
                case 2:
                    System.out.print("Enter the member name: ");
                    String returnMemberName = scanner.nextLine();
                    System.out.print("Enter the book title to return: ");
                    String returnBookTitle = scanner.nextLine();
                    returnBook(returnMemberName, returnBookTitle);
                    break;
                case 3:
                    displayMembers();
                    break;
                case 4:
                    addLibraryItem();
                    break;
                case 5:
                    addMemberToLibrary();
                    break;
                case 6:
                    displayLibraryItems();
                    break;
                case 7:
                    System.out.println("Exiting... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method for issuing a book to a member
    public void issueBook(String memberName, String bookTitle) {
        LibraryMember member = findMemberByName(memberName);
        Book book = findBookByTitle(bookTitle);

        if (member == null) {
            System.out.println("Member not found. Please check the name.");
            return;
        }

        if (book == null) {
            System.out.println("Book not found. Please check the title.");
            return;
        }

        if (book.getQuantity() > 0) {
            member.issueBook(book);
            book.decreaseQuantity();
            System.out.println("Book '" + book.getTitle() + "' issued to " + member.getName());
        } else {
            System.out.println("The book '" + book.getTitle() + "' is currently out of stock.");
        }
    }

    // Method for returning a book from a member
    public void returnBook(String memberName, String bookTitle) {
        LibraryMember member = findMemberByName(memberName);
        Book book = findBookByTitle(bookTitle);

        if (member == null) {
            System.out.println("Member not found. Please check the name.");
            return;
        }

        if (book == null) {
            System.out.println("Book not found. Please check the title.");
            return;
        }

        if (member.getIssuedBooks().contains(book)) {
            member.returnBook(book);
            book.increaseQuantity();
            System.out.println("Book '" + book.getTitle() + "' returned by " + member.getName());
        } else {
            System.out.println("Book was not issued by " + member.getName());
        }
    }

    // Method for adding a Library Item
    public void addLibraryItem() {
        System.out.print("Enter the title of the book: ");
        String title = scanner.nextLine();
        System.out.print("Enter the quantity of the book: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        LibraryItem newBook = new Book(title, quantity);
        addItem(newBook);
        System.out.println("Book added to the library.");
    }

    // Method for adding a Member to the Library
    public void addMemberToLibrary() {
        System.out.print("Enter the name of the member: ");
        String name = scanner.nextLine();
        addMember(name);
    }

    // Method for finding a Member by name
    public LibraryMember findMemberByName(String name) {
        for (LibraryMember member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }

    // Method for finding a Book by title
    public Book findBookByTitle(String title) {
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getTitle().equalsIgnoreCase(title)) {
                    return book;
                }
            }
        }
        return null;
    }

    // Method for displaying all Members
    public void displayMembers() {
        System.out.println("Library Members:");
        for (LibraryMember member : members) {
            System.out.println("Member Name: " + member.getName());
        }
    }

    // Method for displaying all Library Items
    public void displayLibraryItems() {
        System.out.println("Library Items:");
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                System.out.println("Book Title: " + book.getTitle() + ", Quantity: " + book.getQuantity());
            }
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        Library library = new Library();

        // Loading books and members from files
        library.loadBooksFromFile("books.txt");
        library.loadMembersFromFile("members.txt");

        // Starting the library management system
        library.start();

        // Saving books and members to files before exiting
        library.saveBooksToFile("books.txt");
        library.saveMembersToFile("members.txt");
    }
}