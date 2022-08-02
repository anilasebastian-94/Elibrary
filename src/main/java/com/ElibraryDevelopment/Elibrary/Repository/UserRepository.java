package com.ElibraryDevelopment.Elibrary.Repository;

import com.ElibraryDevelopment.Elibrary.Entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    void deleteUserById(String id);
    UserEntity findByEmailVerificationToken(String token);
}
