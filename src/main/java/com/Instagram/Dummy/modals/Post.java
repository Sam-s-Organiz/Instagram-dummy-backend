package com.Instagram.Dummy.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor // Remove this if you have a constructor defined
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String imageUrl;

    private String caption;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    private Set<Like> likes;

}
