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
import project.borrowhen.dao.entity.InventoryOverview;

public interface InventoryDao extends JpaRepository<InventoryEntity, Integer>{
	
	public final String GET_ALL_INVENTORY =
		    """
				SELECT new project.borrowhen.dao.entity.InventoryData(
				   e.id,
				   u.fullName,
				   u.userId,
				   e.itemName,
				   e.price,
				   e.totalQty,
				   e.availableQty,
				   e.createdDate,
				   e.updatedDate,
				   CASE WHEN (EXISTS (
				       SELECT 1 FROM BorrowRequestEntity br
				       WHERE br.inventoryId = e.id AND br.status <> 'PAID'
				   )) THEN false ELSE true END,
				   CASE WHEN (EXISTS (
				       SELECT 1 FROM BorrowRequestEntity br
				       WHERE br.inventoryId = e.id AND br.status <> 'PAID'
				   )) THEN false ELSE true END,
				   u.barangay,
				   0,
				   0.0,
				   e.imageName,
				   e.category
				)
				FROM InventoryEntity e
				LEFT JOIN UserEntity u ON u.id = e.userId
				WHERE e.isDeleted = false
				AND u.isDeleted = false
				AND (
				   (:search IS NOT NULL AND :search <> '' AND (
				       LOWER(e.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR
				       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR
				       LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%')) OR
				       CAST(e.price AS string) LIKE CONCAT('%', :search, '%') OR
				       CAST(e.totalQty AS string) LIKE CONCAT('%', :search, '%')
				   ))
				   OR (:search IS NULL OR :search = '')
				)
				AND (
				   :category IS NULL OR :category = '' OR LOWER(e.category) = LOWER(:category)
				)


			""";



	@Query(value=GET_ALL_INVENTORY)
	public Page<InventoryData> getAllInventory(Pageable pageable, 
			@Param("search") String search,
			@Param("category") String category) throws DataAccessException;
	
	
	
	
	public final String GET_ALL_OWNER_INVENTORY =
		"""
			SELECT new project.borrowhen.dao.entity.InventoryData(
		       e.id,
		       e.itemName,
		       e.price,
		       e.totalQty,
		       e.availableQty,
		       CAST((
                 SELECT COALESCE(SUM(br.qty), 0)
                 FROM BorrowRequestEntity br
                 WHERE br.inventoryId = e.id
                 AND br.status IN ('COMPLETED', 'PAYMENT PENDING', 'PAID')
              ) AS INTEGER) AS total_borrows,
              CAST((
                SELECT COALESCE(SUM(br.price * br.qty), 0)
			    FROM BorrowRequestEntity br
	            WHERE br.inventoryId = e.id
	            AND br.status IN ('COMPLETED', 'PAYMENT PENDING', 'PAID')
              ) AS DOUBLE) AS total_revenue,
              e.category
		   )
		   FROM InventoryEntity e
		   WHERE e.userId = :userId
		     AND e.isDeleted = false
		     AND (
		         (
		             :search IS NOT NULL
		             AND :search <> ''
		             AND (
		                 LOWER(e.itemName) LIKE LOWER(CONCAT('%', :search, '%'))
		                 OR CAST(e.price AS string) LIKE LOWER(CONCAT('%', :search, '%'))
		                 OR LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%'))
		                 OR CAST(e.totalQty AS string) LIKE CONCAT('%', :search, '%')
		                 OR CAST(e.availableQty AS string) LIKE CONCAT('%', :search, '%')
		             )
		         )
		         OR (:search IS NULL OR :search = '')
		     )
		""";

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
	
	public final String UPDATE_INVENTORY = """
				UPDATE inventory
				SET user_id = :userId,
				item_name = :itemName,
				price = :price,
				total_qty = :totalQty,
				available_qty = :availableQty,
				updated_date = :updatedDate,
				category = :category,
				image_name = :imageName
				WHERE id = :id
			""";
	
    @Modifying
    @Transactional
    @Query(value=UPDATE_INVENTORY, nativeQuery=true)
	public void updateInventory(@Param("id") int id,
			@Param("userId") int userId,
			@Param("itemName") String itemName,
			@Param("price") double price, 
			@Param("totalQty") int totalQty, 
			@Param("availableQty") int availableQty,
			@Param("updatedDate") Date updatedDate,
			@Param("category") String category,
			@Param("imageName") String imageName) throws DataAccessException;
    
	public final String UPDATE_INVENTORY_LENT_TIMES = "UPDATE inventory "
			+ "SET total_lent = total_lent + :qty, "
			+ "updated_date = :updatedDate "
			+ "WHERE id = :id ";
	
