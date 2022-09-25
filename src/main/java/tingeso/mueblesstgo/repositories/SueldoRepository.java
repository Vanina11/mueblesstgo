package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.SueldosEntity;

import java.util.List;

@Repository
public interface SueldoRepository extends JpaRepository<SueldosEntity, Long> {
    List<SueldosEntity> findByRut(String rut);
}
