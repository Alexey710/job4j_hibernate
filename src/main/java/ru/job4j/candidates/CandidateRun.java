package ru.job4j.candidates;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

public class CandidateRun implements AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory factory = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private <T> T tx(final Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Candidate add(Candidate candidate) {
        return this.tx(
                session -> {
                    session.save(candidate);
                    return candidate;
                }
        );
    }

    public List<Candidate> findAll() {
        return this.tx(
                session -> {
                    Query query = session.createQuery("from Candidate");
                    List<Candidate> list = query.list();
                    return list;
                }
        );
    }

    public List<Candidate> findAllByFetch() {
        return this.tx(
                session -> session.createQuery(
                            "select distinct c from Candidate c join fetch c.base b join fetch b.vacancies"
                    ).list()
        );
    }

    public Candidate findById(int id) {
        return this.tx(
                session -> {
                    Query query = session.createQuery("from Candidate c where c.id = :idCan");
                    query.setParameter("idCan", id);
                    Candidate can = (Candidate) query.uniqueResult();
                    return can;
                }
        );
    }

    public Candidate findByName(String name) {
        return this.tx(
                session -> {
                    Query query = session.createQuery("from Candidate c where c.name = :nameCan");
                    query.setParameter("nameCan", name);
                    Candidate can = (Candidate) query.uniqueResult();
                    return can;
                }
        );
    }

    public String updateById(int id, String name, String experience, int salary) {
        return this.tx(
                session -> {
                    Query query = session.createQuery(
                            "update Candidate c set "
                                    + "c.name = :newName,"
                                    + " c.experience = :newExperience, "
                                    + "c.salary = :newSalary "
                                    + "where c.id = :targetId");
                    query.setParameter("newName", name);
                    query.setParameter("newExperience", experience);
                    query.setParameter("newSalary", salary);
                    query.setParameter("targetId", id);
                    query.executeUpdate();
                    return "Entry is updated";
                }
        );
    }

    public String deleteById(int id) {
        return this.tx(
                session -> {
                    session.createQuery("delete Candidate c where c.id = :idCan")
                            .setParameter("idCan", id).executeUpdate();
                    return "Candidate is deleted";
                }
        );
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public static void main(String[] args) {
        CandidateRun run = new CandidateRun();
        /*
        Base base = Base.of();
        base.setVacancies(
        List.of(Vacancy.of("программист"), Vacancy.of("повар"), Vacancy.of("таксист")));
        Candidate can1 = Candidate.of("Иван", "продвинутый", 150000, base);
        Candidate can2 = Candidate.of("Степан", "средний", 140000, base);
        Candidate can3 = Candidate.of("Юрий", "начальный", 50000, base);
        run.add(can1);
        run.add(can2);
        run.add(can3);

        List<Candidate> candidates = run.findAll();
        for (Candidate can : candidates) {
            System.out.println(can);
        } // it gives LazyInitializationException
        */
        List<Candidate> candidatesFetch = run.findAllByFetch();
        for (Candidate canFetch : candidatesFetch) {
            System.out.println(canFetch);
        }
        /*run.findById(1);
        run.findByName("Степан");
        run.updateById(3, "Челубей", "отсутствует", 50000);
        run.deleteById(2);*/

    }
}
