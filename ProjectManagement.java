import java.sql.SQLException;
import java.util.Date;

public class ProjectManagement {
    ProjectRepository projRepo;
    PersonRepo personRepo;

    /**
     * Instance of Project Repository and Person Repository
     * 
     * @throws Exception
     */
    public ProjectManagement() throws Exception {
        this.projRepo = new ProjectRepository();
        this.personRepo = new PersonRepo();
    }

    /**
     * Method to get a project from the database
     * 
     * @param projNum
     * @return
     * @throws Exception
     */
    public Project getProj(int projNum) throws Exception {
        return projRepo.get(projNum);
    }

    /**
     * Method to add new project to the database
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
     * @param structEngPerson
     * @param archPerson
     * @param custPerson
     * @param projMangPerson
     * @throws Exception
     */
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

    /**
     * Method to add new Person to database
     * 
     * @param id
     * @param name
     * @param telNum
     * @param email
     * @param address
     * @return
     * @throws Exception
     */
    public Person addNewPerson(int id, String name, String telNum, String email, String address) throws Exception {
        Person newPerson = new Person(id, name, telNum, email, address);

        try {
            personRepo.createPerson(newPerson);
        } catch (Exception e) {
            throw new Exception("Could not add database at this time");
        }

        return newPerson;
    }

    /**
     * Method to update deadline of the project
     * 
     * @param projNum
     * @param newDeadline
     * @throws Exception
     */
    public void changeDeadline(Project proj, Date newDeadline) throws Exception {
        try {
            proj.changeDeadline(newDeadline);
            projRepo.update(proj);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }

    }

    /**
     * Method to update the amount paid
     * 
     * @param projNum
     * @param newAmount
     * @throws Exception
     */
    public void updateAmountPaid(Project proj, int newAmount) throws Exception {
        try {
            proj.addAmount(newAmount);
            projRepo.update(proj);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }
    }

    /**
     * Method to update the details of a person
     * 
     * @param personID
     * @param name
     * @param telNum
     * @param email
     * @param address
     * @throws Exception
     */
    public void updateDetails(int personID, String name, String telNum, String email, String address) throws Exception {
        try {
            personRepo.updateDetails(personID, name, telNum, email, address);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not add to database at this time");
        }

    }

    /**
     * Method to finalise project
     * 
     * @param proj
     * @param completionDate
     * @return
     * @throws Exception
     */
    public Invoice finaliseProj(Project proj, Date completionDate) throws Exception {
        try {
            Invoice invoice = proj.finalise(completionDate);
            projRepo.update(proj);
            return invoice;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not finalise project at this time");
        }
    }

}
