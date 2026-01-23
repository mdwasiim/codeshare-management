package com.codeshare.airline.persistence.repository;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface CSMDataBaseRepository<T extends CSMDataAbstractEntity, ID> extends JpaRepository<T, ID> {

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = true, e.deletedAt = CURRENT_TIMESTAMP, e.deletedBy = :deletedBy WHERE e.id = :id")
    void softDelete(@Param("id") ID id, @Param("deletedBy") String deletedBy);

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = false, e.deletedAt = null, e.deletedBy = null WHERE e.id = :id")
    void restore(@Param("id") ID id);
}

