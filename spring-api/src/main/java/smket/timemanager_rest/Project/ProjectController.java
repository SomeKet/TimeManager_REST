package smket.timemanager_rest.Project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping("projectAPI")

public class ProjectController {

    ProjectService projectService;
    ObjectMapper objectMapper = new ObjectMapper();

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/createProject")
    public ResponseEntity<?> createProject(@RequestParam("name") String name, @RequestBody Optional<String> description) {
        try{
            ProjectDto project = projectService.createProject(name, description);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteProject/{name}")
    public ResponseEntity<String> deleteProject(@PathVariable("name") String name) throws JsonProcessingException {
        try{
            String result = projectService.deleteProject(name);
            return new ResponseEntity<>(objectMapper.writeValueAsString(result), HttpStatus.OK);
        }catch(ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/projectInfos/{name}")
    public ResponseEntity<?> getProjectInfos(@PathVariable("name") String name){
        try{
            return new ResponseEntity<>(projectService.projectInfos(name), HttpStatus.OK);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/changeProjectName/{name}")
    public ResponseEntity<?> changeProjectName(@RequestParam ("newName") String newName, @PathVariable String name){
        try{
            return new ResponseEntity<>(projectService.changeProjectName(name, newName), HttpStatus.ACCEPTED);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @PutMapping("/changeProjectDescription/{name}")
    public ResponseEntity<?> changeProjectDescription(@PathVariable String name, @RequestBody String description){
        try{
            return new ResponseEntity<>(projectService.changeProjectDescription(name, description), HttpStatus.ACCEPTED);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());        }
    }

    @PutMapping("/addTime/{name}")
    public ResponseEntity<?> addTime(@PathVariable ("name") String name, @RequestBody String time){
        try{
            return new ResponseEntity<>(projectService.addTime(name, time), HttpStatus.ACCEPTED);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());        }
    }

    @PutMapping("/deleteTime/{name}")
    public ResponseEntity<?> deleteTime(@PathVariable ("name") String name, @RequestBody String time){
        try{
            return new ResponseEntity<>(projectService.deleteTime(name, time), HttpStatus.ACCEPTED);
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());        }
    }




}
