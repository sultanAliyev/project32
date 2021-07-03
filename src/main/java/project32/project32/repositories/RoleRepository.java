package project32.project32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project32.project32.entities.Roles;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findByRole(String role);

}
