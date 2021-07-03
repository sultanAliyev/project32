package project32.project32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project32.project32.entities.Users;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

   Users findByEmail(String email);
}
