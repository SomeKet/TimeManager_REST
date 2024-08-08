package smket.timemanager_rest.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import smket.timemanager_rest.Project.Project;
import smket.timemanager_rest.Project.ProjectException;
import smket.timemanager_rest.Project.ProjectRepository;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class EntryServiceTest {

    @InjectMocks
    EntryService entryService;

    @Mock
    EntryRepository entryRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    TimeFormatterService timeFormatterService;

    Project project;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        project = new Project();

        project.setPName("Test");
        project.setPId(1L);

        //entryService = new EntryService(entryRepository, projectRepository, timeFormatterService);

    }

    @Test
    void createEntryDefault_success() throws ProjectException {
        //Arrange
        Entry a = new Entry();
        LocalDate date = LocalDate.now();
        long expectedId = 1L;

        when(projectRepository.getProjectByName(project.getPName())).thenReturn(project);

        a = Entry.builder().eId(expectedId).project(project).date(date).build();
        when(entryRepository.save(any(Entry.class))).thenAnswer(invocation -> {
            Entry entry = invocation.getArgument(0);
            entry.setEId(expectedId); // Set the ID as expected
            return entry;
        });

        //Act
        long actualId = entryService.createEntryDefault(project.getPName(), date);

        //Assert
        assertEquals(expectedId, actualId);
        verify(entryRepository, times(1)).save(any(Entry.class));
    }


}
