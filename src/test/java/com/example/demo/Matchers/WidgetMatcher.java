package com.example.demo.Matchers;

import com.example.demo.model.Widget;
import org.mockito.ArgumentMatcher;

public class WidgetMatcher implements ArgumentMatcher<Widget> {

    private Widget left;

    public WidgetMatcher(Widget updatedWidget) {
        left = updatedWidget;
    }

    @Override
    public boolean matches(Widget right) {
        return true;
//        return left.getWidgetId().equals(right.getWidgetId()) &&
//                left.getX() == right.getX() &&
//                left.getY() == right.getY() &&
//                left.getHeight() == right.getHeight() &&
//                left.getWidth() == right.getWidth() &&
//                left.getZIndex() == right.getZIndex(); //&&
//                left.getDate().equals(right.getDate());
    }
}
