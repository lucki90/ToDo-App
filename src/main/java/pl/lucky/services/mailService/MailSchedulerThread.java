package pl.lucky.services.mailService;


import lombok.Getter;
import lombok.Setter;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;

@Setter
@Getter
public class MailSchedulerThread implements Runnable {

    private MailScheduler mailScheduler = new MailScheduler(LocalDateTime.now());

    @Override
    public void run() {

        try {
            mailScheduler.startScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
