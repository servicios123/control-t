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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "POS_NO_ASIG")
@NamedQueries({
    @NamedQuery(name = "PosNoAsig.findAll", query = "SELECT p FROM PosNoAsig p"),
    @NamedQuery(name = "PosNoAsigxDependencia.findAll2", query = "SELECT p FROM PosNoAsig p where p.dependencia = :dep and p.programacion= :prog"),
    @NamedQuery(name = "PosNoAsigxDependencia.findAll", query = "SELECT p FROM PosNoAsig p where p.dependencia = :dep and p.programacion= :prog and p.posicionJornada.pjId not in (SELECT i.posicionJornada.pjId FROM PosicionInactiva i where i.posicionJornada.pjId = p.posicionJornada.pjId and func('TO_DATE',i.fecha,'DD/MM/YY') = func('TO_DATE',p.fecha,'DD/MM/YY') ) order by p.fecha asc"),
    @NamedQuery(name = "PosNoAsig.delete", query = "delete FROM PosNoAsig p where p.dependencia = :dep")})
    public class PosNoAsig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "POSICION")
    private Long posicion;
    @Column(name = "JORNADA")
    private Long jornada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
     @Id
    @Basic(optional = false)
    @Column(name = "PNA_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POS_NO_ASIGNADA")
    @SequenceGenerator(name = "SEQ_POS_NO_ASIGNADA", sequenceName = "SEQ_POS_NO_ASIGNADA", allocationSize = 1)
    private Long pna_id;
    
    

   
    @Column(name = "DEPENDENCIA")
    private Long dependencia;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "PROGRAMACION")
    private Long programacion;

     @JoinColumn(name = "POSI_JORNADA", referencedColumnName = "PJ_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PosicionJornada posicionJornada;
     

    public PosicionJornada getPosicionJornada() {
        return posicionJornada;
    }

    public void setPosicionJornada(PosicionJornada posicionJornada) {
        this.posicionJornada = posicionJornada;
    }
     
    public PosNoAsig() {
    }

  

    public Long getPosicion() {
        return posicion;
    }

    public void setPosicion(Long posicion) {
        this.posicion = posicion;
    }

    public Long getJornada() {
        return jornada;
    }

    public void setJornada(Long jornada) {
        this.jornada = jornada;
    }

   
    public Long getDependencia() {
        return dependencia;
    }

    public void setDependencia(Long dependencia) {
        this.dependencia = dependencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Long programacion) {
        this.programacion = programacion;
    }
    
     public Long getPna_id() {
        return pna_id;
    }

    public void setPna_id(Long pna_id) {
        this.pna_id = pna_id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pna_id != null ? pna_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosNoAsig)) {
            return false;
        }
        PosNoAsig other = (PosNoAsig) object;
        if ((this.pna_id == null && other.pna_id != null) || (this.pna_id != null && !this.pna_id.equals(other.pna_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PosNoAsig[ pna_id=" + pna_id + " ]";
    }
    
}
