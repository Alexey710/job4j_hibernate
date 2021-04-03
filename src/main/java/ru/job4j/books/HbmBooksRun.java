package ru.job4j.books;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmBooksRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book book1 = Book.of("Java Persistence with Hibernate");
            Book book2 = Book.of("Effective Java");

            Author author1 = Author.of("Christian Bauer");
            Author author2 = Author.of("Gavin King");
            book1.getAuthors().add(author1);
            book1.getAuthors().add(author2);

            Author author3 = Author.of("Joshua Bloch");
            book2.getAuthors().add(author3);

            session.save(book1);
            session.save(book2);

            Book book = session.get(Book.class, 2);
            session.remove(book);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
