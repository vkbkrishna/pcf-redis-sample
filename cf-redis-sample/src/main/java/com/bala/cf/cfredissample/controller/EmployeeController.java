/**
 * 
 */
package com.bala.cf.cfredissample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.bala.cf.cfredissample.domain.Employee;

/**
 * @author bala
 *
 */
@RestController
@RequestMapping("employee")
public class EmployeeController {

	private CrudRepository<Employee, String> repository;
	
	@Autowired
	public EmployeeController(CrudRepository<Employee, String>  repository) {
		this.repository = repository;
	}
	
	@GetMapping
	public Iterable<Employee> employees() {
		return repository.findAll();
	}
	
	@PutMapping
	public Employee add(Employee employee) {
		return repository.save(employee);
	}
	
	@DeleteMapping
	public void delete(Employee employee) {
		 repository.delete(employee);
	}
	
	@PostMapping
	public Employee update(Employee employee) {
		return repository.save(employee);
	}
}
