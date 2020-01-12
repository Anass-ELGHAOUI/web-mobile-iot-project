package com.emse.spring.faircorp.DAO;

import com.emse.spring.faircorp.model.AutoController;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoControllerDao extends JpaRepository<AutoController, String>, AutoControllerDaoCustom {
}
