package tingeso.mueblesstgo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "sueldos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SueldosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private String nombres;
    private String apellidos;
    private String categoria;
    private Integer aniosServicio;
    private double sueldoFijo;
    private double montoBonificacion;
    private double montoHorasExtra;
    private double montoDescuentos;
    private double sueldoBruto;
    private double cotizacionPrevisional;
    private double cotizacionSalud;
    private double sueldoFinal;
    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;

}
