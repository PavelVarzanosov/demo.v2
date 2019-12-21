package com.example.demo.repositories;

import com.example.demo.model.Widget;
import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IWidgetRepository {

    Widget save(Widget widget);

    Widget update(Widget widget) throws NotFoundException;

    Widget findById(UUID id) throws NotFoundException;

    void deleteById(UUID id) throws NotFoundException;

    void deleteAll();

    List<Widget> findAll();
}
