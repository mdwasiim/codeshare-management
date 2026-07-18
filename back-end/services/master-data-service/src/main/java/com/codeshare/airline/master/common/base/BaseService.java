package com.codeshare.airline.master.common.base;

import java.util.List;
import java.util.Map;

public interface BaseService<D, ID> {

    D create(D dto);

    D update(ID id, D dto);

    D getById(ID id);

    List<D> getAll();

    List<D> searchExact(Map<String, String> filters);

    void delete(ID id);
}
