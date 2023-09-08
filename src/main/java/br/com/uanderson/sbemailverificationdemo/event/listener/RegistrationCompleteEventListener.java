package br.com.uanderson.sbemailverificationdemo.event.listener;

import br.com.uanderson.sbemailverificationdemo.event.RegistrationCompleteEvent;
import br.com.uanderson.sbemailverificationdemo.model.user.User;
import br.com.uanderson.sbemailverificationdemo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
        private final UserService userService;
        private final JavaMailSender mailSender;
        private User userEvent;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user (user recém registrado)
        userEvent = event.getUser();

        // 2. Create a verification token fot the user
        String verificationToken = UUID.randomUUID().toString();

        // 3. Save the verification token fot the user
        userService.saveUserVerificationToken(userEvent, verificationToken);
        // 4. Build the verifications url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;

        // 5. Send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration : {}", url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification: ";
        String senderName = "User Registration portal service";
        String mailContent = "<p> Hi, "+ userEvent.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("uandersonf02@gmail.com", senderName);
        messageHelper.setTo(userEvent.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}//class
