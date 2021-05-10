import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice {
    /**
     * Atributes
     */
    Person client;
    int amountOut;
    int projNum;
    String projName;
    Date completionDate;

    /**
     * Constructor
     * 
     * @param projNum
     * @param projName
     * @param client
     * @param amountOut
     * @param completionDate
     */
    public Invoice(int projNum, String projName, Person client, int amountOut, Date completionDate) {
        this.projNum = projNum;
        this.projName = projName;
        this.client = client;
        this.amountOut = amountOut;
        this.completionDate = completionDate;
    }

    /**
     * Output for the class Invoice
     * 
     * @return String
     */
    public String toString() {
        String output = "Invoice";
        output += "\nCustomer Name: " + client.name;
        output += "\nProject Number: " + projNum;
        output += "\nProject Name: " + projName;
        output += "\nAmount due: R" + amountOut;
        output += "\nDate of completion: " + new SimpleDateFormat("dd/MM/yyy").format(completionDate) + "\n";

        return output;
    }
}
