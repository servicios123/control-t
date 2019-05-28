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
@Entity
@Table(name = "FUNCIONARIO_SITAH_VIEW")
@NamedQueries({
    @NamedQuery(name = "FuncionarioSitah.findAll", query = "SELECT f FROM FuncionarioSitah f"),
    @NamedQuery(name = "FuncionarioSitah.byFunId", query = "SELECT f FROM FuncionarioSitah f where f.funId =:funId")})

public class FuncionarioSitah implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "FUN_ID")
    private Long funId;
    @Column(name = "FUN_NOMBRE")
    private String funNombre;
    @Column(name = "FUN_DIRECCION")
    private String funDireccion;
    @Column(name = "FUN_TELEFONO")
    private String funTelefono;
    @Column(name = "FUN_CELULAR")
    private String funCelular;
    
    @Column(name = "FUN_FU_TRASLADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFuTraslado;
    
    @Column(name = "FUN_F_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFIngreso;
    
    @Column(name = "FUN_ALIAS")
    private String funAlias;
    @Column(name = "FUN_USUARIO")
    private String funUsuario;

    public FuncionarioSitah() {
    }

    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

    public String getFunNombre() {
        return funNombre;
    }

    public void setFunNombre(String funNombre) {
        this.funNombre = funNombre;
    }

    public String getFunDireccion() {
        return funDireccion;
    }

    public void setFunDireccion(String funDireccion) {
        this.funDireccion = funDireccion;
    }

    public String getFunTelefono() {
        return funTelefono;
    }

    public void setFunTelefono(String funTelefono) {
        this.funTelefono = funTelefono;
    }

    public String getFunCelular() {
        return funCelular;
    }

    public void setFunCelular(String funCelular) {
        this.funCelular = funCelular;
    }

    public Date getFunFuTraslado() {
        return funFuTraslado;
    }

    public void setFunFuTraslado(Date funFuTraslado) {
        this.funFuTraslado = funFuTraslado;
    }

    public Date getFunFIngreso() {
        return funFIngreso;
    }

    public void setFunFIngreso(Date funFIngreso) {
        this.funFIngreso = funFIngreso;
    }

    public String getFunAlias() {
        return funAlias;
    }

    public void setFunAlias(String funAlias) {
        this.funAlias = funAlias;
    }

    public String getFunUsuario() {
        return funUsuario;
    }

    public void setFunUsuario(String funUsuario) {
        this.funUsuario = funUsuario;
    }
    
}
