package lesson1.hw.homework1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Program {
    public static void main(String[] args) {
        HomeWork hw = new HomeWork();

        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            departments.add(new Department("Department #"+i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            persons.add(new Person("Person #"+i,
                    ThreadLocalRandom.current().nextInt(20,61),
                    ThreadLocalRandom.current().nextInt( 20_000,100_000)*1.00,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))));
        }

        persons.forEach(System.out::println);

        System.out.println("\nВывести на консоль отсортированные (по алфавиту) имена персонов\n");

        hw.printNamesOrdered(persons);

        System.out.println("\nВ каждом департаменте найти самого взрослого сотрудника.\n" +
                "     * Вывести на консоль мапипнг department -> personName\n");

        Map<Department, Person> ex1 = hw.printDepartmentOldestPerson(persons);
        ex1.forEach((x,y) -> System.out.println(x + " "+ y));


        System.out.println("\nНайти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000\n");

        List <Person> per = hw.findFirstPersons(persons);
        per.forEach(System.out::println);


        System.out.println("\nНайти депаратмент, чья суммарная зарплата всех сотрудников максимальна\n");


        System.out.println(hw.findTopDepartment(persons));
    }

}
