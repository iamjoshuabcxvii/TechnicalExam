package com.job.technicalexam.repository;

import com.job.technicalexam.model.ShowsList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowsListRepository  extends JpaRepository<ShowsList, Long> {

    ShowsList findShowsListByShowNumber(final int showNumber);

}
