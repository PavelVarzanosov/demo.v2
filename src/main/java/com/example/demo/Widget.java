package com.example.demo;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
@Entity
@Table(name = "Widget")
public class Widget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private UUID  widgetId;
    @Column(name = "X", length = 64, nullable = false)
    private int x;//точка центра виджета по оси абсцисс
    @Column(name = "Y", length = 64, nullable = false)
    private int y;//точка центра виджета по оси ординат
    @Column(name = "WIDTH", length = 64, nullable = false)
    private int width;//ширина виджета
    @Column(name = "HEIGHT", length = 64, nullable = false)
    private int height;//высота виджета
    @Column(name = "Z_INDEX", length = 64, nullable = false)
    private int zIndex;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_FORMATTED_DATE", nullable = false)
    private Date lastFormattedDate;

    public UUID  getWidgetId() {
        return widgetId;
    }
    public int getZIndex() {
        return zIndex;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setDate(Date DT) {
        this.lastFormattedDate = DT;
    }
    public int incZIndex() {
        this.zIndex++;
        return this.zIndex;
    }
    public void updateWidget(int x,int y,int width,int height,int zIndex){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.zIndex=zIndex;
        this.lastFormattedDate=new Date();
    }

    public Widget (){

    };
    public Widget (int x,int y,int width,int height,int zIndex){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.zIndex=zIndex;
        this.widgetId = UUID.randomUUID();
        this.lastFormattedDate=new Date();
    }

}
