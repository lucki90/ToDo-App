package pl.lucky.model;

import javax.validation.constraints.NotEmpty;

public class Person {

    @NotEmpty(message = "firstName nie może być puste ")
    private String firstName;


    public Person() {}
    public Person(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }

}