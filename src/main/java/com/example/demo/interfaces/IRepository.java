package com.example.demo.interfaces;

import com.example.demo.model.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRepository
        extends CrudRepository<Widget, UUID> {
}