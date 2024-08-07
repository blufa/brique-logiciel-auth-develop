package net.atos.zepe.auth.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Objects;

/**
 * The persistent class for the UM_User database table.
 */
@Data
@Entity
@Table(name = "UM_Role")
@EntityListeners(AuditingEntityListener.class)
@TableGenerator(name = "RoleGen", table = "JPA_SEQUENCES", pkColumnName = "SEQ_KEY", valueColumnName = "SEQ_VALUE", pkColumnValue = "UM_UserId", allocationSize = 1)
public class RoleEntity {

    @Id
    @Column(name = "UM_RoleId", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RoleGen")
    private Long id;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "UM_RoleCreationDate", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UM_RoleModificationDate", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(name = "UM_RoleName")
    private String name;

    @ManyToOne
    @JoinColumn(name = "um_userid")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity)) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCreationDate(), that.getCreationDate()) && Objects.equals(getModificationDate(), that.getModificationDate()) && Objects.equals(getName(), that.getName()) && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreationDate(), getModificationDate(), getName(), getUser());
    }

}