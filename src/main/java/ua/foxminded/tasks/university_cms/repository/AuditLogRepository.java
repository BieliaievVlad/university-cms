package ua.foxminded.tasks.university_cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.tasks.university_cms.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog>{

    @Query("SELECT DISTINCT a.username FROM AuditLog a")
    List<String> findDistinctUsernames();
    
    @Query("SELECT DISTINCT a.tableName FROM AuditLog a")
    List<String> findDistinctTableNames();
    
    @Query("SELECT DISTINCT a.operationType FROM AuditLog a")
    List<String> findDistinctOperations();
}
