package com.example.demo.interfaces;

import com.example.demo.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IH2
        extends CrudRepository<Widget, UUID> {
}