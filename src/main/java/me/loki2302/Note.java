package me.loki2302;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Note {
    @Id
    public String id;
    public String text;
}
