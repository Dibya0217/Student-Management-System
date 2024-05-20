import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class StudentManagementSystem {
    // Database Connection
    private static final String url = "jdbc:mysql://localhost:3306/student_db";
    private static final String username = "root";
    private static final String password = "Dibya@2002";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Register the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Establish the Connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("WELCOME TO J SPIDER");
                System.out.println("1. Add Student");
                System.out.println("2. Update Student");
                System.out.println("3. Delete Student");
                System.out.println("4. Show Student Details");
                System.out.println("5. View Students by Filter");
                System.out.println("0. Exit");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline left-over

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
                        viewStudentsByFilter(connection, scanner);
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
        } catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }

    public static void addStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Student Course: ");
            String course = scanner.nextLine();

            System.out.print("Enter Mock Rating: ");
            String mock = scanner.nextLine();

            System.out.print("Enter Fee: ");
            int fee = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            System.out.print("Enter Date Of Birth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine();
            LocalDate dateOfBirth = LocalDate.parse(dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.print("Enter Date Of Join (yyyy-MM-dd): ");
            String dojInput = scanner.nextLine();
            LocalDate dateOfJoin = LocalDate.parse(dojInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.print("Enter Gender (M, F, O): ");
            String gender = scanner.nextLine();

            String sql = "INSERT INTO student(name, course, mock, fee, dob, doj, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, course);
                preparedStatement.setString(3, mock);
                preparedStatement.setInt(4, fee);
                preparedStatement.setDate(5, java.sql.Date.valueOf(dateOfBirth));
                preparedStatement.setDate(6, java.sql.Date.valueOf(dateOfJoin));
                preparedStatement.setString(7, gender);

                int affectedRow = preparedStatement.executeUpdate();
                if (affectedRow > 0) {
                    System.out.println("Student added successfully.");
                } else {
                    System.out.println("Student not added successfully.");
                }
            }

            System.out.println("Student added successfully.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void updateStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Roll Number to Update: ");
            int rollNo = scanner.nextInt();
            scanner.nextLine();

            if (!studentExists(connection, rollNo)) {
                System.out.println("Not Found. Enter Correct Roll Number");
                return;
            }

            System.out.print("Enter New Student Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter New Student Course: ");
            String course = scanner.nextLine();

            System.out.print("Enter New Mock Rating: ");
            String mock = scanner.nextLine();

            System.out.print("Enter New Fee: ");
            int fee = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            System.out.print("Enter New Date Of Birth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine();
            LocalDate dateOfBirth = LocalDate.parse(dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.print("Enter New Date Of Join (yyyy-MM-dd): ");
            String dojInput = scanner.nextLine();
            LocalDate dateOfJoin = LocalDate.parse(dojInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.print("Enter New Gender (M, F, O): ");
            String gender = scanner.nextLine();

            String sql = "UPDATE student SET name = ?, course = ?, mock = ?, fee = ?, dob = ?, doj = ?, gender = ? WHERE roll_no = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, course);
                preparedStatement.setString(3, mock);
                preparedStatement.setInt(4, fee);
                preparedStatement.setDate(5, Date.valueOf(dateOfBirth));
                preparedStatement.setDate(6, Date.valueOf(dateOfJoin));
                preparedStatement.setString(7, gender);
                preparedStatement.setInt(8, rollNo);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Student updated successfully.");
                } else {
                    System.out.println("Update failed.");
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static boolean studentExists(Connection connection, int rollNo) {
        String sql = "SELECT roll_no FROM student WHERE roll_no = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rollNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void deleteStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Roll Number to Delete: ");
            int rollNo = scanner.nextInt();
            scanner.nextLine();

            if (!studentExists(connection, rollNo)) {
                System.out.println("Not Found. Enter Correct Roll Number");
                return;
            }

            String sql = "DELETE FROM student WHERE roll_no = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, rollNo);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Student deleted successfully.");
                } else {
                    System.out.println("Delete failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    public static void viewStudent(Connection connection) {
        String sql = "SELECT roll_no, name, course, mock, fee, dob, doj, gender FROM student";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Current Students:");
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");
            System.out.println("| Roll Number  | Name                 | Course              | Mock Rating    | Fee        | Date of Birth| Date Of Join | Gender |");
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");

            while (resultSet.next()) {
                int rollNo = resultSet.getInt("roll_no");
                String name = resultSet.getString("name");
                String course = resultSet.getString("course");
                String mock = resultSet.getString("mock");
                int fee = resultSet.getInt("fee");
                Date dob = resultSet.getDate("dob");
                Date doj = resultSet.getDate("doj");
                String gender = resultSet.getString("gender");

                // Format and display the student data in a table-like format
                System.out.printf("| %-12d | %-20s | %-19s | %-14s | %-10d | %-12s | %-12s | %-6s |\n", rollNo, name, course, mock, fee, dob.toString(), doj.toString(), gender);
            }
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewStudentsByFilter(Connection connection, Scanner scanner) {
        System.out.println("Choose a filter criterion:");
        System.out.println("1. Name");
        System.out.println("2. Roll Number");
        System.out.println("3. Course");
        System.out.println("4. Mock Rating");
        System.out.println("5. Fee");
        System.out.println("6. Date of Birth (yyyy-MM-dd)");
        System.out.println("7. Date of Join (yyyy-MM-dd)");
        System.out.println("8. Gender");

        int filterChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over
        String sql = "";
        String input = "";

        switch (filterChoice) {
            case 1:
                System.out.print("Enter Name: ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE name = ?";
                break;
            case 2:
                System.out.print("Enter Roll Number: ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE roll_no = ?";
                break;
            case 3:
                System.out.print("Enter Course: ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE course = ?";
                break;
            case 4:
                System.out.print("Enter Mock Rating: ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE mock = ?";
                break;
            case 5:
                System.out.print("Enter Fee: ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE fee = ?";
                break;
            case 6:
                System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE dob = ?";
                break;
            case 7:
                System.out.print("Enter Date of Join (yyyy-MM-dd): ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE doj = ?";
                break;
            case 8:
                System.out.print("Enter Gender (M, F, O): ");
                input = scanner.nextLine();
                sql = "SELECT * FROM student WHERE gender = ?";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, input);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Filtered Students:");
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");
            System.out.println("| Roll Number  | Name                 | Course              | Mock Rating    | Fee        | Date of Birth| Date Of Join | Gender |");
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");

            while (resultSet.next()) {
                int rollNo = resultSet.getInt("roll_no");
                String name = resultSet.getString("name");
                String course = resultSet.getString("course");
                String mock = resultSet.getString("mock");
                int fee = resultSet.getInt("fee");
                Date dob = resultSet.getDate("dob");
                Date doj = resultSet.getDate("doj");
                String gender = resultSet.getString("gender");

                // Format and display the student data in a table-like format
                System.out.printf("| %-12d | %-20s | %-19s | %-14s | %-10d | %-12s | %-12s | %-6s |\n",
                        rollNo, name, course, mock, fee, dob.toString(), doj.toString(), gender);
            }
            System.out.println("+--------------+----------------------+---------------------+----------------+------------+--------------+--------------+--------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Existing System");
        int i = 5;
        while(i != 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("!!!!! Thank You !!!!!");
    }
}
