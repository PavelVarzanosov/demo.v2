package com.example.demo.services;

import com.example.demo.model.Widget;
import javassist.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface IWidgetService {

    Widget save(int x, int y, int width, int height, int zIndex);

    Widget save(int x,int y,int width,int height);

    Widget findById(UUID id) throws NotFoundException;

    Widget updateWidget(Widget widget) throws NotFoundException ;

    void deleteById(UUID  id) throws NotFoundException ;

    void deleteAll();

    List<Widget> getWidgetListSorted();

    List<Widget> getWidgetListSorted(int offset, int limit) ;

    List<Widget> getWidgetListSorted(int x1, int y1, int x2, int y2) ;
}
