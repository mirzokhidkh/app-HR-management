package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.SalaryHistory;
import uz.mk.apphrmanagement.repository.SalaryHistoryRepository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SalaryHistoryService {

    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;


    public List<SalaryHistory> getAllByMonth(Date minDate, Date maxDate){
        List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByDateBetween(minDate, maxDate);
        return salaryHistoryList;
    }

    public List<SalaryHistory> getAllByUserId(UUID userId){
        List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByUserId(userId);
        return salaryHistoryList;
    }

}
