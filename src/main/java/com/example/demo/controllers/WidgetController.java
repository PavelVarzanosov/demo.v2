package com.example.demo.controllers;

import com.example.demo.Widget;
import com.example.demo.repo.h2Repo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;
import com.weddini.throttling.Throttling;
import com.weddini.throttling.ThrottlingType;
//@EnableConfigurationProperties({Properties.class})
@RestController
@RequestMapping("/widget")
public class WidgetController {

    private final String sharedKey = "SHARED_KEY";
    h2Repo db;
    @Value("${spring.isInMemoryStorage}")
    private boolean isInMemoryStorage;
   // private RateLimiter rateLimiter = RateLimiter.created(PERMITS_PER_SECONDS);

    @Value("${spring.rateLimit}")
    private int rateLimit;

    @Value("${spring.rateTimeInSecond}")
    private int rateTimeInSecond;

    public WidgetController() {
        db = new h2Repo();
    }

    //@Throttling(type = ThrottlingType.RemoteAddr, limit = rateLimit, timeUnit = TimeUnit.MINUTES)
    @PostMapping("/newWidgetWithZIndex")
    public Widget newWidget(@RequestBody int x, int y, int width, int height, int zIndex) {
        final Widget widget;
        widget = db.save(x, y, width, height, zIndex);
        return widget;
    }
    @PostMapping("/newWidget")
    public Widget newWidget(@RequestBody int x,int y,int width,int height) {

        final Widget widget;
        widget = db.save(x, y, width, height);
        return widget;
    }
    @PostMapping("/getWidget")
    public Widget getWidgetById(@RequestBody UUID id) {

        final Widget widget;
        widget = db.findById(id);
        return widget;
    }
    @PostMapping("/updateWidget")
    public Widget updateWidget(@RequestBody Widget widget) {

        Widget fWidget = widget;
        fWidget = db.updateWidget(widget);
        return fWidget;
    }
    @PostMapping("/deleteWidget")
    public void deleteWidget(@RequestBody UUID  id) {

        db.deleteById(id);
        //тут тоже может быть разная логика,или вовзращаем, что этого элемента нет (независимо от того, был ли он)
        // или нужно показать, что его не было
    }
    @PostMapping("/getWidgetsSorted")
    public List<Widget> getWidgetListSorted() {

        List<Widget> fWidgetList = db.getWidgetListSorted();
        return fWidgetList;
    }
    //Запрос с пагинацией, никак не учитывает ситуацию, что между запросами от пользователя список виджетов мог измениться
    @PostMapping("/getWidgetsWithLimit")
    public List<Widget> getWidgetListSorted(int offset, int limit) {
        //вернем пустой список, если offset больше количества виджетов
        List<Widget> widgetListSort = db.getWidgetListSorted(offset,limit);
        return widgetListSort;
    }
    //область
    @PostMapping("/getWidgetsByArea")
    public List<Widget> getWidgetListSorted(int x1, int y1, int x2, int y2) {
        List<Widget> widgetListSort =  db.getWidgetListSorted(x1,y1,x2,y2);
        return widgetListSort;
    }
}