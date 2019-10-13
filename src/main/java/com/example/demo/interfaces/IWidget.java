package com.example.demo.interfaces;

import com.example.demo.Widget;

import java.util.List;
import java.util.UUID;

public interface IWidget{
    public Widget save(int x, int y, int width, int height, int zIndex);

    public Widget save(int x,int y,int width,int height);

    public Widget findById(UUID id);

    public Widget updateWidget(Widget widget);

    public void deleteById(UUID  id);

    public List<Widget> getWidgetListSorted();

    public List<Widget> getWidgetListSorted(int offset, int limit) ;

    public List<Widget> getWidgetListSorted(int x1, int y1, int x2, int y2) ;
}
