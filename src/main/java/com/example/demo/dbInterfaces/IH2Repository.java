package com.example.demo.dbInterfaces;

import com.example.demo.model.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IH2Repository
        extends CrudRepository<Widget, UUID> {
}