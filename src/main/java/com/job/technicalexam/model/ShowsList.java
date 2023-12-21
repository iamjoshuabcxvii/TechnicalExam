package com.job.technicalexam.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ShowsList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int showNumber;
    private int rows;
    private String columns;
    private long cancellationTimeFrame = 2;

    @PrePersist
    @PreUpdate
    public void checkValue() {
        if (this.cancellationTimeFrame == 0) {
            this.cancellationTimeFrame = 2;
        }
    }

}
