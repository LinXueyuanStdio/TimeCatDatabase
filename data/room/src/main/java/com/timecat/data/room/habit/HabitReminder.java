package com.timecat.data.room.habit;

import android.database.Cursor;

import com.alibaba.fastjson.JSONObject;
import com.timecat.identity.data.base.IJson;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description 与习惯一一对应，notifyTime 是下一次的提醒时间
 * @usage null
 */
public class HabitReminder implements IJson {
    private long createTime;
    private long habitId;
    private long notifyTime;

    public HabitReminder() {
    }

    public HabitReminder(long createTime, long habitId, long notifyTime) {
        this.createTime = createTime;
        this.habitId = habitId;
        this.notifyTime = notifyTime;
    }

    public HabitReminder(Cursor c) {
        this(c.getLong(0), c.getLong(1), c.getLong(2));
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }


    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("createTime", createTime);
        jsonObject.put("habitId", habitId);
        jsonObject.put("notifyTime", notifyTime);
        return jsonObject;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static HabitReminder fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, HabitReminder.class);
    }

    public void copyFrom(HabitReminder habitReminder) {
        this.createTime = habitReminder.createTime;
        this.habitId = habitReminder.habitId;
        this.notifyTime = habitReminder.notifyTime;
    }
}
