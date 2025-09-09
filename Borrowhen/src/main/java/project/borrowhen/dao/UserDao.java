package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {

}
