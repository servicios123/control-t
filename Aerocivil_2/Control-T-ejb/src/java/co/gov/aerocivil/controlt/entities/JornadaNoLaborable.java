/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;

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

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "JORNADA_NO_LABORABLE")
@NamedQueries({
    @NamedQuery(name = "JornadaNoLaborable.findAll", query = "SELECT j FROM Jornada j"),
    @NamedQuery(name = "JornadaNoLaborable.findByJoId", query = "SELECT j FROM Jornada j WHERE j.joId = :joId")})
public class JornadaNoLaborable implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_JOR_NO_LABORAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JORNADA_NO_LABORAL")
    @SequenceGenerator(name = "SEQ_JORNADA_NO_LABORAL", sequenceName = "SEQ_JORNADA_NO_LABORAL", allocationSize = 1)
    private Long Id;
    @JoinColumn(name = "ID_JORNADA", referencedColumnName = "JO_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Jornada jornada;
    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Funcionario funcionario;

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
