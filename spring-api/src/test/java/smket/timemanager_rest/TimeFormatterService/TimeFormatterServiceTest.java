package smket.timemanager_rest.TimeFormatterService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeFormatterServiceTest {

    @InjectMocks
    TimeFormatterService timeFormatterService;

    @Test
    void millisInString(){
        //Arrange
        long timeMillis_1h = 3600000;
        long timeMillis_3h = timeMillis_1h * 3;
        long timeMillis_1_30h = timeMillis_1h + (timeMillis_1h/2);

        //Act
        String times_1h = timeFormatterService.millisInString(timeMillis_1h);
        String times_3h = timeFormatterService.millisInString(timeMillis_3h);
        String times_1_30h = timeFormatterService.millisInString(timeMillis_1_30h);

        //Assertions
        Assertions.assertEquals("01:00", times_1h);
        Assertions.assertEquals("03:00", times_3h);
        Assertions.assertEquals("01:30", times_1_30h);

    }

    @Test
    void stringInMillis(){
        String oneH = "01:00";
        String threeH = "03:00";
        String oneHalfe = "01:30";
        long hour = 3600000;

        long oneHmillis = timeFormatterService.stringInMillis(oneH);
        long threeHmillis = timeFormatterService.stringInMillis(threeH);
        long oneHalfeMillis = timeFormatterService.stringInMillis(oneHalfe);

        Assertions.assertEquals(hour, oneHmillis);
        Assertions.assertEquals(hour * 3, threeHmillis);
        Assertions.assertEquals( hour * 1.5, oneHalfeMillis);
    }
}
