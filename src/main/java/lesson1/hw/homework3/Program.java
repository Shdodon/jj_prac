package lesson1.hw.homework3;

import java.sql.*;

public class Program {
    public static void main(String[] args) throws SQLException {

               try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {


            // CREATE table Student
            createTable(connection);

            // INSERT INTO Student
            insertData(connection);

            // SELECT * FROM Student
            readAllData(connection);

            // DELETE FROM Student WHERE ... (Generic field value)
            deleteRecordByFieldValue(connection,
                    "id", 2);

            // UPDATE Student SET ... WHERE ... (Generic destination & filter fields value)
            updateRecordByFieldValue(connection,
                    "firstname", "John", "secondname", "Kuznetsov");

            readAllData(connection);

            // SELECT * FROM Student WHERE ... (Generic filter field value)
            readDataByFilter(connection, "id", "<", 4);
        }
    }

    private static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Student");
            boolean res = statement.execute("""
                    create table Student (
                    id bigint,
                    firstname varchar (256),
                    secondname varchar (256),
                    age int
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertData(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            int res = statement.executeUpdate("""
                    insert into Student(id, firstname, secondname, age) values
                    (1, 'Petro', 'Mia', 20),
                    (2, 'Leo', 'Viao', 17),
                    (3, 'Catre', 'Rumnum', 19),
                    (4, 'Loa', 'Soyan', 25)
                    """);

            System.out.println("INSERT, rows affected: " + res);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readAllData(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from Student");
            printDBResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void readDataByFilter(Connection connection, String fieldName, String operator, T filterValue) {
        try (PreparedStatement pStatement = connection
                .prepareStatement("select * from Student where " + fieldName + " " + operator + " ?")) {

            switch (filterValue.getClass().getSimpleName()) {
                case "String" -> pStatement.setString(1, (String) filterValue);
                case "Long", "Integer" -> pStatement.setLong(1, (int) filterValue);
                default -> throw new RuntimeException("Unexpected type of filterValue parameter!");
            }

            printDBResultSet(pStatement.executeQuery());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printDBResultSet(ResultSet resultSet) throws SQLException {

        while (resultSet.next()) {
            System.out.println("" +
                    "id #" + resultSet.getLong("id") + ": "
                    + "firstName = " + resultSet.getString("firstname") + ", "
                    + "secondName = " + resultSet.getString("secondname") + ", "
                    + "Age = " + resultSet.getInt("age") + ";"
            );
        }
        System.out.println();
    }

    private static <T> void deleteRecordByFieldValue(Connection connection, String fieldName, T valueToDelete) {
        try (PreparedStatement pStatement = connection.prepareStatement("delete from Student where " + fieldName + " = ?")) {

            switch (valueToDelete.getClass().getSimpleName()) {
                case "String" -> pStatement.setString(1, (String) valueToDelete);
                case "Long", "Integer" -> pStatement.setLong(1, (int) valueToDelete);
                default -> throw new RuntimeException("Unexpected type of valueToDelete parameter!");
            }
            int res = pStatement.executeUpdate();

            System.out.println("DELETE, rows affected: " + res);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T, U> void updateRecordByFieldValue(Connection connection, String filterFieldName, T filterValue, String changeFieldName, U changeValue) {
        try (PreparedStatement pStatement = connection.prepareStatement("update Student set "
                + changeFieldName + " = ? where "
                + filterFieldName + " = ?")) {

            switch (changeValue.getClass().getSimpleName()) {
                case "String" -> pStatement.setString(1, (String) changeValue);
                case "Long", "Integer" -> pStatement.setLong(1, (int) changeValue);
                default -> throw new RuntimeException("Unexpected type of changeValue parameter!");
            }

            switch (filterValue.getClass().getSimpleName()) {
                case "String" -> pStatement.setString(2, (String) filterValue);
                case "Long", "Integer" -> pStatement.setLong(2, (int) filterValue);
                default -> throw new RuntimeException("Unexpected type of filterValue parameter!");
            }

            int res = pStatement.executeUpdate();

            System.out.println("UPDATE, rows affected: " + res);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}