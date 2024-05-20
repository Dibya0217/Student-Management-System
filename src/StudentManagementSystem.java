import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagementSystem {
//    Database Connection
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Dibya@2002";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Register the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

//        Establish the Connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("WELCOME TO J SPIDER");
                System.out.println("1. Add Student");
                System.out.println("2. Update Student");
                System.out.println("3. Delete Student");
                System.out.println("4. Show Student Details");
                System.out.println("5. Show Students");
                System.out.println("0. Exit");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Chose an option : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(connection, scanner);
                        break;

                    case 2:
                        updateStudent(connection, scanner);
                        break;

                    case 3:
                        deleteStudent(connection, scanner);
                        break;

                    case 4:
                        viewStudent(connection);
                        break;

                    case 5:
                        viewStudents(connection, scanner);
                        break;

                    case 0:
                        exit();
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
