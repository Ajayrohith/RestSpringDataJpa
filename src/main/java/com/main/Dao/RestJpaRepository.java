package com.main.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.main.Entity.Employee;

@Service
public interface RestJpaRepository extends JpaRepository<Employee,Integer>{

}
