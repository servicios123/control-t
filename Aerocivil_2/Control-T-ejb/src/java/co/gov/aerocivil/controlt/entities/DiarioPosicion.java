/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DIARIO_POSICION")
@NamedQueries({
@NamedQuery(name = "DiarioPosicion.findAll", query = "SELECT d FROM DiarioPosicion d")})
public class DiarioPosicion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "DPOS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DIARIO_POSICION")
    @SequenceGenerator(name = "SEQ_DIARIO_POSICION", sequenceName = "SEQ_DIARIO_POSICION", allocationSize = 1)    
    private Long dposId;
    
    @Basic(optional = false)
    @Column(name = "DPOS_HORA_INGRESO")
    private Byte dposHoraIngreso;
    @Basic(optional = false)
    @Column(name = "DPOS_MINUTO_INGRESO")
    private Byte dposMinutoIngreso;
    @Column(name = "DPOS_HORA_SALIDA")
    private Byte dposHoraSalida;
    @Column(name = "DPOS_MINUTO_SALIDA")
    private Byte dposMinutoSalida;
    @Column(name = "DPOS_OBSERVACIONES")
    private String dposObservaciones;
    @Column(name = "DPOS_TIPO_REALIZACION")
    private String dposTipoRealizacion;
    @Column(name = "DPOS_FECHA_REGISTRO_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dposFechaRegistroInicio;
    @Column(name = "DPOS_FECHA_REGISTRO_SALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dposFechaRegistroSalida;

    @JoinColumn(name = "DPOS_FUNCIONARIO_REALIZA", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    @JoinColumn(name = "DPOS_TURNO", referencedColumnName = "TUR_ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Vistaprogramacion turno;
    
    @Column(name = "DPOS_NOTIFICACION")
    private String dposNotificacion;
    
    @Column(name = "DPOS_CERRADO")
    private Boolean dposCerrado;
    
    @Column(name = "DPOS_MIN_RETARDO")
    private Long dposRetardo;    

    @JoinColumn(name = "DPOS_FUNC_SUPERVISOR", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario supervisor;
        
    public Vistaprogramacion getTurno() {
        return turno;
    }

    public void setTurno(Vistaprogramacion turno) {
        this.turno = turno;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


    public DiarioPosicion() {
    }

    public DiarioPosicion(Long dposId) {
        this.dposId = dposId;
    }

    public DiarioPosicion(Long dposId, Byte dposHoraIngreso, Byte dposMinutoIngreso) {
        this.dposId = dposId;
        this.dposHoraIngreso = dposHoraIngreso;
        this.dposMinutoIngreso = dposMinutoIngreso;
    }

    public Long getDposId() {
        return dposId;
    }

    public void setDposId(Long dposId) {
        this.dposId = dposId;
    }

    public Byte getDposHoraIngreso() {
        return dposHoraIngreso;
    }

    public void setDposHoraIngreso(Byte dposHoraIngreso) {
        this.dposHoraIngreso = dposHoraIngreso;
    }

    public Byte getDposMinutoIngreso() {
        return dposMinutoIngreso;
    }

    public void setDposMinutoIngreso(Byte dposMinutoIngreso) {
        this.dposMinutoIngreso = dposMinutoIngreso;
    }

    public Byte getDposHoraSalida() {
        return dposHoraSalida;
    }

    public void setDposHoraSalida(Byte dposHoraSalida) {
        this.dposHoraSalida = dposHoraSalida;
    }

    public Byte getDposMinutoSalida() {
        return dposMinutoSalida;
    }

    public void setDposMinutoSalida(Byte dposMinutoSalida) {
        this.dposMinutoSalida = dposMinutoSalida;
    }

    public String getDposObservaciones() {
        return dposObservaciones;
    }

    public void setDposObservaciones(String dposObservaciones) {
        this.dposObservaciones = dposObservaciones;
    }

    public String getDposTipoRealizacion() {
        return dposTipoRealizacion;
    }

    public void setDposTipoRealizacion(String dposTipoRealizacion) {
        this.dposTipoRealizacion = dposTipoRealizacion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dposId != null ? dposId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioPosicion)) {
            return false;
        }
        DiarioPosicion other = (DiarioPosicion) object;
        if ((this.dposId == null && other.dposId != null) || (this.dposId != null && !this.dposId.equals(other.dposId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioPosicion[ dposId=" + dposId + " ]";
    }
    
        public Date getDposFechaRegistroInicio() {
        return dposFechaRegistroInicio;
    }

    public void setDposFechaRegistroInicio(Date dposFechaRegistroInicio) {
        this.dposFechaRegistroInicio = dposFechaRegistroInicio;
    }

    public Date getDposFechaRegistroSalida() {
        return dposFechaRegistroSalida;
    }

    public void setDposFechaRegistroSalida(Date dposFechaRegistroSalida) {
        this.dposFechaRegistroSalida = dposFechaRegistroSalida;
    }
    
    public String getHoraInicioStr(){
        return StringDateUtil.getFormatoHora(this.dposHoraIngreso, this.dposMinutoIngreso);
    }
    
    public String getHoraFinStr(){
        return StringDateUtil.getFormatoHora(this.dposHoraSalida, this.dposMinutoSalida);
    }
 
    public String getDposNotificacion() {
        return dposNotificacion;
    }

    public void setDposNotificacion(String dposNotificacion) {
        this.dposNotificacion = dposNotificacion;
    }

    public Boolean getDposCerrado() {
        return dposCerrado;
    }

    public void setDposCerrado(Boolean dposCerrado) {
        this.dposCerrado = dposCerrado;
    }

    public Long getDposRetardo() {
        return dposRetardo;
    }

    public void setDposRetardo(Long dposRetardo) {
        this.dposRetardo = dposRetardo;
    }

    public Funcionario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Funcionario supervisor) {
        this.supervisor = supervisor;
    }    

}
