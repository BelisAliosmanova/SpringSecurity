package com.springSecurity.springSecurity.services.impl.security;

import com.springSecurity.springSecurity.config.FrontendConfig;
import com.springSecurity.springSecurity.models.entity.User;
import com.springSecurity.springSecurity.services.TokenService;
import com.springSecurity.springSecurity.services.impl.security.events.OnPasswordResetRequestEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component responsible for listening to password reset request events and sending password reset emails.
 */
@Component
@RequiredArgsConstructor
public class PasswordResetListener implements ApplicationListener<OnPasswordResetRequestEvent> {

    private final JavaMailSender mailSender;
    private final FrontendConfig frontendConfig;
    private final TokenService tokenService;

    /**
     * Sends a password reset email upon receiving the password reset request event.
     */
    @Override
    @Async
    public void onApplicationEvent(@NotNull OnPasswordResetRequestEvent event) {
        sendPasswordResetEmail(event);
    }

    protected void sendPasswordResetEmail(OnPasswordResetRequestEvent event) {
        User user = event.getUser();

        // Generate a password reset token and create a verification token for the user
        String token = generateResetToken(user);
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request";
        String message = getEmailMessage(token, user);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    @NotNull
    private String getEmailMessage(String token, User user) {
        //Constructs the email message for the password reset request.
        String confirmationUrl = frontendConfig.getForgottenPasswordUrl() + "?token=" + token;

        return "Dear " + user.getName() + ",\n\n"
                + "A password reset request has been initiated for your account.\n"
                + "Please click the following link to reset your password:\n"
                + confirmationUrl + "\n\n"
                + "If you did not request this change, please ignore this email.\n\n"
                + "Best regards,\n"
                + "The Sample Team!";
    }

    private String generateResetToken(User user) {
        return UUID.randomUUID().toString();
    }
}