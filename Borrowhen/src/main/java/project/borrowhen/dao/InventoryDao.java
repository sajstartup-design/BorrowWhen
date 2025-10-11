package project.borrowhen.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.InventoryData;
import project.borrowhen.dao.entity.InventoryEntity;

public interface InventoryDao extends JpaRepository<InventoryEntity, Integer>{
	
	public final String GET_ALL_INVENTORY =
		    "SELECT new project.borrowhen.dao.entity.InventoryData(" +
		    "   e.id, " +
		    "   u.fullName, " +
		    "   u.userId, " +
		    "   e.itemName, " +
		    "   e.price, " +
		    "   e.totalQty, " +
		    "   e.availableQty, " +
		    "   e.createdDate, " +
		    "   e.updatedDate, " +
		    "   CASE WHEN (EXISTS (" +
		    "       SELECT 1 FROM BorrowRequestEntity br " +
		    "       WHERE br.inventoryId = e.id AND br.status <> 'PAID'" +
		    "   )) THEN false ELSE true END, " + // isEditable
		    "   CASE WHEN (EXISTS (" +
		    "       SELECT 1 FROM BorrowRequestEntity br " +
		    "       WHERE br.inventoryId = e.id AND br.status <> 'PAID'" +
		    "   )) THEN false ELSE true END " +  // isDeletable
		    ") " +
		    "FROM InventoryEntity e " +
		    "LEFT JOIN UserEntity u ON u.id = e.userId " +
		    "WHERE e.isDeleted = false " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND ( " +
		    "       LOWER(e.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       CAST(e.price AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "       CAST(e.totalQty AS string) LIKE CONCAT('%', :search, '%')" +
		    "   )) " +
		    "   OR (:search IS NULL OR :search = '') " +
		    ")";


	@Query(value=GET_ALL_INVENTORY)
	public Page<InventoryData> getAllInventory(Pageable pageable, 
			@Param("search") String search) throws DataAccessException;
	
	
	
	
	public final String GET_ALL_OWNER_INVENTORY =
		    "SELECT new project.borrowhen.dao.entity.InventoryData(" +
		    "   e.id, " +
		    "   e.itemName, " +
		    "   e.price, " +
		    "   e.totalQty, " +
		    "   e.availableQty " +
		    ") " +
		    "FROM InventoryEntity e " +
		    "WHERE e.userId = :userId " +
		    "AND e.isDeleted = false " +
		    "AND ( " +
		    "   (:search IS NOT NULL AND :search <> '' AND ( " +
		    "       LOWER(e.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       CAST(e.price AS string) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		    "       CAST(e.totalQty AS string) LIKE CONCAT('%', :search, '%') OR " +
		    "       CAST(e.availableQty AS string) LIKE CONCAT('%', :search, '%')" +
		    "   )) " +
		    "   OR (:search IS NULL OR :search = '') " +
		    ")";

	@Query(value = GET_ALL_OWNER_INVENTORY)
	public Page<InventoryData> getAllOwnedInventory(Pageable pageable, 
			@Param("search") String search,
			@Param("userId") int userId) throws DataAccessException;
	
	public final String GET_INVENTORY = "SELECT e "
			+ "FROM InventoryEntity e "
			+ "WHERE e.id = :id "
			+ "AND e.isDeleted = false ";
	
	@Query(value=GET_INVENTORY)
	public InventoryEntity getInventory(@Param("id") int id) throws DataAccessException;
	
	public final String UPDATE_INVENTORY = "UPDATE inventory "
			+ "SET user_id = :userId, "
			+ "item_name = :itemName, "
			+ "price = :price, "
			+ "total_qty = :totalQty, "
			+ "updated_date = :updatedDate "
			+ "WHERE id = :id ";
	
    @Modifying
    @Transactional
    @Query(value=UPDATE_INVENTORY, nativeQuery=true)
	public void updateInventory(@Param("id") int id,
			@Param("userId") int userId,
			@Param("itemName") String itemName,
			@Param("price") double price, 
			@Param("totalQty") int totalQty, 
			@Param("updatedDate") Date updatedDate) throws DataAccessException;
    
    public final String UPDATE_INVENTORY_QTY = 
    	    "UPDATE inventory " +
    	    "SET available_qty = available_qty + :deltaQty, " +
    	    "updated_date = :updatedDate " +
    	    "WHERE id = :id";

	@Modifying
	@Transactional
	@Query(value=UPDATE_INVENTORY_QTY, nativeQuery=true)
	void updateInventoryQty(
	    @Param("id") int inventoryId,
	    @Param("deltaQty") int deltaQty,
	    @Param("updatedDate") Date updatedDate
	) throws DataAccessException;

	public String GET_RECENT_INVENTORY_BY_USER_ID = """
				SELECT new project.borrowhen.dao.entity.InventoryData(
			        e.id,
			        e.itemName,
			        e.price,
			        e.totalQty,
			        e.availableQty
			    )
			    FROM InventoryEntity e
			    WHERE e.userId = :userId
			    ORDER BY e.createdDate DESC
			""";
	
	@Query(GET_RECENT_INVENTORY_BY_USER_ID)
	public List<InventoryData> getRecentInventory(@Param("userId") int userId, 
			Pageable pageable) throws DataAccessException;


}
