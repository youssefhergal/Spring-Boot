package org.youssefhergal.my_app_ws.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.youssefhergal.my_app_ws.entities.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);


    UserEntity save(UserEntity userEntity);


    void delete(UserEntity userEntity);
}
