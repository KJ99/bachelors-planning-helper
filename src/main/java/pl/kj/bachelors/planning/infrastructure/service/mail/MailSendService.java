package pl.kj.bachelors.planning.infrastructure.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.config.MailerConfig;
import pl.kj.bachelors.planning.domain.service.mail.MailSender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailSendService implements MailSender {
    private final JavaMailSender emailSender;
    private final MailerConfig config;

    @Autowired
    public MailSendService(JavaMailSender emailSender, MailerConfig config) {
        this.emailSender = emailSender;
        this.config = config;
    }

    private SimpleMailMessage createPlainTextMessage(final String to, final String subject, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.config.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }

    private void sendMessage(final SimpleMailMessage message, final int nThread) {
        ExecutorService executor = Executors.newFixedThreadPool(nThread);
        executor.execute(() -> this.emailSender.send(message));
    }
}
