/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.entities;

import co.gov.aerocivil.controlt.enums.RolEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Viviana Farfan
 */
@Entity
@Table(name = "FUNCIONARIO")
@NamedQueries({
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f"),
    @NamedQuery(name = "Funcionario.findById", query = "SELECT f FROM Funcionario f WHERE f.funId = :funId"),
    @NamedQuery(name = "Funcionario.findByFunNombre", query = "SELECT f FROM Funcionario f WHERE f.funNombre = :funNombre"),
    @NamedQuery(name = "Funcionario.findByFuNivel", query = "SELECT f FROM Funcionario f WHERE f.fuNivel = :fuNivel"),
    @NamedQuery(name = "Funcionario.findByFunGrado", query = "SELECT f FROM Funcionario f WHERE f.funGrado = :funGrado"),
    @NamedQuery(name = "Funcionario.findByFunNumLicencia", query = "SELECT f FROM Funcionario f WHERE f.funNumLicencia = :funNumLicencia"),
    @NamedQuery(name = "Funcionario.findByFunFvCertmedico", query = "SELECT f FROM Funcionario f WHERE f.funFvCertmedico = :funFvCertmedico"),
    @NamedQuery(name = "Funcionario.findByFunDireccion", query = "SELECT f FROM Funcionario f WHERE f.funDireccion = :funDireccion"),
    @NamedQuery(name = "Funcionario.findByFunTelefono", query = "SELECT f FROM Funcionario f WHERE f.funTelefono = :funTelefono"),
    @NamedQuery(name = "Funcionario.findByFunCelular", query = "SELECT f FROM Funcionario f WHERE f.funCelular = :funCelular"),
    @NamedQuery(name = "Funcionario.findByFunFIngreso", query = "SELECT f FROM Funcionario f WHERE f.funFIngreso = :funFIngreso"),
    @NamedQuery(name = "Funcionario.findByFunFuTraslado", query = "SELECT f FROM Funcionario f WHERE f.funFuTraslado = :funFuTraslado"),
    @NamedQuery(name = "Funcionario.findByFunFvCurso", query = "SELECT f FROM Funcionario f WHERE f.funFvCurso = :funFvCurso"),
    @NamedQuery(name = "Funcionario.findByFunFvEvaluacion", query = "SELECT f FROM Funcionario f WHERE f.funFvEvaluacion = :funFvEvaluacion"),
    @NamedQuery(name = "Funcionario.findByFunSueldo", query = "SELECT f FROM Funcionario f WHERE f.funSueldo = :funSueldo"),
    @NamedQuery(name = "Funcionario.findByFunUsuario", query = "SELECT f FROM Funcionario f WHERE f.funUsuario = :funUsuario"),
    @NamedQuery(name = "Funcionario.findByFunAliasDep", query = "SELECT f FROM Funcionario f WHERE f.funAlias = :funAlias and f.dependencia.depId= :dep"),
    @NamedQuery(name = "Funcionario.findByFunAliasPosHabilitadas", query = "SELECT f FROM Funcionario f "
    + " JOIN f.listaPosicionesHabilitadas o where o.funcionario.funId = f.funId and "
    + " f.funAlias = :funAlias and f.dependencia.depId= :dep "),
    @NamedQuery(name = "Funcionario.findByFunClave", query = "SELECT f FROM Funcionario f WHERE f.funClave = :funClave"),
    @NamedQuery(name = "Funcionario.findByFunAlias", query = "SELECT f FROM Funcionario f WHERE f.funAlias = :funAlias"),
    @NamedQuery(name = "Funcionario.findByFunEstado", query = "SELECT f FROM Funcionario f WHERE f.funEstado = :funEstado"),
    @NamedQuery(name = "Funcionario.findByFunHorasExtras", query = "SELECT f FROM Funcionario f WHERE f.funHorasExtras = :funHorasExtras"),
    @NamedQuery(name = "Funcionario.findByDep", query = "SELECT f FROM Funcionario f WHERE f.dependencia.depId = :depId and f.funEstado = :estado"),
    @NamedQuery(name = "Funcionario.findByFunPuntaje", query = "SELECT f FROM Funcionario f WHERE f.funPuntaje = :funPuntaje")})
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "FUN_ID")
    private Long funId;
    @Basic(optional = false)
    @Column(name = "FUN_NOMBRE")
    private String funNombre;
    @Basic(optional = false)
    @Column(name = "FU_NIVEL")
    private Long fuNivel;
    @Basic(optional = false)
    @Column(name = "FUN_GRADO")
    private String funGrado;
    @Basic(optional = false)
    @Column(name = "FUN_NUM_LICENCIA")
    private String funNumLicencia;
    @Column(name = "FUN_FV_CERTMEDICO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFvCertmedico;
    @Column(name = "FUN_DIRECCION")
    private String funDireccion;
    @Column(name = "FUN_TELEFONO")
    private String funTelefono;
    @Column(name = "FUN_CELULAR")
    private String funCelular;
    @Basic(optional = false)
    @Column(name = "FUN_F_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFIngreso;
    @Column(name = "FUN_FU_TRASLADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFuTraslado;
    @Column(name = "FUN_FV_CURSO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFvCurso;
    @Column(name = "FUN_FV_EVALUACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date funFvEvaluacion;
    @Basic(optional = false)
    @Column(name = "FUN_SUELDO")
    private Long funSueldo;
    @Basic(optional = false)
    @Column(name = "FUN_USUARIO")
    private String funUsuario;
    @Basic(optional = false)
    @Column(name = "FUN_CLAVE")
    private String funClave;
    @Basic(optional = false)
    @Column(name = "FUN_ALIAS")
    private String funAlias;
    @Basic(optional = false)
    @Column(name = "FUN_ESTADO")
    private String funEstado;
    @Column(name = "FUN_HORAS_EXTRAS")
    private Boolean funHorasExtras;
    @Column(name = "FUN_PUNTAJE")
    private Double funPuntaje;
    @Column(name = "FUN_CANT_MAX_HE")
    private Integer funCantMaxHE;
    @Column(name = "FUN_CAT_DOM")
    private Integer funCatDom;
    @Column(name = "FUN_DESCANSO_SEMANA")
    private Boolean funDescansoSemana;
    @Column(name = "FUN_CERTMEDICO")
    private String funCertMedico;
    @Column(name = "FUN_CORREO_ELECTRONICO")
    private String funCorreoElectronico;
    @Transient
    private RolEnum rol;
    @Transient
    private Date fechaini;
    @Transient
    private Date fechafin;
    @Transient
    private int evalFecha;
    @Transient
    private String sectorTransporte;
    @Column(name = "FUN_INTENTOS_FALLIDOS")
    private Long intentosFallidos;
    @Column(name = "FUN_CARGO")
    private String funCargo;
    @Transient
    private Long[] roles;
    @Transient
    private String color;
    @Transient
    private EvaluacionCompetencia evaluacionCompetencia;
    @Transient
    private EvaluacionCompetencia evaluacionCompetenciaEdicion;

    public Long getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Long intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Funcionario(Long funId, String funAlias, String funNombre, String funDireccion, String sectorTransporte) {
        this.funId = funId;
        this.funNombre = funNombre;
        this.funDireccion = funDireccion;
        this.funAlias = funAlias;
        this.sectorTransporte = sectorTransporte;
    }
    @Column(name = "FUN_HORA_EXTRA_EXC")
    private Integer funHEExcedente;

    public RolEnum getRol() {
        return RolEnum.getRol(fuNivel);
    }

    public String getRolName() {
        return RolEnum.getRol(fuNivel).getRolNombre();
    }
    @JoinColumn(name = "FUN_DEPENDENCIA", referencedColumnName = "DEP_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Dependencia dependencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    List<PosicionHabilitada> listaPosicionesHabilitadas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    List<EvaluacionCompetencia> listaEvaluaciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.EAGER)
    List<JornadaNoLaborable> listaJornadasNoLaborables;
    @Transient
    private boolean seleccionado;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "curso_recurrente_funcionario",
    joinColumns =
    @JoinColumn(name = "crc_funcionario"),
    inverseJoinColumns =
    @JoinColumn(name = "crc_curso_recurrente"))
    private List<CursoRecurrente> listaCursos;

    public Funcionario() {
    }

    public String getFunNombre() {
        return funNombre;
    }

    public void setFunNombre(String funNombre) {
        this.funNombre = funNombre;
    }

    public Long getFuNivel() {
        return fuNivel;
    }

    public void setFuNivel(Long fuNivel) {
        this.fuNivel = fuNivel;
    }

    public String getFunGrado() {
        return funGrado;
    }

    public void setFunGrado(String funGrado) {
        this.funGrado = funGrado;
    }

    public String getFunNumLicencia() {
        return funNumLicencia;
    }

    public void setFunNumLicencia(String funNumLicencia) {
        this.funNumLicencia = funNumLicencia;
    }

    public Date getFunFvCertmedico() {
        return funFvCertmedico;
    }

    public void setFunFvCertmedico(Date funFvCertmedico) {
        this.funFvCertmedico = funFvCertmedico;
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

    public Date getFunFIngreso() {
        return funFIngreso;
    }

    public void setFunFIngreso(Date funFIngreso) {
        this.funFIngreso = funFIngreso;
    }

    public Date getFunFuTraslado() {
        return funFuTraslado;
    }

    public void setFunFuTraslado(Date funFuTraslado) {
        this.funFuTraslado = funFuTraslado;
    }

    public Date getFunFvCurso() {
        return funFvCurso;
    }

    public void setFunFvCurso(Date funFvCurso) {
        this.funFvCurso = funFvCurso;
    }

    public Date getFunFvEvaluacion() {
        return funFvEvaluacion;
    }

    public void setFunFvEvaluacion(Date funFvEvaluacion) {
        this.funFvEvaluacion = funFvEvaluacion;
    }

    public Long getFunSueldo() {
        return funSueldo;
    }

    public void setFunSueldo(Long funSueldo) {
        this.funSueldo = funSueldo;
    }

    public String getFunUsuario() {
        return funUsuario;
    }

    public void setFunUsuario(String funUsuario) {
        this.funUsuario = funUsuario;
    }

    public String getFunClave() {
        return funClave;
    }

    public void setFunClave(String funClave) {
        this.funClave = funClave;
    }

    public String getFunAlias() {
        return funAlias;
    }

    public void setFunAlias(String funAlias) {
        this.funAlias = funAlias;
    }

    public String getFunEstado() {
        return funEstado;
    }

    public void setFunEstado(String funEstado) {
        this.funEstado = funEstado;
    }

    public Boolean getFunHorasExtras() {
        return funHorasExtras;
    }

    public void setFunHorasExtras(Boolean funHorasExtras) {
        this.funHorasExtras = funHorasExtras;
    }

    public Double getFunPuntaje() {
        return funPuntaje;
    }

    public void setFunPuntaje(Double funPuntaje) {
        this.funPuntaje = funPuntaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funId != null ? funId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.funId == null && other.funId != null) || (this.funId != null && !this.funId.equals(other.funId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id: " + funId
                + " alias: " + funAlias
                + " nombre: " + funNombre
                + " rol: " + fuNivel
                + " grado: " + funGrado
                + " cargo: " + funCargo
                + " email: " + funCorreoElectronico
                + " num Licencia: " + funNumLicencia
                + " cert. medico: " + funCertMedico
                + " f. venc. cert. medico: " + funFvCertmedico
                + " direccion: " + funDireccion
                + " telefono: " + funTelefono
                + " celular: " + funCelular
                + " f. ingreso: " + funFIngreso
                + " f. ult. traslado: " + funFuTraslado
                + " f. venc. curso: " + funFvCurso
                + " f. venc. evaluacion: " + funFvEvaluacion
                + " salario: " + funSueldo
                + " estado: " + funEstado
                + " dependencia: " + dependencia.getDepAbreviatura()
                + " horas extras: " + funHorasExtras
                + " puntaje: " + funPuntaje
                + " máx. horas extras: " + funCantMaxHE
                + " máx dominicales laborales: " + funCatDom
                + " horas extras: " + funHorasExtras
                + " descanso semanal: " + funDescansoSemana
                + " horas ext. compensa: " + funHEExcedente
                + " intentos ing. fallidos: " + intentosFallidos
                + "";
    }

    public Dependencia getDependencia() {
        return dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

    public String getFunCertMedico() {
        return funCertMedico;
    }

    public void setFunCertMedico(String funCertMedico) {
        this.funCertMedico = funCertMedico;
    }

    public boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public List<PosicionHabilitada> getListaPosicionesHabilitadas() {
        return listaPosicionesHabilitadas;
    }

    public void setListaPosicionesHabilitadas(List<PosicionHabilitada> listaPosicionesHabilitadas) {
        this.listaPosicionesHabilitadas = listaPosicionesHabilitadas;
    }

    public Date getFechaini() {
        return fechaini;
    }

    public void setFechaini(Date fechaini) {
        this.fechaini = fechaini;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public int getEvalFecha() {
        return evalFecha;
    }

    public void setEvalFecha(int evalFecha) {
        this.evalFecha = evalFecha;
    }

    public Boolean getFunDescansoSemana() {
        return funDescansoSemana;
    }

    public void setFunDescansoSemana(Boolean funDescansoSemana) {
        this.funDescansoSemana = funDescansoSemana;
    }

    public Integer getFunCantMaxHE() {
        return funCantMaxHE;
    }

    public void setFunCantMaxHE(Integer funCantMaxHE) {
        this.funCantMaxHE = funCantMaxHE;
    }

    public Integer getFunCatDom() {
        return funCatDom;
    }

    public void setFunCatDom(Integer funCatDom) {
        this.funCatDom = funCatDom;
    }

    public Integer getFunHEExcedente() {
        return funHEExcedente;
    }

    public void setFunHEExcedente(Integer funHEExc) {
        this.funHEExcedente = funHEExc;
    }

    public List<CursoRecurrente> getListaCursos() {
        return listaCursos;
    }

    public List<EvaluacionCompetencia> getListaEvaluaciones() {
        return listaEvaluaciones;
    }

    public void setListaEvaluaciones(List<EvaluacionCompetencia> listaEvaluaciones) {
        this.listaEvaluaciones = listaEvaluaciones;
    }

    public String getSectorTransporte() {
        return sectorTransporte;
    }

    public void setSectorTransporte(String sectorTransporte) {
        this.sectorTransporte = sectorTransporte;
    }

    public String getFunCargo() {
        return funCargo;
    }

    public void setFunCargo(String funCargo) {
        this.funCargo = funCargo;
    }

    public void setRoles(Long[] roles) {
        this.roles = roles;
    }

    public Long[] getRoles() {
        return roles;
    }

    public String getFunCorreoElectronico() {
        return funCorreoElectronico;
    }

    public void setFunCorreoElectronico(String funCorreoElectronico) {
        this.funCorreoElectronico = funCorreoElectronico;
    }

    public List<JornadaNoLaborable> getListaJornadasNoLaborables() {
        return listaJornadasNoLaborables;
    }

    public void setListaJornadasNoLaborables(List<JornadaNoLaborable> listaJornadasNoLaborables) {
        this.listaJornadasNoLaborables = listaJornadasNoLaborables;
    }

    public String getColor() {
        Date today = new Date();
        long diff = this.funFvCertmedico.getTime() - today.getTime();
        float days = (diff / (1000 * 60 * 60 * 24));
        if (this.funFvCertmedico.before(today) || this.funFvCertmedico.equals(today)) {
            color = "vencido";
        } else {
            if (days >= 0 && days < 30) {
                color = "por-vencer";
            } else {
                color = "";
            }
        }
        return color;
    }

    public String getColorEvaluacion() {
        if (this.funFvEvaluacion != null) {
            Date today = new Date();
            long diff = this.funFvEvaluacion.getTime() - today.getTime();
            float days = (diff / (1000 * 60 * 60 * 24));
            if (this.funFvEvaluacion.before(today) || this.funFvEvaluacion.equals(today)) {
                return "vencido";
            } else {
                if (days >= 0 && days < 30) {
                    return "por-vencer";
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EvaluacionCompetencia getEvaluacionCompetencia() {
        return evaluacionCompetencia;
    }

    public void setEvaluacionCompetencia(EvaluacionCompetencia evaluacionCompetencia) {
        this.evaluacionCompetencia = evaluacionCompetencia;
    }

    public EvaluacionCompetencia getEvaluacionCompetenciaEdicion() {
        if (evaluacionCompetenciaEdicion == null) {
            evaluacionCompetenciaEdicion = new EvaluacionCompetencia();
        }
        return evaluacionCompetenciaEdicion;
    }

    public void setEvaluacionCompetenciaEdicion(EvaluacionCompetencia evaluacionCompetenciaEdicion) {
        this.evaluacionCompetenciaEdicion = evaluacionCompetenciaEdicion;
    }
}
