/**
 * 
 */
package com.bala.cf.cfredissample.repository;

import java.util.Collection;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import com.bala.cf.cfredissample.domain.Employee;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author bala
 *
 */
@Component
public class EmployeeRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>{
	
	
	private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    

    public EmployeeRepositoryPopulator()
    {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("employees.json");
    }
    
    @Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 if (event.getApplicationContext().equals(applicationContext)) {
	            CrudRepository employeeRepository =
	                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, CrudRepository.class);
	            if (employeeRepository != null && employeeRepository.count() == 0) {
	                populate(employeeRepository);
	            }
	        }
	}

    @SuppressWarnings("unchecked")
    public void populate(CrudRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Employee person : (Collection<Employee>) entity) {
                if (person != null) {
                    repository.save(person);
                }
            }
        } else {
            repository.save(entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
