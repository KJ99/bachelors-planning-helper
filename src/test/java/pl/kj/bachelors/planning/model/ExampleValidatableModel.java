package pl.kj.bachelors.planning.model;

import org.hibernate.validator.constraints.Length;
import pl.kj.bachelors.planning.domain.constraint.FieldValueMatch;

import javax.validation.constraints.Min;

@FieldValueMatch(field = "confirmPassword", target = "password")
public class ExampleValidatableModel {
    @Length(min = 5, max = 10)
    private String firstName;
    @Min(10)
    private int age;
    private String password;
    private String confirmPassword;

    public static ExampleValidatableModel getValidInstance() {
        ExampleValidatableModel instance = new ExampleValidatableModel();
        instance.setFirstName("Bogdan");
        instance.setAge(12);
        instance.setPassword("P@ssw0rd");
        instance.setConfirmPassword("P@ssw0rd");

        return instance;
    }

    public static ExampleValidatableModel getInvalidInstance() {
        ExampleValidatableModel instance = new ExampleValidatableModel();
        instance.setFirstName("Bog");
        instance.setAge(3);
        instance.setPassword("op");
        instance.setConfirmPassword("P@ssw0rd");

        return instance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
