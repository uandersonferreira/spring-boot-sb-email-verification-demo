package br.com.uanderson.sbemailverificationdemo.interfaces;

import br.com.uanderson.sbemailverificationdemo.model.token.VerificationToken;
import br.com.uanderson.sbemailverificationdemo.model.user.User;
import br.com.uanderson.sbemailverificationdemo.requestDto.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User userEvent, String verificationToken);

    String validateToken(String token);
}
