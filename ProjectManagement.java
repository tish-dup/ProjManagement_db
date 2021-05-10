import java.sql.SQLException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectManagement {
    ProjectRepository projRepo;
    PersonRepo personRepo;

    // Instance of Project Repository and Person Repository
    public ProjectManagement() throws Exception {
        this.projRepo = new ProjectRepository();
        this.personRepo = new PersonRepo();
    }

    // Method to get a project from the database
    public Project getProj(int projNum) throws Exception {
        return projRepo.get(projNum);
    }

    // Method to add new project to the database
    public void addNewProj(int projNum, String projName, String buildingType, String address, String erfNum,
            int totalFee, int amountPaid, Date deadline, boolean isFinalised, Date completionDate,
            Person structEngPerson, Person archPerson, Person custPerson, Person projMangPerson) throws Exception {

        // If the user did not enter a name for the project, then the program will
        // concatenate the building type and the client's surname as the new project
        // name.
        if (projName == "") {
            String[] customerName = custPerson.name.split(" ");
            String lastname = customerName[customerName.length - 1];
            projName = buildingType + " " + lastname;
        }

        Project newProj = new Project(projNum, projName, buildingType, address, erfNum, totalFee, amountPaid, deadline,
                isFinalised, completionDate, structEngPerson, archPerson, custPerson, projMangPerson);
        try {
            projRepo.create(newProj);
        } catch (SQLException e) {
            throw new Exception("Could not add project to database at this time");
        }

    }

    public Person addNewPerson(int id, String name, String telNum, String email, String address) throws Exception {
        Person newPerson = new Person(id, name, telNum, email, address);

        try {
            personRepo.createPerson(newPerson);
        } catch (Exception e) {
            throw new Exception("Could not add database at this time");
        }

        return newPerson;
    }

    public void changeDeadline(int projNum, Date newDeadline) throws Exception {
        try {
            projRepo.updateDeadline(projNum, newDeadline);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }

    }

    public void updateAmountPaid(int projNum, int newAmount) throws Exception {
        try {
            projRepo.updatePayment(projNum, newAmount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }
    }

    public void updateDetails(int personID, String name, String telNum, String email, String address) throws Exception {
        try {
            personRepo.updateDetails(personID, name, telNum, email, address);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }

    }

}
