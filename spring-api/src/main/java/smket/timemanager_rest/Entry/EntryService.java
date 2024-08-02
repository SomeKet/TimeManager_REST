package smket.timemanager_rest.Entry;

import org.springframework.stereotype.Service;
import smket.timemanager_rest.Project.ProjectException;
import smket.timemanager_rest.Project.ProjectRepository;
import smket.timemanager_rest.TimeFormatterService.TimeFormatterService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntryService {


    EntryRepository entryRepository;
    ProjectRepository projectRepository;
    TimeFormatterService timeFormatterService;


    public EntryService(EntryRepository entryRepository, ProjectRepository projectRepository, TimeFormatterService timeFormatterService){
        this.entryRepository = entryRepository;
        this.projectRepository = projectRepository;
        this.timeFormatterService = timeFormatterService;
    }

    public long createEntryDefault(String pName) throws ProjectException {
        if(projectRepository.getProjectByName(pName) == null){
            throw new ProjectException("Project with name " + pName + " does not exist");
        }
        Entry defaultEntry = Entry.builder()
                .project(projectRepository.getProjectByName(pName))
                .build();
        entryRepository.save(defaultEntry);
        return defaultEntry.getEId();
    }

    public EntryDto moveEntryToProject(long eId, String pName) throws ProjectException {
        return null;
    }

    public EntryDto setEntryComplete(long eId, String pName,
                                     EntryValuesDto entryValuesDto
                                   ) throws EntryException {

        if(!entryRepository.getEntryById(eId).getProject().getPName().equals(pName)){
            throw new EntryException("Mismatch between project and entry");
        }
        if(!checkIfDateIsFree(entryValuesDto.getDate(), pName)){
            throw new EntryException("Date already freed");
        }

        //Values
        LocalDateTime entryStart = entryValuesDto.getStartTime();
        LocalDateTime entryEnd = entryValuesDto.getEndTime();
        String entryBreakTime = "00:00";
        long totalTimeInMillis=0;
        Entry entry = entryRepository.getEntryById(eId);
        if(entry == null){
            throw new EntryException("Entry with id " + eId + " does not exist");
        }

        //timeCheck
        if(!entryValuesDto.getBreakTime().isEmpty()) entryBreakTime = entryValuesDto.getBreakTime();

        if(entryStart != null && entryEnd != null){
            if(!timeFormatterService.checkIfTimeIsCorrectChose(timeFormatterService.convertLocalDateTime(entryStart), timeFormatterService.convertLocalDateTime(entryEnd))){
                throw new EntryException("Finish time cannot be less than start time");
            }
            entry.setStartTime(entryValuesDto.getStartTime());
            entry.setEndTime(entryValuesDto.getEndTime());
            entry.setBreakTime(entryValuesDto.getBreakTime());

            totalTimeInMillis = timeFormatterService.convertLocalDateTime(entryEnd)
                    - timeFormatterService.convertLocalDateTime(entryStart)
                    - timeFormatterService.stringInMillis(entryBreakTime);
        }

        entry.setDate(entryValuesDto.getDate());
        entry.setTotalTimeMillis(totalTimeInMillis);
        entry.setTotalTimeString(timeFormatterService.millisInString(totalTimeInMillis));
        entry.setCompleted(entryComplete(entry));

        entryRepository.save(entry);
        return convertEntryToDto(entry);
    }

    public EntryDto convertEntryToDto(Entry entry){
        return EntryDto.builder()
                .eId(entry.eId)
                .date(entry.date)
                .startTime(entry.startTime)
                .endTime(entry.endTime)
                .breakTimeString(entry.breakTime)
                .totalTimeString(entry.totalTimeString)
                .totalTimeMillis(entry.totalTimeMillis)
                .complete(entry.completed)
                .pId(entry.getProject().getPId())
                .build();
    }

    public String deleteEntry(long eId, String pName) throws EntryException {
        Entry deleteEntry = entryRepository.getEntryById(eId);
        if(!deleteEntry.getProject().getPName().equals(pName)){
            throw new EntryException("Mismatch between project and entry");
        }else{
            entryRepository.delete(deleteEntry);
            return "Entry deleted successfully";
        }
    }




    private boolean entryComplete(Entry entry){

        return entry.startTime != null  && entry.endTime != null && entry.getProject() != null && entry.date != null;
    }

    private boolean checkIfDateIsFree(LocalDate date, String pName){
        boolean flag = true;
        List<Entry> entries = projectRepository.getProjectByName(pName).getEntries();
        for(Entry entry : entries){
            if(entry.getDate().isEqual(date)){
                flag = false;
                break;
            }
        }
        return flag;
    }

}
