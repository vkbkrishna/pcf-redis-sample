package com.bala.cf.cfredissample.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import com.bala.cf.cfredissample.domain.Employee;

//@Repository
public class EmployeeRepository implements CrudRepository<Employee, String>{
	
	public static final String EMPLOYEE_KEY = "employee";
	
	private final  HashOperations<String, String, Employee> hashOps;
	
	public EmployeeRepository(RedisTemplate<String, Employee> redisTemplate) {
		hashOps = redisTemplate.opsForHash();
	}

	@Override
	public long count() {
		return hashOps.keys(EMPLOYEE_KEY).size();
	}

	@Override
	public void delete(String id) {
		hashOps.delete(EMPLOYEE_KEY, id);
		
	}

	@Override
	public void delete(Employee employee) {
		hashOps.delete(employee.getId());
		
	}

	@Override
	public void delete(Iterable<? extends Employee> employees) {
		for (Employee emp : employees) {
			delete(emp);
		}
		
		
	}

	@Override
	public void deleteAll() {
		Set<String>employeeIds =  hashOps.keys(EMPLOYEE_KEY);
		for(String id : employeeIds) {
			delete(id);
		}
	}

	@Override
	public boolean exists(String emailAddress) {
		return hashOps.hasKey(EMPLOYEE_KEY,  emailAddress);
	}

	@Override
	public Iterable<Employee> findAll() {
		return hashOps.values(EMPLOYEE_KEY);
	}

	@Override
	public Iterable<Employee> findAll(Iterable<String> id) {
		return hashOps.multiGet(EMPLOYEE_KEY, convertIterableToList(id));
	}

	@Override
	public Employee findOne(String emailAddress) {
		return hashOps.get(EMPLOYEE_KEY, emailAddress);
	}

	@Override
	public <S extends Employee> S save(S employee) {
		hashOps.put(EMPLOYEE_KEY, employee.getId(), employee);

		return employee;
	}

	@Override
	public <S extends Employee> Iterable<S> save(Iterable<S> employees) {
		List<S> result = new ArrayList<>();

		for(S entity : employees) {
			save(entity);
			result.add(entity);
		}

		return result;
	}

	private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T object : iterable) {
            list.add(object);
        }
        return list;
    }

}
