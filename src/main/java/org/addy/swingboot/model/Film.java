package org.addy.swingboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "film")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Film {
    @Id
    @GeneratedValue
    @Column(name = "film_id")
    private Integer id;

    @NotEmpty
    @Size(max = 128)
    private String title;

    @Lob
    private String description;

    @Column(name = "release_year")
    private Short releaseYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration")
    private short rentalDuration;

    @Column(name = "rental_rate")
    private float rentalRate;

    private Integer length;

    @Column(name = "replacement_cost", nullable = false)
    private BigDecimal replacementCost;

    private String rating;

    @Column(name = "special_features")
    private String specialFeatures;

    @CurrentTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @ManyToMany
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Collection<Category> categories;

    @Builder.Default
    @ManyToMany(mappedBy = "films", cascade = CascadeType.PERSIST)
    private Collection<Actor> actors = Set.of();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Film film)) return false;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Film.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .toString();
    }
}
