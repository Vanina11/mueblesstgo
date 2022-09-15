package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;

@Repository
public interface HorasExtraRepository extends JpaRepository<HorasExtraEntity, Long> {
    HorasExtraEntity findByEmpleadoAndMes(EmpleadoEntity empleado, String mes);
}
