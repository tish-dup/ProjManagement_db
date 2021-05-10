import java.io.Serializable;

class Person implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Attributes
     */
    int id;
    String name;
    String telNum;
    String email;
    String address;

    /**
     * 
     * @param id
     * @param name
     * @param telNum
     * @param email
     * @param address
     */
    public Person(int id, String name, String telNum, String email, String address) {
        this.id = id;
        this.name = name;
        this.telNum = telNum;
        this.email = email;
        this.address = address;
    }

    /**
     * @return String // Output for the class Person
     */

    public String toString() {
        String output = "\nID: " + id;
        output += "\nName: " + name;
        output += "\nContact Details: " + telNum;
        output += "\nEmail address: " + email;
        output += "\nResidentail Address: " + address;

        return output;
    }

    /**
     * @param telNum
     * @param email
     * @param address The contact details for either the client, contractor or
     *                architect for the project is updated
     */
    public void updateDetails(String telNum, String email, String address) {
        this.telNum = telNum;
        this.email = email;
        this.address = address;
    }
}
