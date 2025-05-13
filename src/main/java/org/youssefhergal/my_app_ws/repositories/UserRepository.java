package org.youssefhergal.my_app_ws.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.youssefhergal.my_app_ws.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity , Long> {

    UserEntity findByEmail(String email);

}
