package ru.rabiarill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rabiarill.models.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   Optional<User> findByUsername(String username);
}
