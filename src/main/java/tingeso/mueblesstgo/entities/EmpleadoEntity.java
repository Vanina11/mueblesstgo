package tingeso.mueblesstgo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    // Datos desde el archivo excel
    private String rut;
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String categoria;
    private String fechaIngreso;
    private Integer inasistencias = 0;
    private Integer descuentoAtraso = 0;

    @OneToMany(mappedBy = "empleado")
    private List<MarcasRelojEntity> marcas;
    @OneToMany(mappedBy = "empleado")
    private List<JustificativosEntity> justificativos;
    @OneToMany(mappedBy = "empleado")
    private List<HorasExtraEntity> horasExtra;
    @OneToMany(mappedBy = "empleado")
    private List<SueldosEntity> sueldos;
}
