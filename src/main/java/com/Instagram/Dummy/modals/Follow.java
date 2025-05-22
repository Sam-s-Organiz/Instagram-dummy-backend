package com.Instagram.Dummy.modals;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "follow",
    uniqueConstraints = @UniqueConstraint(columnNames = {"source_user_id", "target_user_id"}),
    indexes = {
      @Index(name = "idx_source_user", columnList = "source_user_id"),
      @Index(name = "idx_target_user", columnList = "target_user_id")
    })
@Getter
@Setter
@NoArgsConstructor
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "source_user_id", nullable = false)
  private User sourceUser;

  @ManyToOne
  @JoinColumn(name = "target_user_id", nullable = false)
  private User targetUser;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
