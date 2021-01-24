package fr.enix.vendor.r2dbc.crud.repository;

import fr.enix.vendor.r2dbc.crud.model.Employee;
import fr.enix.vendor.r2dbc.crud.model.EmployeeSearchRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {
    Flux<Employee> findAllByOrderByIdAsc();

    @Query("SELECT * FROM EMPLOYEE WHERE SALARY > :#{#employeeSearchRequest.salary}")
    Flux<Employee> findAllEmployeesHavingSalaryGreaterThan(final EmployeeSearchRequest employeeSearchRequest);
}
