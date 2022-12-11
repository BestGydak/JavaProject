package Person;

import java.time.LocalDate;

public class Person {
    public final String Name;
    public final String Surname;
    public final String Gender;
    public final String PhoneNumber;
    public final String HomeTown;
    public final String BirthDate;

    public Person(String name, String surname, String gender, String phone, String homeTown, String birthDate) {
        Name = name;
        Surname = surname;
        Gender = gender;
        PhoneNumber = phone;
        HomeTown = homeTown;
        BirthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "Name='" + Name + '\'' +
                ", Surname='" + Surname + '\'' +
                ", Gender='" + Gender + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", HomeTown='" + HomeTown + '\'' +
                ", BirthDate=" + BirthDate +
                '}';
    }
}
