package project.borrowhen.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.borrowhen.dao.entity.InventoryData;
import project.borrowhen.dao.entity.InventoryEntity;

public interface InventoryDao extends JpaRepository<InventoryEntity, Integer>{

	@Query("SELECT new project.borrowhen.dao.entity.InventoryData(e.id, u.firstName, u.familyName, e.itemName, e.price, e.totalQty, e.createdDate, e.updatedDate) " +
		       "FROM InventoryEntity e " +
		       "LEFT JOIN UserEntity u ON u.id = e.userId " +
		       "WHERE e.isDeleted = false")
	Page<InventoryData> getAllInventory(Pageable pageable);
}
