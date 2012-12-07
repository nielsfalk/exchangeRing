package de.hh.changeRing.domain;

import javax.xml.bind.annotation.XmlElement;

/**
 * User: nielsfalk
 * Date: 07.12.12 10:29
 */
public class BaseEntity {
    @XmlElement
    protected Long id;

    public Long getId() {
        return id;
    }
}
