package ru.codeline.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name", columnDefinition = "varchar")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "varchar")
    private String lastName;

    // No reference back to Credentials
    @Column(unique = true, columnDefinition = "varchar")
    private String email;
}
