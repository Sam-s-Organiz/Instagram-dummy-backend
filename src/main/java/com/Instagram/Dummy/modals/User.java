package com.Instagram.Dummy.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  private String profilePicture;

  private String bio;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  @ToString.Exclude
  private Set<Post> posts;

  @OneToMany(mappedBy = "sourceUser")
  @JsonBackReference
  @ToString.Exclude
  private Set<Follow> following;

  @OneToMany(mappedBy = "targetUser")
  @JsonBackReference
  @ToString.Exclude
  private Set<Follow> followers;
}
