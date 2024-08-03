package smket.timemanager_rest.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class EntryValuesDto {
    //private LocalDate date;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime startTime;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime endTime;
    @JsonFormat(pattern="HH:mm")
    private String breakTime;
}
