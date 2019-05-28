/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Viviana
 */
@Entity
@Table(name = "REP_HORAS_EXTRAS")
@NamedQueries({
    @NamedQuery(name = "RepHorasExtras.findAll", query = "SELECT r FROM RepHorasExtras r")})
public class RepHorasExtras implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EX_ID")
    private BigDecimal exId;
    
    @JoinColumn(name = "FUNCIONARIO", referencedColumnName = "FUN_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;
        
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Transient
    private Date fechaFinReporte;
    
    @JoinColumn(name = "DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;
    
    @Column(name = "COL2")
    private BigInteger col2;
    @Column(name = "COL3")
    private BigInteger col3;
    @Column(name = "COL4")
    private BigInteger col4;
    @Column(name = "COL5")
    private BigInteger col5;
    @Column(name = "COL6")
    private BigInteger col6;
    @Column(name = "COL7")
    private BigInteger col7;
    @Column(name = "COL8")
    private BigInteger col8;
    @Column(name = "COL9")
    private BigInteger col9;
    @Column(name = "COL10")
    private BigInteger col10;
    @Column(name = "COL11")
    private BigInteger col11;
    @Column(name = "COL12")
    private BigInteger col12;
    @Column(name = "COL13")
    private BigInteger col13;
    @Column(name = "COL14")
    private String col14;
    @Column(name = "COL15")
    private String col15;
    @Column(name = "COL16")
    private String col16;

    public RepHorasExtras() {
    }

    public RepHorasExtras(BigDecimal exId) {
        this.exId = exId;
    }

    public RepHorasExtras(BigDecimal exId, Date fecha) {
        this.exId = exId;
        this.fecha = fecha;
    }

    public BigDecimal getExId() {
        return exId;
    }

    public void setExId(BigDecimal exId) {
        this.exId = exId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getCol2() {
        return col2;
    }

    public void setCol2(BigInteger col2) {
        this.col2 = col2;
    }

    public BigInteger getCol3() {
        return col3;
    }

    public void setCol3(BigInteger col3) {
        this.col3 = col3;
    }

    public BigInteger getCol4() {
        return col4;
    }

    public void setCol4(BigInteger col4) {
        this.col4 = col4;
    }

    public BigInteger getCol5() {
        return col5;
    }

    public void setCol5(BigInteger col5) {
        this.col5 = col5;
    }

    public BigInteger getCol6() {
        return col6;
    }

    public void setCol6(BigInteger col6) {
        this.col6 = col6;
    }

    public BigInteger getCol7() {
        return col7;
    }

    public void setCol7(BigInteger col7) {
        this.col7 = col7;
    }

    public BigInteger getCol8() {
        return col8;
    }

    public void setCol8(BigInteger col8) {
        this.col8 = col8;
    }

    public BigInteger getCol9() {
        return col9;
    }

    public void setCol9(BigInteger col9) {
        this.col9 = col9;
    }

    public BigInteger getCol10() {
        return col10;
    }

    public void setCol10(BigInteger col10) {
        this.col10 = col10;
    }

    public BigInteger getCol11() {
        return col11;
    }

    public void setCol11(BigInteger col11) {
        this.col11 = col11;
    }

    public BigInteger getCol12() {
        return col12;
    }

    public void setCol12(BigInteger col12) {
        this.col12 = col12;
    }

    public BigInteger getCol13() {
        return col13;
    }

    public void setCol13(BigInteger col13) {
        this.col13 = col13;
    }

    public String getCol14() {
        return (col14==null)?"":col14;
    }

    public void setCol14(String col14) {
        this.col14 = col14;
    }

    public String getCol15() {
        return (col15==null)?"":col15;
    }

    public void setCol15(String col15) {
        this.col15 = col15;
    }

    public String getCol16() {
        return (col16==null)?"":col16;
    }

    public void setCol16(String col16) {
        this.col16 = col16;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exId != null ? exId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RepHorasExtras)) {
            return false;
        }
        RepHorasExtras other = (RepHorasExtras) object;
        if ((this.exId == null && other.exId != null) || (this.exId != null && !this.exId.equals(other.exId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.RepHorasExtras[ exId=" + exId + " ]";
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getFechaFinReporte() {
        return fechaFinReporte;
    }

    public void setFechaFinReporte(Date fechaFinReporte) {
        this.fechaFinReporte = fechaFinReporte;
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    

}
