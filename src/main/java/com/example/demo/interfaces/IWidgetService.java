package com.example.demo.interfaces;

import com.example.demo.model.Widget;

import java.util.List;
import java.util.UUID;

public interface IWidgetService {
    Widget save(int x, int y, int width, int height, int zIndex);

    Widget save(int x,int y,int width,int height);

    Widget findById(UUID id);

    Widget updateWidget(Widget widget);

    void deleteById(UUID  id);

    void deleteAll();

    List<Widget> getWidgetListSorted();

    List<Widget> getWidgetListSorted(int offset, int limit) ;

    List<Widget> getWidgetListSorted(int x1, int y1, int x2, int y2) ;
}
