package com.main.jngroup.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nove1398 on 4/4/2014.
 */
public class DepartmentObject {
    private int deptId;
    private String deptName;
    private String deptLocation;

    public DepartmentObject(){

    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId( int deptId ) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName( String deptName ) {
        this.deptName = deptName;
    }

    public String getDeptLocation() {
        return deptLocation;
    }

    public void setDeptLocation( String deptLocation ) {
        this.deptLocation = deptLocation;
    }
}
