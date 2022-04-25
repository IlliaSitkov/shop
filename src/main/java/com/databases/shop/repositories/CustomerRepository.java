package com.databases.shop.repositories;

import com.databases.shop.models.Customer;
import com.databases.shop.repositories.queryinterfaces.MinMaxValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT c " +
            "FROM Customer c")
    Iterable<Customer> getAll();


    @Query(value =
            "SELECT ROUND(COALESCE(MIN(COALESCE(CAST(avgCost AS numeric),0)),0),2) AS minValue, ROUND(COALESCE(MAX(COALESCE(CAST(avgCost AS numeric),0)),0),2) AS maxValue\n" +
                    "FROM customer LEFT OUTER JOIN\n" +
                    "    (\n" +
                    "        SELECT AVG(order_cost1) AS avgCost, customer_id\n" +
                    "        FROM (\n" +
                    "                 SELECT order_id, SUM(prod_quantity * prod_price) AS order_cost1\n" +
                    "                 FROM product_in_order\n" +
                    "                 GROUP BY order_id\n" +
                    "             ) OrderCost INNER JOIN order_t ON OrderCost.order_id = order_t.id\n" +
                    "        WHERE status = 'DONE'\n" +
                    "        GROUP BY customer_id\n" +
                    "    ) AllCosts ON customer.id = AllCosts.customer_id", nativeQuery = true)
    MinMaxValues getMinMaxAvgOrderCost();



    @Query(value =
            "SELECT COALESCE(MIN(COALESCE(AllQuants.prod_quant_customer,0)),0) AS minValue, COALESCE(MAX(COALESCE(AllQuants.prod_quant_customer,0)),0) AS maxValue\n" +
                    "FROM customer LEFT OUTER JOIN (\n" +
                    "         SELECT customer_id, SUM(prod_quantity) AS prod_quant_customer\n" +
                    "         FROM order_t INNER JOIN product_in_order pio on order_t.id = pio.order_id\n" +
                    "         WHERE status = 'DONE'\n" +
                    "         GROUP BY customer_id) AllQuants ON customer.id = AllQuants.customer_id", nativeQuery = true)
    MinMaxValues getMinMaxOverallQuantity();


    @Query(value = "SELECT case when COUNT(*)> 0 then true else false end FROM customer WHERE contacts_email LIKE :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM customer WHERE contacts_email LIKE :email", nativeQuery = true)
    Customer getByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM customer WHERE id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);


    @Transactional
    @Modifying
    @Query(value =
        "UPDATE customer\n" +
        "SET person_name = :name, person_lastname = :lastname, person_surname = :surname,\n" +
        "addr_country = :country, addr_region = :region, addr_city = :city, addr_street = :street,\n" +
        "addr_apartment = :apartment, contacts_phone_number = :phone\n" +
        "WHERE id = :id", nativeQuery = true)
    void updateCustomer(@Param("id") Long id, @Param("name")String name, @Param("lastname")String lastname,@Param("surname") String surname,@Param("phone") String phone,
                        @Param("country") String country,@Param("region") String region,@Param("city") String city, @Param("street")String street,@Param("apartment") String apartment);

}
