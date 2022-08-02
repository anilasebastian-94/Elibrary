package com.ElibraryDevelopment.Elibrary.Repository;

import com.ElibraryDevelopment.Elibrary.Entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity,Long> {

    PasswordResetTokenEntity findByToken(String token);

}
