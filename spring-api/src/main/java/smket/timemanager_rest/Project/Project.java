package smket.timemanager_rest.Project;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pId;

    @Column
    @NonNull
    private String pName;

    @Column
    private String pDescription;

    @Column
    private String totalTimeString;

    @Column
    private long totalTimeMillis;

    @Column
    private long configTimeMillis;


}

