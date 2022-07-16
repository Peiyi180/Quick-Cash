package com.csci3130g13.g13quickcash;

import com.csci3130g13.g13quickcash.utils.Database;
import com.csci3130g13.g13quickcash.utils.FirebaseDB;

public class UserMetricManager {
    Database db;

    public UserMetricManager(Database db) {
        this.db = db;
    }

    /**
     * Update employee and employer's reputation based on the amount of salary
     *
     * @param employer - The person who issue payment (rate 20)
     * @param employee - The person who receive payment (rate 10)
     * @param amount - Amount of salary
     */
    public void updateReputation(Employer employer, Employee employee, int amount) {
        employer.setReputation(employer.getReputation() + (20 * amount));
        employee.setReputation(employee.getReputation() + (10 * amount));
        db.updateUser(employer);
        db.updateUser(employee);
    }

    public void updateIncomeExpenditure(Employer employer, Employee employee, int amount) {
        employer.setTotalExpenditure(employer.getTotalExpenditure() + amount);
        employee.setTotalIncome(employee.getTotalIncome() + amount);
        db.updateUser(employee);
        db.updateUser(employer);

    }

    public void updateMetricsPayment(Employer employer, Employee employee, int amount) {
        updateReputation(employer, employee, amount);
        updateIncomeExpenditure(employer, employee, amount);
    }


}
