package com.springSecurity.springSecurity.services.impl.security;

import com.springSecurity.springSecurity.models.entity.User;
import com.springSecurity.springSecurity.services.TokenService;
import com.springSecurity.springSecurity.services.impl.security.events.OnRegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component responsible for handling registration confirmation emails.
 */
@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final TokenService tokenService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    protected void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "The Sample Project Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "auth/registrationConfirm?token=" + token;

        // Construct the email message
        String message = "Dear, " + user.getName() + "\n\n"
                + "Thank you for registering with The Sample Project!\n\n"
                + "To complete your registration, please click the following link to verify your email:\n"
                + confirmationUrl + "\n"
                + "If you did not create an account with us, please ignore this email.\n"
                + "Best regards,\n"
                + "The Sample team!";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
