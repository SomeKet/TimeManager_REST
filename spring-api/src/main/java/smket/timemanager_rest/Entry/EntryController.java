package smket.timemanager_rest.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smket.timemanager_rest.Project.ProjectException;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping("entryAPI")
public class EntryController {

    final EntryService entryService;
    final ObjectMapper mapper = new ObjectMapper();

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/createEntryDefault")
    public ResponseEntity<?> createEntryDefault(@RequestParam ("name")String pName, @RequestParam("date")LocalDate date) {
        try{
            return ResponseEntity.ok(entryService.createEntryDefault(pName, date));
        }catch(ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //API um Entry zu vervollständigen
    @PostMapping("/setEntry/{pName}/{eId}")
    public ResponseEntity<?> setEntry(@PathVariable long eId,
                                      @PathVariable String pName,
                                      @RequestBody EntryValuesDto entryValuesDto) {
        try{
            return ResponseEntity.ok(entryService.setEntryComplete(eId,pName, entryValuesDto));
        }catch (EntryException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteEntry/{pName}/{eId}")
    public ResponseEntity<String> deleteEntry(@PathVariable long eId, @PathVariable String pName){
        try{
            String response = entryService.deleteEntry(eId, pName);
            return ResponseEntity.ok(mapper.writeValueAsString(response));
        }catch (EntryException | JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/changeDate/{pName}/{eId}")
    public ResponseEntity<?> changeDate(@PathVariable String pName, @PathVariable long eId, @RequestParam("date") LocalDate date){
        try{
            return ResponseEntity.ok(entryService.changeDate(eId, pName, date));
        }catch (EntryException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/moveEntry/{pName}/{eId}")
    public ResponseEntity<?> moveEntry(@PathVariable String pName, @PathVariable long eId){
        try{
            return ResponseEntity.ok(entryService.moveEntryToProject(eId, pName));
        }catch (ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
