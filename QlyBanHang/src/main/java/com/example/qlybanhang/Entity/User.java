package com.example.qlybanhang.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "{not.null}")
    private String name;


//    @Pattern(regexp =  "^[\\w]{6,}$")
    @Column(unique = true)
    private String username;

//    @Pattern( regexp = "^((?=.*[A-Z]).(?=.*[a-z])(?=.*[0-9])(?=.*[&^*%!$#@])).{8,}$")
    private String password;

//    @Column(name = "email")
//    @Email(message = "{email}")
    private String email;

    @OneToOne(mappedBy = "users")
    private Bill bill;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "roles" , joinColumns = @JoinColumn(name = "role_id"))
    private List<String> role;
}
