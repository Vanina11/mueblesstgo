package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    public EmpleadoEntity findByRut(String rut);

    @Query("select e from EmpleadoEntity e where e.nombres = :nombres")
    EmpleadoEntity findByNameCustomQuery(@Param("nombres") String nombre);

    @Query(value = "select * from empleados as e where e.nombres = :nombres",
            nativeQuery = true)
    EmpleadoEntity findByNameNativeQuery(@Param("nombres") String nombre);

}