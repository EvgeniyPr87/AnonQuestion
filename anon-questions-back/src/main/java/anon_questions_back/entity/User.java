package anon_questions_back.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vk_user_id", nullable = false, unique = true)
    private Long vkUserId;

    @Column(name="link_slug", nullable = false, unique = true, length = 32)
    private String linkSlug;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt=Instant.now();

}
