package pl.lucky.services.mailService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lucky.model.Task;
import pl.lucky.services.TaskService;

import java.util.Date;
import java.util.List;

@Component
public class MailService implements Job {

    @Autowired
    private TaskService taskService;

    public void sendMail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(new Date());

        ApplicationContext ctx = new AnnotationConfigApplicationContext(MailConfiguration.class);
        JavaMailSender mailSender = (JavaMailSender) ctx.getBean("JavaMailSenderBean");
        mailSender.send(message);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Task> taskForTomorrow = taskService.findTasksForTomorrow();

        for (Task task : taskForTomorrow) {
            sendMail(task.getUser().getEmail(), "Task for tomorrow: \"" + task.getTaskSubject() + "\"", task.getTaskDetails());
        }

    }
}
