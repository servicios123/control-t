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
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "DIARIO_SENAL")
@NamedQueries({
    @NamedQuery(name = "DiarioSenal.findAll", query = "SELECT d FROM DiarioSenal d"),
    @NamedQuery(name = "DiarioSenal.findByDsenId", query = "SELECT d FROM DiarioSenal d WHERE d.dsenId = :dsenId"),
    @NamedQuery(name = "DiarioSenal.findByDsenDescripcion", query = "SELECT d FROM DiarioSenal d WHERE d.dsenDescripcion = :dsenDescripcion"),
    @NamedQuery(name = "DiarioSenal.findByDsenFechaSuceso", query = "SELECT d FROM DiarioSenal d WHERE d.dsenFechaSuceso = :dsenFechaSuceso"),
    @NamedQuery(name = "DiarioSenal.findByDsenHoraSuceso", query = "SELECT d FROM DiarioSenal d WHERE d.dsenHoraSuceso = :dsenHoraSuceso"),
    @NamedQuery(name = "DiarioSenal.findByDsenFechaRegistro", query = "SELECT d FROM DiarioSenal d WHERE d.dsenFechaRegistro = :dsenFechaRegistro"),
    /*@NamedQuery(name = "DiarioSenal.findByDsenHoraRegistro", query = "SELECT d FROM DiarioSenal d WHERE d.dsenHoraRegistro = :dsenHoraRegistro"),*/
    @NamedQuery(name = "DiarioSenal.findByDsenLugarSuceso", query = "SELECT d FROM DiarioSenal d WHERE d.dsenLugarSuceso = :dsenLugarSuceso")})
public class DiarioSenal implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DSEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DIARIO_SENAL")
    @SequenceGenerator(name = "SEQ_DIARIO_SENAL", sequenceName = "SEQ_DIARIO_SENAL", allocationSize = 1)
    private Long dsenId;
    @Column(name = "DSEN_DESCRIPCION")
    private String dsenDescripcion;
    @Basic(optional = false)
    @Column(name = "DSEN_FECHA_SUCESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dsenFechaSuceso;
    @Transient
    private Date dsenFechaSucesoFinal;
    @Column(name = "DSEN_HORA_SUCESO")
    private Byte dsenHoraSuceso;
    @Column(name = "DSEN_MIN_SUCESO")
    private Byte dsenMinSuceso;
    @Basic(optional = false)
    @Column(name = "DSEN_FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dsenFechaRegistro;
    @Transient
    private Date dsenFechaRegistroFinal;
    /*   @Column(name = "DSEN_HORA_REGISTRO")
     private Long dsenHoraRegistro;
     @Column(name = "DSEN_MIN_REGISTRO")
     private Long dsenMinRegistro;*/
    @Column(name = "DSEN_LUGAR_SUCESO")
    private String dsenLugarSuceso;
    @JoinColumn(name = "DSEN_TIPO", referencedColumnName = "DST_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DsTipo dsTipo;
    @JoinColumn(name = "DSEN_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;
    @JoinColumn(name = "DSEN_FUNCIONARIO_REGISTRA", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @Column(name = "DSEN_RESALTAR")
    private Boolean resaltar;
    @Transient
    private String dsenHoraSucesoStr;

    public DiarioSenal() {
    }

    public DiarioSenal(Long dsenId) {
        this.dsenId = dsenId;
    }

    public DiarioSenal(Long dsenId, Date dsenFechaSuceso, Date dsenFechaRegistro, Long dsenHoraRegistro) {
        this.dsenId = dsenId;
        this.dsenFechaSuceso = dsenFechaSuceso;
        this.dsenFechaRegistro = dsenFechaRegistro;
        // this.dsenHoraRegistro = dsenHoraRegistro;
    }

    public Byte getDsenMinSuceso() {
        return dsenMinSuceso;
    }

    public void setDsenMinSuceso(Byte dsenMinSuceso) {
        this.dsenMinSuceso = dsenMinSuceso;
    }

    /*public Long getDsenMinRegistro() {
     return dsenMinRegistro;
     }

     public void setDsenMinRegistro(Long dsenMinRegistro) {
     this.dsenMinRegistro = dsenMinRegistro;
     }*/
    public Long getDsenId() {
        return dsenId;
    }

    public void setDsenId(Long dsenId) {
        this.dsenId = dsenId;
    }

    public String getDsenDescripcion() {
        return dsenDescripcion;
    }

    public void setDsenDescripcion(String dsenDescripcion) {
        this.dsenDescripcion = dsenDescripcion;
    }

    public Date getDsenFechaSuceso() {
        return dsenFechaSuceso;
    }

    public void setDsenFechaSuceso(Date dsenFechaSuceso) {
        this.dsenFechaSuceso = dsenFechaSuceso;
    }

    public Byte getDsenHoraSuceso() {
        return dsenHoraSuceso;
    }

    public void setDsenHoraSuceso(Byte dsenHoraSuceso) {
        this.dsenHoraSuceso = dsenHoraSuceso;
    }

    public Date getDsenFechaRegistro() {
        return dsenFechaRegistro;
    }

    public void setDsenFechaRegistro(Date dsenFechaRegistro) {
        this.dsenFechaRegistro = dsenFechaRegistro;
    }

    public Boolean getResaltar() {
        return resaltar;
    }

    public void setResaltar(Boolean resaltar) {
        this.resaltar = resaltar;
    }

    /* public Long getDsenHoraRegistro() {
     return dsenHoraRegistro;
     }

     public void setDsenHoraRegistro(Long dsenHoraRegistro) {
     this.dsenHoraRegistro = dsenHoraRegistro;
     }*/
    public String getDsenLugarSuceso() {
        return dsenLugarSuceso;
    }

    public void setDsenLugarSuceso(String dsenLugarSuceso) {
        this.dsenLugarSuceso = dsenLugarSuceso;
    }

    public DsTipo getDsTipo() {
        return dsTipo;
    }

    public void setDsTipo(DsTipo dsTipo) {
        this.dsTipo = dsTipo;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsenId != null ? dsenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioSenal)) {
            return false;
        }
        DiarioSenal other = (DiarioSenal) object;
        if ((this.dsenId == null && other.dsenId != null) || (this.dsenId != null && !this.dsenId.equals(other.dsenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenal[ dsenId=" + dsenId + " ]";
    }

    public String getDsenHoraSucesoStr() {
        //return StringDateUtil.getFormatoHora(dsenHoraSuceso, dsenMinSuceso);
        Calendar c = Calendar.getInstance();
        c.setTime(dsenFechaSuceso);
        //c.add(Calendar.HOUR_OF_DAY, 5);
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }

    public Date getDsenFechaSucesoFinal() {
        return dsenFechaSucesoFinal;
    }

    public void setDsenFechaSucesoFinal(Date dsenFechaSucesoFinal) {
        this.dsenFechaSucesoFinal = dsenFechaSucesoFinal;
    }

    /**
     * @return the dsenFechaRegistroFinal
     */
    public Date getDsenFechaRegistroFinal() {
        return dsenFechaRegistroFinal;
    }

    /**
     * @param dsenFechaRegistroFinal the dsenFechaRegistroFinal to set
     */
    public void setDsenFechaRegistroFinal(Date dsenFechaRegistroFinal) {
        this.dsenFechaRegistroFinal = dsenFechaRegistroFinal;
    }
}
