package be.better.at.processing.model.jpa.base;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter
public abstract class LongIdAuditJPA extends LongIdJPA implements AuditableJPA {

    @CreatedDate
    @NotNull
    private Instant createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @NotNull
    private Instant modifiedAt;

    @LastModifiedBy
    private String modifiedBy;

}
