/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author JUANCAMILOSOSASUAREZ
 */
@Entity
@Table(name = "RESUMEN_PROGRAMACION")
@NamedQueries({
    @NamedQuery(name = "findByProg", query = "SELECT n FROM Resumen n")})
public class Resumen implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "RES_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_RESUMEN")
    @SequenceGenerator(name = "SQ_RESUMEN", sequenceName = "SQ_RESUMEN", allocationSize = 1)
    private Long resId;
    @Column(name = "RES_PROGRAMACION")
    private Long programacion;
    @Column(name = "RES_FUN_ALIAS")
    private String funAlias;
    @Column(name = "RES_DIURNASLS_ORD")
    private Long diurnasLsOrd;
    @Column(name = "RES_DIURNASLS_EXT")
    private Long diurnasLsExt;
    @Column(name = "RES_DIURNASDF_ORD")
    private Long diurnasDfOrd;
    @Column(name = "RES_DIURNASDF_EXT")
    private Long diurnasDfExt;
    @Column(name = "RES_NOCTURNASLS_ORD")
    private Long nocturnasLsOrd;
    @Column(name = "RES_NOCTURNASLS_EXT")
    private Long nocturnasLsExt;
    @Column(name = "RES_NOCTURNASDF_ORD")
    private Long nocturnasDfOrd;
    @Column(name = "RES_NOCTURNASDF_EXT")
    private Long nocturnasDfExt;
    @Column(name = "RES_AMANECELS_ORD")
    private Long amaneceLsOrd;
    @Column(name = "RES_AMANECEDF_ORD")
    private Long amaneceDfOrd;
    @Column(name = "RES_DIU_ESP_LS_ORD")
    private Long diurnasEspLsOrd;
    @Column(name = "RES_DIU_ESP_LS_EXT")
    private Long diurnasEspLsExt;
    @Column(name = "RES_DIU_ESP_DF_ORD")
    private Long diurnasEspDfOrd;
    @Column(name = "RES_DIU_ESP_DF_EXT")
    private Long diurnasEspDfExt;
    @Column(name = "RES_NOC_ESP_LS_ORD")
    private Long nocturnasEspLsOrd;
    @Column(name = "RES_NOC_ESP_LS_EXT")
    private Long nocturnasEspLsExt;
    @Column(name = "RES_NOC_ESP_DF_ORD")
    private Long nocturnasEspDfOrd;
    @Column(name = "RES_NOC_ESP_DF_EXT")
    private Long nocturnasEspDfExt;
    @Column(name = "RES_AMA_ESP_LS_ORD")
    private Long amaneceEspLsOrd;
    @Column(name = "RES_AMA_ESP_DF_ORD")
    private Long amaneceEspDfOrd;

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public Long getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Long programacion) {
        this.programacion = programacion;
    }

    public String getFunAlias() {
        return funAlias;
    }

    public void setFunAlias(String funAlias) {
        this.funAlias = funAlias;
    }

    public Long getDiurnasLsOrd() {
        return diurnasLsOrd;
    }

    public void setDiurnasLsOrd(Long diurnasLsOrd) {
        this.diurnasLsOrd = diurnasLsOrd;
    }

    public Long getDiurnasLsExt() {
        return diurnasLsExt;
    }

    public void setDiurnasLsExt(Long diurnasLsExt) {
        this.diurnasLsExt = diurnasLsExt;
    }

    public Long getDiurnasDfOrd() {
        return diurnasDfOrd;
    }

    public void setDiurnasDfOrd(Long diurnasDfOrd) {
        this.diurnasDfOrd = diurnasDfOrd;
    }

    public Long getDiurnasDfExt() {
        return diurnasDfExt;
    }

    public void setDiurnasDfExt(Long diurnasDfExt) {
        this.diurnasDfExt = diurnasDfExt;
    }

    public Long getNocturnasLsOrd() {
        return nocturnasLsOrd;
    }

    public void setNocturnasLsOrd(Long nocturnasLsOrd) {
        this.nocturnasLsOrd = nocturnasLsOrd;
    }

    public Long getNocturnasLsExt() {
        return nocturnasLsExt;
    }

    public void setNocturnasLsExt(Long nocturnasLsExt) {
        this.nocturnasLsExt = nocturnasLsExt;
    }

    public Long getNocturnasDfOrd() {
        return nocturnasDfOrd;
    }

    public void setNocturnasDfOrd(Long nocturnasDfOrd) {
        this.nocturnasDfOrd = nocturnasDfOrd;
    }

    public Long getNocturnasDfExt() {
        return nocturnasDfExt;
    }

    public void setNocturnasDfExt(Long nocturnasDfExt) {
        this.nocturnasDfExt = nocturnasDfExt;
    }

    public Long getAmaneceLsOrd() {
        return amaneceLsOrd;
    }

    public void setAmaneceLsOrd(Long amaneceLsOrd) {
        this.amaneceLsOrd = amaneceLsOrd;
    }

    public Long getAmaneceDfOrd() {
        return amaneceDfOrd;
    }

    public void setAmaneceDfOrd(Long amaneceDfOrd) {
        this.amaneceDfOrd = amaneceDfOrd;
    }

    public Long getDiurnasEspLsOrd() {
        return diurnasEspLsOrd;
    }

    public void setDiurnasEspLsOrd(Long diurnasEspLsOrd) {
        this.diurnasEspLsOrd = diurnasEspLsOrd;
    }

    public Long getDiurnasEspLsExt() {
        return diurnasEspLsExt;
    }

    public void setDiurnasEspLsExt(Long diurnasEspLsExt) {
        this.diurnasEspLsExt = diurnasEspLsExt;
    }

    public Long getDiurnasEspDfOrd() {
        return diurnasEspDfOrd;
    }

    public void setDiurnasEspDfOrd(Long diurnasEspDfOrd) {
        this.diurnasEspDfOrd = diurnasEspDfOrd;
    }

    public Long getDiurnasEspDfExt() {
        return diurnasEspDfExt;
    }

    public void setDiurnasEspDfExt(Long diurnasEspDfExt) {
        this.diurnasEspDfExt = diurnasEspDfExt;
    }

    public Long getNocturnasEspLsOrd() {
        return nocturnasEspLsOrd;
    }

    public void setNocturnasEspLsOrd(Long nocturnasEspLsOrd) {
        this.nocturnasEspLsOrd = nocturnasEspLsOrd;
    }

    public Long getNocturnasEspLsExt() {
        return nocturnasEspLsExt;
    }

    public void setNocturnasEspLsExt(Long nocturnasEspLsExt) {
        this.nocturnasEspLsExt = nocturnasEspLsExt;
    }

    public Long getNocturnasEspDfOrd() {
        return nocturnasEspDfOrd;
    }

    public void setNocturnasEspDfOrd(Long nocturnasEspDfOrd) {
        this.nocturnasEspDfOrd = nocturnasEspDfOrd;
    }

    public Long getNocturnasEspDfExt() {
        return nocturnasEspDfExt;
    }

    public void setNocturnasEspDfExt(Long nocturnasEspDfExt) {
        this.nocturnasEspDfExt = nocturnasEspDfExt;
    }

    public Long getAmaneceEspLsOrd() {
        return amaneceEspLsOrd;
    }

    public void setAmaneceEspLsOrd(Long amaneceEspLsOrd) {
        this.amaneceEspLsOrd = amaneceEspLsOrd;
    }

    public Long getAmaneceEspDfOrd() {
        return amaneceEspDfOrd;
    }

    public void setAmaneceEspDfOrd(Long amaneceEspDfOrd) {
        this.amaneceEspDfOrd = amaneceEspDfOrd;
    }
    
    
}
