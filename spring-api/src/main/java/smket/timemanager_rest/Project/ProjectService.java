package smket.timemanager_rest.Project;

import org.springframework.stereotype.Service;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.util.Optional;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    TimeFormatterService timeFormatterService;

    public ProjectService(ProjectRepository projectRepository, TimeFormatterService timeFormatterService){
        this.projectRepository = projectRepository;
        this.timeFormatterService = timeFormatterService;
    }

    public ProjectDto createProject(String name, Optional<String> description) throws ProjectException {
        String pDescription = description.orElse("");

        if(projectRepository.getProjectByName(name)==null){
            Project createdProject = Project.builder()
                    .pName(name)
                    .pDescription(pDescription)
                    .totalTimeString("00:00")
                    .totalTimeMillis(0)
                    .configTimeMillis(0)
                    .build();
            projectRepository.save(createdProject);
            return projectDtoTransform(createdProject);
        }else{
            throw new ProjectException("Project already exists");
        }
    }

    public String deleteProject(String name) throws ProjectException {
        Project project = projectRepository.getProjectByName(name);
        if(project != null){
            projectRepository.delete(project);
            return "Project deleted successfully";
        }else {
            throw new ProjectException("Project not found");
        }
    }

    public ProjectDto changeProjectName(String oldName, String newName) throws ProjectException{
        Project project = projectRepository.getProjectByName(oldName);

        if(projectRepository.getProjectByName(newName)!= null) {
            throw new ProjectException("Project already exists");
        }
        if(project!=null){
            project.setPName(newName);
            projectRepository.save(project);
            return projectDtoTransform(project);
        }else{
            throw new ProjectException("Project not found");
        }

    }

    public ProjectDto changeProjectDescription(String name, String newDescription)throws ProjectException {
        Project project = projectRepository.getProjectByName(name);
        if (project != null) {
            project.setPDescription(newDescription);
            return projectDtoTransform(project);
        } else {
            throw new ProjectException("Project not found");
        }
    }

    public ProjectDto projectInfos(String name)throws ProjectException{
        if(projectRepository.getProjectByName(name)==null){
            throw new ProjectException("Project not found");
        }
        return projectDtoTransform(projectRepository.getProjectByName(name));
    }

    public ProjectDto addTime(String name, String time) throws ProjectException {
        Project project = projectRepository.getProjectByName(name);
        if(project!= null){
            long millis = timeFormatterService.stringInMillis(time);
            project.setConfigTimeMillis(project.getConfigTimeMillis() + millis);
            project.setTotalTimeMillis(project.getTotalTimeMillis() + millis);
            project.setTotalTimeString(timeFormatterService.millisInString(project.getTotalTimeMillis()));
            projectRepository.save(project);
            return projectDtoTransform(project);
        }else{
            throw new ProjectException("Project not found");
        }

    }

    public ProjectDto deleteTime(String name, String time) throws ProjectException{
        Project project = projectRepository.getProjectByName(name);
        if(project!=null){
            long millis = timeFormatterService.stringInMillis(time);
            project.setConfigTimeMillis(project.getConfigTimeMillis() - millis);
            project.setTotalTimeMillis(project.getTotalTimeMillis() - millis);
            project.setTotalTimeString(timeFormatterService.millisInString(project.getTotalTimeMillis()));
            projectRepository.save(project);
            return projectDtoTransform(project);
        }else{
            throw new ProjectException("Project not found");
        }
    }


    //Private Methodes
    private ProjectDto projectDtoTransform(Project project){
        ProjectDto transformedProject = new ProjectDto();
        transformedProject.setPId(project.getPId());
        transformedProject.setPName(project.getPName());
        transformedProject.setPDescription(project.getPDescription());
        transformedProject.setTotalTimeString(project.getTotalTimeString());
        transformedProject.setConfigTimeString(timeFormatterService.millisInString(project.getConfigTimeMillis()));

        return transformedProject;
    }
}
