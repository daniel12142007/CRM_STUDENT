package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    boolean existsByEmail(String email);




    @Query("""
            select new com.kaitech.student_crm.dtos.UserResponse(
            u.id,
            u.firstname,
            u.lastname,
            u.email
            )
            from User u
            where u.id = :id
            """)
    UserResponse findByIdResponse(@Param(value = "id") Long id);
}