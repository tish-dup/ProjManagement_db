import java.sql.*;
import java.util.ArrayList;

public class PersonRepo {

    // Constructor
    public PersonRepo() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        // Connect to the PoisePMS database, via the jdbc:mysql: channel on localhost
        // (this PC)
    }

    public void createPerson(Person newPerson) throws SQLException {
        String personSQLquery = "INSERT INTO Person VALUES (" + newPerson.id + ", '" + newPerson.name + "','"
                + newPerson.telNum + "', '" + newPerson.email + "', '" + newPerson.address + "')";
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");
        Statement statement = connection.createStatement();
        statement.executeUpdate(personSQLquery);
        connection.close();
    }

    public Person getPerson(int id) throws Exception {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");
        String SQLquery = "SELECT id, name, telNum, email, address FROM Person WHERE id=" + id;
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(SQLquery);

        while (results.next()) {
            Person person = new Person(results.getInt("id"), results.getString("name"), results.getString("telNum"),
                    results.getString("email"), results.getString("address"));
            return person;
        }

        throw new Exception("Could not be found person with id" + id);
    }

    public ArrayList<Person> getAll() throws SQLException {
        ArrayList<Person> allPerson = new ArrayList<Person>();
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");
        String SQLquery = "SELECT id, name, telNum, email, address FROM Person";
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(SQLquery);

        while (results.next()) {
            Person person = new Person(results.getInt("id"), results.getString("name"), results.getString("telNum"),
                    results.getString("email"), results.getString("address"));
            allPerson.add(person);
        }

        return allPerson;
    }

    public void updateDetails(int personID, String name, String telNum, String email, String address)
            throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");

        String SQLquery = "UPDATE Person SET name= '" + name + "', telNum= '" + telNum + "', email= '" + email
                + "', address= '" + address + "' WHERE id=" + personID;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLquery);
        connection.close();
    }

}
