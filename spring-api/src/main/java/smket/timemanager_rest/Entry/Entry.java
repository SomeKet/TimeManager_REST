package smket.timemanager_rest.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import smket.timemanager_rest.Project.Project;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long eId;

    @Column
    LocalDate date;

    @Column
    @JsonFormat(pattern="HH:mm")
    String startTimeString;

    @Column
    @JsonFormat(pattern="HH:mm")
    String endTimeString;

    @Column
    @JsonFormat(pattern="HH:mm")
    String breakTimeString;

    @Column
    @JsonFormat(pattern="HH:mm")
    String totalTimeString;

    @Column
    long totalTimeMillis;

    @Column
    boolean completed;

    @ManyToOne
    @JoinColumn(name ="project_id", nullable = false)
    private Project project;

}
