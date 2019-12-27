/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "JORNADA_OP")
@NamedQueries({
    @NamedQuery(name = "Jornada.findAll", query = "SELECT j FROM Jornada j"),
    @NamedQuery(name = "Jornada.findByJoId", query = "SELECT j FROM Jornada j WHERE j.joId = :joId"),
    @NamedQuery(name = "Jornada.findByJoAlias", query = "SELECT j FROM Jornada j WHERE j.joAlias = :joAlias"),
    @NamedQuery(name = "Jornada.findByJoHoraInicio", query = "SELECT j FROM Jornada j WHERE j.joHoraInicio = :joHoraInicio"),
    @NamedQuery(name = "Jornada.findByJoHoraFin", query = "SELECT j FROM Jornada j WHERE j.joHoraFin = :joHoraFin"),
    @NamedQuery(name = "Jornada.findByDependencia", query = "SELECT j FROM Jornada j WHERE j.dependencia.depId = :dep and j.joEstado='Activo' order by j.joAlias")})
public class Jornada implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "JO_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JORNADA")
    @SequenceGenerator(name = "SEQ_JORNADA", sequenceName = "SEQ_JORNADA", allocationSize = 1)
    private Long joId;

    public Jornada() {
    }

    public Jornada(Long joId) {
        this.joId = joId;
    }
    @Basic(optional = false)
    @Column(name = "JO_ALIAS")
    private String joAlias;
    @Basic(optional = false)
    @Column(name = "JO_HORA_INICIO")
    private Byte joHoraInicio;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "jornada_restriccion",
    joinColumns =
    @JoinColumn(name = "jr_jornada"),
    inverseJoinColumns =
    @JoinColumn(name = "jr_jornada_ev"))
    private List<Jornada> jornadasRestriccion;
    @Basic(optional = false)
    @Column(name = "JO_MIN_INICIO")
    private Byte joMinutoInicio;
    @Basic(optional = false)
    @Column(name = "JO_HORA_fIN")
    private Byte joHoraFin;
    @Basic(optional = false)
    @Column(name = "JO_MIN_FIN")
    private Byte joMinutoFin;
    @Column(name = "JO_ESTADO")
    private String joEstado;
    @JoinColumn(name = "JO_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;
    @JoinColumn(name = "JO_JORNADA_OBLIGATORIA", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Jornada jornadaObligatoria;
    @Transient
    private Boolean seleccionado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jornada", fetch = FetchType.EAGER)
    List<JornadaNoLaborable> listaJornadasNoLaborables;

    public Long getJoId() {
        return joId;
    }

    public void setJoId(Long joId) {
        this.joId = joId;
    }

    public String getJoAlias() {
        return joAlias;
    }

    public void setJoAlias(String joAlias) {
        this.joAlias = joAlias;
    }

    public Byte getJoHoraInicio() {
        return joHoraInicio;
    }

    public void setJoHoraInicio(Byte joHoraInicio) {
        this.joHoraInicio = joHoraInicio;
    }

    public List<Jornada> getJornadasRestriccion() {
        return jornadasRestriccion;
    }

    public void setJornadasRestriccion(List<Jornada> jornadasRestriccion) {
        this.jornadasRestriccion = jornadasRestriccion;
    }

    public Byte getJoMinutoInicio() {
        return joMinutoInicio;
    }

    public void setJoMinutoInicio(Byte joMinutoInicio) {
        this.joMinutoInicio = joMinutoInicio;
    }

    public Byte getJoHoraFin() {
        return joHoraFin;
    }

    public void setJoHoraFin(Byte joHoraFin) {
        this.joHoraFin = joHoraFin;
    }

    public Byte getJoMinutoFin() {
        return joMinutoFin;
    }

    public void setJoMinutoFin(Byte joMinutoFin) {
        this.joMinutoFin = joMinutoFin;
    }

    public String getJoEstado() {
        return joEstado;
    }

    public void setJoEstado(String joEstado) {
        this.joEstado = joEstado;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Jornada getJornadaObligatoria() {
        return jornadaObligatoria;
    }

    public void setJornadaObligatoria(Jornada jornadaObligatoria) {
        this.jornadaObligatoria = jornadaObligatoria;
    }

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public List<JornadaNoLaborable> getListaJornadasNoLaborables() {
        return listaJornadasNoLaborables;
    }

    public void setListaJornadasNoLaborables(List<JornadaNoLaborable> listaJornadasNoLaborables) {
        this.listaJornadasNoLaborables = listaJornadasNoLaborables;
    }
    
    
    public String getHoraInicioFull(){
        String hora = "";
        if(joHoraInicio.toString().length()==1){
            hora+="0"+joHoraInicio.toString();
        }else{
            hora+=joHoraInicio.toString();
        }
        hora+=":";
        if(joMinutoInicio.toString().length()==1){
            hora+="0"+joMinutoInicio.toString();
        }else{
            hora+=joMinutoInicio.toString();
        }
        return hora;
        
    }
    
    public String getHoraFinFull(){
        String hora = "";
        if(joHoraFin.toString().length()==1){
            hora+="0"+joHoraFin.toString();
        }else{
            hora+=joHoraFin.toString();
        }
        hora+=":";
        if(joMinutoFin.toString().length()==1){
            hora+="0"+joMinutoFin.toString();
        }else{
            hora+=joMinutoFin.toString();
        }
        return hora;
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (joId != null ? joId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jornada)) {
            return false;
        }
        Jornada other = (Jornada) object;
        if ((this.joId == null && other.joId != null) || (this.joId != null && !this.joId.equals(other.joId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.Jornada[ joId=" + joId + " ]";
    }
}
