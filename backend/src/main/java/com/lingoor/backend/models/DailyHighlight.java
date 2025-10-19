package com.lingoor.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_highlight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;
}
