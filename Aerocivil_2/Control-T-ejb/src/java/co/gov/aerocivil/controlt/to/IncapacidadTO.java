/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.to;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Incapacidad;
import java.util.Date;

/**
 *
 * @author Administrador
 */
public class IncapacidadTO {

    private int codEmpr;
    private Long rmtInca;
    private Integer rmtDias;
    private String nroCont;
    
    private Funcionario funcionario;
    private int codConc;
    private String tipInca;
    private Character indPrga;
    private Character indPago;
    private String numReso;
    private String numInpr;
    private Long numDias;
    private Long numDiat;
    private Date fecDesd;
    private Date fecHast;
    private Float basLiqu;
    private Float valInca;
    private Integer codEnti;
    private Integer codDiag;
    private Date fecInic;
    private Date fecRadi;
    private String numRadi;
    private Character desDi31;
    private Character liq100p;
    private String obsErva;
    private String actUsua;
    private Date actHora;
    private char actEsta;
    private String codDiar;
    private Character indIrec;
    private String numIrec;

    private Long count;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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
        IncapacidadTO other = (IncapacidadTO) object;
        if ((this.rmtInca == null && other.rmtInca != null) || (this.rmtInca != null && !this.rmtInca.equals(other.rmtInca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.NmIncap[ rmtInca=" + rmtInca + " ]";
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }    
}
