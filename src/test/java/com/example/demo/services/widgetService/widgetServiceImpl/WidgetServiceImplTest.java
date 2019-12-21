package com.example.demo.services.widgetService.widgetServiceImpl;

import com.example.demo.model.Widget;
import com.example.demo.repositories.IWidgetRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WidgetServiceImplTest {

    @Mock
    private IWidgetRepository widgetRepositoryMock;

    @InjectMocks
    private WidgetServiceImpl widgetService;

    @Before
    public void setUp() {
        widgetService.deleteAll();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetWidgetListSorted() {
        List<Widget> widgetList = new ArrayList<>();
        Widget widget = new Widget(10,10,10,10,1, new Date());
        Widget widget1 = new Widget(10,10,10,10,3, new Date());
        Widget widget2 = new Widget(10,10,10,10,2, new Date());
        widgetList.add(widget);
        widgetList.add(widget2);
        widgetList.add(widget1);
        doReturn(widgetList).when(widgetRepositoryMock).findAll();

        List<Widget> getWidgetList =  widgetService.getWidgetListSorted();

        Assert.assertEquals(widget.getWidgetId(), getWidgetList.get(0).getWidgetId());
        Assert.assertEquals(widget2.getWidgetId(), getWidgetList.get(1).getWidgetId());
        Assert.assertEquals(widget1.getWidgetId(), getWidgetList.get(2).getWidgetId());
        verify(widgetRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(widgetRepositoryMock);
    }

    @Test
    public void testGetWidgetListSortedWithLimit() {
        List<Widget> widgetList = new ArrayList<>();
        Widget widget = new Widget(10,10,10,10,1, new Date());
        Widget widget1 = new Widget(10,10,10,10,3, new Date());
        Widget widget2 = new Widget(10,10,10,10,2, new Date());
        widgetList.add(widget);
        widgetList.add(widget2);
        widgetList.add(widget1);
        doReturn(widgetList).when(widgetRepositoryMock).findAll();

        List<Widget> getWidgetList =  widgetService.getWidgetListSorted(1, 2);

        Assert.assertEquals(widget2.getWidgetId(), getWidgetList.get(0).getWidgetId());
        Assert.assertEquals(widget1.getWidgetId(), getWidgetList.get(1).getWidgetId());
        verify(widgetRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(widgetRepositoryMock);
    }

    @Test
    public void testGetWidgetListSortedWithArea() {
        Widget widget = new Widget(3,3,1,1,11, new Date());
        Widget widget1 = new Widget(-100,100,10,10,5, new Date());
        Widget widget2 = new Widget(5,5,2,2,10, new Date());
        List<Widget> widgetList = new ArrayList<>();
        widgetList.add(widget);
        widgetList.add(widget1);
        widgetList.add(widget2);
        doReturn(widgetList).when(widgetRepositoryMock).findAll();

        List<Widget> getWidgetList = widgetService.getWidgetListSorted(0, 10, 0, 10);

        Assert.assertEquals(getWidgetList.get(0).getWidgetId(), widget2.getWidgetId());
        Assert.assertEquals(getWidgetList.get(1).getWidgetId(), widget.getWidgetId());
        verify(widgetRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(widgetRepositoryMock);
    }
}