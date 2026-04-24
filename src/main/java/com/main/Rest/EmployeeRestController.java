package com.main.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.Entity.Employee;
import com.main.service.Employeeservice;

import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private Employeeservice EmpService;

    private JsonMapper jsonMapper;

    public EmployeeRestController(Employeeservice EmpService,JsonMapper jsonMapper)
    {
        this.EmpService = EmpService;
        this.jsonMapper =  jsonMapper;
    }

    @GetMapping("/employees")
    public List<Employee> returnEmployees()
    {
        return EmpService.servicefindAll();

    }

    @GetMapping("/employees/{id}")
    public Employee returnEmployeebyId(@PathVariable int id)
    {
        return EmpService.serviceFindbyId(id);
    }

    // @PutMapping("/employees/{id}/{parameter}/{value}")
    // public void returnupdateEmployee(@PathVariable int id,
    //                              @PathVariable String parameter,
    //                              @PathVariable String value)
    // {
    //     EmpService.serviceupdateEmployee(id, parameter, value);
    // }

    /*    
    The below function can be used to both create a new employee and update an existing one
    
    1.Request to add a new user 
        eg :Request
                {
                    "firstName": "Reddin",
                    "lastName": "Kingsley",
                    "department": "CPO"
                }
            Response Received :
                {
                    "firstName": "Reddin",
                    "lastName": "Kingsley",
                    "department": "CPO",
                    "id": 7
                }
    2.     Request to update the existing user  we pass along with id in request 
    Request 

        {
            "firstName": "Rohith",
            "lastName": "Kingsley",
            "department": "CPO",
            "id": 7
        }

    */
    @PostMapping("/employees")
    public Employee addUser(@RequestBody Employee emp)
    {
        Employee Dbobject = EmpService.serviceSave(emp);
        return Dbobject;
    }

    @PatchMapping("/employees/{id}")
    public Employee patchEmployeeDetails(@PathVariable int id, @RequestBody Map<String,Object> patchMap)
    {
        Employee temEmp = EmpService.serviceFindbyId(id);
        if(temEmp == null)
        {
            throw new RuntimeException("There is no employee data for the given id "+id);
        }
        Employee patchedEmployee = jsonMapper.updateValue(temEmp,patchMap);

        Employee DbEmployee = EmpService.serviceSave(patchedEmployee);

        return DbEmployee;
    }

    @DeleteMapping("/employees/{id}")
    public List<Employee> deleteEmployee(@PathVariable int id)
    {
        EmpService.serviceDeleteEmpl(id);
        return EmpService.servicefindAll();
    }
}
