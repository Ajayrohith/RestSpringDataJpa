package com.main.service;

import com.main.Entity.*;
import java.util.List;

public interface Employeeservice {

    List<Employee> servicefindAll();

    Employee serviceFindbyId(int i);

     //public void serviceupdateEmployee(int id, String parameter, String value);

     Employee serviceSave(Employee emp);

     public void serviceDeleteEmpl(int id);

}
