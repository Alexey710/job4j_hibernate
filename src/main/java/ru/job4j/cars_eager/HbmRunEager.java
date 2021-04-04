package ru.job4j.cars_eager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRunEager {

    public static void main(String[] args) {
        List<Trademark> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try {

            Session session = sf.openSession();
            session.beginTransaction();

            Model model1 = Model.of("LandCruiserPrado");
            Model model2 = Model.of("LandCruiser 200");
            Model model3 = Model.of("Corolla");
            Model model4 = Model.of("Camry");
            Model model5 = Model.of("Highlander");

            Trademark trademark = Trademark.of("Toyota");
            trademark.getModels().add(model1);
            trademark.getModels().add(model2);
            trademark.getModels().add(model3);
            trademark.getModels().add(model4);
            trademark.getModels().add(model5);

            session.save(trademark);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Session session = sf.openSession();
            session.beginTransaction();
            list = session.createQuery("from Trademark").list();
            System.out.println(list);
            for (Trademark trademark : list) {
                for (Model model : trademark.getModels()) {
                    System.out.println(model);
                }
            }
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}
