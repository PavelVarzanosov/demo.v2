package com.example.demo.controllers;

import com.example.demo.model.Widget;
import com.example.demo.interfaces.IWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/widget")
public class WidgetController {

    private IWidgetService db;
    private final static Logger LOGGER = Logger.getLogger(WidgetController.class.getName());
    @Autowired
    public WidgetController(@Qualifier("h2Impl") IWidgetService dbH2
            , @Qualifier("listImpl") IWidgetService dbList
            , @Value("${spring.isInMemoryStorage}") String isInMemoryStorage) {
        this.db = (isInMemoryStorage.equals("true")) ? dbH2 : dbList;
    }
    @PostMapping("/newWidgetWithZIndex")
    public ResponseEntity<Widget> newWidget(@RequestParam int x, int y, int width, int height, int zIndex) {

        Widget widget =db.save(x, y, width, height, zIndex);
        return ResponseEntity.ok(widget);
    }
    @PostMapping("/newWidget")
    public ResponseEntity<Widget> newWidget(@RequestParam  int x,int y,int width,int height) {

        Widget widget =db.save(x, y, width, height);
        return ResponseEntity.ok(widget);
    }
    @GetMapping("{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable  UUID id) {

        try {
            Widget widget = db.findById(id);
            return ResponseEntity.ok(widget);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateWidget")
    public ResponseEntity<Widget> updateWidget(@RequestBody Widget widget){ //не приходит поле zIndex объекта widget
        try {
            Widget fWidget = db.updateWidget(widget);
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
    }
    @GetMapping("/getWidgetsSorted")
    public ResponseEntity<List<Widget>> getWidgetListSorted() {

        List<Widget> fWidgetList = db.getWidgetListSorted();
        return ResponseEntity.ok(fWidgetList);
    }
    @GetMapping("/getWidgetsWithLimit")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam int offset, int limit) {
        List<Widget> widgetListSort = db.getWidgetListSorted(offset,limit);
        return ResponseEntity.ok(widgetListSort);
    }
    @GetMapping("/getWidgetsByArea")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam int x1, int x2, int y1, int y2) {
        List<Widget> widgetListSort =  db.getWidgetListSorted(x1,x2,y1,y2);
        return ResponseEntity.ok(widgetListSort);
    }
}