package com.Instagram.Dummy.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jwt;
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // Ignore password in JSON response
    @Column(nullable = false)
    private String password;

    private String profilePicture;

    private String bio;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference // Manage the relationship to avoid recursion
    private Set<Post> posts;

    @OneToMany(mappedBy = "follower")
    @JsonBackReference // Prevent recursive serialization
    private Set<Follow> following;

    @OneToMany(mappedBy = "following")
    @JsonBackReference
    private Set<Follow> followers;
}
