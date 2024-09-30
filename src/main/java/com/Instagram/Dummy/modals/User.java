package com.Instagram.Dummy.modals;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePicture;

    private String bio;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Post> posts;

    @OneToMany(mappedBy = "sender")
    @ToString.Exclude
    private Set<DirectMessage> sentMessages;

    @OneToMany(mappedBy = "receiver")
    @ToString.Exclude
    private Set<DirectMessage> receivedMessages;

    @OneToMany(mappedBy = "follower")
    @ToString.Exclude
    private Set<Follow> following;

    @OneToMany(mappedBy = "following")
    @ToString.Exclude
    private Set<Follow> followers;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Comment> comments; // Add this line for comments
}
