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
@NoArgsConstructor
@ToString
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    @ToString.Exclude
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    @ToString.Exclude
    private User following;
}
