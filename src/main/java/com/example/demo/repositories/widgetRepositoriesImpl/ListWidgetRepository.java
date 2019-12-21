package com.example.demo.repositories.widgetRepositoriesImpl;

import com.example.demo.model.Widget;
import com.example.demo.repositories.IWidgetRepository;
import javassist.NotFoundException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListWidgetRepository implements IWidgetRepository {

    private CopyOnWriteArrayList<Widget> widgetList;
    private final Object lock = new Object();

    public Widget save(Widget widget) {
        widgetList.add(widget);
        return widget;
    }

    public Widget update(Widget widget) throws NotFoundException {
        Optional<Widget> optionalWidget = widgetList.stream().filter((w) -> w.getWidgetId().equals(widget.getWidgetId())).findFirst();

        if (optionalWidget.isPresent()) {
            synchronized (lock) {
                widgetList.remove(optionalWidget.get());
                optionalWidget.get()
                        .updateWidget(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), widget.getZIndex(), new Date());
                widgetList.add(optionalWidget.get());
                return optionalWidget.get();
            }
        } else {
            throw new NotFoundException("Widget with id " + widget.getWidgetId() + " not found");
        }
    }

    public Widget findById(UUID id) throws NotFoundException {
        Optional<Widget> optionalWidget = widgetList.stream().filter((w) -> w.getWidgetId().equals(id)).findFirst();

        if (optionalWidget.isPresent()) {
            return optionalWidget.get();
        } else {
            throw new NotFoundException("Widget with id " + id + " not found");
        }
    }

    public void deleteById(UUID id) throws NotFoundException {
        Optional<Widget> optionalWidget = widgetList.stream().filter((w) -> w.getWidgetId().equals(id)).findFirst();

        if(optionalWidget.isPresent())
        {
            Widget deleteWidget = optionalWidget.get();
            synchronized (lock) {
                widgetList.remove(deleteWidget);
            }
        } else {
            throw new NotFoundException("Widget with id " + id + " not found");
        }
    }

    public void deleteAll() {
        widgetList = new CopyOnWriteArrayList<>();
    }

    public List<Widget> findAll() {
        return widgetList;
    }
}
