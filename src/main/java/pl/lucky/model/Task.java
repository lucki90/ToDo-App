package pl.lucky.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_subject")
    @NotEmpty(message = "{pl.lucky.model.NotEmpty}")
    private String taskSubject;

    @Column(name = "task_details")
    private String taskDetails;

    @Column(name = "task_create_time")
    private LocalDateTime taskCreateTime = LocalDateTime.now();

    @Column(name = "task_deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "{pl.lucky.model.Task.taskDeadLine.FutureOrPresent}")
    @NotNull(message = "{pl.lucky.model.NotEmpty}")
    private LocalDate taskDeadline;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String taskSubject, String taskDetails, LocalDateTime taskCreateTime, LocalDate taskDeadline, User user) {
        this.taskSubject = taskSubject;
        this.taskDetails = taskDetails;
        this.taskCreateTime = taskCreateTime;
        this.taskDeadline = taskDeadline;
        this.user = user;
    }
}
