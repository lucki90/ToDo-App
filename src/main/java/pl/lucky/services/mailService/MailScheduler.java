package pl.lucky.services.mailService;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class MailScheduler {

    private LocalDateTime startOfApplication;
    private LocalDateTime startOfScheduler;

    public MailScheduler(LocalDateTime now) {
        startOfApplication = now;
    }

    public void startScheduler() throws SchedulerException {

        setStartOfScheduler();

        JobDetail jobDetail = JobBuilder.newJob(MailService.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger")
                .startAt(Date.from(startOfScheduler.toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusHours(7).toInstant()))
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever())
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        if (!scheduler.checkExists(trigger.getKey())) {
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            scheduler.start();
        }

    }

    //Check when start, today at 7:00 or tomorrow
    private void setStartOfScheduler() {
        LocalDateTime triggerTime = startOfApplication.truncatedTo(ChronoUnit.DAYS).plusHours(7);

        if (startOfApplication.isAfter(triggerTime)) {
            startOfScheduler = triggerTime.plusDays(1);
        } else {
            startOfScheduler = triggerTime;
        }
    }
}
