package smket.timemanager_rest.Project;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ProjectDto {
    long pId;
    String pName;
    String pDescription;
    String totalTimeString;
    String configTimeString;

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
