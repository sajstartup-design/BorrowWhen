package project.borrowhen.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.borrowhen.dao.entity.InventoryItemConditionEntity;

public interface InventoryItemConditionDao extends JpaRepository<InventoryItemConditionEntity, Integer>{

	public static final String GET_INVENTORY_ITEM_CONDITION_BY_BORROW_ID = """
				SELECT e
				FROM InventoryItemConditionEntity e
				WHERE borrowRequestId = :borrowRequestId
				AND condition = :condition
			""";
	
	@Query(GET_INVENTORY_ITEM_CONDITION_BY_BORROW_ID)
	public InventoryItemConditionEntity getInventoryItemConditionByBorrowId(@Param("borrowRequestId") int borrowRequestId,
			@Param("condition") String condition) throws DataAccessException;
}
