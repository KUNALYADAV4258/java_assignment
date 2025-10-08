import java.util.Scanner;

class Account {
    int accNo;
    String name;
    double balance;
    String email;
    String phone;
    
    static int nextNo = 1001;
    
    Account(String n, double amt, String e, String p) {
        accNo = nextNo;
        nextNo++;
        name = n;
        balance = amt;
        email = e;
        phone = p;
    }
    
    void deposit(double amt) {
        if (amt > 0) {
            balance = balance + amt;
            System.out.println("Money added. Balance: " + balance);
        } else {
            System.out.println("Invalid amount");
        }
    }
    
    void withdraw(double amt) {
        if (amt <= 0) {
            System.out.println("Invalid amount");
        } else if (amt > balance) {
            System.out.println("Not enough money. Balance: " + balance);
        } else {
            balance = balance - amt;
            System.out.println("Money taken. Balance: " + balance);
        }
    }
    
    void show() {
        System.out.println("Account Number: " + accNo);
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }
    
    void updateContact(String e, String p) {
        email = e;
        phone = p;
        System.out.println("Contact updated");
    }
    
    int getAccNo() {
        return accNo;
    }
}

class BankApp {
    Account[] accounts = new Account[100];
    int count = 0;
    Scanner sc = new Scanner(System.in);
    
    void createAccount() {
        if (count >= 100) {
            System.out.println("Cannot create more accounts");
            return;
        }
        
        System.out.println("Create New Account");
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        
        System.out.print("Enter starting money: ");
        double money = sc.nextDouble();
        sc.nextLine();
        
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        
        Account acc = new Account(name, money, email, phone);
        accounts[count] = acc;
        count++;
        
        System.out.println("Account created. Number: " + acc.getAccNo());
    }
    
    void depositMoney() {
        if (count == 0) {
            System.out.println("No accounts");
            return;
        }
        
        System.out.print("Enter account number: ");
        int no = sc.nextInt();
        sc.nextLine();
        
        Account acc = findAccount(no);
        if (acc == null) {
            System.out.println("Account not found");
            return;
        }
        
        System.out.print("Enter amount to deposit: ");
        double amt = sc.nextDouble();
        sc.nextLine();
        
        acc.deposit(amt);
    }
    
    void withdrawMoney() {
        if (count == 0) {
            System.out.println("No accounts");
            return;
        }
        
        System.out.print("Enter account number: ");
        int no = sc.nextInt();
        sc.nextLine();
        
        Account acc = findAccount(no);
        if (acc == null) {
            System.out.println("Account not found");
            return;
        }
        
        System.out.print("Enter amount to withdraw: ");
        double amt = sc.nextDouble();
        sc.nextLine();
        
        acc.withdraw(amt);
    }
    
    void showAccount() {
        if (count == 0) {
            System.out.println("No accounts");
            return;
        }
        
        System.out.print("Enter account number: ");
        int no = sc.nextInt();
        sc.nextLine();
        
        Account acc = findAccount(no);
        if (acc == null) {
            System.out.println("Account not found");
            return;
        }
        
        acc.show();
    }
    
    void updateContacts() {
        if (count == 0) {
            System.out.println("No accounts");
            return;
        }
        
        System.out.print("Enter account number: ");
        int no = sc.nextInt();
        sc.nextLine();
        
        Account acc = findAccount(no);
        if (acc == null) {
            System.out.println("Account not found");
            return;
        }
        
        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        
        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();
        
        acc.updateContact(email, phone);
    }
    
    Account findAccount(int no) {
        for (int i = 0; i < count; i++) {
            if (accounts[i].getAccNo() == no) {
                return accounts[i];
            }
        }
        return null;
    }
    
    void showMenu() {
        int choice;
        
        do {
            System.out.println("\nBank Menu");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account");
            System.out.println("5. Update Contacts");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            
            choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    showAccount();
                    break;
                case 5:
                    updateContacts();
                    break;
                case 6:
                    System.out.println("Thank you");
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        } while (choice != 6);
    }
    
    public static void main(String[] args) {
        BankApp app = new BankApp();
        app.showMenu();
    }
}