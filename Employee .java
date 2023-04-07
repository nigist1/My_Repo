
import java.util.Scanner;

class Employee {
    // instance variable creation
    private String firstName;
    private String fatherName;
    private int idNumber;
    private double monthlySalary;

    // constuctor initialization
    public Employee(String fname, String faname, int idno, double msalary) {
        firstName = fname;
        fatherName = faname;
        idNumber = idno;
        monthlySalary = msalary;
    }

    // set and get method for each intsance variable
    public void setfirstName(String fname) {
        firstName = fname;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setfatherName(String faname) {
        fatherName = faname;
    }

    public String getfatherName() {
        return fatherName;
    }

    public void setIdNumber(int idno) {
        idNumber = idno;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setMonthlySalary(double msalary) {
        monthlySalary = msalary;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    // if the salary is not positive,set it to 0.0
    public void setmonthlySalary(double m) {
        if (m < 0.0) {
            monthlySalary = 0.0;
        } else
            monthlySalary = m;
    }
}

class EmployeeTest {
    public static void main(String[] args) {
        Scanner S = new Scanner(System.in);
        System.out.println("enter your first name:");
        String fname = S.next();
        System.out.println("enter your father name:");
        String faname = S.next();
        System.out.println("enter your id number:");
        int idno = S.nextInt();
        System.out.println("enter your monthly salary:");
        double msalary = S.nextDouble();
        Employee e1 = new Employee(fname, faname, idno, msalary);
        System.out.println("Information about the employee are:" + e1.getfirstName() + " " + e1.getfatherName() + "\n"
                + e1.getIdNumber());
        System.out.println("yearly salary" + e1.getMonthlySalary() * 12);
        Employee e2 = new Employee(fname, faname, idno, msalary);
        double newsalary = e2.getMonthlySalary() * 0.5 + e2.getMonthlySalary();
        e2.setMonthlySalary(newsalary);
        System.out.println("Information about the employee are:" + e2.getfirstName() + " " + e2.getfatherName() + "\n"
                + e2.getIdNumber());
        System.out.println(" the new yearly salary" + e2.getMonthlySalary() * 12);
    }
}