package pl.lucky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.lucky.services.mailService.MailSchedulerThread;

@SpringBootApplication
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);

        MailSchedulerThread mailSchedulerThread = new MailSchedulerThread();
        Thread t = new Thread(mailSchedulerThread);
        t.start();
    }

}
