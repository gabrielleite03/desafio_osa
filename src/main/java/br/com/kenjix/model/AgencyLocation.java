package br.com.kenjix.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "agency_location", uniqueConstraints = {@UniqueConstraint(columnNames = {"pos_x", "pos_y"})})
public class AgencyLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pos_x", nullable = false)
    private Double posX;

    @Column(name = "pos_y", nullable = false)
    private Double posY;

    public AgencyLocation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AgencyLocation that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(posX, that.posX) && Objects.equals(posY, that.posY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, posX, posY);
    }
}
