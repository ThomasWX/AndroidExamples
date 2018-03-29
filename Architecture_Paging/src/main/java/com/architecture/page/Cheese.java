package com.architecture.page;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 *  Data class that represents our items.
 */
@Entity
public class Cheese {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

}
/*
@Entity
data class Cheese(@PrimaryKey(autoGenerate = true) val id: Int, val name: String)
 */
