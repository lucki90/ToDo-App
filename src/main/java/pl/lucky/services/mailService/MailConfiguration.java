package pl.lucky.services.mailService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class MailConfiguration {
    private String smtpHost;
    private String fromMail;
    private String smtpPassword;
    private int smtpPort;

    @Bean("JavaMailSenderBean")
    public JavaMailSender getJavaMailSender() {
        loadMailProperties();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(fromMail);
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private void loadMailProperties() {
        try (InputStream input = new FileInputStream("src/main/resources/mail.properties")) {
            Properties props = new Properties();
            props.load(input);

            smtpHost = props.getProperty("smtp.host");
            fromMail = props.getProperty("from.mail");
            smtpPassword = props.getProperty("smtp.password");
            smtpPort = Integer.parseInt(props.getProperty("smtp.port"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
