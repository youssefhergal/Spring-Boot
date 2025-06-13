package org.youssefhergal.my_app_ws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.youssefhergal.my_app_ws.entities.AddressEntity;
import org.youssefhergal.my_app_ws.entities.UserEntity;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {


    List<AddressEntity> findByUser(UserEntity user);

    AddressEntity findByAddressId(String addressId);
}
