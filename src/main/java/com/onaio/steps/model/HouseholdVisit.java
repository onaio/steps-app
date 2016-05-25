package com.onaio.steps.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.onaio.steps.helper.CursorHelper;
import com.onaio.steps.helper.DatabaseHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class HouseholdVisit implements Serializable, Comparable<HouseholdVisit> {
    private static String FIND_BY_ID_QUERY = "SELECT * FROM HOUSEHOLD_VISITS WHERE id = %s";
    private static String FIND_BY_HH_ID_QUERY = "SELECT * FROM HOUSEHOLD_VISITS WHERE household_id = %s order by created_at desc";
    public static final String FIND_BY_NAME_QUERY = "SELECT * FROM HOUSEHOLD_VISITS WHERE %s = '%s'";
    private static String FIND_ALL_QUERY = "SELECT * FROM HOUSEHOLD_VISITS ORDER BY Id desc";
    private static String FIND_ALL_COUNT_QUERY = "SELECT count(*) FROM HOUSEHOLD_VISITS ORDER BY Id desc";
    private static String FIND_ALL_COUNT_BY_HHID_QUERY = "SELECT count(*) FROM HOUSEHOLD_VISITS WHERE household_id = %s";
    public static final String TABLE_NAME = "household_visits";
    public static final String ID = "Id";
    public static final String HH_ID = "household_id";
    public static final String STATUS = "Status";
    public static final String COMMENTS = "Comments";
    public static final String CREATED_AT = "Created_At";
    public static final String CREATED_BY = "Created_By";

    public static final String TABLE_CREATE_QUERY = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, ID, HH_ID, STATUS, COMMENTS, CREATED_AT, CREATED_BY);

    Long id;
    Long householdId;
    String status;
    String createdAt;
    String comments;
    String createdBy;

    public HouseholdVisit() {

    }

    public HouseholdVisit(Long id, Long householdId, String status, String createdAt, String comments, String createdBy) {
        this.id = id;
        this.householdId = householdId;
        this.status = status;
        this.comments = comments;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }


    public long save(DatabaseHelper db) {
        ContentValues householdValues = populateWithBasicDetails();
        householdValues.put(CREATED_AT, createdAt);

        long savedId = db.save(householdValues, TABLE_NAME);
        if (savedId != -1)
            id = savedId;
        return savedId;
    }

    public long update(DatabaseHelper db) {
        ContentValues householdValues = populateWithBasicDetails();
        return db.update(householdValues, TABLE_NAME, ID + " = ?", new String[]{String.valueOf(getId())});
    }

    private ContentValues populateWithBasicDetails() {
        ContentValues values = new ContentValues();
        values.put(HH_ID, householdId);
        values.put(STATUS, status);
        values.put(COMMENTS, comments);
        values.put(STATUS, status.toString());
        values.put(CREATED_AT, createdAt);
        values.put(CREATED_BY, createdBy);

        return values;
    }

    public static HouseholdVisit findById(DatabaseHelper db, Long id) {
        Cursor cursor = db.exec(String.format(FIND_BY_ID_QUERY, String.valueOf(id)));
        HouseholdVisit household = new CursorHelper().getHouseholdVisits(cursor).get(0);
        db.close();
        return household;
    }
    public static HouseholdVisit findByHouseholdId(DatabaseHelper db, Long hhId) {
        Cursor cursor = db.exec(String.format(FIND_BY_HH_ID_QUERY, String.valueOf(hhId)));
        HouseholdVisit household = new CursorHelper().getHouseholdVisits(cursor).get(0);
        db.close();
        return household;
    }

    public static int getAllCountByHouseholdId(DatabaseHelper db, Long hhId) {
        Cursor cursor = db.exec(String.format(FIND_ALL_COUNT_BY_HHID_QUERY, String.valueOf(hhId)));
        cursor.moveToFirst();
        int householdCounts = cursor.getInt(0);
        db.close();
        return householdCounts;
    }


    public static int getAllCount(DatabaseHelper db) {
        Cursor cursor = db.exec(FIND_ALL_COUNT_QUERY);
        cursor.moveToFirst();
        int householdCounts = cursor.getInt(0);
        db.close();
        return householdCounts;
    }

    public int getCount(DatabaseHelper db, String query) {
        Cursor cursor = db.exec(query);
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    @Override
    public int compareTo(HouseholdVisit otherHousehold) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        return false;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + householdId.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
