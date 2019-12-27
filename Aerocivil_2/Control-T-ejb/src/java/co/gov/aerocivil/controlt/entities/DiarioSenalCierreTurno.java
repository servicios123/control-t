/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Administrador
 */
@Entity
public class DiarioSenalCierreTurno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long id;
    
    @Column(name="PRODUCTO")
    private String Producto;
    
    @Column(name="SERIE")
    private String Serie;    
    
    @Column(name="PUBLICADAS")
    private String publicadas;

    @Column(name="EI")
    private String ei;
    
    @Column(name="EE")
    private String ee;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioSenalCierreTurno)) {
            return false;
        }
        DiarioSenalCierreTurno other = (DiarioSenalCierreTurno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalCierreTurno[ id=" + id + " ]";
    }

    /**
     * @return the Producto
     */
    public String getProducto() {
        return Producto;
    }

    /**
     * @param Producto the Producto to set
     */
    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

    /**
     * @return the Serie
     */
    public String getSerie() {
        return Serie;
    }

    /**
     * @param Serie the Serie to set
     */
    public void setSerie(String Serie) {
        this.Serie = Serie;
    }

    /**
     * @return the publicadas
     */
    public String getPublicadas() {
        return publicadas;
    }

    /**
     * @param publicadas the publicadas to set
     */
    public void setPublicadas(String publicadas) {
        this.publicadas = publicadas;
    }

    /**
     * @return the ei
     */
    public String getEi() {
        return ei;
    }

    /**
     * @param ei the ei to set
     */
    public void setEi(String ei) {
        this.ei = ei;
    }

    /**
     * @return the ee
     */
    public String getEe() {
        return ee;
    }

    /**
     * @param ee the ee to set
     */
    public void setEe(String ee) {
        this.ee = ee;
    }
    
}
