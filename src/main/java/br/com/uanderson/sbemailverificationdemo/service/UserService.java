package br.com.uanderson.sbemailverificationdemo.service;

import br.com.uanderson.sbemailverificationdemo.exception.UserAlreadyExistsException;
import br.com.uanderson.sbemailverificationdemo.model.user.User;
import br.com.uanderson.sbemailverificationdemo.interfaces.UserServiceInterface;
import br.com.uanderson.sbemailverificationdemo.model.token.VerificationToken;
import br.com.uanderson.sbemailverificationdemo.repository.VerificationTokenRepository;
import br.com.uanderson.sbemailverificationdemo.requestDto.RegistrationRequest;
import br.com.uanderson.sbemailverificationdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;//Pegando o method(@Bean) declarado no package 'security'
    private final VerificationTokenRepository verificationTokenRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException("User with email "+ request.email() + " already exists");
        }

        User newUser = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User userEvent, String token) {
        VerificationToken verificationToken = new VerificationToken(token, userEvent);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(String.valueOf(token));
        if (verificationToken == null) return "Invalid verification token!";

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        long tokenExpiration = verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime();
        if (tokenExpiration <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Valid";
    }

}//class
