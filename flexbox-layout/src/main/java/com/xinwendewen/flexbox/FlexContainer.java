package com.xinwendewen.flexbox;

import java.util.List;

public interface FlexContainer {
    void setFlexDirection(FlexDirection flexDirection);
    void setFlexWrap(FlexWrap flexWrap);
    void setJustifyContent(JustifyContent justifyContent);
    void setAlignContent(AlignContent alignContent);
    void setAlignItems(AlignItems alignItems);
    void setPaddings(Paddings paddings);
    <T extends FlexItem> void setFlexItems(List<T> flexItems, int count);
    void measure(MeasureRequest mainAxisMeasureRequest, MeasureRequest crossAxisMeasureRequest);
    void layout(int left, int top, int right, int bottom, boolean isRtl);
    List<FlexLine> getFlexLines();
}
