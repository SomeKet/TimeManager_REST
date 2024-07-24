package smket.timemanager_rest.Project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
public class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    TimeFormatterService timeFormatterService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_success() throws Exception {
        //Arrange
        String pName = "Test";
        Optional<String> description = Optional.of("Description");

        //act
        when(projectRepository.getProjectByName(pName)).thenReturn(null);
        ProjectDto createdProject = projectService.createProject(pName, description);

        //Assert
        Assertions.assertNotNull(createdProject);
        Assertions.assertEquals(pName, createdProject.getPName());
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void createProject_failure() throws Exception {
        //Arrange
        String pName = "Test";

        //Act
        when(projectRepository.getProjectByName(pName)).thenReturn(new Project());

        //Assert
        Assertions.assertThrows(ProjectException.class, () -> projectService.createProject(pName, Optional.empty()));

    }
}
