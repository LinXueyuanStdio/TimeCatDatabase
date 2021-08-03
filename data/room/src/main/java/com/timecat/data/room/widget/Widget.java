package com.timecat.data.room.widget;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Widget", indices = {@Index("mId"), @Index("uuid")})
public class Widget  implements Serializable {
    public static final int SIZE_TINY = 0;
    public static final int SIZE_SMALL = 1;
    public static final int SIZE_MIDDLE = 2;
    public static final int SIZE_LARGE = 3;

    @IntDef({SIZE_TINY, SIZE_SMALL, SIZE_MIDDLE, SIZE_LARGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Size {
    }

    public static final int HEADER_ALPHA_0 = -19950129;

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_SIMPLE = 1;

    @IntDef({STYLE_NORMAL, STYLE_SIMPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String uuid;
    @Size
    private int mSize;
    @IntRange(from = 0, to = 100)
    private int mAlpha = 100; // from 0-100, 0 means transparent and 100 means solid
    @Style
    private int mStyle = 0;

    @Ignore
    public Widget() {
    }

    public Widget(
            int id,
            String uuid,
            @Size int size,
            @IntRange(from = 0, to = 100) int alpha,
            @Style int style
    ) {
        this.mId = id;
        this.uuid = uuid;
        this.mSize = size;
        this.mAlpha = alpha;
        this.mStyle = style;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Size
    public int getSize() {
        return mSize;
    }

    public void setSize(@Size int size) {
        mSize = size;
    }

    @IntRange(from = 0, to = 100)
    public int getAlpha() {
        return mAlpha;
    }

    public void setAlpha(@IntRange(from = 0, to = 100) int alpha) {
        mAlpha = alpha;
    }

    @Style
    public int getStyle() {
        return mStyle;
    }

    public void setStyle(@Style int style) {
        mStyle = style;
    }
}
