package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_THAT_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@NamedQuery(
        name = FIND_ALL_TRAINERS_THAT_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME,
        query = "FROM Trainer er LEFT JOIN FETCH er.specialization " +
                "WHERE NOT EXISTS (FROM er.trainees ee WHERE ee.username = :username)"
)
@Entity
@Table(name = "trainers")
public class Trainer extends User {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.TRAINER.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
