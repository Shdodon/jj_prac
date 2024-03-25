package lesson1.hw.homework1;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingDouble;

public class HomeWork {

    /**
     * Реалзиовать методы, описанные ниже:
     */

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .sorted(Comparator.comparing(Person::getName))
                .forEach(x -> System.out.println(x.getName()));
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.toMap(Person::getDepartment, p -> p, new BinaryOperator<Person>() {
                    @Override
                    public Person apply(Person t, Person t2) {
                        if (Comparator.comparing(Person::getAge).compare(t, t2) > 0) {
                            return t;
                        }
                        return t2;
                    }
                }));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(p -> p.getAge() < 30)
                .filter(p -> p.getSalary() > 50_000)
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */

    public Optional<Department> findTopDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment, summingDouble(Person::getSalary)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

    }

}