package com.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.main.Dao.RestJpaRepository;
import com.main.Entity.Employee;


@Service
public class EmployeeServiceImpl implements Employeeservice{


    private RestJpaRepository jpaobj;

    public EmployeeServiceImpl(RestJpaRepository jpaobj)
    {
        this.jpaobj=jpaobj;
    }

    @Override
    public List<Employee> servicefindAll() {
       
        return jpaobj.findAll();
    }

    @Override
    public Employee serviceFindbyId(int i) {

        Optional<Employee> tempEmp = jpaobj.findById(i);

        Employee dbEmployee = null;

        if(tempEmp.isPresent())
        {
            dbEmployee = tempEmp.get();
        }
        else{
            throw new RuntimeException("The employee with this id is not found");
        }

        return dbEmployee;
    }


    @Override
    public Employee serviceSave(Employee emp) {
        
        return jpaobj.save(emp);
    }

   @Override
    public void serviceDeleteEmpl(int id) {
    if (!jpaobj.existsById(id)) {
        throw new RuntimeException("Employee not found");
    }
    jpaobj.deleteById(id);
}
}
