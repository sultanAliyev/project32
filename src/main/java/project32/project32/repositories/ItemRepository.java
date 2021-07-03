package project32.project32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project32.project32.entities.Items;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items, Long> {
}
