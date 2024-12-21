package org.roadmap.tasktrackerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false, length = 40)
    private UUID uuid;

    @Column(name = "title", length = 100, nullable = false)
    @NotNull
    private String title;

    @Column(name = "text", length = 300)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "finished_time")
    private Instant finishedTime;

    @Column(name = "do_before")
    private Instant doBefore;

    public Task(@NotNull String title, String text, @NotNull User owner, Instant doBefore) {
        this.title = title;
        this.text = text;
        this.owner = owner;
        this.doBefore = doBefore;
    }
}