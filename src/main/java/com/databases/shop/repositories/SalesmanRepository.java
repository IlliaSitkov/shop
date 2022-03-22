package com.databases.shop.repositories;

import com.databases.shop.models.Salesman;
import com.databases.shop.repositories.queryinterfaces.MinMaxOrderCount;
import com.databases.shop.repositories.queryinterfaces.MinMaxSalesmanIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman,Long> {

    @Query("SELECT s FROM Salesman s ORDER BY s.personName.surname")
    Iterable<Salesman> getAll();

//    @Query("SELECT case when count(s)> 0 then true else false end FROM Salesman s WHERE s.contacts.email LIKE :email AND s.id = :id")
//    boolean existsByEmailAndNotId(@Param("email") String email, @Param("id") Long id);
//
//    @Query("SELECT case when count(s)> 0 then true else false end FROM Salesman s WHERE s.contacts.email LIKE :email")
//    boolean existsByEmail(@Param("email") String email);

//    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DONE' AND o.salesman.id = :id")
//    int salesmanOrderQuantity(@Param("id") Long id);
//
//    @Query("SELECT s FROM Salesman s WHERE :quantity <= (SELECT COUNT(o) FROM Order o WHERE o.status = 'DONE' AND o.salesman.id = s.id)")
//    int haveGEDoneOrderQuantity(@Param("quantity") int quantity);


    @Query(value =
            "SELECT MIN(COALESCE(num_ord,0)) AS minCount,MAX(COALESCE(num_ord,0)) AS maxCount\n" +
            "FROM salesman LEFT OUTER JOIN (\n" +
            "    SELECT salesman_id,COUNT(*) AS num_ord\n" +
            "    FROM order_t\n" +
            "    WHERE status = 'DONE'\n" +
            "    GROUP BY salesman_id) T ON salesman.id = salesman_id", nativeQuery = true)
    MinMaxOrderCount minMaxOrderCount();


    @Query(value =
            "SELECT MIN(COALESCE(sum_per_salesman,0)) AS minIncome, MAX(COALESCE(sum_per_salesman,0)) AS maxIncome\n" +
            "FROM salesman LEFT OUTER JOIN (SELECT salesman_id, SUM(row_cost) AS sum_per_salesman\n" +
            "                               FROM\n" +
            "                                   (SELECT salesman_id, prod_price*prod_quantity AS row_cost\n" +
            "                                    FROM order_t INNER JOIN product_in_order pio ON order_t.id = pio.order_id\n" +
            "                                    WHERE status = 'DONE') AS RowCosts\n" +
            "                               GROUP BY salesman_id) SalesmanCost ON salesman.id = salesman_id", nativeQuery = true)
    MinMaxSalesmanIncome minMaxSalesmanIncome();



//    @Query(value =
//            "SELECT * \n" +
//            "FROM salesman\n" +
//            "WHERE NOT EXISTS (\n" +
//            "        SELECT category.id\n" +
//            "        FROM category\n" +
//            "        WHERE NOT EXISTS(\n" +
//            "                SELECT articul\n" +
//            "                FROM product\n" +
//            "                WHERE category_fk = category.id AND EXISTS(\n" +
//            "                        SELECT product_articul\n" +
//            "                        FROM product_in_order INNER JOIN order_t ot ON product_in_order.order_id = ot.id\n" +
//            "                        WHERE status = 'DONE' AND product_articul = articul AND salesman_id = salesman.id\n" +
//            "                    )\n" +
//            "            )\n" +
//            "    )\n" +
//            "  AND id IN (\n" +
//            "    SELECT salesman_id\n" +
//            "    FROM order_t\n" +
//            "    WHERE status = 'DONE'\n" +
//            "    GROUP BY salesman_id\n" +
//            "    HAVING COUNT(id) <= :order_num_val\n" +
//            ")\n" +
//            "  AND id IN (\n" +
//            "    SELECT salesman_id\n" +
//            "    FROM\n" +
//            "        (SELECT salesman_id, prod_price*prod_quantity AS row_cost\n" +
//            "         FROM order_t INNER JOIN product_in_order pio ON order_t.id = pio.order_id\n" +
//            "         WHERE status = 'DONE') AS RowCosts\n" +
//            "    GROUP BY salesman_id\n" +
//            "    HAVING SUM(row_cost) <= :income_val\n" +
//            ")", nativeQuery = true)
//    Iterable<Salesman> salesmanFilter(@Param("income_val") double income, @Param("order_num_val") int orderNum);



}
