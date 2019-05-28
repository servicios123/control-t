/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.QueryUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "SECUENCIA")
@NamedQueries({
    @NamedQuery(name = "Secuencia.findAll", query = "SELECT s FROM Secuencia s"),
    @NamedQuery(name = "Secuencia.findBySecuId", query = "SELECT s FROM Secuencia s WHERE s.secuId = :secuId")})
public class Secuencia implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Id
    @Basic(optional = false)
    @Column(name = "SECU_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SECUENCIA")
    @SequenceGenerator(name = "SEQ_SECUENCIA", sequenceName = "SEQ_SECUENCIA", allocationSize = 1)
    private Long secuId;
    
     @Column(name = "SECU_ESTADO")
    private String secuEstado;

   
     
    @JoinColumn(name = "SECU_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;

     @OneToMany(cascade= CascadeType.ALL, mappedBy="secuencia", fetch= FetchType.LAZY)
    List<DetSecuencia> detSecuencias;

    @Transient
    private String secuencia;

    public Secuencia() {
    }

    public Secuencia(Long secuId) {
        this.secuId = secuId;
    }

    public Long getSecuId() {
        return secuId;
    }

    public void setSecuId(Long secuId) {
        this.secuId = secuId;
    }
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

     public String getSecuEstado() {
        return secuEstado;
    }

    public void setSecuEstado(String secuEstado) {
        this.secuEstado = secuEstado;
    }
    
    public List<DetSecuencia> getDetSecuencias() {
        return detSecuencias;
    }

    public void setDetSecuencias(List<DetSecuencia> detSecuencias) {
        this.detSecuencias = detSecuencias;
    }
  
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuId != null ? secuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Secuencia)) {
            return false;
        }
        Secuencia other = (Secuencia) object;
        if ((this.secuId == null && other.secuId != null) || (this.secuId != null && !this.secuId.equals(other.secuId))) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Secuencia[ secuId=" + secuId + " ]";
    }

    public String getSecuencia() {
        
        //ordenamos la lista por nombre de empleado  
        Collections.sort(detSecuencias, new Comparator<Object>() {  
  
            @Override
            public int compare(Object o1, Object o2) {  
                DetSecuencia e1 = (DetSecuencia) o1;  
                DetSecuencia e2 = (DetSecuencia) o2;  
                if (e1.getDsOrden() > e2.getDsOrden()) {  
                    return 1;  
                } else if (e1.getDsOrden() < e2.getDsOrden()) {  
                    return -1;  
                } else {  
                    return 0;  
                }                  
            }  
        });  
        List<String> list = new ArrayList<String>();
        for(DetSecuencia detSec:detSecuencias){
            list.add(detSec.getJornada().getJoAlias());
        }
        secuencia = new StringBuilder(QueryUtil.join(list,",")).toString();
        return secuencia;
    }
    

}
