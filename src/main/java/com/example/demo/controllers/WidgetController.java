package com.example.demo.controllers;

import com.example.demo.model.Widget;
import com.example.demo.services.widgetService.IWidgetService;
import javassist.NotFoundException;
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

    @Autowired
    private IWidgetService db;
    private final static Logger LOGGER = Logger.getLogger(WidgetController.class.getName());

    public WidgetController(){

    }
    @PostMapping("/newWidgetWithZIndex")
    public ResponseEntity<Widget> newWidget(@RequestParam int x, int y, int width, int height, int zIndex) {
        Widget widget = db.save(x, y, width, height, zIndex);
        return ResponseEntity.ok(widget);
    }

    @PostMapping("/newWidget")
    public ResponseEntity<Widget> newWidget(@RequestParam  int x,int y,int width,int height) {
        Widget widget = db.save(x, y, width, height);
        return ResponseEntity.ok(widget);
    }

    @GetMapping("{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable  UUID id) {
        try {
            Widget widget = db.findById(id);
            return ResponseEntity.ok(widget);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateWidget")
    public ResponseEntity<Widget> updateWidget(@RequestBody Widget widget){
        try {
            Widget fWidget = db.updateWidget(widget);
            return ResponseEntity.ok(fWidget);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteWidget(@PathVariable UUID id) {
        try {
            db.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getWidgetsSorted")
    public ResponseEntity<List<Widget>> getWidgetListSorted() {
        List<Widget> widgetList = db.getWidgetListSorted();
        return ResponseEntity.ok(widgetList);
    }

    @GetMapping("/getWidgetsWithLimit")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam(value = "offset") int offset,
                                                             @RequestParam(value = "limit") int limit) {
        List<Widget> widgetListSort = db.getWidgetListSorted(offset,limit);
        return ResponseEntity.ok(widgetListSort);
    }

    @GetMapping("/getWidgetsByArea")
    public ResponseEntity<List<Widget>>  getWidgetListSorted(@RequestParam(value = "x1")  int x1,
                                                             @RequestParam(value = "x2")  int x2,
                                                             @RequestParam(value = "y1")  int y1,
                                                             @RequestParam(value = "y2")  int y2) {
        List<Widget> widgetListSort =  db.getWidgetListSorted(x1,x2,y1,y2);
        return ResponseEntity.ok(widgetListSort);
    }
}