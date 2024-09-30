package com.Instagram.Dummy.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "follow")
@Getter
@Setter
@NoArgsConstructor // No-args constructor for JPA
@ToString // Optional, can exclude follower and following if necessary
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    @ToString.Exclude // Exclude from toString to avoid circular references
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    @ToString.Exclude // Exclude from toString to avoid circular references
    private User following;
}
