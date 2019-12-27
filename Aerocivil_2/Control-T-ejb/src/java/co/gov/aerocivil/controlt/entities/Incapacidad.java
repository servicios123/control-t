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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
/*@Entity
@Table(name = "NM_INCAP")
@NamedQueries({
    @NamedQuery(name = "NmIncap.findAll", query = "SELECT n FROM NmIncap n")})*/
public class Incapacidad implements Serializable {
    /*private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "COD_EMPR")
    private int codEmpr;
    @Id
    @Basic(optional = false)
    @Column(name = "RMT_INCA")
    private Long rmtInca;
    @Column(name = "RMT_DIAS")
    private Integer rmtDias;
    @Basic(optional = false)
    @Column(name = "NRO_CONT")
    private String nroCont;
    @Basic(optional = false)
    @Column(name = "COD_EMPL")
    private long codEmpl;
    @Basic(optional = false)
    @Column(name = "COD_CONC")
    private int codConc;
    @Column(name = "TIP_INCA")
    private String tipInca;
    @Column(name = "IND_PRGA")
    private Character indPrga;
    @Column(name = "IND_PAGO")
    private Character indPago;
    @Column(name = "NUM_RESO")
    private String numReso;
    @Column(name = "NUM_INPR")
    private String numInpr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NUM_DIAS")
    private Long numDias;
    @Column(name = "NUM_DIAT")
    private Long numDiat;
    @Column(name = "FEC_DESD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDesd;
    @Column(name = "FEC_HAST")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecHast;
    @Column(name = "BAS_LIQU")
    private Float basLiqu;
    @Column(name = "VAL_INCA")
    private Float valInca;
    @Column(name = "COD_ENTI")
    private Integer codEnti;
    @Column(name = "COD_DIAG")
    private Integer codDiag;
    @Column(name = "FEC_INIC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInic;
    @Basic(optional = false)
    @Column(name = "FEC_RADI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRadi;
    @Column(name = "NUM_RADI")
    private String numRadi;
    @Column(name = "DES_DI31")
    private Character desDi31;
    @Column(name = "LIQ_100P")
    private Character liq100p;
    @Column(name = "OBS_ERVA")
    private String obsErva;
    @Basic(optional = false)
    @Column(name = "ACT_USUA")
    private String actUsua;
    @Basic(optional = false)
    @Column(name = "ACT_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actHora;
    @Basic(optional = false)
    @Column(name = "ACT_ESTA")
    private char actEsta;
    @Column(name = "COD_DIAR")
    private String codDiar;
    @Column(name = "IND_IREC")
    private Character indIrec;
    @Column(name = "NUM_IREC")
    private String numIrec;

    public Incapacidad() {
    }

    public int getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(int codEmpr) {
        this.codEmpr = codEmpr;
    }

    public Long getRmtInca() {
        return rmtInca;
    }

    public void setRmtInca(Long rmtInca) {
        this.rmtInca = rmtInca;
    }

    public Integer getRmtDias() {
        return rmtDias;
    }

    public void setRmtDias(Integer rmtDias) {
        this.rmtDias = rmtDias;
    }

    public String getNroCont() {
        return nroCont;
    }

    public void setNroCont(String nroCont) {
        this.nroCont = nroCont;
    }

    public long getCodEmpl() {
        return codEmpl;
    }

    public void setCodEmpl(long codEmpl) {
        this.codEmpl = codEmpl;
    }

    public int getCodConc() {
        return codConc;
    }

    public void setCodConc(int codConc) {
        this.codConc = codConc;
    }

    public String getTipInca() {
        return tipInca;
    }

    public void setTipInca(String tipInca) {
        this.tipInca = tipInca;
    }

    public Character getIndPrga() {
        return indPrga;
    }

    public void setIndPrga(Character indPrga) {
        this.indPrga = indPrga;
    }

    public Character getIndPago() {
        return indPago;
    }

    public void setIndPago(Character indPago) {
        this.indPago = indPago;
    }

    public String getNumReso() {
        return numReso;
    }

    public void setNumReso(String numReso) {
        this.numReso = numReso;
    }

    public String getNumInpr() {
        return numInpr;
    }

    public void setNumInpr(String numInpr) {
        this.numInpr = numInpr;
    }

    public Long getNumDias() {
        return numDias;
    }

    public void setNumDias(Long numDias) {
        this.numDias = numDias;
    }

    public Long getNumDiat() {
        return numDiat;
    }

    public void setNumDiat(Long numDiat) {
        this.numDiat = numDiat;
    }

    public Date getFecDesd() {
        return fecDesd;
    }

    public void setFecDesd(Date fecDesd) {
        this.fecDesd = fecDesd;
    }

    public Date getFecHast() {
        return fecHast;
    }

    public void setFecHast(Date fecHast) {
        this.fecHast = fecHast;
    }

    public Float getBasLiqu() {
        return basLiqu;
    }

    public void setBasLiqu(Float basLiqu) {
        this.basLiqu = basLiqu;
    }

    public Float getValInca() {
        return valInca;
    }

    public void setValInca(Float valInca) {
        this.valInca = valInca;
    }

    public Integer getCodEnti() {
        return codEnti;
    }

    public void setCodEnti(Integer codEnti) {
        this.codEnti = codEnti;
    }

    public Integer getCodDiag() {
        return codDiag;
    }

    public void setCodDiag(Integer codDiag) {
        this.codDiag = codDiag;
    }

    public Date getFecInic() {
        return fecInic;
    }

    public void setFecInic(Date fecInic) {
        this.fecInic = fecInic;
    }

    public Date getFecRadi() {
        return fecRadi;
    }

    public void setFecRadi(Date fecRadi) {
        this.fecRadi = fecRadi;
    }

    public String getNumRadi() {
        return numRadi;
    }

    public void setNumRadi(String numRadi) {
        this.numRadi = numRadi;
    }

    public Character getDesDi31() {
        return desDi31;
    }

    public void setDesDi31(Character desDi31) {
        this.desDi31 = desDi31;
    }

    public Character getLiq100p() {
        return liq100p;
    }

    public void setLiq100p(Character liq100p) {
        this.liq100p = liq100p;
    }

    public String getObsErva() {
        return obsErva;
    }

    public void setObsErva(String obsErva) {
        this.obsErva = obsErva;
    }

    public String getActUsua() {
        return actUsua;
    }

    public void setActUsua(String actUsua) {
        this.actUsua = actUsua;
    }

    public Date getActHora() {
        return actHora;
    }

    public void setActHora(Date actHora) {
        this.actHora = actHora;
    }

    public char getActEsta() {
        return actEsta;
    }

    public void setActEsta(char actEsta) {
        this.actEsta = actEsta;
    }

    public String getCodDiar() {
        return codDiar;
    }

    public void setCodDiar(String codDiar) {
        this.codDiar = codDiar;
    }

    public Character getIndIrec() {
        return indIrec;
    }

    public void setIndIrec(Character indIrec) {
        this.indIrec = indIrec;
    }

    public String getNumIrec() {
        return numIrec;
    }

    public void setNumIrec(String numIrec) {
        this.numIrec = numIrec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rmtInca != null ? rmtInca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incapacidad)) {
            return false;
        }
        Incapacidad other = (Incapacidad) object;
        if ((this.rmtInca == null && other.rmtInca != null) || (this.rmtInca != null && !this.rmtInca.equals(other.rmtInca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.NmIncap[ rmtInca=" + rmtInca + " ]";
    }
*/    
}
