package smket.timemanager_rest.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("FROM Project WHERE pId = ?1")
    Project getProjectById(Long id);

    @Query("FROM Project")
    List<Project> getAllProjects();

    @Query("FROM Project WHERE pName = ?1")
    Project getProjectByName(String name);
}
