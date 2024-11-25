package ru.codeline.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    @Query("SELECT u.firstName FROM User u WHERE u.id = ?1")
    String findFirstNameById(UUID userId);

    @Query("SELECT u.lastName FROM User u WHERE u.id = ?1")
    String findLastNameById(UUID userId);
}


