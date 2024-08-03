package smket.timemanager_rest.Project;

import org.springframework.stereotype.Service;
import smket.timemanager_rest.Entry.Entry;
import smket.timemanager_rest.Entry.EntryDto;
import smket.timemanager_rest.Entry.EntryService;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    TimeFormatterService timeFormatterService;
    EntryService entryService;

    public ProjectService(ProjectRepository projectRepository, TimeFormatterService timeFormatterService, EntryService entryService){
        this.projectRepository = projectRepository;
        this.timeFormatterService = timeFormatterService;
        this.entryService = entryService;
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
            return convertProjectToDto(createdProject);
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
            return convertProjectToDto(project);
        }else{
            throw new ProjectException("Project not found");
        }

    }

    public ProjectDto changeProjectDescription(String name, String newDescription)throws ProjectException {
        Project project = projectRepository.getProjectByName(name);
        if (project != null) {
            project.setPDescription(newDescription);
            projectRepository.save(project);
            return convertProjectToDto(project);
        } else {
            throw new ProjectException("Project not found");
        }
    }

    public ProjectDto projectInfos(String name)throws ProjectException{
        if(projectRepository.getProjectByName(name)==null){
            throw new ProjectException("Project not found");
        }
        return convertProjectToDto(projectRepository.getProjectByName(name));
    }

    public ProjectDto addTime(String name, String time) throws ProjectException {
        Project project = projectRepository.getProjectByName(name);
        if(project!= null){
            long millis = timeFormatterService.stringInMillis(time);
            project.setConfigTimeMillis(project.getConfigTimeMillis() + millis);
            project.setTotalTimeMillis(project.getTotalTimeMillis() + millis);
            project.setTotalTimeString(timeFormatterService.millisInString(project.getTotalTimeMillis()));
            projectRepository.save(project);
            return convertProjectToDto(project);
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
            return convertProjectToDto(project);
        }else{
            throw new ProjectException("Project not found");
        }
    }

    public List<ProjectDto> getAllProjects() throws ProjectException {
        List<Project> projects = projectRepository.getAllProjects();
        if(projects == null){
            throw new ProjectException("No projects found");
        }else{
            List<ProjectDto> projectDto = new ArrayList<>();
            for(Project project : projects){
                projectDto.add(convertProjectToDto(project));
            }
            return projectDto;
        }
    }

    public List<EntryDto> getAllEntries(String name) throws ProjectException {
        if(projectRepository.getProjectByName(name)==null){
            throw new ProjectException("Project not found");
        }else{
            return projectRepository.getProjectByName(name).getEntries().stream().map(e -> entryService.convertEntryToDto(e)).toList();
        }

    }


    //Private Methodes
    private ProjectDto convertProjectToDto(Project project){
        ProjectDto transformedProject = new ProjectDto();
        transformedProject.setPId(project.getPId());
        transformedProject.setPName(project.getPName());
        transformedProject.setPDescription(project.getPDescription());
        transformedProject.setTotalTimeString(project.getTotalTimeString());
        transformedProject.setConfigTimeString(timeFormatterService.millisInString(project.getConfigTimeMillis()));
        List<Entry> entries = project.getEntries();
        if (entries == null) {
            entries = new ArrayList<>();
        }

        transformedProject.setEntries(entries.stream()
                .map(entry -> entryService.convertEntryToDto(entry))
                .collect(Collectors.toList()));


        return transformedProject;
    }

}
