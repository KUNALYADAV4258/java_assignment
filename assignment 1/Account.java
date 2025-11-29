class Account
{
    int account_number;
    String holder_name;
    double balance;
    String email;
    String contact;

    static int newaccount_no = 214;

    Account(String name)
    {
        double first_deposit = 0;
        this.account_number=newaccount_no++;
        this.holder_name=name;
        this.balance=first_deposit;
        this.email=email;
        this.contact=contact;
    }

    void deposit(double amount)
    {
     if(amount > 0)
     {
         balance += amount;
         System.out.println("deposit done! new balance: RS" + balance);
     } else{
         System.out.println("error 101");
     }
    }
    void withdraw(double amount)
    {
        if (amount <= 0)
        {
            System.out.println("error withdraw amount not entered");
        }else if (amount > balance)
        {
            System.out.println("error amount insufficient");
        }else
        {
          balance -= amount;
            System.out.println("withdraw succesfull! new balance: RS" + balance);
        }
    }

    void displayAccountDetails() {
        System.out.println("ACCOUNT DETAILS");
        System.out.println("Account Number:    " + account_number);
        System.out.println("Account Holder:    " + holder_name);
        System.out.println("Balance:           RS" + balance);
        System.out.println("Email:             " + email);
        System.out.println("Phone Number:      " + contact);
    }
    
}
 class BankApp{
    public static void main(String[] args) {
        Account acc1 = new Account("Bhragender kumar singh");
        acc1.deposit(5000);
        acc1.withdraw(2000);
        acc1.contact="9876543210";
        acc1.email="bhargender06@gmail.com";
        acc1.displayAccountDetails();
    }
 }
              