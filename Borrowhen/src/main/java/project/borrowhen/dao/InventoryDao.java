package project.borrowhen.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.borrowhen.dao.entity.InventoryEntity;

public interface InventoryDao extends JpaRepository<InventoryEntity, Integer>{

	public final String GET_ALL_INVENTORY = "SELECT e "
			+ "FROM InventoryEntity e "
			+ "WHERE e.isDeleted = false ";
	
	@Query(value=GET_ALL_INVENTORY)
	public Page<InventoryEntity> getAllInventory(Pageable pageable)  throws DataAccessException;
}
