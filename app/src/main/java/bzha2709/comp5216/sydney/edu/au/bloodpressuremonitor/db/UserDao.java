package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Email = new Property(1, String.class, "email", false, "EMAIL");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
        public final static Property Year_of_birth = new Property(4, int.class, "year_of_birth", false, "YEAR_OF_BIRTH");
        public final static Property Gender = new Property(5, short.class, "gender", false, "GENDER");
        public final static Property Area = new Property(6, String.class, "area", false, "AREA");
        public final static Property Memo = new Property(7, String.class, "memo", false, "MEMO");
        public final static Property Auth = new Property(8, String.class, "auth", false, "AUTH");
        public final static Property Starttime = new Property(9, Long.class, "starttime", false, "STARTTIME");
        public final static Property Endtime = new Property(10, Long.class, "endtime", false, "ENDTIME");
        public final static Property Psd = new Property(11, String.class, "psd", false, "PSD");
        public final static Property Lastupdate = new Property(12, Long.class, "lastupdate", false, "LASTUPDATE");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"EMAIL\" TEXT," + // 1: email
                "\"PHONE\" TEXT," + // 2: phone
                "\"NICKNAME\" TEXT," + // 3: nickname
                "\"YEAR_OF_BIRTH\" INTEGER NOT NULL ," + // 4: year_of_birth
                "\"GENDER\" INTEGER NOT NULL ," + // 5: gender
                "\"AREA\" TEXT," + // 6: area
                "\"MEMO\" TEXT," + // 7: memo
                "\"AUTH\" TEXT," + // 8: auth
                "\"STARTTIME\" INTEGER," + // 9: starttime
                "\"ENDTIME\" INTEGER," + // 10: endtime
                "\"PSD\" TEXT," + // 11: psd
                "\"LASTUPDATE\" INTEGER);"); // 12: lastupdate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
        stmt.bindLong(5, entity.getYear_of_birth());
        stmt.bindLong(6, entity.getGender());
 
        String area = entity.getArea();
        if (area != null) {
            stmt.bindString(7, area);
        }
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(8, memo);
        }
 
        String auth = entity.getAuth();
        if (auth != null) {
            stmt.bindString(9, auth);
        }
 
        Long starttime = entity.getStarttime();
        if (starttime != null) {
            stmt.bindLong(10, starttime);
        }
 
        Long endtime = entity.getEndtime();
        if (endtime != null) {
            stmt.bindLong(11, endtime);
        }
 
        String psd = entity.getPsd();
        if (psd != null) {
            stmt.bindString(12, psd);
        }
 
        Long lastupdate = entity.getLastupdate();
        if (lastupdate != null) {
            stmt.bindLong(13, lastupdate);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
        stmt.bindLong(5, entity.getYear_of_birth());
        stmt.bindLong(6, entity.getGender());
 
        String area = entity.getArea();
        if (area != null) {
            stmt.bindString(7, area);
        }
 
        String memo = entity.getMemo();
        if (memo != null) {
            stmt.bindString(8, memo);
        }
 
        String auth = entity.getAuth();
        if (auth != null) {
            stmt.bindString(9, auth);
        }
 
        Long starttime = entity.getStarttime();
        if (starttime != null) {
            stmt.bindLong(10, starttime);
        }
 
        Long endtime = entity.getEndtime();
        if (endtime != null) {
            stmt.bindLong(11, endtime);
        }
 
        String psd = entity.getPsd();
        if (psd != null) {
            stmt.bindString(12, psd);
        }
 
        Long lastupdate = entity.getLastupdate();
        if (lastupdate != null) {
            stmt.bindLong(13, lastupdate);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickname
            cursor.getInt(offset + 4), // year_of_birth
            cursor.getShort(offset + 5), // gender
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // area
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // memo
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // auth
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // starttime
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // endtime
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // psd
            cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12) // lastupdate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setYear_of_birth(cursor.getInt(offset + 4));
        entity.setGender(cursor.getShort(offset + 5));
        entity.setArea(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMemo(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAuth(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStarttime(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setEndtime(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setPsd(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLastupdate(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
