import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    /**
     * Creates a new DatabaseManager instance with the given database connection details.
     *
     * @param jdbcUrl      The JDBC URL of the database.
     * @param jdbcUsername The username for the database connection.
     * @param jdbcPassword The password for the database connection.
     */
    public DatabaseManager(String jdbcUrl, String jdbcUsername, String jdbcPassword) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param fullName = The full name of the user.
     * @param email = The email of the user.
     * @param password = The password of the user.
     * @param idNumber = The ID number of the user.
     * @param type = The user type.
     * @param cesPoints = The CES points of the user.
     */
    public void insertUserData(String fullName, String email, String password, int idNumber, String type, int cesPoints) {
        try (Connection dbConnection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO users (userName, userEmail, userPassword, userIDNumber, userType, userCESPoints) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, idNumber);
            preparedStatement.setString(5, type);
            preparedStatement.setInt(6, cesPoints);
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new SQLException("Failed to insert user into the database.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to insert user into the database.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAdminEmail(String email) {
        try (Connection dbConnection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM admins WHERE adminEmail = ?");
            statement.setString(1,email);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Fetches user data from the database and returns the result set.
     *
     * @return The result set containing user data.
     */
    public ResultSet fetchUserData() {
        try (Connection dbConnection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            Statement statement = dbConnection.createStatement();
            return statement.executeQuery("SELECT * FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}