package com.example.demo.repo;

import com.example.demo.model.Widget;
import com.example.demo.interfaces.IWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
@Component("listImpl")
public class listWidgetServiceImpl implements IWidgetService {

    private LinkedList<Widget> widgetList;
    @Autowired
    public listWidgetServiceImpl() {
        widgetList = new LinkedList<Widget>();
        Widget w = new Widget(100,100,30,10,5,new Date());
        widgetList.add(w);
        w = new Widget(-10, 10, 20, 10, 10,new Date());
        widgetList.add(w);
        w = new Widget(10, -10, 10, 20, 20,new Date());
        widgetList.add(w);
        w = new Widget(-10, -10, 30, 30, 30,new Date());
        widgetList.add(w);
    }
    public Widget save(int x, int y, int width, int height, int zIndex) {

        if (widgetList.stream().anyMatch((w) -> w.getZIndex() == zIndex))
        {
            widgetList.stream().filter((w) -> w.getZIndex() >=zIndex)
                    .map((w) -> w.incZIndex())
                    .collect(Collectors.toList());
        }
        Widget widget = new Widget(x,y,width,height,zIndex,new Date());
        widgetList.add(widget);
        return widget;
    }
    public Widget save(int x,int y,int width,int height) {

        int zIndex = 0;
        zIndex = widgetList.stream()
                .max(Comparator.comparingInt(Widget::getZIndex))
                .get()
                .getZIndex();
        Widget widget = new Widget(x,y,width,height,zIndex+1,new Date());
        widgetList.add(widget);
        return widget;
    }
    public Widget findById(UUID id) {

        Widget widget = widgetList.stream().filter((w) -> w.getWidgetId().equals(id)).findFirst().get();
        return widget;
    }
    public Widget updateWidget(Widget widget) {

        if(widgetList.stream().anyMatch((w) -> w.getWidgetId().equals(widget.getWidgetId()))) {
            if (widgetList.stream().anyMatch((w) -> w.getZIndex() == widget.getZIndex())) {
                widgetList.stream().filter((w) -> w.getZIndex() >= widget.getZIndex())
                        .map((w) -> w.incZIndex());
            }
            widgetList.stream()
                    .filter((w) -> w.getWidgetId().equals(widget.getWidgetId()))
                    .findFirst()
                    .get()
                    .updateWidget(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), widget.getZIndex(), new Date());

            Widget fWidget = widgetList.stream()
                    .filter((w) -> w.getWidgetId().equals(widget.getWidgetId()))
                    .findFirst()
                    .get();
            return fWidget;
        }
        return null;
    }
    public void deleteById(UUID  id) {

        Widget deleteWidget = widgetList.stream().filter((w) -> w.getWidgetId().equals(id)).findFirst().get();
        widgetList.remove(deleteWidget);
    }
    public void deleteAll() {
        widgetList = new LinkedList<Widget>();;
    }
    public List<Widget> getWidgetListSorted() {

        return widgetList.stream().sorted(Comparator.comparingInt(Widget::getZIndex)).collect(Collectors.toList());
    }
    public List<Widget> getWidgetListSorted(int offset, int limit) {
        List<Widget> widgetListSort = (offset > widgetList.size()) ? new LinkedList<Widget>() :
                widgetList.stream()
                        .sorted(Comparator.comparingInt(Widget::getZIndex))
                        .skip(offset)
                        .limit(limit)
                        .collect(Collectors.toList());
        return widgetListSort;
    }
    public List<Widget> getWidgetListSorted(int x1, int x2, int y1, int y2) {
        List<Widget> widgetListSort;
        final int xMin;
        final int xMax;
        final int yMin;
        final int yMax;
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
        widgetListSort = widgetList.stream()
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .filter((w) ->
                        (w.getX()-w.getWidth()/2 >= xMin) && (w.getX()+w.getWidth()/2 <= xMax)
                                && (w.getY()-w.getHeight()/2 >= yMin) && (w.getY()+w.getHeight()/2 <= yMax))
                .collect(Collectors.toList());
        return widgetListSort;
    }
}
