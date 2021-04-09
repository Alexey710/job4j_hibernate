package ru.job4j.candidates;

import javax.persistence.*;

@Entity(name = "vacancies")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;

    public static Vacancy of(String description) {
        Vacancy vacancy = new Vacancy();
        vacancy.description = description;
        return vacancy;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Vacancy{"
                + "id=" + id
                + ", description='" + description + '\''
                + '}';
    }
}
