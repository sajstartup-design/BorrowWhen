package project.borrowhen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.borrowhen.dao.entity.PaymentEntity;

public interface PaymentDao extends JpaRepository<PaymentEntity, Integer> {
	
}
