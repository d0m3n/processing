package be.better.at.processing.model.jpa.base;

import java.io.Serializable;

public interface IdentifiableJPA<ID extends Serializable> extends Serializable {
    ID getId();

    Integer getVersion();

}
