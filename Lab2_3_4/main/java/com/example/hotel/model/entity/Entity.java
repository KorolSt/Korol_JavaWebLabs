package com.example.hotel.model.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 7340550462587810665L;

    private int id;

    protected Entity() {
    }

    protected Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
