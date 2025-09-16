package com.inventa.inventa.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "token_recuperacion")
public class TokenRecuperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

  @ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
@OnDelete(action = OnDeleteAction.CASCADE)
private Usuario usuario;


    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime fechaExpiracion;
    private Boolean usado;
}
