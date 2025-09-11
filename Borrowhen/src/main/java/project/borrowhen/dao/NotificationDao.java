package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.NotificationEntity;

public interface NotificationDao extends JpaRepository<NotificationEntity, Integer>{

}
