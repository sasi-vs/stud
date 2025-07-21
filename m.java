import java.sql.*;
import java.util.Scanner;

public class m {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root"; // Replace with your MySQL username
    static final String PASS = "sasi"; // Replace with your MySQL password

    static Connection conn;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
            System.out.println("Connected to Database");

            while (true) {
                System.out.println("\n--- Student Database Menu ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Search Student");
                System.out.println("6. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                try {
                    switch (choice) {
                        case 1:
                            addStudent();
                            break;
                        case 2:
                            viewStudents();
                            break;
                        case 3:
                            updateStudent();
                            break;
                        case 4:
                            deleteStudent();
                            break;
                        case 5:
                            searchStudent();
                            break;
                        case 6:
                            conn.close();
                            System.out.println("Disconnected. Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addStudent() throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter department: ");
        String dept = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        String sql = "INSERT INTO students (name, department, age) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, dept);
        stmt.setInt(3, age);
        stmt.executeUpdate();
        System.out.println("Student added.");
    }

    static void viewStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nID | Name | Department | Age");
        while (rs.next()) {
            System.out.printf("%d | %s | %s | %d\n",
                rs.getInt("id"), rs.getString("name"),
                rs.getString("department"), rs.getInt("age"));
        }
    }

    static void updateStudent() throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New name: ");
        String name = sc.nextLine();
        System.out.print("New department: ");
        String dept = sc.nextLine();
        System.out.print("New age: ");
        int age = sc.nextInt();
        sc.nextLine();

        String sql = "UPDATE students SET name = ?, department = ?, age = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, dept);
        stmt.setInt(3, age);
        stmt.setInt(4, id);
        int rows = stmt.executeUpdate();
        if (rows > 0) System.out.println("Student updated.");
        else System.out.println("Student not found.");
    }

    static void deleteStudent() throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();
        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int rows = stmt.executeUpdate();
        if (rows > 0) System.out.println("Student deleted.");
        else System.out.println("Student not found.");
    }

    static void searchStudent() throws SQLException {
        System.out.print("Search by name: ");
        String name = sc.nextLine();
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + name + "%");
        ResultSet rs = stmt.executeQuery();

        System.out.println("\nID | Name | Department | Age");
        while (rs.next()) {
            System.out.printf("%d | %s | %s | %d\n",
                rs.getInt("id"), rs.getString("name"),
                rs.getString("department"), rs.getInt("age"));
        }
    }
}
