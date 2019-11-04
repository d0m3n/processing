package be.better.at.processing.model.jpa.base;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@ToString
@Data
public abstract class LongIdJPA implements IdentifiableJPA<Long>, Comparable<LongIdJPA> {

    @Id
    @Access(AccessType.PROPERTY)
//    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;

    @Version
    @Column(columnDefinition = "INT DEFAULT 0")
    @NotNull
    private Integer version;

    @Override
    public int compareTo(LongIdJPA o) {
        return 0;
    }
}
