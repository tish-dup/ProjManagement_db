import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.io.FileWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

    /**
     * Scanner Method for user's input
     */

    static Scanner input = new Scanner(System.in);

    /**
     * Global method to format date in an appropriate format
     */
    static DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Create instance of Project repository
     */
    static ProjectRepository projRepo;
    static PersonRepo personRepo;

    /**
     * Create instance of Project repository
     */

    static ProjectManagement projMang;

    /**
     * Method to display a menu from which the user can selects
     * 
     * @return String
     * @throws ParseException
     */

    public static String menuSelection() throws ParseException {
        System.out.println("""
                Welcome to Poised!
                1\t-\tAdd a new project
                2\t-\tChange due date of a project
                3\t-\tChange payment on project
                4\t-\tUpdate contact details
                5\t-\tFinalise a project
                6\t-\tView list of uncompleted projects
                7\t-\tView list of overdue Projects
                0\t-\tExit
                """);

        // User's selection determine which method should be called
        int userSelection = getUserInt("Please make your selection: ");

        switch (userSelection) {
            // If user's selection is '1', then user can create a new project
            case 1:
                createProject();
                break;

            // If user's selection is '2', then user can amend the due date of the project
            case 2:
                newDeadline();
                break;

            // If user selects '3', then user can change the payment on the project
            case 3:
                makePayment();
                break;

            // If user selects '4', then user can update the details of either the
            // contractor, client or architect.
            case 4:
                updatePersonDetails();
                break;

            // If user selects '5', then user can finalise the project
            case 5:
                finalisation();
                break;

            // User can view list of uncompleted projects by entering '6'
            case 6:
                ArrayList<Project> incomplete = projRepo.uncompletedProj();
                for (Project proj : incomplete) {
                    System.out.println(proj.toString() + "\n");
                }
                break;

            // User can view list of overdue projects by entering '7'
            case 7:
                ArrayList<Project> overdue = projRepo.overdueProj();
                for (Project proj : overdue) {
                    System.out.println(proj.toString() + "\n");
                }
                break;

            // User can exit program by entering '8'
            case 0:
                exitProgram();
                break;

            default:
                System.out.println("Incorrect selection. Please enter a valid option");
        }

        return menuSelection();
    }

    /**
     * @param message
     * @return String // Method to get user input as a string
     */

    public static String getUserString(String message) {
        System.out.println(message);
        return input.nextLine();
    }

    /**
     * @param message
     * @return int // Method to get user input as an integer
     */

    public static int getUserInt(String message) {
        try {
            System.out.println(message);
            int value = input.nextInt();
            input.nextLine();
            return value;
        } catch (Exception e) {
            input.nextLine();
            System.out.println("That is not a valid entry. Please enter a valid number");
            return getUserInt(message);
        }

    }

    /**
     * Method to create a project by requesting for user's input for each instance
     * in the Project class. If the user did not enter a name for the project, then
     * the program will concatenate the building type and the client's surname as
     * the new project name.
     * 
     * @throws Exception
     */

    public static void createProject() {
        try {
            int projNum = getUserInt("Please enter the project number: ");
            String projName = getUserString("Please enter the project name: ");
            String buildingType = getUserString("Please enter the type of building: ");
            String address = getUserString("Please enter the physical address for the project: ");
            String erfNum = getUserString("Please enter the ERF number: ");
            int totalFee = getUserInt("Please enter the total fee for the project: ");
            int amountPaid = getUserInt("Please enter the total amount paid to date: ");
            String sdeadline = getUserString("Please enter the deadline for the project (dd/MM/yyyy): ");
            Date deadline = dateformat.parse(sdeadline);

            Person structEngPerson = createPerson("Structural Engineer");
            Person archPerson = createPerson("Architect");
            Person custPerson = createPerson("Customer");
            Person projMangPerson = createPerson("Project Manager");

            projMang.addNewProj(projNum, projName, buildingType, address, erfNum, totalFee, amountPaid, deadline, false,
                    null, structEngPerson, archPerson, custPerson, projMangPerson);
        } catch (ParseException pe) {
            System.out.println("Incorrect date format. Please try again");
            createProject();
            return;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            createProject();
            return;
        }

    }

    /**
     * Method lets the user add the details of each person who works on the project.
     * 
     * @param type
     * @param input
     * @return Person
     * @throws Exception
     */

    static Person createPerson(String personType) {
        System.out.println("\nPlease enter the details for " + personType + ": ");
        int id = getUserInt("\nID: ");
        String name = getUserString("\nName: ");
        String telNum = getUserString("\nContact number: ");
        String email = getUserString("\nEmail address : ");
        String address = getUserString("\nAddress: ");

        try {
            Person createdPerson = projMang.addNewPerson(id, name, telNum, email, address);
            return createdPerson;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return createPerson(personType);
        }

    }

    /**
     * Method to get the project that the user wishes to work on. If there is no
     * project in the program an appropriate message will appear.
     * 
     * @return Project
     */

    public static Project getProject() {
        int projNum = getUserInt("\nPlease enter the project number: ");
        try {
            return projRepo.get(projNum);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return getProject();
    }

    /**
     * @throws ParseException // Method to change the due date of the project, by
     *                        requesting for user's input and changing the input in
     *                        the appropriate format.
     */

    public static void newDeadline() throws ParseException {
        // Get the project number the user wishes to work on
        Project proj = getProject();
        int projNum = proj.projNum;

        String snewDeadline = getUserString("Please enter the new deadline for the project (dd/MM/yyyy): ");
        try {
            Date newDeadline = dateformat.parse(snewDeadline);
            projMang.changeDeadline(projNum, newDeadline);

        } catch (ParseException ex) {
            System.out.println("Date is not valid. Please try again.");
            newDeadline();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            newDeadline();
        }
    }

    /**
     * User can add the payments made for the project to the amount paid by the
     * client.
     * 
     */

    public static void makePayment() {
        // Get the project number the user wishes to work on
        Project proj = getProject();
        int newAmount = getUserInt("Add client's next payment: ");

        try {
            projMang.updateAmountPaid(proj.projNum, newAmount);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to update a client, architect or contractor's details. User selects
     * the type of person he/she wishes to update and the program will prompt the
     * user for the new details.
     */

    public static Person getPerson() {
        try {
            ArrayList<Person> allPersons = personRepo.getAll();
            for (Person person : allPersons) {
                System.out.println(person.id + "\t" + person.name + ": " + person.email);
            }

            int personID = getUserInt("Please enter the id of the person you wish to select: ");
            Optional<Person> selectedPerson = allPersons.stream().filter(person -> person.id == personID).findFirst();
            if (selectedPerson.isEmpty()) {
                System.out.println("Could not find the id on the database");
                return getPerson();
            } else {
                return selectedPerson.get();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static void updatePersonDetails() {
        // Get the project number the user wishes to work on
        Person person = getPerson();
        String name = getUserString("\nName: ");
        String telNum = getUserString("\nContact number: ");
        String email = getUserString("\nEmail address : ");
        String address = getUserString("\nAddress: ");

        try {
            projMang.updateDetails(person.id, name, telNum, email, address);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * To finalise a project, user needs to complete the date on which the project
     * has been finalised and an invoice will be generated. Project will be stored
     * in it's own file. If the project has been paid in full, the appropriate
     * message will display
     */
    public static void finalisation() {
        // Get the project number the user wishes to work on
        Project proj = getProject();

        if (proj.isFinalised) {
            System.out.println("Project already completed on " + dateformat.format(proj.completionDate));
            return;
        }

        String scomplete = getUserString("Please enter the date of completion for the project (dd/MM/yyyy): ");
        try {
            Date complete = dateformat.parse(scomplete);

            // If there is an outstanding amount on the project, an invoice will be
            // generated. Project will be stored in it's own file
            // If the project has been paid in full, the appropriate message will
            // display
            Invoice invoice = proj.finalise(complete);
            projRepo.update(proj);
            if (invoice != null) {
                System.out.print(invoice.toString());

                ArrayList<Project> completedList = new ArrayList<Project>();
                completedList.add(proj);
                FileWriter writer = new FileWriter("Completed project.txt");
                writer.write(completedList + System.lineSeparator());
                writer.close();

            } else {
                System.out.print("Project complete and paid in full");
            }

        } catch (ParseException pex) {
            System.out.println("Date is not valid. Please try again.");
            finalisation();
            return;

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    /**
     * @throws ParseException // Method to exit program and save projects
     */

    public static void exitProgram() throws ParseException {
        String answer = getUserString("Are you sure you want to exit program (y/n)");
        if (answer.equals("y")) {
            projRepo.writeToFile();
            System.exit(0);
        } else {
            menuSelection();
        }
    }

    /**
     * @param args
     * @throws ParseException Main Method - User has the choice to either use the
     *                        example project in program or to generate a new
     *                        project to navigate through the program.
     */
    public static void main(String[] args) {

        try {
            projRepo = new ProjectRepository();
            projMang = new ProjectManagement();
            personRepo = new PersonRepo();
            projRepo.readFromFile();
            menuSelection();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

}

// Reference:
// https://www.codegrepper.com/code-examples/java/taking+date+as+input+in+java
// To covert user's date input into readable format.