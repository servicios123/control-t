/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "RESTRICCION_PROGR")
@NamedQueries({
    @NamedQuery(name = "RestriccionProgramacion.findAll", query = "SELECT r FROM RestriccionProgramacion r"),
    @NamedQuery(name = "RestriccionProgramacion.findByRpId", query = "SELECT r FROM RestriccionProgramacion r WHERE r.rpId = :rpId"),
    @NamedQuery(name = "RestriccionProgramacion.findByRpDescripcion", query = "SELECT r FROM RestriccionProgramacion r WHERE r.rpDescripcion = :rpDescripcion")})
public class RestriccionProgramacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "RP_ID")
    private Long rpId;
    @Basic(optional = false)
    @Column(name = "RP_DESCRIPCION")
    private String rpDescripcion;
    /*@OneToMany(mappedBy = "RestriccionProgramacion", fetch = FetchType.LAZY)
    private List<RestriccionDependencia> restriccionesDepenList;*/

    public RestriccionProgramacion() {
    }

    public RestriccionProgramacion(Long rpId) {
        this.rpId = rpId;
    }



    public Long getRpId() {
        return rpId;
    }

    public void setRpId(Long rpId) {
        this.rpId = rpId;
    }

    public String getRpDescripcion() {
        return rpDescripcion;
    }

    public void setRpDescripcion(String rpDescripcion) {
        this.rpDescripcion = rpDescripcion;
    }

    /*public List<RestriccionesDepen> getRestriccionesDepenList() {
        return restriccionesDepenList;
    }

    public void setRestriccionesDepenList(List<RestriccionesDepen> restriccionesDepenList) {
        this.restriccionesDepenList = restriccionesDepenList;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpId != null ? rpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RestriccionProgramacion)) {
            return false;
        }
        RestriccionProgramacion other = (RestriccionProgramacion) object;
        if ((this.rpId == null && other.rpId != null) || (this.rpId != null && !this.rpId.equals(other.rpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.RestriccionProgr[ rpId=" + rpId + " ]";
    }
    
/*    public List<RestriccionDependencia> getRestriccionesDepenList() {
        return restriccionesDepenList;
    }

    public void setRestriccionesDepenList(List<RestriccionDependencia> restriccionesDepenList) {
        this.restriccionesDepenList = restriccionesDepenList;
    }*/

    
}
