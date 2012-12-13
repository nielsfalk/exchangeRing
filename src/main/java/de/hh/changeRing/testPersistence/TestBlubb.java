package de.hh.changeRing.testPersistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: niles
 * Date: 13.12.12
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class TestBlubb {
    @Id
    @GeneratedValue
    private Long id;

    private String bla;

    public TestBlubb(String bla) {
        this.bla = bla;
    }

    public TestBlubb() {
    }
}
