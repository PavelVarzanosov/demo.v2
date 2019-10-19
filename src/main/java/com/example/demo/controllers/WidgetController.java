package com.example.demo.controllers;

import com.example.demo.interfaces.IRepository;
import com.example.demo.rateLimit.SimpleRateLimiter;
import com.example.demo.model.Widget;
import com.example.demo.interfaces.IWidgetService;
import com.example.demo.repo.h2WidgetServiceImpl;
import com.example.demo.repo.listWidgetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//@EnableConfigurationProperties({Properties.class})
@RestController
@RequestMapping("/widget")
public class WidgetController {

    @Value("${spring.isInMemoryStorage}")
    private boolean isInMemoryStorage;

    //private SimpleRateLimiter SRL1;
    @Value("${spring.baseRateLimit}")
    private int rateLimit;
    private final IWidgetService db;
    private final static Logger LOGGER = Logger.getLogger(WidgetController.class.getName());

    @Autowired
    public WidgetController(@Qualifier("listImpl") IWidgetService dbH2, @Qualifier("h2Impl") IWidgetService dbList) {
        //this.db = db;
        //System.out.println("widget controller");
        this.db = (isInMemoryStorage) ? dbH2 : dbList;
    }
    @PostMapping("/newWidgetWithZIndex")
    public ResponseEntity<Widget> newWidget(@RequestParam int x, int y, int width, int height, int zIndex) {

        final Widget widget;
        try {
            widget =db.save(x, y, width, height, zIndex);
            return ResponseEntity.ok(widget);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.badRequest().build();
            //return ResponseEntity.ok(e.getMessage());
        }
    }
    @PostMapping("/newWidget")
    public ResponseEntity<Widget> newWidget(@RequestParam  int x,int y,int width,int height) {

        final Widget widget;
        try {
            widget =db.save(x, y, width, height);
            return ResponseEntity.ok(widget);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable  UUID id) {

        final Widget widget;
        try {
            widget = db.findById(id);
            return ResponseEntity.ok(widget);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/updateWidget")
    public ResponseEntity<Widget> updateWidget(@RequestParam  Widget widget) {

        Widget fWidget = widget;
        try {
            fWidget = db.updateWidget(widget);
            return ResponseEntity.ok(fWidget);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteWidget(@PathVariable   UUID  id) {
        try {
            db.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        //тут тоже может быть разная логика,или вовзращаем, что этого элемента нет (независимо от того, был ли он)
        // или нужно показать, что его не было
    }
    @GetMapping("/getWidgetsSorted")
    public ResponseEntity<List<Widget>> getWidgetListSorted() {

        List<Widget> fWidgetList = db.getWidgetListSorted();
        return ResponseEntity.ok(fWidgetList);
    }
    //Запрос с пагинацией, никак не учитывает ситуацию, что между запросами от пользователя список виджетов мог измениться
    @PostMapping("/getWidgetsWithLimit")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam int offset, int limit) {
        //вернем пустой список, если offset больше количества виджетов
        List<Widget> widgetListSort = db.getWidgetListSorted(offset,limit);
        return ResponseEntity.ok(widgetListSort);
    }
    //область
    @PostMapping("/getWidgetsByArea")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam int x1, int x2, int y1, int y2) {
        List<Widget> widgetListSort =  db.getWidgetListSorted(x1,x2,y1,y2);
        return ResponseEntity.ok(widgetListSort);
    }
}