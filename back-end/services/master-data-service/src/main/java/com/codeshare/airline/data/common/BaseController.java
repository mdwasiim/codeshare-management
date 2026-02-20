package com.codeshare.airline.data.common;

import com.codeshare.airline.persistence.persistence.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseController<D, ID> {

    protected final BaseService<D, ID> service;

    @PostMapping
    public D create(@RequestBody D dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public D update(@PathVariable ID id, @RequestBody D dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public D getById(@PathVariable ID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<D> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        service.delete(id);
    }
}