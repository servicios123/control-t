/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "MENU")
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m"),
    @NamedQuery(name = "Menu.findByMenId", query = "SELECT m FROM Menu m WHERE m.menId = :menId"),
    @NamedQuery(name = "Menu.findByMenPosicion", query = "SELECT m FROM Menu m WHERE m.menPosicion = :menPosicion"),
    @NamedQuery(name = "Menu.findByMenLabel", query = "SELECT m FROM Menu m WHERE m.menLabel = :menLabel"),
    @NamedQuery(name = "Menu.findByMenMetodo", query = "SELECT m FROM Menu m WHERE m.menMetodo = :menMetodo")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "MEN_ID")
    private Long menId;
    @Column(name = "MEN_POSICION")
    private String menPosicion;
    @Column(name = "MEN_LABEL")
    private String menLabel;
    @Column(name = "MEN_METODO")
    private String menMetodo;
    

    public Menu() {
    }

    public Menu(Long menId, String menPosicion, String menLabel, String menMetodo) {
        this.menId = menId;
        this.menPosicion = menPosicion;
        this.menLabel = menLabel;
        this.menMetodo = menMetodo;
    }

    public Menu(Long menId) {
        this.menId = menId;
    }

    public Long getMenId() {
        return menId;
    }

    public void setMenId(Long menId) {
        this.menId = menId;
    }

    public String getMenPosicion() {
        return menPosicion;
    }

    public void setMenPosicion(String menPosicion) {
        this.menPosicion = menPosicion;
    }

    public String getMenLabel() {
        return menLabel;
    }

    public void setMenLabel(String menLabel) {
        this.menLabel = menLabel;
    }

    public String getMenMetodo() {
        return menMetodo;
    }

    public void setMenMetodo(String menMetodo) {
        this.menMetodo = menMetodo;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menId != null ? menId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.menId == null && other.menId != null) || (this.menId != null && !this.menId.equals(other.menId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.menLabel;
    }
}
