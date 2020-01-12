package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.Ringer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RingerDao extends JpaRepository<Ringer, String>, RingerDaoCustom {
}
