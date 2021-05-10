import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * Create a instance of the Person respository
     */
    PersonRepo personRepo;

    /**
     * Create an instance for a SQL date format
     */
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Constructor
    public ProjectRepository() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        personRepo = new PersonRepo();

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
     * Method to get a project
     * 
     * @param projNum
     * @return Project
     * @throws Exception
     */

    public Project get(int projNum) throws Exception {
        ArrayList<Project> results = getAll("WHERE projNum=" + projNum);
        if (results.size() == 1) {
            return results.get(0);
        }

        throw new Exception("Project number " + projNum + " could not be found.");
    }

    /**
     * Method to get projects that are overdue or has been finalised
     * 
     * @param isFinalised
     * @param isOverdue
     * @return
     * @throws Exception
     */
    public ArrayList<Project> getAll(String whereSQL) throws Exception {
        ArrayList<Project> allProject = new ArrayList<Project>();
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");
        String SQLquery = "SELECT projNum, projName, buildingType, address, erfNum, totalFee, amountPaid, deadline, isFinalised, "
                + "completionDate, archPersonID, custPersonID, structEngPersonID, projMangPersonID  FROM Project ";

        if (whereSQL != null && whereSQL != "") {
            SQLquery += whereSQL;
        }
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
            allProject.add(proj);
        }

        connection.close();
        return allProject;
    }

    /**
     * Method to update the projects in the arrayList ProjectList
     * 
     * @param projectToUpdate
     * @throws Exception
     */

    public void update(Project projectToUpdate) throws Exception {

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "tish", "MySQL@Gemini074");

        String SQLquery = "UPDATE Project SET projName='" + projectToUpdate.projName + "', buildingType='"
                + projectToUpdate.buildingType + "', address='" + projectToUpdate.address + "', erfNum='"
                + projectToUpdate.erfNum + "', totalFee=" + projectToUpdate.totalFee + ",amountPaid="
                + projectToUpdate.amountPaid + ", deadline='" + sqlDateFormat.format(projectToUpdate.deadline)
                + "', isFinalised=" + projectToUpdate.isFinalised + ", completionDate='"
                + sqlDateFormat.format(projectToUpdate.completionDate) + "',structEngPersonID="
                + projectToUpdate.structEngPersonID + ", archPersonID=" + projectToUpdate.archPersonID
                + ", custPersonID=" + projectToUpdate.custPersonID + ", projMangPersonID="
                + projectToUpdate.projMangPersonID + " WHERE projnum=" + projectToUpdate.projNum;
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQLquery);
        connection.close();
    }

    /**
     * @return ArrayList<Project> Method to print uncompleted projects
     * @throws Exception
     */
    public ArrayList<Project> uncompletedProj() throws Exception {
        return getAll("WHERE isFinalised=0 ");
    }

    /**
     * @return ArrayList<Project> Method to create a list of projects that are
     *         overdue
     * @throws Exception
     */
    public ArrayList<Project> overdueProj() throws Exception {
        return getAll("WHERE date(deadline)< date '" + sqlDateFormat.format(new Date()) + "'");
    }

}

// Reference:
// https://stackoverflow.com/questions/16111496/java-how-can-i-write-my-arraylist-to-a-file-and-read-load-that-file-to-the
// To save and read an ArrayList to and from a file as an object
// Reference: https://www.geeksforgeeks.org/compare-dates-in-java/
// To compare two dates with each other.
