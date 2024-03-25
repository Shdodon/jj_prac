package lesson1;

import java.util.*;
import java.util.function.*;

public class Lambdas {
    public static void main(String[] args) {

        Foo foo = () -> System.out.println("Hello world!");
        foo.foo();


        // Лямбда, которая принимает целое число и возвращает его квадрат

        Function<Integer, Integer> square = x -> x * x;
        UnaryOperator<Integer> square2 = y -> y * y;

        System.out.println(square.apply((5)));
        System.out.println(square2.apply((5)));

        // Лямбда, которая принимает строку, возвращает ее длину
        Function<String, Integer> lengthExtractor = str -> str.length();
        System.out.println(lengthExtractor.apply("Hello"));

        //Лямбда, которая принимает строку и печатает ее в консоль
        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("Hello world");

        // Лямбда, которая ничего не принимает, но что то возвращает (Например генерирует рандомные числа)
        Supplier<Integer> randomer = () -> new Random().nextInt(101);
        System.out.println(randomer.get());
        System.out.println(randomer.get());
        System.out.println(randomer.get());
        System.out.println(randomer.get());

        //Лямбда, которая ничего не принимает, ничего не возвращает.
        //(Выводит на консоль рандомное целое число в диапозоне от 0 до 100)
        Runnable runnable = () -> printer.accept(String.valueOf(randomer.get()));
        runnable.run();
        runnable.run();
        runnable.run();
        runnable.run();
        runnable.run();

        //Лямбда, Тестер, возвращаемое значение примтив. Лябмда которая проверяет целое число на четность
        Predicate<Integer> evenTester = n -> n % 2 == 0;
        System.out.println(evenTester.test(5));
        System.out.println(evenTester.test(10));
        System.out.println(evenTester.test(12));
        System.out.println(evenTester.test(50));

        List<Integer> integerList = new ArrayList<>();
        Random random = new Random();
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        integerList.add(random.nextInt(101));
        System.out.println(integerList);

        Collections.sort(integerList);
        Collections.reverse(integerList);
        System.out.println(integerList);


        //Пример использования
        List<Integer> integers2 = new ArrayList<>();
        Random random1 = new Random();
        integers2.add(5);
        integers2.add(2);
        integers2.add(10);
        integers2.add(15);
        integers2.add(23);
        integers2.add(63);
        System.out.println(integers2);

        Comparator<Integer> customComparator = (x, y) ->{
            if(x % 2 == 0 && y % 2 != 0){
                return -1;
            } else if(x % 2 != 0 && y % 2 == 0) {
                return 1;
            }
            return Integer.compare(x, y);
        };
        Collections.sort(integers2, customComparator);
        System.out.println(integers2);

        // 1:00:00
    }
}
