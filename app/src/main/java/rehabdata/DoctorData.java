package rehabdata;

/**
 * Created by Emil on 01/02/16.
 */
public class DoctorData {
    int id;
    String name;
    String surname;
    String username;
    String password;
    String email;
    String phoneNumber;

    public DoctorData() {}

    public DoctorData(int id, String name, String surname, String username, String password, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}