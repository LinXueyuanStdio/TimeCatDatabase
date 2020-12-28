package com.timecat.data.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.timecat.data.greendao.model.TimeBlockTemplate;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TIME_BLOCK_TEMPLATE".
*/
public class TimeBlockTemplateDao extends AbstractDao<TimeBlockTemplate, Long> {

    public static final String TABLENAME = "TIME_BLOCK_TEMPLATE";

    /**
     * Properties of entity TimeBlockTemplate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Created_datetime = new Property(2, long.class, "created_datetime", false, "CREATED_DATETIME");
        public final static Property Color = new Property(3, String.class, "color", false, "COLOR");
        public final static Property Archive = new Property(4, boolean.class, "archive", false, "ARCHIVE");
        public final static Property Star = new Property(5, boolean.class, "star", false, "STAR");
    }


    public TimeBlockTemplateDao(DaoConfig config) {
        super(config);
    }
    
    public TimeBlockTemplateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TIME_BLOCK_TEMPLATE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"CREATED_DATETIME\" INTEGER NOT NULL ," + // 2: created_datetime
                "\"COLOR\" TEXT," + // 3: color
                "\"ARCHIVE\" INTEGER NOT NULL ," + // 4: archive
                "\"STAR\" INTEGER NOT NULL );"); // 5: star
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_TIME_BLOCK_TEMPLATE_CREATED_DATETIME ON \"TIME_BLOCK_TEMPLATE\"" +
                " (\"CREATED_DATETIME\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TIME_BLOCK_TEMPLATE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TimeBlockTemplate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
        stmt.bindLong(3, entity.getCreated_datetime());
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(4, color);
        }
        stmt.bindLong(5, entity.getArchive() ? 1L: 0L);
        stmt.bindLong(6, entity.getStar() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TimeBlockTemplate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
        stmt.bindLong(3, entity.getCreated_datetime());
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(4, color);
        }
        stmt.bindLong(5, entity.getArchive() ? 1L: 0L);
        stmt.bindLong(6, entity.getStar() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TimeBlockTemplate readEntity(Cursor cursor, int offset) {
        TimeBlockTemplate entity = new TimeBlockTemplate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.getLong(offset + 2), // created_datetime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // color
            cursor.getShort(offset + 4) != 0, // archive
            cursor.getShort(offset + 5) != 0 // star
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TimeBlockTemplate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreated_datetime(cursor.getLong(offset + 2));
        entity.setColor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setArchive(cursor.getShort(offset + 4) != 0);
        entity.setStar(cursor.getShort(offset + 5) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TimeBlockTemplate entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TimeBlockTemplate entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TimeBlockTemplate entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
