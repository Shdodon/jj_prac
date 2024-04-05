package lesson1.hw.homework4;

import lesson1.hw.homework1.Person;;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


import java.sql.SQLException;
import java.util.List;


public class Program {
    public static void main(String[] args) throws SQLException {

        Configuration configuration = new Configuration().configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {


            SelectOldStudents(sessionFactory);

        }

    }

    public static void AddStudent(SessionFactory sessionFactory, Student student) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        }
    }

    public static Student findById(SessionFactory sessionFactory, long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Student.class, id);
        }
    }

    public static void UpdateStudent(SessionFactory sessionFactory, Long id, String newName) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Student student = findById(sessionFactory, id);
            student.setFirstName(newName);
            Transaction tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
        }
    }

    public static void RemoveStudent(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Student student = findById(sessionFactory, id);
            Transaction tx = session.beginTransaction();
            session.remove(student);
            tx.commit();
        }
    }

    public static void SelectOldStudents (SessionFactory sessionFactory){
        try (Session session = sessionFactory.openSession()) {

            Query<Student> query = session.createQuery("select s from Student s where age > 20",Student.class);
            List<Student> resultList = query.getResultList();
            System.out.println(resultList);

        }
    }

}