/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "TEMP_VER_PROGRAMACION")
@NamedQueries({
    @NamedQuery(name = "TempVerProgramacion.findAll", query = "SELECT t FROM TempVerProgramacion t "),
@NamedQuery(name = "TempVerProgramacion.orderpuntaje", query = "SELECT t FROM TempVerProgramacion t order by t.funcionario.funPuntaje DESC"),
@NamedQuery(name = "TempVerProgramacion.ordergrado", query = "SELECT t FROM TempVerProgramacion t order by t.funcionario.funGrado")})
public class TempVerProgramacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
 
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_TVP")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEMP_VISTA_PROGRAMACION")
    @SequenceGenerator(name = "SEQ_TEMP_VISTA_PROGRAMACION", sequenceName = "SEQ_TEMP_VISTA_PROGRAMACION", allocationSize = 1)
    private Long idTvp;

           
    @Lob
    @Column(name = "PROGRAMACION")
    private String programacion;
    
    @Transient
    String[] prog;

    public String[] getProg() {
        return programacion.split(" / ");
    }
    
    
  
    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    
   public TempVerProgramacion() {
        
    }

    public TempVerProgramacion(Long idTvp) {
        this.idTvp = idTvp;
    }

    

    public String getProgramacion() {
        return programacion;
    }

    public void setProgramacion(String programacion) {
        this.programacion = programacion;
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
        hash += (idTvp != null ? idTvp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TempVerProgramacion)) {
            return false;
        }
        TempVerProgramacion other = (TempVerProgramacion) object;
        if ((this.idTvp == null && other.idTvp != null) || (this.idTvp != null && !this.idTvp.equals(other.idTvp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.TempVerProgramacion[ idTvp=" + idTvp + " ]";
    }
    
}
