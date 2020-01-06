package com.example.demo.services.widgetService.widgetServiceImpl;

import com.example.demo.model.Widget;
import com.example.demo.repositories.IWidgetRepository;
import com.example.demo.services.IWidgetService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class WidgetServiceImpl implements IWidgetService {

    @Autowired
    private IWidgetRepository repository;

    public synchronized Widget save(int x, int y, int width, int height, int zIndex) {
        List<Widget> widgetList = repository.findAll();
        if(widgetList != null) {
            if (widgetList
                    .stream()
                    .anyMatch((w) -> w.getZIndex() == zIndex))
            {
                widgetList.stream().filter((w) -> w.getZIndex() >= zIndex)
                        .map(Widget::incZIndex);
                widgetList.forEach((widget) -> repository.save(widget));
            }
        }
        Widget widget = new Widget(x,y,width,height,zIndex, new Date());
        repository.save(widget);
        return widget;
    }

    public Widget save(int x,int y,int width,int height) {
        int zIndex=0;
        List<Widget> widgetList = repository.findAll();

        if(widgetList != null) {
            zIndex = widgetList.stream()
                    .max(Comparator.comparingInt(Widget::getZIndex))
                    .get()
                    .getZIndex();
        }
        Widget widget = new Widget(x,y,width,height,zIndex+1,new Date());
        repository.save(widget);
        return widget;
    }

    public Widget findById(UUID id) throws NotFoundException {
       return repository.findById(id);
    }

    public Widget updateWidget(Widget widget) throws NotFoundException {
        return repository.update(widget);
    }

    public void deleteById(UUID  id) throws NotFoundException {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Widget> getWidgetListSorted() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .collect(Collectors.toList());
    }

    public List<Widget> getWidgetListSorted(int offset, int limit) {
        List<Widget> widgetList = repository.findAll();
        return(offset > widgetList.size()) ? new ArrayList<>() :
                widgetList.stream()
                        .sorted(Comparator.comparingInt(Widget::getZIndex))
                        .skip(offset)
                        .limit(limit)
                        .collect(Collectors.toList());
    }

    public List<Widget> getWidgetListSorted(int x1, int x2, int y1, int y2) {
        List<Widget> widgetList = repository.findAll();
        final int xMin;
        final int xMax;
        final int yMin;
        final int yMax;
        //определим минимальные максимальные точки по осям
        if( x1 > x2 ){
            xMin = x2;
            xMax = x1;
        } else {
            xMin = x1;
            xMax = x2;
        }
        if( y1 > y2 ){
            yMin = y2;
            yMax = y1;
        } else {
            yMin = y1;
            yMax = y2;
        }
        //меньшая сторона виджета (по значению) должна быть больше нижней области видимости,
        // а большая сторона должна быть меньше верхней области видимости, как по оси x, так и по y
        return widgetList.stream()
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .filter((w) ->
                        (w.getX()-w.getWidth()/2 >= xMin) && (w.getX()+w.getWidth()/2 <= xMax)
                                && (w.getY()-w.getHeight()/2 >= yMin) && (w.getY()+w.getHeight()/2 <= yMax))
                .collect(Collectors.toList());
    }
}
