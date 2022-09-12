package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tingeso.mueblesstgo.entities.EmpleadoEntity;
import tingeso.mueblesstgo.entities.MarcasRelojEntity;

@Repository
public interface MarcasRelojRepository extends JpaRepository<MarcasRelojEntity, Long> {
    //MarcasRelojEntity findByRut(String rut);
    //public MarcasRelojEntity findByEmpleado(String rut);

}
