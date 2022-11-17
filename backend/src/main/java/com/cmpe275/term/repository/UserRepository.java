package com.cmpe275.term.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmpe275.term.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	List<User> findByEmail(String email);

	User getByEmail(String email);

	List<User> getByScreenName(String screenName);

	@Modifying
	@Query(value = "  DELETE FROM user WHERE user.email = :email" , nativeQuery = true)
	void deleteUserByEmail(@Param("email") String email);

}
