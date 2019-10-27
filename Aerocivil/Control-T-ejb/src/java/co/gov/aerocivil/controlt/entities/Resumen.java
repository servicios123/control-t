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
    @Column(name = "RES_MORD_LD")
    private Long mold;//Mañanas ordinarias L-D
    @Column(name = "RES_TORD_LD")
    private Long told;//TArdes ordinarias L-D
    @Column(name = "RES_TEXT_LS")
    private Long tels;//Tarde extra lunes a sabado
    @Column(name = "RES_TEXT_DF")
    private Long tedf;//tarde extra D-F
    @Column(name = "RES_NORD_LS")
    private Long nols;
    @Column(name = "RES_NORD_DF")
    private Long nodf;
    @Column(name = "RES_NEXT_LS")
    private Long nels;
    @Column(name = "RES_NEXT_DF")
    private Long nedf;
    @Column(name = "RES_AORD_LS")
    private Long aols;
    @Column(name = "RES_AORD_DF")
    private Long aodf;

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

    public Long getMold() {
        return mold;
    }

    public void setMold(Long mold) {
        this.mold = mold;
    }

    public Long getTold() {
        return told;
    }

    public void setTold(Long told) {
        this.told = told;
    }

    public Long getTels() {
        return tels;
    }

    public void setTels(Long tels) {
        this.tels = tels;
    }

    public Long getTedf() {
        return tedf;
    }

    public void setTedf(Long tedf) {
        this.tedf = tedf;
    }

    public Long getNols() {
        return nols;
    }

    public void setNols(Long nols) {
        this.nols = nols;
    }

    public Long getNodf() {
        return nodf;
    }

    public void setNodf(Long nodf) {
        this.nodf = nodf;
    }

    public Long getNels() {
        return nels;
    }

    public void setNels(Long nels) {
        this.nels = nels;
    }

    public Long getNedf() {
        return nedf;
    }

    public void setNedf(Long nedf) {
        this.nedf = nedf;
    }

    public Long getAols() {
        return aols;
    }

    public void setAols(Long aols) {
        this.aols = aols;
    }

    public Long getAodf() {
        return aodf;
    }

    public void setAodf(Long aodf) {
        this.aodf = aodf;
    }
}
