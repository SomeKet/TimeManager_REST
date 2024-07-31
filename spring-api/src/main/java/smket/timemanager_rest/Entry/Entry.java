package smket.timemanager_rest.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import smket.timemanager_rest.Project.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long eId;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate date;

    @Column
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    LocalDateTime startTime;

    @Column
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    LocalDateTime endTime;

    @Column
    @JsonFormat(pattern="HH:mm")
    String breakTime;

    @Column
    @JsonFormat(pattern="HH:mm")
    String totalTimeString;

    @Column
    long totalTimeMillis;

    @Column
    boolean completed;

    @ManyToOne
    @JoinColumn(name ="project_id")
    private Project project;

}
