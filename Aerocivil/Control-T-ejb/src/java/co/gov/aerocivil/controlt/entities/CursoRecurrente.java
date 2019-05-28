/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "CURSO_RECURRENTE")
@NamedQueries({
    @NamedQuery(name = "CursoRecurrente.findAll", query = "SELECT c FROM CursoRecurrente c"),
    @NamedQuery(name = "CursoRecurrente.findByCurId", query = "SELECT c FROM CursoRecurrente c WHERE c.curId = :curId"),
    @NamedQuery(name = "CursoRecurrente.findByCurNombre", query = "SELECT c FROM CursoRecurrente c WHERE c.curNombre = :curNombre"),
    @NamedQuery(name = "CursoRecurrente.findByCurFechaRealiza", query = "SELECT c FROM CursoRecurrente c WHERE c.curFechaRealiza = :curFechaRealiza"),
    @NamedQuery(name = "CursoRecurrente.findByCurFechaVencimiento", query = "SELECT c FROM CursoRecurrente c WHERE c.curFechaVencimiento = :curFechaVencimiento")})
public class CursoRecurrente implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CUR_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CURSO_RECURRENTE")
    @SequenceGenerator(name = "SEQ_CURSO_RECURRENTE", sequenceName = "SEQ_CURSO_RECURRENTE", allocationSize = 1)
    private Long curId;
    @Column(name = "CUR_NOMBRE")
    private String curNombre;
    @Column(name = "CUR_FECHA_REALIZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date curFechaRealiza;
    @Column(name = "CUR_FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date curFechaVencimiento;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "curso_recurrente_funcionario", 
            joinColumns        = @JoinColumn(name = "crc_curso_recurrente"), 
            inverseJoinColumns = @JoinColumn(name = "crc_funcionario")
    )
    private List<Funcionario> listaFuncionarios;

    public CursoRecurrente() {
    }

    public CursoRecurrente(Long curId) {
        this.curId = curId;
    }

    public Long getCurId() {
        return curId;
    }

    public void setCurId(Long curId) {
        this.curId = curId;
    }

    public String getCurNombre() {
        return curNombre;
    }

    public void setCurNombre(String curNombre) {
        this.curNombre = curNombre;
    }

    public Date getCurFechaRealiza() {
        return curFechaRealiza;
    }

    public void setCurFechaRealiza(Date curFechaRealiza) {
        this.curFechaRealiza = curFechaRealiza;
    }

    public Date getCurFechaVencimiento() {
        return curFechaVencimiento;
    }

    public void setCurFechaVencimiento(Date curFechaVencimiento) {
        this.curFechaVencimiento = curFechaVencimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (curId != null ? curId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursoRecurrente)) {
            return false;
        }
        CursoRecurrente other = (CursoRecurrente) object;
        if ((this.curId == null && other.curId != null) || (this.curId != null && !this.curId.equals(other.curId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.CursoRecurrente[ curId=" + curId + " ]";
    }

    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }
}
