package com.csci3130g13.g13quickcash.utils;

import com.csci3130g13.g13quickcash.DBConst;
import com.csci3130g13.g13quickcash.Employee;
import com.csci3130g13.g13quickcash.Employer;

/**
 * Firebase Implementation of DB
 */
public class FirebaseDB implements Database{
    @Override
    public void updateUser(Employee employee) {
        DBConst.dbRef
                .child(DBConst.CL_USERS)
                .child(DBConst.CL_EMPLOYEE)
                .child(employee.getId())
                .setValue(employee);
    }

    @Override
    public void updateUser(Employer employer) {
        DBConst.dbRef
                .child(DBConst.CL_USERS)
                .child(DBConst.CL_EMPLOYER)
                .child(employer.getId())
                .setValue(employer);
    }
}
