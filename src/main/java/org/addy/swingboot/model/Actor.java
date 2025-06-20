package org.addy.swingboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "actor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue
    @Column(name = "actor_id")
    private Integer id;

    @NotEmpty
    @Column(name = "first_name", length = 45)
    private String firstName;

    @NotEmpty
    @Column(name = "last_name", length = 45)
    private String lastName;

    @CurrentTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id"))
    private Collection<Film> films;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Actor actor)) return false;
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName).strip();
    }
}
