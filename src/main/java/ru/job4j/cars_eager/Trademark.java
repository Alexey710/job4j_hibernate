package ru.job4j.cars_eager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trademarks")
public class Trademark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "trademark", cascade = CascadeType.ALL)
    private List<Model> models = new ArrayList<>();

    public static Trademark of(String name) {
        Trademark trademark = new Trademark();
        trademark.name = name;
        return trademark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trademark trademark = (Trademark) o;
        return id == trademark.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trademark{" + "id=" + id + ", name='" + name + '\'' + ", models=" + models + '}';
    }
}
