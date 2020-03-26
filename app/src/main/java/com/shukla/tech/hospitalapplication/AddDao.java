package com.shukla.tech.hospitalapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
interface AddDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAssetInventory(AddData addData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssetInventory(List<AddData> addData);
}
