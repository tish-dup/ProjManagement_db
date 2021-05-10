import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.*;

class ProjectRepository implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Assign each newly created project to an array
     */
    public ArrayList<Project> ProjectList = new ArrayList<Project>();

    PersonRepo personRepo;

    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Constructor
    public ProjectRepository() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        personRepo = new PersonRepo();
        // Connect to the PoisePMS database, via the jdbc:mysql: channel on localhost
        // (this PC)

    }

    /**
     * Method to create a new project
     * 
     * @param projNum
     * @param projName
     * @param buildingType
     * @param address
     * @param erfNum
     * @param totalFee
     * @param amountPaid
     * @param deadline
     * @param isFinalised
     * @param completionDate
     * @param archPersonID
     * @param custPersonID
     * @param structEngPersonID
     * @param projMangPersonID
     * @throws SQLException
     */

    public void create(Project newProj) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");
        String completionDate = "NULL";
        if (newProj.completionDate != null) {
            completionDate = "'" + sqlDateFormat.format(newProj.completionDate) + "'";

        }
        String projSQLquery = "INSERT INTO Project VALUES (" + newProj.projNum + ", '" + newProj.projName + "','"
                + newProj.buildingType + "', '" + newProj.address + "', '" + newProj.erfNum + "', " + newProj.totalFee
                + ", " + newProj.amountPaid + ",'" + sqlDateFormat.format(newProj.deadline) + "'," + newProj.isFinalised
                + ", " + completionDate + ", " + newProj.archPersonID + ", " + newProj.custPersonID + ", "
                + newProj.structEngPersonID + ", " + newProj.projMangPersonID + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(projSQLquery);
        connection.close();
    }

    /**
     * Method to get a new project
     * 
     * @param projNum
     * @return Project
     * @throws Exception
     */

    public Project get(int projNum) throws Exception {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");

        String SQLquery = "SELECT projNum, projName, buildingType, address, erfNum, totalFee, amountPaid, deadline, isFinalised, "
                + "completionDate, archPersonID, custPersonID, structEngPersonID, projMangPersonID  FROM Project WHERE projNum="
                + projNum;
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(SQLquery);

        while (results.next()) {
            int archPersonID = results.getInt("archPersonID");
            Person archPerson = personRepo.getPerson(archPersonID);
            int custPersonID = results.getInt("custPersonID");
            Person custPerson = personRepo.getPerson(custPersonID);
            int structEngPersonID = results.getInt("structEngPersonID");
            Person structEngPerson = personRepo.getPerson(structEngPersonID);
            int projMangPersonID = results.getInt("projMangPersonID");
            Person projMangPerson = personRepo.getPerson(projMangPersonID);
            String sdeadline = results.getString("deadline");
            Date deadline = sqlDateFormat.parse(sdeadline);
            String scompletion = results.getString("completionDate");
            Date completionDate;
            if (scompletion == null) {
                completionDate = null;
            } else {
                completionDate = sqlDateFormat.parse(scompletion);
            }
            Project proj = new Project(results.getInt("projNum"), results.getString("projName"),
                    results.getString("buildingType"), results.getString("address"), results.getString("erfNum"),
                    results.getInt("totalFee"), results.getInt("amountPaid"), deadline,
                    results.getBoolean("isFinalised"), completionDate, structEngPerson, archPerson, custPerson,
                    projMangPerson);

            connection.close();
            return proj;
        }

        throw new Exception("Project number " + projNum + " could not be found.");
    }

    /**
     * Method to update the projects in the arrayList ProjectList
     * 
     * @param projectToUpdate
     * @throws Exception
     */

    public void update(Project projectToUpdate) throws Exception {
        for (Project listproj : ProjectList) {
            if (projectToUpdate.projNum == listproj.projNum) {
                listproj = projectToUpdate;
                return;
            }
        }

        throw new Exception("Project number " + projectToUpdate.projNum + " could not be found.");
    }

    /**
     * Method to update the deadline for the project
     * 
     * @param projNum
     * @param newDeadline
     * @throws SQLException
     */

    public void updateDeadline(int projNum, Date newDeadline) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");

        String SQLquery = "UPDATE Project SET deadline= '" + sqlDateFormat.format(newDeadline) + "' WHERE projnum="
                + projNum;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLquery);
        connection.close();
    }

    /**
     * Method to update the payment that the client has made on the database
     * 
     * @throws SQLException
     */

    public void updatePayment(int projNum, int amountPaid) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");

        String SQLquery = "UPDATE Project SET amountPaid=" + amountPaid + " WHERE projnum=" + projNum;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLquery);
        connection.close();
    }

    /**
     * Method to write the projects to a binary file when the program is ended
     */
    public void writeToFile() {
        try {
            FileOutputStream outputFile = new FileOutputStream("Projectfile.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(outputFile);
            oos.writeObject(ProjectList);
            oos.close();

        } catch (IOException e) {
            System.out.println("Cannot load projects to file.");
        }
    }

    /**
     * Mehod to read the projects saved in a binary file
     */
    public void readFromFile() {

        try {
            FileInputStream inputFile = new FileInputStream("Projectfile.tmp");
            ObjectInputStream ois = new ObjectInputStream(inputFile);
            ProjectList = (ArrayList<Project>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("File cannot be found");
            // System.out.println("File cannot be found");
        }
    }

    /**
     * @return ArrayList<Project> Method to print uncompleted projects
     */
    public ArrayList<Project> uncompletedProj() {
        ArrayList<Project> uncompletedList = new ArrayList<Project>();
        for (Project proj : ProjectList) {
            if (!proj.isFinalised) {
                uncompletedList.add(proj);
            }
        }
        return uncompletedList;
    }

    /**
     * @return ArrayList<Project> Method to create a list of projects that are
     *         overdue
     */
    public ArrayList<Project> overdueProj() {
        java.util.Date date = new java.util.Date();
        ArrayList<Project> overdueList = new ArrayList<Project>();
        for (Project proj : ProjectList) {
            if (date.compareTo(proj.deadline) > 0) {
                overdueList.add(proj);
            }
        }
        return overdueList;
    }

}

// Reference:
// https://stackoverflow.com/questions/16111496/java-how-can-i-write-my-arraylist-to-a-file-and-read-load-that-file-to-the
// To save and read an ArrayList to and from a file as an object
// Reference: https://www.geeksforgeeks.org/compare-dates-in-java/
// To compare two dates with each other.
