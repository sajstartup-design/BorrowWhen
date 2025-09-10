package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.BorrowRequestEntity;

public interface BorrowRequestDao extends JpaRepository<BorrowRequestEntity, Integer> {

}
