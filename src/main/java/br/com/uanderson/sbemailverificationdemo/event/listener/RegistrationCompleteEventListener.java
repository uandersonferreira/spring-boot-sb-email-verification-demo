package br.com.uanderson.sbemailverificationdemo.event.listener;

import br.com.uanderson.sbemailverificationdemo.event.RegistrationCompleteEvent;
import br.com.uanderson.sbemailverificationdemo.model.user.User;
import br.com.uanderson.sbemailverificationdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
        private final UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user (user recém registrado)
        User userEvent = event.getUser();

        // 2. Create a verification token fot the user
        String verificationToken = UUID.randomUUID().toString();

        // 3. Save the verification token fot the user
        userService.saveUserVerificationToken(userEvent, verificationToken);
        // 4. Build the verifications url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;

        // 5. Send the email
        log.info("Click the link to verify your registration : {}", url);
    }
}
