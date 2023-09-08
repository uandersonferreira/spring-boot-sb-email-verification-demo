package br.com.uanderson.sbemailverificationdemo.controller;

import br.com.uanderson.sbemailverificationdemo.event.RegistrationCompleteEvent;
import br.com.uanderson.sbemailverificationdemo.model.token.VerificationToken;
import br.com.uanderson.sbemailverificationdemo.model.user.User;
import br.com.uanderson.sbemailverificationdemo.repository.VerificationTokenRepository;
import br.com.uanderson.sbemailverificationdemo.requestDto.RegistrationRequest;
import br.com.uanderson.sbemailverificationdemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        //publish registration event
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        String message = "Success! Please, check your email for to complete your registration.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @GetMapping(path = "/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }



    private String applicationUrl(HttpServletRequest request) {
        return new StringBuilder()
                .append("http://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .toString();
        //http://localhost:8080/register
    }

}//class
