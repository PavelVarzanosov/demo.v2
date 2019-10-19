package com.example.demo.repo;

import com.example.demo.interfaces.IWidgetService;
import com.example.demo.model.Widget;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class h2WidgetServiceImplTest {

    private LinkedList<Widget> widgetList = new LinkedList<Widget>();
    Widget widgetSave5;
    Widget widgetSave50;
    @Resource
    @Qualifier("h2Impl")
    private IWidgetService h2Rep;

    @Before
    public void setUp() throws Exception {
        System.out.println("");
        System.out.println("setup");
        h2Rep.deleteAll();
        Widget widget = new Widget(1,2,3,4,5,new Date());
        Widget widget2 = new Widget(10,20,30,40,50,new Date());
        widgetSave5 = h2Rep.save(1,2,3,4,5);
        widgetSave50 = h2Rep.save(10,20,30,40,50);
        widgetList.add(widget);
        widgetList.add(widget2);
        //System.out.println("1");
        //System.out.println(widget.getWidgetId().toString());
        //System.out.println("2");
        //System.out.println(widget2.getWidgetId().toString());
        System.out.println("end setup");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("clear");
        widgetList.clear();
        h2Rep.deleteAll();
    }

    @Test
    public void save() {
        //Widget widget = new Widget(1,2,3,4,5,new Date());
       // System.out.println("test save");
        Widget createWidget = h2Rep.save(1,2,3,4,5);
        //System.out.println(createWidget.getWidgetId());
        //System.out.println(widgetSave5.getWidgetId());
        Assert.assertNotNull(h2Rep.findById(createWidget.getWidgetId()));
        Assert.assertNotEquals(createWidget.getZIndex(), h2Rep.findById(widgetSave5.getWidgetId()).getZIndex());
    }

    @Test
    public void testSave() {
        //System.out.println("test testSave");
        Widget createWidget = h2Rep.save(1,2,3,4);
        //Widget widgetFirst = widgetList.getFirst();
        Widget widgetLast = h2Rep.findById(widgetSave5.getWidgetId());
        Assert.assertNotNull(h2Rep.findById(createWidget.getWidgetId()));
        Assert.assertTrue(createWidget.getZIndex() > widgetLast.getZIndex());
    }

    @Test
    public void findById() {

        //System.out.println("test find");
        Widget createWidget = h2Rep.save(1,2,3,4);
        //System.out.println(createWidget.getWidgetId());
        Widget getWidget = h2Rep.findById(createWidget.getWidgetId());
        //System.out.println(createWidget.getHeight());
        //System.out.println(getWidget.getHeight());
        Assert.assertEquals(getWidget.getHeight(), createWidget.getHeight());
        Assert.assertEquals(getWidget.getWidth(), createWidget.getWidth());
    }

    @Test
    public void updateWidget() {
        System.out.println("test update");
        Date dt = new Date();
        //создаем виджет
        Widget widgetCreate = h2Rep.save(1,2,3,4,5);
        System.out.println(widgetCreate.getWidgetId());
        //обновляем виджет изменяя параметры
        widgetCreate.updateWidget(2,3,4,5,6,dt);
        Widget updateWidget = h2Rep.updateWidget(widgetCreate);
        System.out.println(updateWidget.getWidgetId());
        Assert.assertNotEquals(widgetCreate.getZIndex(), updateWidget.getZIndex());
        Assert.assertNotEquals(widgetCreate.getHeight(), updateWidget.getHeight());
        Assert.assertNotEquals(widgetCreate.getWidth(), updateWidget.getWidth());
    }

//    @Test
//    public void deleteById() {
//    }

    @Test
    public void getWidgetListSorted() {
        //System.out.println("test sorted");
        Widget createWidget = new Widget(10,20,30,40,60, new Date());
        widgetList.add(createWidget);
        h2Rep.save(10,20,30,40,60);
        List<Widget> widgetListTest = h2Rep.getWidgetListSorted();
        Assert.assertEquals(widgetListTest.get(2).getZIndex(), widgetList.get(2).getZIndex());
        //Assert.assertEquals(h2Rep.getWidgetListSorted(), widgetList);
    }

    @Test
    public void testGetWidgetListSorted() {
        //System.out.println("test testSorted");
        Widget createWidget = new Widget(10,20,30,40,60, new Date());
        widgetList.add(createWidget);
        h2Rep.save(10,20,30,40,60);
        List<Widget> widgetListTest = h2Rep.getWidgetListSorted(1,2);
        Assert.assertEquals(widgetListTest.get(0).getZIndex(), widgetList.get(1).getZIndex());
    }

    @Test
    public void testGetWidgetListSorted1() {
        //System.out.println("test testSorted1");
        h2Rep.save(10,20,30,50,60);
        List<Widget> widgetListTest = h2Rep.getWidgetListSorted(-3,5, 0,50);
        System.out.println(widgetListTest.size());
        Assert.assertEquals(widgetListTest.size(), 1);
    }
}