package com.codeshare.airline.master.common.base;

import java.util.List;

public interface BaseService<D, ID> {

    D create(D dto);

    D update(ID id, D dto);

    D getById(ID id);

    List<D> getAll();

    void delete(ID id);
}