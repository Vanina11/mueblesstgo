package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.SueldosEntity;

@Repository
public interface SueldoRepository extends JpaRepository<SueldosEntity, Long> {
}
