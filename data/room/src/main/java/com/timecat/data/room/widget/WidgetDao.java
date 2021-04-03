package com.timecat.data.room.widget;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Widget widget);

    @Delete
    void delete(Widget widget);

    @Query("SELECT * FROM Widget WHERE mId = :uid LIMIT 1")
    Widget getByID(int uid);

    @Query("SELECT * FROM Widget WHERE uuid = :uuid")
    List<Widget> getAllByThing(long uuid);

    @Query("SELECT * FROM Widget WHERE uuid = :uuid")
    List<Widget> getByNote(String uuid);

    @Query("SELECT * FROM Widget")
    List<Widget> getAll();

    @Query("DELETE FROM Widget WHERE mId = :appWidgetId")
    void delete(int appWidgetId);

}
