package com.csci3130g13.g13quickcash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.csci3130g13.g13quickcash.utils.Database;
import com.csci3130g13.g13quickcash.utils.FirebaseDB;

public class PaySalaryUnitTest {
    @Test
    public void updateReputationTest() {
        Database dbMock = mock(FirebaseDB.class);
        UserMetricManager reputationManager = new UserMetricManager(dbMock);

        Employee employee = new Employee("testEmployeePaypal");
        Employer employer = new Employer("testEmployerPaypal");
        employee.setReputation(100);
        employer.setReputation(200);
        reputationManager.updateReputation(employer, employee, 100);
        assertEquals(1100, employee.getReputation());
        assertEquals(2200, employer.getReputation());
    }
}
