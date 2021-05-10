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
     * Constructor
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
     * Output for the class Person
     * 
     * @return String
     */

    public String toString() {
        String output = "\nID: " + id;
        output += "\nName: " + name;
        output += "\nContact Details: " + telNum;
        output += "\nEmail address: " + email;
        output += "\nResidentail Address: " + address;

        return output;
    }

}
