package singles420.entrision.com.singles420;

/**
 * Created by travis on 7/22/15.
 */
public class Person {
    String firstName;
    String lastName;
    String emailAddress;
    String location;
    int age;
    String birthDate;
    int userID;
    double latitude;
    double longitude;
    String[] images;
    String biography;
    String sex;

    public Person() {

    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String initials() {
        return String.valueOf(firstName.charAt(0)).toUpperCase() + String.valueOf(lastName.charAt(0)).toUpperCase();
    }
}
