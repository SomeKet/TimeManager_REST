package smket.timemanager_rest.Project;

import lombok.Data;
import smket.timemanager_rest.Entry.EntryDto;

import java.util.List;

@Data
public class ProjectDto {
    private long pId;
    private String pName;
    private String pDescription;
    private String totalTimeString;
    private String configTimeString;
    private List<EntryDto> entries;

    @Override
    public String toString() {
        return "ProjectDto{" +
                "pId=" + pId +
                ", pName='" + pName + '\'' +
                ", pDescription='" + pDescription + '\'' +
                ", totalTimeString='" + totalTimeString + '\'' +
                ", configTimeString='" + configTimeString + '\'' +
                '}';
    }
}
