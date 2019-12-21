package com.example.demo.repositories.widgetRepositoriesImpl;

import com.example.demo.dbInterfaces.IH2Repository;
import com.example.demo.model.Widget;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class H2WidgetRepositoryTest {

    @Mock
    private IH2Repository h2RepositoryMock;

    @InjectMocks
    private H2WidgetRepository widgetRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        Widget widget = new Widget(10,10,10,10,10, new Date());
        Optional<Widget> optionalWidget = Optional.ofNullable(widget);
        doReturn(widget).when(h2RepositoryMock).save(widget);
        doReturn(optionalWidget).when(h2RepositoryMock).findById(widget.getWidgetId());

        widgetRepository.update(widget);

        verify(h2RepositoryMock, times(1)).save(widget);
        verify(h2RepositoryMock, times(1)).findById(widget.getWidgetId());
        verifyNoMoreInteractions(h2RepositoryMock);
    }

    @Test
    public void testUpdateWithException() {
        Widget widget = new Widget(10,10,10,10,10, new Date());
        Optional<Widget> optionalWidget = Optional.ofNullable(null);
        doReturn(widget).when(h2RepositoryMock).save(widget);
        doReturn(optionalWidget).when(h2RepositoryMock).findById(widget.getWidgetId());

        try {
            Widget updatedWidget = widgetRepository.update(widget);
        } catch (NotFoundException e) {
            Assert.assertEquals("Widget with id " + widget.getWidgetId() + " not found" , e.getMessage());
        }
    }

    @Test
    public void testFindById() throws NotFoundException {
        Optional<Widget> widget = Optional.of(new Widget(10,10,10,10,10, new Date()));
        doReturn(widget).when(h2RepositoryMock).findById(widget.get().getWidgetId());

        Widget getWidget = widgetRepository.findById(widget.get().getWidgetId());

        Assert.assertEquals(10, getWidget.getX());
        Assert.assertEquals(10, getWidget.getY());
        Assert.assertEquals(10, getWidget.getHeight());
        Assert.assertEquals(10, getWidget.getWidth());
        Assert.assertEquals(10, getWidget.getZIndex());

        verify(h2RepositoryMock, times(1)).findById(widget.get().getWidgetId());
        verifyNoMoreInteractions(h2RepositoryMock);
    }

    @Test
    public void testFindByIdWithException() {
        Widget widget = new Widget(10,10,10,10,10, new Date());
        Optional<Widget> optionalWidget = Optional.ofNullable(null);
        doReturn(optionalWidget).when(h2RepositoryMock).findById(widget.getWidgetId());

        try {
            widgetRepository.findById(widget.getWidgetId());
        } catch (NotFoundException e) {
            Assert.assertEquals("Widget with id " + widget.getWidgetId() + " not found" , e.getMessage());
        }
    }
}