    @Modifying
    @Transactional
    @Query(value=UPDATE_INVENTORY_LENT_TIMES, nativeQuery=true)
	public void updateInventoryLentTimes(@Param("id") int id,
			@Param("qty") int qty,
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
	
    public final String UPDATE_INVENTORY_TOTAL_AVAILABLE_QTY = 
    	    "UPDATE inventory " +
    	    "SET total_qty = total_qty + :deltaQty, " +
    	    "available_qty = available_qty + :deltaQty, " +
    	    "updated_date = :updatedDate " +
    	    "WHERE id = :id";

	@Modifying
	@Transactional
	@Query(value=UPDATE_INVENTORY_TOTAL_AVAILABLE_QTY, nativeQuery=true)
	void updateInventoryTotalAndAvailableQty(
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
	
	public static final String GET_LENDER_INVENTORY_OVERVIEW = """
		    WITH inventory_totals AS (
		        SELECT
		            e.id AS inventory_id,
		            e.total_qty,
		            e.available_qty,
		            (SELECT COALESCE(SUM(br.price * br.qty), 0)
		             FROM borrow_request br
		             WHERE br.inventory_id = e.id AND br.status = 'PAID') AS total_revenue,
		            (SELECT COALESCE(SUM(ic.qty), 0)
		             FROM inventory_item_condition ic
		             WHERE ic.inventory_id = e.id AND ic.condition = 'LOST' AND ic.is_deleted = FALSE) AS total_lost,
		            (SELECT COALESCE(SUM(ic.qty), 0)
		             FROM inventory_item_condition ic
		             WHERE ic.inventory_id = e.id AND ic.condition = 'DAMAGED' AND ic.is_deleted = FALSE) AS total_damaged
		        FROM inventory e
		        WHERE e.user_id = :userId AND e.is_deleted = FALSE
		    )
		    SELECT
		        COALESCE(CAST(COUNT(*) AS integer), 0) AS totalItem,
		        COALESCE(CAST(SUM(total_qty) AS integer), 0) AS totalQty,
		        COALESCE(CAST(SUM(available_qty) AS integer), 0) AS totalAvailableQty,
		        COALESCE(CAST(SUM(total_revenue) AS double precision), 0) AS totalRevenue,
		        COALESCE(CAST(SUM(total_lost) AS integer), 0) AS totalLostItems,
		        COALESCE(CAST(SUM(total_damaged) AS integer), 0) AS totalDamagedItems
		    FROM inventory_totals;
		""";



	@Query(value=GET_LENDER_INVENTORY_OVERVIEW, nativeQuery=true)
	public InventoryOverview getLenderInventoryOverview(@Param("userId") int userId) throws DataAccessException;
	
	public static final String GET_ADMIN_INVENTORY_OVERVIEW = """
		    WITH inventory_totals AS (
			    SELECT
			        e.id AS inventory_id,
			        e.total_qty,
			        e.available_qty,
			        (SELECT COALESCE(SUM(br.price * br.qty), 0)
			         FROM borrow_request br
			         WHERE br.inventory_id = e.id AND br.status = 'PAID') AS total_revenue,
			        (SELECT COALESCE(SUM(ic.qty), 0)
			         FROM inventory_item_condition ic
			         WHERE ic.inventory_id = e.id AND ic.condition = 'LOST' AND ic.is_deleted = FALSE) AS total_lost,
			        (SELECT COALESCE(SUM(ic.qty), 0)
			         FROM inventory_item_condition ic
			         WHERE ic.inventory_id = e.id AND ic.condition = 'DAMAGED' AND ic.is_deleted = FALSE) AS total_damaged
			    FROM inventory e
			    WHERE e.is_deleted = FALSE
			)
			SELECT
			    COALESCE(CAST(COUNT(*) AS integer), 0) AS totalItem,
			    COALESCE(CAST(SUM(total_qty) AS integer), 0) AS totalQty,
			    COALESCE(CAST(SUM(available_qty) AS integer), 0) AS totalAvailableQty,
			    COALESCE(CAST(SUM(total_revenue) AS double precision), 0) AS totalRevenue,
			    COALESCE(CAST(SUM(total_lost) AS integer), 0) AS totalLostItems,
			    COALESCE(CAST(SUM(total_damaged) AS integer), 0) AS totalDamagedItems
			FROM inventory_totals;

		""";





	@Query(value=GET_ADMIN_INVENTORY_OVERVIEW, nativeQuery=true)
	public InventoryOverview getAdminInventoryOverview() throws DataAccessException;
	
	
	public static final String GET_LENDER_POPULAR_ITEMS = """
				SELECT e.*
				FROM inventory e
				WHERE e.user_id = :userId
				ORDER BY e.total_lent DESC
				LIMIT 3 
			""";
	
	@Query(value=GET_LENDER_POPULAR_ITEMS, nativeQuery=true)
	public List<InventoryEntity> getLenderPopularItems(@Param("userId") int userId) throws DataAccessException;
	
	public static final String GET_ADMIN_POPULAR_ITEMS = """
			SELECT e.*
			FROM inventory e
			ORDER BY e.total_lent DESC
			LIMIT 5
		""";

@Query(value=GET_ADMIN_POPULAR_ITEMS, nativeQuery=true)
public List<InventoryEntity> getAdminPopularItems() throws DataAccessException;
	
    public final String DELETE_INVENTORY = 
    	    """
    			UPDATE inventory
    			SET is_deleted = true,
    			updated_date = :updatedDate
    			WHERE id = :id
    		""";

	@Modifying
	@Transactional
	@Query(value=DELETE_INVENTORY, nativeQuery=true)
	void deleteInventory(
	    @Param("id") int inventoryId,
	    @Param("updatedDate") Date updatedDate
	) throws DataAccessException;
	
	public final String GET_TOTAL_BORROWS_OF_INVENTORY = """
				 SELECT CAST(COALESCE(SUM(br.qty), 0) AS INTEGER)
                 FROM BorrowRequestEntity br
                 WHERE br.inventoryId = :inventoryId
                 AND br.status IN ('COMPLETED', 'PAYMENT PENDING', 'PAID')
			""";

	@Query(GET_TOTAL_BORROWS_OF_INVENTORY)
	public int getTotalBorrowsOfInventory(@Param("inventoryId") int inventoryId) throws DataAccessException;
	
	public final String GET_TOTAL_REVENUE_OF_INVENTORY = """
			 SELECT CAST(COALESCE(SUM(br.price * br.qty), 0) AS DOUBLE)
			 FROM BorrowRequestEntity br
			 WHERE br.inventoryId = :inventoryId
	         AND br.status IN ('COMPLETED', 'PAYMENT PENDING', 'PAID')
		""";

	@Query(GET_TOTAL_REVENUE_OF_INVENTORY)
	public double getTotalRevenueOfInventory(@Param("inventoryId") int inventoryId) throws DataAccessException;
}
