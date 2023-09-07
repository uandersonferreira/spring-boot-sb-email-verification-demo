package br.com.uanderson.sbemailverificationdemo.repository;

import br.com.uanderson.sbemailverificationdemo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
