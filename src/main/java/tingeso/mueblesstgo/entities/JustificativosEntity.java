package tingeso.mueblesstgo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "justificativos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JustificativosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String fecha;
    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleado;
}
