package com.example.demo.repositories.widgetRepositoriesImpl;

import com.example.demo.dbInterfaces.IH2Repository;
import com.example.demo.model.Widget;
import com.example.demo.repositories.IWidgetRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class H2WidgetRepository implements IWidgetRepository {

    @Autowired
    private IH2Repository h2Rep;

    public Widget save(Widget widget) {
        return h2Rep.save(widget);
    }

    public Widget update(Widget widget) throws NotFoundException {
        Optional<Widget> optionalWidget = h2Rep.findById(widget.getWidgetId());

        if (optionalWidget.isPresent()) {
            return h2Rep.save(widget);
        } else {
            throw new NotFoundException("Widget with id " + widget.getWidgetId() + " not found");
        }
    }

    public Widget findById(UUID id) throws NotFoundException {
        Optional<Widget> optionalWidget = h2Rep.findById(id);

        if (optionalWidget.isPresent()) {
            return optionalWidget.get();
        } else {
            throw new NotFoundException("Widget with id " + id + " not found");
        }
    }

    public void deleteById(UUID id) throws NotFoundException {
        Optional<Widget> optionalWidget = h2Rep.findById(id);

        if(optionalWidget.isPresent())
        {
            h2Rep.delete(optionalWidget.get());
        } else {
            throw new NotFoundException("Widget with id " + id + " not found");
        }
    }

    public void deleteAll() {
        h2Rep.deleteAll();
    }

    public List<Widget> findAll() {
        return StreamSupport.stream(h2Rep.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
