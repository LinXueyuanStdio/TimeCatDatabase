package com.timecat.data.room.tag;

import com.alibaba.fastjson.JSONObject;
import com.timecat.identity.data.base.IJson;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag", indices = {@Index("uid")})
public class Tag implements IJson, Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String title;

    public int color;

    public String uuid;

    public Tag() {
    }

    @Ignore
    public Tag(String title, int color, String uuid) {
        this.title = title;
        this.color = color;
        this.uuid = uuid;
    }

    @Ignore
    public Tag(String title, int color) {
        this(title, color, UUID.randomUUID().toString());
    }

    @NotNull
    @Override
    public String toString() {
        return "Tag{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", color=" + color +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put("uid", uid);
        json.put("title", title);
        json.put("color", color);
        json.put("uuid", uuid);
        return json;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static Tag fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, Tag.class);
    }

    public static Tag fromJson(JSONObject json) {
        return fromJson(json.toJSONString());
    }
}
