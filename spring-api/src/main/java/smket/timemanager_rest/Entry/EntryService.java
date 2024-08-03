package smket.timemanager_rest.Entry;

import org.springframework.stereotype.Service;
import smket.timemanager_rest.Project.Project;
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

    public long createEntryDefault(String pName, LocalDate date) throws ProjectException {
        if(projectRepository.getProjectByName(pName) == null){
            throw new ProjectException("Project with name " + pName + " does not exist");
        }else if(checkIfDateIsUsed(date, pName)){
            throw new ProjectException("Date " + date + " already exists");
        }else{
            Entry defaultEntry = Entry.builder()
                    .project(projectRepository.getProjectByName(pName))
                    .date(date)
                    .build();
            entryRepository.save(defaultEntry);
            return defaultEntry.getEId();
        }

    }

    public EntryDto moveEntryToProject(long eId, String pName) throws ProjectException {
        Entry entry = entryRepository.getEntryById(eId);
        Project project = projectRepository.getProjectByName(pName);
        if(entry == null || project == null){
            throw new ProjectException("Fail");
        }
        if(checkIfDateIsUsed(entry.getDate(), pName)){
            throw new ProjectException("Date " + entry.getDate() + " already exists");
        }

        entry.setProject(project);
        entryRepository.save(entry);
        return convertEntryToDto(entry);

    }

    public EntryDto setEntryComplete(long eId, String pName,
                                     EntryValuesDto entryValuesDto
                                   ) throws EntryException {

        if(!entryRepository.getEntryById(eId).getProject().getPName().equals(pName)){
            throw new EntryException("Mismatch between project and entry");
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

    public EntryDto changeDate(long eId, String pName, LocalDate date) throws EntryException {
        if(checkIfDateIsUsed(date, pName)){
            throw new EntryException("Date already used");
        }else{
            Entry entry = entryRepository.getEntryById(eId);
            entry.setDate(date);
            entryRepository.save(entry);
            return convertEntryToDto(entry);
        }
    }



    private boolean entryComplete(Entry entry){

        return entry.startTime != null  && entry.endTime != null && entry.getProject() != null && entry.date != null;
    }

    private boolean checkIfDateIsUsed(LocalDate date, String pName){
        boolean flag = false;
        List<Entry> entries = projectRepository.getProjectByName(pName).getEntries();
        for(Entry entry : entries){
            if(entry.getDate().isEqual(date)){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
