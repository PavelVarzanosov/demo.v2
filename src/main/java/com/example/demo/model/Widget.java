package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Widget")
@JsonPropertyOrder({ "widgetId","x", "y", "width", "height", "zIndex", "lastFormattedDate"  })
public class Widget implements Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    private UUID  widgetId;
    @Column(name = "X", length = 64, nullable = false)
    private int x;
    @Column(name = "Y", length = 64, nullable = false)
    private int y;
    @Column(name = "WIDTH", length = 64, nullable = false)
    private int width;
    @Column(name = "HEIGHT", length = 64, nullable = false)
    private int height;
    @Column(name = "Z_INDEX", length = 64, nullable = false)
    private int zIndex;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_FORMATTED_DATE", nullable = false)
    private Date lastFormattedDate;

    public Widget (){

    }

    public Widget (@JsonProperty("x") int x,
                   @JsonProperty("y") int y,
                   @JsonProperty("width") int width,
                   @JsonProperty("height") int height,
                   @JsonProperty("zIndex") int zIndex,
                   @JsonProperty("date") Date dt){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.widgetId = UUID.randomUUID();
        this.lastFormattedDate = dt;
    }

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

    public Date getDate() {
        return this.lastFormattedDate;
    }

    public int incZIndex() {
        return this.zIndex++;
    }

    public void updateWidget(int x,int y,int width,int height,int zIndex, Date dt){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.lastFormattedDate = dt;
    }
    public String toString()
    {
        return "[Widget: x=" + x +
                " y=" + y +
                " width=" + width +
                " height=" + height +
                " zIndex=" + zIndex +
                " lastFormattedDate=" + lastFormattedDate +
                "]";
    }
}
