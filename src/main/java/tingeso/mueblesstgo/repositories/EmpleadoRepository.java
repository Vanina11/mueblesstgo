package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    EmpleadoEntity findByRut(String rut);
}