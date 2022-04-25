package com.databases.shop.repositories;

import com.databases.shop.models.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TelephoneRepository extends JpaRepository<Telephone, String> {

    @Modifying
    @Transactional
    @Query(value =
            "DELETE FROM telephone WHERE tel_number = :telNumber", nativeQuery = true)
    void delete(@Param("telNumber") String telNumber);


}
