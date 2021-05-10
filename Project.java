import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Project implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributes
     */
    int projNum;
    String projName;
    String buildingType;
    String address;
    String erfNum;
    int totalFee;
    int amountPaid;
    int amountOut;
    Date deadline;
    boolean isFinalised;
    Date completionDate;
    int structEngPersonID;
    int archPersonID;
    int custPersonID;
    int projMangPersonID;

    Person structEngPerson;
    Person archPerson;
    Person custPerson;
    Person projMangPerson;

    /**
     * Constructor
     * 
     * @param projNum
     * @param projName
     * @param buildingType
     * @param address
     * @param erfNum
     * @param totalFee
     * @param amountPaid
     * @param deadline
     * @param structEngPerson
     * @param archPerson
     * @param custPerson
     * @param projMangPerson
     */
    public Project(int projNum, String projName, String buildingType, String address, String erfNum, int totalFee,
            int amountPaid, Date deadline, boolean isFinalised, Date completionDate, Person structEngPerson,
            Person archPerson, Person custPerson, Person projMangPerson) {

        this.projNum = projNum;
        this.projName = projName;
        this.buildingType = buildingType;
        this.address = address;
        this.erfNum = erfNum;
        this.totalFee = totalFee;
        this.amountPaid = amountPaid;
        this.deadline = deadline;
        this.isFinalised = isFinalised;
        this.completionDate = completionDate;
        this.structEngPersonID = structEngPerson.id;
        this.archPersonID = archPerson.id;
        this.custPersonID = custPerson.id;
        this.projMangPersonID = projMangPerson.id;
        this.structEngPerson = structEngPerson;
        this.archPerson = archPerson;
        this.custPerson = custPerson;
        this.projMangPerson = projMangPerson;
    }

    /**
     * To give the class Project an output.
     * 
     * @return String
     */
    public String toString() {
        DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String output = "Project Number: " + projNum;
        output += "\nProject Name: " + projName;
        output += "\nType of building: " + buildingType;
        output += "\nAddress: " + address;
        output += "\nERF number: " + erfNum;
        output += "\nTotal fee for project: " + totalFee;
        output += "\nTotal amount paid to date: " + amountPaid;
        output += "\nDate when project is due: " + dateformat.format(deadline);

        // A string to represent the completion date. If the project hasn't been
        // completed, then it will be represented by a '-', otherwise a completion date
        // will be entered

        String completionDateString = "-";
        if (completionDate != null) {
            completionDateString = dateformat.format(completionDate);
        }

        output += "\nDate of completion: " + completionDateString;

        // Output to determine whether the project has been completed or not
        String isFinalisedString = "-";
        if (isFinalised) {
            isFinalisedString = "Yes";
        } else {
            isFinalisedString = "No";
        }

        output += "\nFinalised (yes/no): " + isFinalisedString;

        // Details of the Structural Engineer, architect and client and Project Manager
        output += "\nStructural Engineer: " + structEngPerson;
        output += "\nArchitect: " + archPerson;
        output += "\nClient: " + custPerson;
        output += "\nProject Manager: " + projMangPerson;

        return output;
    }

    /**
     * Method to add a new due date to the project
     * 
     * @param newDeadline
     */

    public void changeDeadline(Date newDeadline) {
        this.deadline = newDeadline;
    }

    /**
     * Method to add the new amount paid by the client
     * 
     * @param newAmmount
     *
     */

    public void addAmount(int newAmount) {
        this.amountPaid = amountPaid + newAmount;
    }

    /**
     * To complete a project, the completion date is assigned to the project, the
     * finalisation status is changed to true, and the oustanding amount is
     * calculated for the invoice
     * 
     * @param completionDate
     * @return Invoice
     */
    public Invoice finalise(Date completionDate) {
        this.completionDate = completionDate;
        this.isFinalised = true;
        this.amountOut = totalFee - amountPaid;

        if (amountOut > 0) {
            return new Invoice(projNum, projName, custPerson, amountOut, completionDate);
        } else {
            return null;
        }
    }

}
