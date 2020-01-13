package pl.lucky.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_login", unique = true)
    @NotBlank(message = "{pl.lucky.model.NotEmpty}")
    @Length(min = 3, max = 50, message = "{pl.lucky.model.length}")
    private String ownerLogin;

    @Column(name = "password")
    @Length(min = 3, max = 255, message = "{pl.lucky.model.length.password}")
    @NotBlank(message = "{pl.lucky.model.NotEmpty}")
    private String password;

    @Column(name = "first_name")
    @Length(min = 3, max = 50, message = "{pl.lucky.model.length}")
    private String firstName;

    @Column(name = "last_name")
    @Length(min = 3, max = 50, message = "{pl.lucky.model.length}")
    private String lastName;

    @Column(name = "email", unique = true)
    @Email(message = "{pl.lucky.model.email}")
    @NotBlank(message = "{pl.lucky.model.NotEmpty}")
    @Length(min = 3, max = 50, message = "{pl.lucky.model.length}")
    private String email;

    @Column(name = "phone_number", unique = true)
    @Length(max = 9)
    @Pattern(regexp = "(^$|[0-9]{9})")
    private String phoneNumber;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Transient
    private String globalError;

    public User(String ownerLogin, String password, String firstName, String lastName, String email, String phoneNumber, UserRole role) {
        this.ownerLogin = ownerLogin;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
