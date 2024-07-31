package smket.timemanager_rest.TimeFormatterService;

import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class TimeFormatterService {

    final ZoneId zoneId = ZoneId.of("Europe/Berlin");
    /*
        TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
              --Muss da sonst bei "01:00", "01:60" Ã¼bersetzt werden wÃ¼rde--
         */
    public String millisInString(long millis){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    public long stringInMillis(String time){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime parsedTime = LocalTime.parse(time, timeFormatter);
        return parsedTime.toSecondOfDay() * 1000L;
    }

    public long convertLocalDateTime(LocalDateTime time){
        return time.atZone(zoneId).toInstant().toEpochMilli();
    }

    public boolean checkTimeChange(long startTime_1, long endTime_1, long startTime_2, long endTime_2) {
        return (startTime_2 < startTime_1 && endTime_2 < startTime_1) || (startTime_2 > startTime_1 && startTime_2 > endTime_1);
    }

    public boolean checkIfTimeIsCorrectChose(long startTime, long endTime) {
        return startTime <= endTime;
    }

}
