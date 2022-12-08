package by.bsuir.ufpnjh.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
public class Student implements Serializable {

    private String name;

    private String surname;

    private String gradeBookNumber;

    private String speciality;

    private Gender gender;

    private Date birthday;

    public Student(String name, String surname, String gradeBookNumber, String speciality, Gender gender, Date birthday) {
        this.name = name;
        this.surname = surname;
        this.gradeBookNumber = gradeBookNumber;
        this.speciality = speciality;
        this.gender = gender;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gradeBookNumber='" + gradeBookNumber + '\'' +
                ", speciality='" + speciality + '\'' +
                ", gender=" + gender +
                ", birthday=" + new SimpleDateFormat("dd.MM.yyyy").format(birthday) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student that = (Student) o;
        return name.equals(that.name) && surname.equals(that.surname)
                && gradeBookNumber.equals(that.gradeBookNumber) && speciality.equals(that.speciality)
                && gender == that.gender && birthday.equals(that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, gradeBookNumber, speciality, gender, birthday);
    }

}
