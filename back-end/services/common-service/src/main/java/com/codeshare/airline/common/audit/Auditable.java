package com.codeshare.airline.common.audit;


public interface Auditable {

    void setCreatedAt(java.time.LocalDateTime createdAt);
    void setUpdatedAt(java.time.LocalDateTime updatedAt);

    void setCreatedBy(String user);
    void setUpdatedBy(String user);

    void setTransactionId(String txnId);

}
