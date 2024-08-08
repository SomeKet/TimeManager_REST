package smket.timemanager_rest.Entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryDto {

    private long eId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String breakTimeString;
    private String totalTimeString;
    private long totalTimeMillis;
    private long pId;
    private boolean complete;

    @Override
    public String toString() {
        return "EntryDto{" +
                "eId=" + eId +
                ", date=" + date +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", breakTime='" + breakTimeString + '\'' +
                ", totalTimeString='" + totalTimeString + '\'' +
                ", totalTimeMillis=" + totalTimeMillis +
                ", complete=" + complete +
                ", pId=" + pId +
                '}';
    }
}
