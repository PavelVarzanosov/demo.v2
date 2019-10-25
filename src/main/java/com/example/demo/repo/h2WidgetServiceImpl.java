package com.example.demo.repo;

import com.example.demo.model.Widget;
import com.example.demo.interfaces.IRepository;
import com.example.demo.interfaces.IWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
@Component("h2Impl")
public class h2WidgetServiceImpl implements IWidgetService {

    @Autowired
    private IRepository h2Rep;
    public Widget save(int x, int y, int width, int height, int zIndex) {

        final Widget widget;
        Iterable<Widget> widgetL = h2Rep.findAll();
        if(widgetL != null) {
            List<Widget> widgetList = StreamSupport
                    .stream(widgetL.spliterator(), false)
                    //.filter()
                    .collect(Collectors.toList());
            if (widgetList
                    .stream()
                    .anyMatch((w) -> w.getZIndex() == zIndex))
            {
                widgetList.stream().filter((w) -> w.getZIndex() >=zIndex)
                        .map((w) -> w.incZIndex())
                        .collect(Collectors.toList());
                h2Rep.saveAll(widgetList);
            }
        }
        widget = new Widget(x,y,width,height,zIndex, new Date());
        h2Rep.save(widget);
        return widget;
    }
    public Widget save(int x,int y,int width,int height) {

        final Widget widget;
        int zIndex=0;
        Iterable<Widget> widgetL = h2Rep.findAll();
        if(widgetL != null) {
            List<Widget> widgetList = StreamSupport.stream(widgetL.spliterator(), false).collect(Collectors.toList());
            zIndex = widgetList.stream()
                    .max(Comparator.comparingInt(Widget::getZIndex))
                    .get()
                    .getZIndex();
        }
        widget = new Widget(x,y,width,height,zIndex+1,new Date());
        h2Rep.save(widget);
        //widgetL = h2Rep.findAll();
        //widgetL.forEach(widget1 -> System.out.println("w save " + widget1.getWidgetId() + " " + widget1.getZIndex()));
        return widget;
    }
    public Widget findById(UUID id) {

        Widget widget = h2Rep.findById(id).get();//какая логика должна быть?
       return widget;
    }
    public Widget updateWidget(Widget widget) {

        Iterable<Widget> widgetL = h2Rep.findAll();
        List<Widget> widgetList = StreamSupport.stream(widgetL.spliterator(), false).collect(Collectors.toList());

       if(widgetList.stream().anyMatch((w) -> w.getWidgetId().equals(widget.getWidgetId())))
        {
            if (widgetList.stream().anyMatch((w) -> w.getZIndex() == widget.getZIndex()))
            {
                widgetList.stream().filter((w) -> w.getZIndex() >=widget.getZIndex())
                        .map((w) -> w.incZIndex());
                h2Rep.saveAll(widgetList);
            }

            Widget fWidget = h2Rep.save(widget);
            return fWidget;
        }
        return null;
    }
    public void deleteById(UUID  id) {

        h2Rep.deleteById(id);
    }
    public void deleteAll() {

        h2Rep.deleteAll();
    }
    public List<Widget> getWidgetListSorted() {

        List<Widget> widgetList = StreamSupport
                .stream(h2Rep.findAll().spliterator(), false)
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .collect(Collectors.toList());

        return widgetList;
    }
    public List<Widget> getWidgetListSorted(int offset, int limit) {
        List<Widget> widgetList = StreamSupport.stream(h2Rep.findAll().spliterator(), false)
                .collect(Collectors.toList());
        //вернем пустой список, если offset больше количества виджетов
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
        Iterable<Widget> widgetL = h2Rep.findAll();
        List<Widget> widgetList = StreamSupport.stream(widgetL.spliterator(), false)
                .collect(Collectors.toList());
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
//        System.out.println("xMin " + xMin);
//        System.out.println("xMax " + xMax);
//        System.out.println("yMin " + yMin);
//        System.out.println("yMax " + yMax);
//        widgetList.forEach(widget1 ->
//                System.out.println("wList is "
//                        + (widget1.getX()- widget1.getWidth()/2) + " "
//                        + (widget1.getX()+widget1.getWidth()/2) + " "
//                        + (widget1.getY()-widget1.getHeight()/2)+ " "
//                        + (widget1.getY()+widget1.getHeight()/2)));
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
