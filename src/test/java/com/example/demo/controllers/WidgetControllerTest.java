package com.example.demo.controllers;

import com.example.demo.Matchers.WidgetMatcher;
import com.example.demo.model.Widget;
import com.example.demo.services.IWidgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WidgetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IWidgetService widgetServiceMock;

    @InjectMocks
    private WidgetController widgetController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(widgetController)
                .build();
    }

    @Test
    public void testNewWidgetWithZIndex() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget widget = new Widget(10,10,10,10,10, new Date());
        doReturn(widget).when(widgetServiceMock).save(10,10,10,10,10);

        mockMvc.perform(
                post("/widget/newWidgetWithZIndex")
                        .param("x", "10")
                        .param("y", "10")
                        .param("width", "10")
                        .param("height", "10")
                        .param("zIndex", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widget)));

        verify(widgetServiceMock, times(1)).save(10,10,10,10,10);
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testNewWidget() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget widget = new Widget(10,10,10,10,1, new Date());
        doReturn(widget).when(widgetServiceMock).save(10,10,10,10);

        mockMvc.perform(
                post("/widget/newWidget")
                        .param("x", "10")
                        .param("y", "10")
                        .param("width", "10")
                        .param("height", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widget)));

        verify(widgetServiceMock, times(1)).save(10,10,10,10);
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testGetWidgetById() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget widget = new Widget(10,10,10,10,1, new Date());
        doReturn(widget).when(widgetServiceMock).findById(widget.getWidgetId());

        mockMvc.perform(
                get("/widget/{id}", widget.getWidgetId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widget)));

        verify(widgetServiceMock, times(1)).findById(widget.getWidgetId());
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testGetWidgetByIdWithException() throws Exception {
        Widget widget = new Widget(10,10,10,10,1, new Date());
        when(widgetServiceMock.findById(widget.getWidgetId())).thenThrow(new NotFoundException("Widget with id = " + widget.getWidgetId().toString() +" not found"));

        mockMvc.perform(
                get("/widget/{id}", widget.getWidgetId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

        verify(widgetServiceMock, times(1)).findById(widget.getWidgetId());
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testUpdateWidget() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget updatedWidget = new Widget(10,10,10,10,1, new Date());
        when(widgetServiceMock.updateWidget(any(Widget.class))).thenReturn(updatedWidget);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/widget/updateWidget")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedWidget));
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(updatedWidget)));

        verify(widgetServiceMock, times(1)).updateWidget(argThat(new WidgetMatcher(updatedWidget)));
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testUpdateWidgetWithException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget updatedWidget = new Widget(10,10,10,10,1, new Date());
        when(widgetServiceMock.updateWidget(any(Widget.class))).thenThrow(new NotFoundException("Widget with id = " + updatedWidget.getWidgetId().toString() +" not found"));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/widget/updateWidget")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsBytes(updatedWidget));
        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

        verify(widgetServiceMock, times(1)).updateWidget(argThat(new WidgetMatcher(updatedWidget)));
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testDeleteWidget() throws Exception {
        UUID widgetId = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/widget/{id}", widgetId))
                .andExpect(status().isNoContent());

        verify(widgetServiceMock, times(1)).deleteById(widgetId);
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testDeleteWidgetWithException() throws Exception {
        UUID widgetId = UUID.randomUUID();
        doThrow(new NotFoundException("Bank with id = " + widgetId.toString() +" not found")).when(widgetServiceMock).deleteById(widgetId);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/widget/{id}", widgetId))
                .andExpect(status().isNotFound());

        verify(widgetServiceMock, times(1)).deleteById(widgetId);
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testGetWidgetListSorted() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget bank = new Widget(10,10,10,10,1, new Date());
        Widget bank2 = new Widget(10,10,10,10,2, new Date());
        Widget bank3 =new Widget(10,10,10,10,3, new Date());
        List<Widget> widgetList = new ArrayList<>();
        widgetList.add(bank);
        widgetList.add(bank2);
        widgetList.add(bank3);
        when(widgetServiceMock.getWidgetListSorted()).thenReturn(widgetList);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/widget/getWidgetsSorted")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widgetList)));

        verify(widgetServiceMock, times(1)).getWidgetListSorted();
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testGetWidgetListSortedWithLimit() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget bank = new Widget(10,10,10,10,1, new Date());
        Widget bank2 = new Widget(10,10,10,10,2, new Date());
        Widget bank3 =new Widget(10,10,10,10,3, new Date());
        List<Widget> widgetList = new ArrayList<>();
        widgetList.add(bank2);
        widgetList.add(bank3);
        when(widgetServiceMock.getWidgetListSorted(1,2)).thenReturn(widgetList);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/widget/getWidgetsWithLimit")
                        .param("offset", "1")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widgetList)));

        verify(widgetServiceMock, times(1)).getWidgetListSorted(1,2);
        verifyNoMoreInteractions(widgetServiceMock);
    }

    @Test
    public void testGetWidgetListSortedByArea() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Widget bank = new Widget(5,5,2,2,1, new Date());
        Widget bank2 = new Widget(-10,-10,1,1,2, new Date());
        Widget bank3 =new Widget(3,3,1,1,3, new Date());
        List<Widget> widgetList = new ArrayList<>();
        widgetList.add(bank);
        widgetList.add(bank3);
        when(widgetServiceMock.getWidgetListSorted(0,10,0,10)).thenReturn(widgetList);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/widget/getWidgetsByArea")
                        .param("x1", "0")
                        .param("x2", "10")
                        .param("y1", "0")
                        .param("y2", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(widgetList)));

        verify(widgetServiceMock, times(1)).getWidgetListSorted(0,10,0,10);
        verifyNoMoreInteractions(widgetServiceMock);
    }
}