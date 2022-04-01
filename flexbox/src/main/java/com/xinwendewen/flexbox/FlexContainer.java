package com.xinwendewen.flexbox;

import java.util.List;

public interface FlexContainer {
    void setFlexDirection(FlexDirection flexDirection);
    void setFlexWrap(FlexWrap flexWrap);
    void setJustifyContent(JustifyContent justifyContent);
    void setAlignContent(AlignContent alignContent);
    void setAlignItems(AlignItems alignItems);
    void setPaddings(Paddings paddings);
    void setFlexItems(List<NewFlexItem> flexItems);
    void measure(MeasureRequest mainAxisMeasureRequest, MeasureRequest crossAxisMeasureRequest);
    void layout();
    List<FlexLine> getFlexLines();
}
