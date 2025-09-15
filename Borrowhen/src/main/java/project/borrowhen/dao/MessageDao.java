package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.MessageEntity;

public interface MessageDao extends JpaRepository<MessageEntity, Long>{

}
