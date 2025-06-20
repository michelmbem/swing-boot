package org.addy.swingboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
