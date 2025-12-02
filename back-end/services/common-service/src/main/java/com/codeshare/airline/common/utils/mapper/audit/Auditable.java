package com.codeshare.airline.common.utils.mapper.audit;

import java.time.LocalDateTime;

public interface Auditable {

    // Creation / Update
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);

    void setCreatedBy(String user);
    void setUpdatedBy(String user);

    void setTransactionId(String txnId);

    // Soft Delete
    void setIsDeleted(Boolean deleted);
    void setDeletedAt(LocalDateTime deletedAt);
    void setDeletedBy(String deletedBy);

    // Active Flag
    void setActive(Boolean active);
}
