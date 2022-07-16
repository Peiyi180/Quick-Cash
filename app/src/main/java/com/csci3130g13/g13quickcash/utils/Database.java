package com.csci3130g13.g13quickcash.utils;

import com.csci3130g13.g13quickcash.Employee;
import com.csci3130g13.g13quickcash.Employer;

public interface Database {
    /**
     * @param employee - Employee object that is updated
     */
    public void updateUser(Employee employee);

    /**
     * @param employer - Employer object that is updated
     */
    public void updateUser(Employer employer);
}
