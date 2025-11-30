package com.codeshare.airline.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface BaseRepository<T extends AbstractEntity, ID> extends JpaRepository<T, ID> {

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deleted = true, e.deletedAt = CURRENT_TIMESTAMP, e.deletedBy = :deletedBy WHERE e.id = :id")
    void softDelete(@Param("id") Long id, @Param("deletedBy") String deletedBy);

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deleted = false, e.deletedAt = null, e.deletedBy = null WHERE e.id = :id")
    void restore(@Param("id") Long id);

}
