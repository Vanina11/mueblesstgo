package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.HorasExtraEntity;

import java.util.ArrayList;

@Repository
public interface HorasExtraRepository extends JpaRepository<HorasExtraEntity, Long> {
    HorasExtraEntity findByEmpleadoAndMes(EmpleadoEntity empleado, String mes);
    @Query("select j from HorasExtraEntity j where j.empleado.rut = :rut")
    ArrayList<HorasExtraEntity> findByRut(@Param("rut") String rut);
}
