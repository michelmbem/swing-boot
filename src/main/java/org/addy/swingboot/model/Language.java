package org.addy.swingboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "language")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Language {
    @Id
    @GeneratedValue
    @Column(name = "language_id")
    private Short id;

    @NotEmpty
    @Size(max = 20)
    private String name;

    @CurrentTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Language category)) return false;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
