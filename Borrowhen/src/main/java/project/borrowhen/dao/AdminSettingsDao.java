package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.AdminSettingsEntity;

public interface AdminSettingsDao extends JpaRepository<AdminSettingsEntity, Integer>{
		
	
}
