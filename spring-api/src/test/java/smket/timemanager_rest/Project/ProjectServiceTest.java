package smket.timemanager_rest.Project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.util.Arrays;
import java.util.List;
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
    Project test = new Project();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_success() throws ProjectException {
        //Arrange
        String pName = "Test";
        Optional<String> description = Optional.of("Description");

        when(projectRepository.getProjectByName(pName)).thenReturn(null);

        //act
        ProjectDto createdProject = projectService.createProject(pName, description);

        //Assert
        Assertions.assertNotNull(createdProject);
        Assertions.assertEquals(pName, createdProject.getPName());
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void createProject_failure(){
        //Arrange
        String pName = "Test";

        //Act
        when(projectRepository.getProjectByName(pName)).thenReturn(test);

        //Assert
        ProjectException createProject_failure = Assertions.assertThrows(ProjectException.class,
                ()-> projectService.createProject(pName, Optional.empty()));
        Assertions.assertEquals("Project already exists", createProject_failure.getMessage());

    }

    @Test
    void deleteProject_success() throws ProjectException{
        //Act
        when(projectRepository.getProjectByName(test.getPName())).thenReturn(test);
        String delete_success = projectService.deleteProject(test.getPName());

        //Assert
        Assertions.assertNotNull(delete_success);
        Assertions.assertEquals("Project deleted successfully", delete_success);
        verify(projectRepository, times(1)).delete(any(Project.class));

    }

    @Test
    void deleteProject_failure() {
        //Arrange
        String pName = "Test";

        //Act
        when(projectRepository.getProjectByName(pName)).thenReturn(null);

        //Assert
        ProjectException deleteProject_failure = Assertions.assertThrows(ProjectException.class, () ->
                projectService.deleteProject(pName));
        Assertions.assertEquals("Project not found", deleteProject_failure.getMessage());
        verify(projectRepository, times(0)).delete(any(Project.class));


    }

    @Test
    void changeProjectName_success() throws ProjectException{
        //Arrange
        String newName = "Test new Name";


        when(projectRepository.getProjectByName(test.getPName())).thenReturn(test);
        when(projectRepository.getProjectByName(newName)).thenReturn(null);

        //Act
        ProjectDto test_success = projectService.changeProjectName(test.getPName(), newName);

        //Assert
        Assertions.assertNotNull(test_success);
        Assertions.assertEquals(newName, test_success.getPName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void changeProjectName_failure_noProject(){
        //Arrange
        String pName = "Test";
        String newName = "Test new Name";

        when(projectRepository.getProjectByName(pName)).thenReturn(null);

        //Act + Assert
        ProjectException changeProjectName_failure = Assertions.assertThrows(ProjectException.class,
                ()-> projectService.changeProjectName(pName, newName));
        Assertions.assertEquals("Project not found", changeProjectName_failure.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void changeProjectName_failure_NameAlreadyExists(){
        //Arrange
        String pName = "Test";
        String newName = "Test new Name";

        when(projectRepository.getProjectByName(pName)).thenReturn(test);
        when(projectRepository.getProjectByName(newName)).thenReturn(test);

        //Act + Assert
        ProjectException failed = Assertions.assertThrows(ProjectException.class,
                ()-> projectService.changeProjectName(pName, newName));
        Assertions.assertEquals("Project already exists", failed.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));

    }

    @Test
    void changeProjectDescription_success() throws ProjectException{
        //Arrange
        String newDescription = "Hallo";
        String pName = "Test";
        Project test = new Project(pName);

        when(projectRepository.getProjectByName(pName)).thenReturn(test);

        //Act
        ProjectDto success = projectService.changeProjectDescription(pName, newDescription);

        //Assert
        Assertions.assertNotNull(success);
        Assertions.assertEquals(newDescription, success.getPDescription());
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void changeProjectDescription_failed(){
        //Arrange
        String pName = "Test";
        String newDescription = "Hallo";

        when(projectRepository.getProjectByName(pName)).thenReturn(null);

        //Act+Assert
        ProjectException failed = Assertions.assertThrows(ProjectException.class,
                ()->projectService.changeProjectDescription(pName, newDescription));
        Assertions.assertEquals("Project not found", failed.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));

    }

    @Test
    void projectInfos_success()throws ProjectException{
        //Arrange

        when(projectRepository.getProjectByName(test.getPName())).thenReturn(test);

        //act
        ProjectDto success = projectService.projectInfos(test.getPName());


        //Assertions
        Assertions.assertNotNull(success);
        Assertions.assertEquals(success.getPName(), test.getPName());


    }

    @Test
    void projectInfos_failure() {
        //Arrange
        String pName = "Test";

        when(projectRepository.getProjectByName(pName)).thenReturn(null);

        //Act+Assertions
        ProjectException projectException = Assertions.assertThrows(ProjectException.class,
                () -> projectService.projectInfos(pName));
        Assertions.assertEquals("Project not found", projectException.getMessage());
        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void addTime_success() throws ProjectException{
        //Arrange
        String timeString = "01:00";
        long timeMillis = 3600000;
        test.setTotalTimeMillis(0);
        test.setConfigTimeMillis(0);

        when(projectRepository.getProjectByName(test.getPName())).thenReturn(test);
        when(timeFormatterService.stringInMillis(timeString)).thenReturn(timeMillis);
        when(timeFormatterService.millisInString(timeMillis)).thenReturn(timeString);

        //act
        ProjectDto success = projectService.addTime(test.getPName(), timeString);

        //Assert
        Assertions.assertNotNull(success);
        Assertions.assertEquals(timeMillis, test.getConfigTimeMillis());
        Assertions.assertEquals(timeString, test.getTotalTimeString());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void addTime_failure() {
        //Arrange
        String time = "01:00";
        String name = "Test";

        when(projectRepository.getProjectByName(name)).thenReturn(null);

        //Act+Assert
        ProjectException failed = Assertions.assertThrows(ProjectException.class,
                ()-> projectService.addTime(name,time));
        Assertions.assertEquals("Project not found", failed.getMessage());
    }

    @Test
    void deleteTime_success() throws ProjectException {
        //Arrange
        String deleteTimeString = "01:00";
        long timeMillis = 3600000;
        test.setTotalTimeString("02:00");
        test.setConfigTimeMillis(7200000);
        test.setTotalTimeMillis(7200000);

        when(projectRepository.getProjectByName(test.getPName())).thenReturn(test);
        when(timeFormatterService.stringInMillis(deleteTimeString)).thenReturn(timeMillis);
        when(timeFormatterService.millisInString(test.getTotalTimeMillis() - timeMillis)).thenReturn("01:00");

        //Act
        ProjectDto success = projectService.deleteTime(test.getPName(), deleteTimeString);

        //Assertions
        Assertions.assertNotNull(success);
        Assertions.assertEquals("01:00", success.getConfigTimeString());
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void deleteTime_failure() {
        //Arrange
        String pname = "Test";
        String deleteTimeString = "01:00";
        long timeMillis = 3600000;

        when(projectRepository.getProjectByName(test.getPName())).thenReturn(null);

        //Act+Assert
        ProjectException failed = Assertions.assertThrows(ProjectException.class,
                () -> projectService.deleteTime(pname, deleteTimeString));
        Assertions.assertEquals("Project not found", failed.getMessage());
    }

    @Test
    void getAllProjects_success() throws ProjectException{
        //Arrange
        Project p1 = new Project("Test1");
        Project p2 = new Project("Test2");
        Project p3 = new Project("Test3");

        List<Project> success = Arrays.asList(p1, p2, p3);
        when(projectRepository.getAllProjects()).thenReturn(success);

        //act
        List<ProjectDto> allProjects = projectService.getAllProjects();

        //Assertions
        Assertions.assertNotNull(allProjects);
        Assertions.assertEquals(success.size(), allProjects.size());

    }

    @Test
    void getAllProjects_failure() {
        //Arrange

        when(projectRepository.getAllProjects()).thenReturn(null);

        //Act + Assertions
        ProjectException failed = Assertions.assertThrows(ProjectException.class,
                () -> projectService.getAllProjects());
        Assertions.assertEquals("No projects found", failed.getMessage());
    }
}
