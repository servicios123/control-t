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


import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "PERMISO_ESPECIAL")
@NamedQueries({
    @NamedQuery(name = "PermisoEspecial.findAll", query = "SELECT p FROM PermisoEspecial p"),
    @NamedQuery(name = "PermisoEspecial.findByPeId", query = "SELECT p FROM PermisoEspecial p WHERE p.peId = :peId"),
    @NamedQuery(name = "PermisoEspecial.findByPeFuncionarioAprueba", query = "SELECT p FROM PermisoEspecial p WHERE p.funcionarioAprueba = :peFunAprobo"),
    @NamedQuery(name = "PermisoEspecial.findByUser", query = "SELECT p FROM PermisoEspecial p WHERE p.funcionario.funId = :funId and p.peFechaPermiso = :fecha and p.peEstado='Aprobado'"),
    @NamedQuery(name = "PermisoEspecial.findByPeEstado", query = "SELECT p FROM PermisoEspecial p WHERE p.peEstado = :peEstado")
})
public class PermisoEspecial implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "PE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERMISO")
    @SequenceGenerator(name = "SEQ_PERMISO", sequenceName = "SEQ_PERMISO", allocationSize = 1)
    private Long peId;
    
    @Basic(optional = false)
    @Column(name = "PE_FECHA_PER")
    @Temporal(TemporalType.DATE)
    private Date peFechaPermiso;
    
    @Column(name = "PE_FECHA_REGISTRO")
    @Temporal(TemporalType.DATE)
    private Date peFechaRegistro;

     @Column(name = "PE_FAPROBACION")
    @Temporal(TemporalType.DATE)
    private Date peFechaAprobacion;

    @JoinColumn(name = "PE_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
    
    @JoinColumn(name = "PE_FUN_APROBO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionarioAprueba;
    
     @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permiso_especial_jornada", 
            joinColumns        = @JoinColumn(name = "pej_permiso_especial"), 
            inverseJoinColumns = @JoinColumn(name = "pej_jornada")
    )
    private List<Jornada> listaJornadas;

   @Transient
    private String jornadaString;

   
    
    @Transient
    private Dependencia dependencia;

    @Basic(optional = false)
    @Column(name = "PE_ESTADO")
    private String peEstado;
    
    @Column(name = "PE_DESCRIPCION")
    private String peDescripcion;
    
    @Transient 
    private String jornadasRequeridas;

    public Long getPeId() {
        return peId;
    }

    public void setPeId(Long peId) {
        this.peId = peId;
    }

    public String getPeEstado() {
        return peEstado;
    }

    public void setPeEstado(String peEstado) {
        this.peEstado = peEstado;
    }

    public String getPeDescripcion() {
        return peDescripcion;
    }

    public void setPeDescripcion(String peDescripcion) {
        this.peDescripcion = peDescripcion;
    }

     public String getJornadaString() {
        return jornadaString;
    }

    public void setJornadaString(String jornadaString) {
        this.jornadaString = jornadaString;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peId != null ? peId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoEspecial)) {
            return false;
        }
        PermisoEspecial other = (PermisoEspecial) object;
        if ((this.peId == null && other.peId != null) || (this.peId != null && !this.peId.equals(other.peId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.PermisoEspecial[ peId=" + peId + " ]";
    }

    public Date getPeFechaPermiso() {
        return peFechaPermiso;
    }

    public void setPeFechaPermiso(Date peFechaPermiso) {
        this.peFechaPermiso = peFechaPermiso;
    }

    
    public Date getPeFechaAprobacion() {
        return peFechaAprobacion;
    }

    public void setPeFechaAprobacion(Date peFechaAprobacion) {
        this.peFechaAprobacion = peFechaAprobacion;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Funcionario getFuncionarioAprueba() {
        return funcionarioAprueba;
    }

    public void setFuncionarioAprueba(Funcionario funcionarioAprueba) {
        this.funcionarioAprueba = funcionarioAprueba;
    }

   
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Date getPeFechaRegistro() {
        return peFechaRegistro;
    }

    public void setPeFechaRegistro(Date peFechaRegistro) {
        this.peFechaRegistro = peFechaRegistro;
    }

     public List<Jornada> getListaJornadas() {
        return listaJornadas;
    }

    public void setListaJornadas(List<Jornada> listaJornadas) {
        this.listaJornadas = listaJornadas;
    }

    public String getJornadasRequeridas() {
            
        Collections.sort(listaJornadas, new Comparator<Object>() {  
  
            @Override
            public int compare(Object o1, Object o2) {  
                Jornada e1 = (Jornada) o1;  
                Jornada e2 = (Jornada) o2;  
                if (e1.getJoHoraInicio() > e2.getJoHoraInicio()) {  
                    return 1;  
                } else if (e1.getJoHoraInicio() < e2.getJoHoraInicio()) {  
                    return -1;  
                } else {  
                    return 0;  
                }                  
            }  
        });  
        List<String> list = new ArrayList<String>();
        for(Jornada detSec:listaJornadas){
            list.add(detSec.getJoAlias());
        }
        jornadasRequeridas = new StringBuilder(QueryUtil.join(list,",")).toString();

        return jornadasRequeridas;
    }
   
}