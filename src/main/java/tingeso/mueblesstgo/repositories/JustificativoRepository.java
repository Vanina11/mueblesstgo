package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.JustificativosEntity;

import java.util.ArrayList;

@Repository
public interface JustificativoRepository extends JpaRepository<JustificativosEntity, Long> {
    @Query("select j from JustificativosEntity j where j.empleado.rut = :rut")
    ArrayList<JustificativosEntity> findByRut(@Param("rut") String rut);
}
