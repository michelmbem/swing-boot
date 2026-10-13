package org.addy.swingboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_poster")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FilmPoster {
    @Id
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @NotEmpty
    @Column(name = "poster")
    private String filename;

    @CurrentTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
