package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.csv.CSVRecord;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_USERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_USER_BY_USERNAME_QUERY_NAME;

@Data
@SuperBuilder
@NoArgsConstructor
@NamedQuery(
        name = FIND_USER_BY_USERNAME_QUERY_NAME,
        query = "FROM User WHERE username = :username"
)
@NamedQuery(
        name = FIND_ALL_USERS_QUERY_NAME,
        query = "FROM User"
)
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements CsvRecordInitializer {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActive;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        setUserId(Integer.parseInt(record.get(0)));
        setFirstName(record.get(1));
        setLastName(record.get(2));
        setUsername(record.get(3));
        setPassword(record.get(4));
        setActive(Boolean.parseBoolean(record.get(5)));
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
