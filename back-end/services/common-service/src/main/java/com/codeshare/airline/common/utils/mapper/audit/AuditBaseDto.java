package com.codeshare.airline.common.utils.mapper.audit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuditBaseDto {

    private AuditInfo audit = new AuditInfo();

}
