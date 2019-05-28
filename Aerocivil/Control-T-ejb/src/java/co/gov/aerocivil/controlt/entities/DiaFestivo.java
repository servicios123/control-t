/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DIA_FESTIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiaFestivo.findAll", query = "SELECT d FROM DiaFestivo d"),
    @NamedQuery(name = "DiaFestivo.findByDfId", query = "SELECT d FROM DiaFestivo d WHERE d.dfId = :dfId"),
    @NamedQuery(name = "DiaFestivo.findByDfFecha", query = "SELECT d FROM DiaFestivo d WHERE d.dfFecha = :dfFecha")})
public class DiaFestivo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DF_ID")    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FESTIVO")
    @SequenceGenerator(name = "SEQ_FESTIVO", sequenceName = "SEQ_FESTIVO", allocationSize = 1)
    private Long dfId;
    @Basic(optional = false)
    @Column(name = "DF_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dfFecha;

    public DiaFestivo() {
    }

    public DiaFestivo(Long dfId) {
        this.dfId = dfId;
    }

    public DiaFestivo(Date dfFecha) {
        this.dfFecha = dfFecha;
    }

    public Long getDfId() {
        return dfId;
    }

    public void setDfId(Long dfId) {
        this.dfId = dfId;
    }

    public Date getDfFecha() {
        return dfFecha;
    }

    public void setDfFecha(Date dfFecha) {
        this.dfFecha = dfFecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dfId != null ? dfId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiaFestivo)) {
            return false;
        }
        DiaFestivo other = (DiaFestivo) object;
        if ((this.dfId == null && other.dfId != null) || (this.dfId != null && !this.dfId.equals(other.dfId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiaFestivo[ dfId=" + dfId + " ]";
    }
    
}
