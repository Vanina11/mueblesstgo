package tingeso.mueblesstgo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

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
    private String categoria;
    private Calendar fechaIngreso;
    // Calcula el suelo fijo correspondiente según la categoría
    private double sueldoFijo;
    // Calcula los años de servicio
    private int aniosServicio;

}
