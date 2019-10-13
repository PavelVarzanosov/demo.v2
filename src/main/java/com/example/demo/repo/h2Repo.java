package com.example.demo.repo;

import com.example.demo.Widget;
import com.example.demo.interfaces.IH2;
import com.example.demo.interfaces.IWidget;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public class h2Repo implements IWidget{
    //private LinkedList<Widget> widgetList;
    private IH2 h2Repo;
    public h2Repo() {
        //LinkedList<Widget> widgetList = new LinkedList<Widget>();
    }

    public Widget save(int x, int y, int width, int height, int zIndex) {

        final Widget widget;
        List<Widget> widgetList = StreamSupport.stream(h2Repo.findAll().spliterator(), false).collect(Collectors.toList());
        if (widgetList.stream().anyMatch((w) -> w.getZIndex() == zIndex))
        {
            widgetList.stream().filter((w) -> w.getZIndex() >=zIndex)
                    .map((w) -> w.incZIndex())
                    .collect(Collectors.toList());
            h2Repo.saveAll(widgetList);
        }
        else {

        }
        widget = new Widget(x,y,width,height,zIndex);
        h2Repo.save(widget);
        return widget;
    }

    public Widget save(int x,int y,int width,int height) {

        final Widget widget;
        List<Widget> widgetList = StreamSupport.stream(h2Repo.findAll().spliterator(), false).collect(Collectors.toList());
        int zIndex = widgetList.stream()
                .max(Comparator.comparingInt(Widget::getZIndex))
                .get()
                .getZIndex();
        widget = new Widget(x,y,width,height,zIndex+1);
        h2Repo.save(widget);
        return widget;
    }

    public Widget findById(UUID id) {

        Widget widget = h2Repo.findById(id).orElse(new Widget());//какая логика должна быть?
       return widget;
    }

    public Widget updateWidget(Widget widget) {

        //Widget fWidget = widget;
        List<Widget> widgetList = StreamSupport.stream(h2Repo.findAll().spliterator(), false).collect(Collectors.toList());
        if (widgetList.stream().anyMatch((w) -> w.getZIndex() == widget.getZIndex()))
        {
            widgetList.stream().filter((w) -> w.getZIndex() >=widget.getZIndex())
                    .map((w) -> w.incZIndex());
            h2Repo.saveAll(widgetList);
        }
        Widget fWidget = widgetList.stream()
                .filter((w) -> w.getWidgetId() == widget.getWidgetId())
                .findFirst()//что, если такого виджета нет?
                .get();
        fWidget.setDate(new Date());
        h2Repo.save(fWidget);
        return fWidget;
    }

    public void deleteById(UUID  id) {

        h2Repo.deleteById(id);
    }

    public List<Widget> getWidgetListSorted() {

        List<Widget> widgetList = StreamSupport
                .stream(h2Repo.findAll().spliterator(), false)
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .collect(Collectors.toList());

        return widgetList;
    }
    //Запрос с пагинацией, никак не учитывает ситуацию, что между запросами от пользователя список виджетов мог измениться
    public List<Widget> getWidgetListSorted(int offset, int limit) {
        List<Widget> widgetList = StreamSupport.stream(h2Repo.findAll().spliterator(), false)
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
    //область

    public List<Widget> getWidgetListSorted(int x1, int y1, int x2, int y2) {
        List<Widget> widgetListSort;
        List<Widget> widgetList = StreamSupport.stream(h2Repo.findAll().spliterator(), false)
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
        //меньшая сторона виджета (по значению) должна быть больше нижней области видимости,
        // а большая сторона должна быть меньше верхней области видимости, как по оси x, так и по y
        widgetListSort = widgetList.stream()
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .filter((w) ->
                        (w.getX()-w.getWidth()/2 >= xMin) && (w.getX()+w.getWidth()/2 <= xMax)
                                && (w.getX()-w.getHeight()/2 >= yMin) && (w.getX()+w.getHeight()/2 <= yMax))
                .collect(Collectors.toList());
        return widgetListSort;
    }
}
