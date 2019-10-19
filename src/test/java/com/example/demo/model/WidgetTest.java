package com.example.demo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.UUID;
public class WidgetTest {
    private Widget widget;

    private int x;
    private int y;
    private int width;
    private int height;
    private int zIndex;
    private Date dt = new Date();

    @Before
    public void setUp() throws Exception {
        widget = new Widget (x,y,width,height,zIndex, dt);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getWidgetId() {
        UUID id = widget.getWidgetId();
    }

    @Test
    public void getZIndex() {
        int zIndexTest = widget.getZIndex();
        assertEquals(zIndexTest, zIndex);
    }

    @Test
    public void getX() {
        int xTest = widget.getX();
        assertEquals(x, xTest);
    }

    @Test
    public void getY() {
        int yTest = widget.getX();
        assertEquals(y, yTest);
    }

    @Test
    public void getWidth() {
        int widthTest = widget.getWidth();
        assertEquals(width, widthTest);
    }

    @Test
    public void getHeight() {
        int heightTest = widget.getHeight();
        assertEquals(height, heightTest);
    }

    @Test
    public void setDate() {
        Date dt= new Date();
        widget.setDate(dt);
        Date dtTest = widget.getDate();//bad practice?
        assertEquals(dt,dtTest);
    }

    @Test
    public void incZIndex() {
        int zTest = widget.incZIndex();
        int zInc=zIndex+1;
        assertEquals(zInc, zTest);
    }

//    @Test
//    public void updateWidget() {
//        Date dt= new Date();
//        widget.updateWidget(1,2,3,4,5, dt);
//        Widget newWidget = new Widget(1,2,3,4,5, dt);
//        assertEquals(newWidget, widget);
//    }
}