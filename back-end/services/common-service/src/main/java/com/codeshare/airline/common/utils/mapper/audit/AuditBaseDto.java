package com.codeshare.airline.common.utils.mapper.audit;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class   AuditBaseDto {

    private AuditInfo audit = new AuditInfo();

}
