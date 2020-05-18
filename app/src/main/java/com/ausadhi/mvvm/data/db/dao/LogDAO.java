package com.ausadhi.mvvm.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.ausadhi.mvvm.data.db.entity.LogClass;

import java.util.List;

/**
 * Created by Ali Asadi on 07/03/2018.
 */

@Dao
public interface LogDAO {

    @Query("SELECT * FROM LogClass")
    List<LogClass> getAll();

    @Query("DELETE FROM LogClass")
    void dropTable();

    @Insert
    void insertAll(LogClass... logs);

    @Delete
    void delete(LogClass log);
}
