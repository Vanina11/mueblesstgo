package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;

import java.util.List;

@Repository
public interface MarcasRelojRepository extends JpaRepository<MarcasRelojEntity, Long> {

    MarcasRelojEntity findByFechaAndEmpleado(String fecha, EmpleadoEntity empleado);

    @Query("select m from MarcasRelojEntity m where m.empleado.rut = :rut")
    List<MarcasRelojEntity> findByRut(@Param("rut") String rut);



}
