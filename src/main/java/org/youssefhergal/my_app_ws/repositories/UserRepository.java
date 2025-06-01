package org.youssefhergal.my_app_ws.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.youssefhergal.my_app_ws.entities.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);


    UserEntity save(UserEntity userEntity);


    void delete(UserEntity userEntity);

//    @Query(value = "SELECT * FROM users u WHERE u.firstname LIKE ?1%", nativeQuery = true)
//    Page<UserEntity> findAll(Pageable pageableRequest , String search);

    @Query(value = "SELECT * FROM users u WHERE u.firstname LIKE %:search% OR u.lastname LIKE %:search% ", nativeQuery = true)
    Page<UserEntity> findAll(Pageable pageableRequest, @Param("search") String search);
}
