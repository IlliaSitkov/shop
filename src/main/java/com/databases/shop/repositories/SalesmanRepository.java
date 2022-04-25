package com.databases.shop.repositories;

import com.databases.shop.models.Salesman;
import com.databases.shop.repositories.queryinterfaces.MinMaxValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman,Long> {

    @Query("SELECT s FROM Salesman s ORDER BY s.personName.surname")
    Iterable<Salesman> getAll();

    @Query(value = "SELECT case when COUNT(*)> 0 then true else false end FROM salesman WHERE salesman_email LIKE :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);


    @Query(value =
            "SELECT COALESCE(MIN(COALESCE(num_ord,0)),0) AS minValue, COALESCE(MAX(COALESCE(num_ord,0)),0) AS maxValue\n" +
            "FROM salesman LEFT OUTER JOIN (\n" +
            "    SELECT salesman_id,COUNT(*) AS num_ord\n" +
            "    FROM order_t\n" +
            "    WHERE status = 'DONE'\n" +
            "    GROUP BY salesman_id) T ON salesman.id = salesman_id", nativeQuery = true)
    MinMaxValues minMaxOrderCount();


    @Query(value =
            "SELECT ROUND(COALESCE(MIN(COALESCE(CAST(sum_per_salesman AS numeric),0)),0),2) AS minValue, ROUND(COALESCE(MAX(COALESCE(CAST(sum_per_salesman AS numeric),0)),0),2) AS maxValue\n" +
            "FROM salesman LEFT OUTER JOIN (\n" +
            "    SELECT salesman_id, SUM(prod_price*prod_quantity) AS sum_per_salesman\n" +
            "    FROM order_t INNER JOIN product_in_order pio ON order_t.id = pio.order_id\n" +
            "    WHERE status = 'DONE'\n" +
            "    GROUP BY salesman_id) SalesmanCost ON salesman.id = salesman_id", nativeQuery = true)
    MinMaxValues minMaxSalesmanIncome();

    @Query(value = "SELECT * FROM salesman WHERE salesman_email LIKE :email", nativeQuery = true)
    Salesman getByEmail(@Param("email")String email);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM salesman WHERE id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);


}
