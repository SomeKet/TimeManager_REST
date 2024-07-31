package smket.timemanager_rest.Entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query("FROM Entry WHERE eId = ?1")
    Entry getEntryById(long id);

    @Query("FROM Entry WHERE project.pId = ?1")
    List<Entry> getEntriesByProject(long projectId);


}
