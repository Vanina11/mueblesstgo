package tingeso.mueblesstgo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "horas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorasExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Integer horas;
    private String fecha;
    @OneToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;
}
