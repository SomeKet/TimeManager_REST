package smket.timemanager_rest.Project;

import jakarta.persistence.*;
import lombok.*;
import smket.timemanager_rest.Entry.Entry;

import java.util.ArrayList;
import java.util.List;

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
    private String pName;

    @Column
    private String pDescription;

    @Column
    private String totalTimeString;

    @Column
    private long totalTimeMillis;

    @Column
    private long configTimeMillis;

    @OneToMany(mappedBy = "project", cascade= CascadeType.ALL, orphanRemoval = true)
    List<Entry> entries = new ArrayList<>();


}

