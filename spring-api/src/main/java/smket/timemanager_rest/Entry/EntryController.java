package smket.timemanager_rest.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smket.timemanager_rest.Project.ProjectException;

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
    public ResponseEntity<?> createEntryDefault(@RequestParam ("name")String pName) {
        try{
            return ResponseEntity.ok(entryService.createEntryDefault(pName));
        }catch(ProjectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //API um Entry zu vervollständigen
    @PostMapping("/setEntry/{pId}/{eId}")
    public ResponseEntity<?> setEntry(@PathVariable long eId,
                                      @PathVariable long pId,
                                      @RequestBody EntryValuesDto entryValuesDto) {
        try{
            return ResponseEntity.ok(entryService.setEntry(eId,pId, entryValuesDto));
        }catch (EntryException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
