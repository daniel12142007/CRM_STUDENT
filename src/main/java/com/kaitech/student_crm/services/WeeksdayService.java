package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.WeeksdayDTO;
import com.kaitech.student_crm.exceptions.WeeksdayNotFoundException;
import com.kaitech.student_crm.models.Weeksday;
import com.kaitech.student_crm.repositories.WeeksdayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeeksdayService {

    private final WeeksdayRepository weeksdayRepository;

    @Autowired
    public WeeksdayService(WeeksdayRepository weeksdayRepository) {
        this.weeksdayRepository = weeksdayRepository;
    }


    public Weeksday createWeeksday(WeeksdayDTO weeksdayDTO) {
        Weeksday weeksday = new Weeksday();
        weeksday.setName(weeksdayDTO.getName());

        return weeksdayRepository.save(weeksday);
    }

    public List<Weeksday> getAllWeeksdays() {
        return weeksdayRepository.findAll();
    }

    public Weeksday getWeeksdayById(Long weeksdayId){
        return weeksdayRepository.findById(weeksdayId)
                .orElseThrow(() ->new WeeksdayNotFoundException("Week's day not found"));
    }

    public void deleteWeeksday(Long weeksdayId) {
        Weeksday weeksday = weeksdayRepository.findById(weeksdayId)
                .orElseThrow(() ->new WeeksdayNotFoundException("Week's day not found"));

        weeksdayRepository.delete(weeksday);
    }
}
