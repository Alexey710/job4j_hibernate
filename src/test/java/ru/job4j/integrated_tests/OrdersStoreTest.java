package ru.job4j.integrated_tests;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private static BasicDataSource pool = new BasicDataSource();

    @BeforeClass
    public static void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Before
    public void add() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        List<Order> all = (List<Order>) store.findAll();
        assertThat(store.findByName("name1").getDescription(), is("description1"));
    }

    @Test
    public void whenUpdateOrderById() {
        OrdersStore store = new OrdersStore(pool);
        store.update(1, Order.of("name2", "description2"));
        assertThat(store.findById(1).getDescription(), is("description2"));
    }

    @Test
    public void whenFindOrderById() {
        assertThat(new OrdersStore(pool).findById(1).getDescription(), is("description1"));
    }

    @Test
    public void whenFindOrderByName() {
        assertThat(new OrdersStore(pool).findByName("name1").getDescription(), is("description1"));
    }

}