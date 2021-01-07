package fr.enix.vendor.r2dbc.crud;

import fr.enix.vendor.r2dbc.crud.model.Employee;
import fr.enix.vendor.r2dbc.crud.model.EmployeeSearchRequest;
import fr.enix.vendor.r2dbc.crud.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
public class EmployeeRepositoryTest {

    @Autowired private EmployeeRepository employeeRepository;

    @Test
    public void testFindAll() {
        saveEmployee();

        employeeRepository
        .findAll        ()
        .as             ( StepVerifier::create)
        .consumeNextWith( employee -> {
            assertEquals("employee_1",                              employee.getName()      );
            assertEquals( new BigDecimal("5565.56547"),                 employee.getSalary()    );
            assertEquals(parseDatetimeFromString("2020-01-01 08:00:00"),    employee.getHiringDate());
        })
        .verifyComplete();
    }

    @Test
    public void testOrderBy() {
        employeeRepository
        .saveAll        (getEmployees())
        .as             (StepVerifier::create)
        .expectNextCount(3L)
        .verifyComplete ();

        employeeRepository
        .findAllByOrderByIdAsc()
        .as             ( StepVerifier::create)
        .consumeNextWith( employee2 -> {
            assertEquals("employee_2",                              employee2.getName()      );
            assertEquals( new BigDecimal("4500.12000"),                 employee2.getSalary()    );
            assertEquals(parseDatetimeFromString("2020-02-28 18:30:00"),    employee2.getHiringDate());
        })
        .consumeNextWith( employee3 -> {
            assertEquals("employee_3",                              employee3.getName()      );
            assertEquals( new BigDecimal("4582.45000"),                 employee3.getSalary()    );
            assertEquals(parseDatetimeFromString("2020-03-01 12:00:00"),    employee3.getHiringDate());
        })
        .consumeNextWith( employee4 -> {
            assertEquals("employee_4",                              employee4.getName()      );
            assertEquals( new BigDecimal("3500.45000"),                 employee4.getSalary()    );
            assertEquals(parseDatetimeFromString("2019-12-31 14:30:00"),    employee4.getHiringDate());
        })
        .verifyComplete();
    }

    @Test
    public void testQNativeQueryWithSpELExpression() {
        employeeRepository
        .saveAll        (getEmployees())
        .as             (StepVerifier::create)
        .expectNextCount(3L)
        .verifyComplete ();

        employeeRepository
        .findAllEmployeesHavingSalaryGreaterThan(EmployeeSearchRequest.builder().salary(new BigDecimal("4500")).build())
        .as             (StepVerifier::create)
        .expectNextCount(2L)
        .verifyComplete ();
    }

    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll().subscribe();
    }

    /**
     * name: employee_1, salary: 5565.56547, hiring date: 2020-01-01 08:00:00
     */
    private void saveEmployee() {
        employeeRepository.save(
            Employee
            .builder    ()
            .name       ("employee_1")
            .salary     (new BigDecimal("5565.56547"))
            .hiringDate (parseDatetimeFromString("2020-01-01 08:00:00"))
            .build      ()
        ).subscribe();
    }

    private List<Employee> getEmployees() {
         return Arrays.asList(

                Employee
                .builder    ()
                .name       ("employee_2")
                .salary     (new BigDecimal("4500.12000"))
                .hiringDate (parseDatetimeFromString("2020-02-28 18:30:00"))
                .build      (),
                // -----------
                Employee
                .builder    ()
                .name       ("employee_3")
                .salary     (new BigDecimal("4582.45000"))
                .hiringDate (parseDatetimeFromString("2020-03-01 12:00:00"))
                .build      (),
                // -----------
                 Employee
                 .builder    ()
                 .name       ("employee_4")
                 .salary     (new BigDecimal("3500.45000"))
                 .hiringDate (parseDatetimeFromString("2019-12-31 14:30:00"))
                 .build      ()
        );
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime parseDatetimeFromString(final String datetime) {
        return LocalDateTime.parse(datetime, formatter);
    }
}
