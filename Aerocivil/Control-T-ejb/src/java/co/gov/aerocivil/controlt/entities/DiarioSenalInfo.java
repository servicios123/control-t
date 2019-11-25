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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DIARIO_SENAL_INFO")
public class DiarioSenalInfo implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Basic(optional = false)
    @Column(name = "DSI_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DIARIO_SENAL_INFO")
    @SequenceGenerator(name = "SEQ_DIARIO_SENAL_INFO", sequenceName = "SEQ_DIARIO_SENAL_INFO", allocationSize = 1)
    private Long dsiId;
    @Column(name = "DSI_SERIE_NR")
    private String dsiSerieNr;
    @Column(name = "DSI_SOLICITUD")
    private String dsiSolicitud;
    @Column(name = "DSI_CODIGO")
    private String dsiCodigo;
    @Column(name = "DSI_OBSERVACION")
    private String dsiObservacion;
    @Column(name = "DSI_AUTORIZADO")
    private String dsiAutorizado;
    @Column(name = "DSI_ERRORES_INTERNOS")
    private String dsiErroresInternos;
    @Column(name = "DSI_ERRORES_EXTERNOS")
    private String dsiErroresExternos;
    @Column(name = "DSI_ACTIVIDAD")
    private String dsiActividad;
    @Column(name = "DSI_DOCUMENTO")
    private String dsiDocumento;
    @Column(name = "DSI_EQUIPO_SOFTWARE")
    private String dsiEquipoSoftware;
    @Column(name = "DSI_HORA_UTC")
    private String dsiHoraUtc;
    @Column(name = "DSI_FALLA")
    private String dsiFalla;
    @Column(name = "DSI_PRODUCTO")
    private String dsiProducto;
    @Column(name = "DSI_SERIE")
    private String dsiSerie;
    @Column(name = "DSI_SERIE_A")
    private String dsiSerieA;
    @Column(name = "DSI_SERIE_B")
    private String dsiSerieB;
    @Column(name = "DSI_SERIE_C")
    private String dsiSerieC;
    @Column(name = "DSI_SERIE_D")
    private String dsiSerieD;
    @Column(name = "DSI_SERIE_VA")
    private String dsiSerieVa;
    @Column(name = "DSI_CANTIDAD_PUBLICADOS")
    private String dsiCantidadPublicados;
    @Column(name = "DSI_CANT_ERR_INTERNOS")
    private String dsiCantErrInternos;
    @Column(name = "DSI_CANT_ERR_EXTERNO")
    private String dsiCantErrExterno;
    @Column(name = "DSI_HORA_REGISTRO")
    private String dsiHoraRegistro;
    @Column(name = "DSI_HORA_MODIFICADO")
    private String dsiHoraModificado;
    @Column(name = "DSI_POSICION_TRABAJO")
    private String dsiPosicionTrabajo;
    @JoinColumn(name = "DSI_TIPO", referencedColumnName = "DST_ID")
    @ManyToOne(optional = false)
    private DiarioSenalTipo dsiTipo;
    @JoinColumn(name = "DSI_LOCALIZACION", referencedColumnName = "DSL_ID")
    @ManyToOne
    private DiarioSenalLocalizacion dsiLocalizacion;
    @JoinColumn(name = "DSI_DIARIO_SENAL_FUNCIONARIO", referencedColumnName = "DSF_ID")
    @ManyToOne(optional = false)
    private DiarioSenalFuncionario dsiDiarioSenalFuncionario;
    @JoinColumn(name = "DSI_CATEGORIA", referencedColumnName = "DSC_ID")
    private DiarioSenalCategoria dsiCategoria;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getDsiId() != null ? getDsiId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dsiId fields are not set
        if (!(object instanceof DiarioSenalInfo)) {
            return false;
        }
        DiarioSenalInfo other = (DiarioSenalInfo) object;
        if ((this.getDsiId() == null && other.getDsiId() != null) || (this.getDsiId() != null && !this.dsiId.equals(other.dsiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.gov.aerocivil.controlt.entities.DiarioSenalInfo[ id=" + getDsiId() + " ]";
    }

    /**
     * @return the dsiId
     */
    public Long getDsiId() {
        return dsiId;
    }

    /**
     * @param dsiId the dsiId to set
     */
    public void setDsiId(Long dsiId) {
        this.dsiId = dsiId;
    }

    /**
     * @return the dsiSerieNr
     */
    public String getDsiSerieNr() {
        return dsiSerieNr;
    }

    /**
     * @param dsiSerieNr the dsiSerieNr to set
     */
    public void setDsiSerieNr(String dsiSerieNr) {
        this.dsiSerieNr = dsiSerieNr;
    }

    /**
     * @return the dsiSolicitud
     */
    public String getDsiSolicitud() {
        return dsiSolicitud;
    }

    /**
     * @param dsiSolicitud the dsiSolicitud to set
     */
    public void setDsiSolicitud(String dsiSolicitud) {
        this.dsiSolicitud = dsiSolicitud;
    }

    /**
     * @return the dsiCodigo
     */
    public String getDsiCodigo() {
        return dsiCodigo;
    }

    /**
     * @param dsiCodigo the dsiCodigo to set
     */
    public void setDsiCodigo(String dsiCodigo) {
        this.dsiCodigo = dsiCodigo;
    }

    /**
     * @return the dsiObservacion
     */
    public String getDsiObservacion() {
        return dsiObservacion;
    }

    /**
     * @param dsiObservacion the dsiObservacion to set
     */
    public void setDsiObservacion(String dsiObservacion) {
        this.dsiObservacion = dsiObservacion;
    }

    /**
     * @return the dsiAutorizado
     */
    public String getDsiAutorizado() {
        return dsiAutorizado;
    }

    /**
     * @param dsiAutorizado the dsiAutorizado to set
     */
    public void setDsiAutorizado(String dsiAutorizado) {
        this.dsiAutorizado = dsiAutorizado;
    }

    /**
     * @return the dsiErroresInternos
     */
    public String getDsiErroresInternos() {
        return dsiErroresInternos;
    }

    /**
     * @param dsiErroresInternos the dsiErroresInternos to set
     */
    public void setDsiErroresInternos(String dsiErroresInternos) {
        this.dsiErroresInternos = dsiErroresInternos;
    }

    /**
     * @return the dsiErroresExternos
     */
    public String getDsiErroresExternos() {
        return dsiErroresExternos;
    }

    /**
     * @param dsiErroresExternos the dsiErroresExternos to set
     */
    public void setDsiErroresExternos(String dsiErroresExternos) {
        this.dsiErroresExternos = dsiErroresExternos;
    }

    /**
     * @return the dsiActividad
     */
    public String getDsiActividad() {
        return dsiActividad;
    }

    /**
     * @param dsiActividad the dsiActividad to set
     */
    public void setDsiActividad(String dsiActividad) {
        this.dsiActividad = dsiActividad;
    }

    /**
     * @return the dsiDocumento
     */
    public String getDsiDocumento() {
        return dsiDocumento;
    }

    /**
     * @param dsiDocumento the dsiDocumento to set
     */
    public void setDsiDocumento(String dsiDocumento) {
        this.dsiDocumento = dsiDocumento;
    }

    /**
     * @return the dsiEquipoSoftware
     */
    public String getDsiEquipoSoftware() {
        return dsiEquipoSoftware;
    }

    /**
     * @param dsiEquipoSoftware the dsiEquipoSoftware to set
     */
    public void setDsiEquipoSoftware(String dsiEquipoSfotware) {
        this.dsiEquipoSoftware = dsiEquipoSfotware;
    }

    /**
     * @return the dsiHoraUtc
     */
    public String getDsiHoraUtc() {
        return dsiHoraUtc;
    }

    /**
     * @param dsiHoraUtc the dsiHoraUtc to set
     */
    public void setDsiHoraUtc(String dsiHoraUtc) {
        this.dsiHoraUtc = dsiHoraUtc;
    }

    /**
     * @return the dsiFalla
     */
    public String getDsiFalla() {
        return dsiFalla;
    }

    /**
     * @param dsiFalla the dsiFalla to set
     */
    public void setDsiFalla(String dsiFalla) {
        this.dsiFalla = dsiFalla;
    }

    /**
     * @return the dsiProducto
     */
    public String getDsiProducto() {
        return dsiProducto;
    }

    /**
     * @param dsiProducto the dsiProducto to set
     */
    public void setDsiProducto(String dsiProducto) {
        this.dsiProducto = dsiProducto;
    }

    /**
     * @return the dsiSerie
     */
    public String getDsiSerie() {
        return dsiSerie;
    }

    /**
     * @param dsiSerie the dsiSerie to set
     */
    public void setDsiSerie(String dsiSerie) {
        this.dsiSerie = dsiSerie;
    }

    /**
     * @return the dsiCantidadPublicados
     */
    public String getDsiCantidadPublicados() {
        return dsiCantidadPublicados;
    }

    /**
     * @param dsiCantidadPublicados the dsiCantidadPublicados to set
     */
    public void setDsiCantidadPublicados(String dsiCantidadPublicados) {
        this.dsiCantidadPublicados = dsiCantidadPublicados;
    }

    /**
     * @return the dsiCantErrInternos
     */
    public String getDsiCantErrInternos() {
        return dsiCantErrInternos;
    }

    /**
     * @param dsiCantErrInternos the dsiCantErrInternos to set
     */
    public void setDsiCantErrInternos(String dsiCantErrInternos) {
        this.dsiCantErrInternos = dsiCantErrInternos;
    }

    /**
     * @return the dsiCantErrExterno
     */
    public String getDsiCantErrExterno() {
        return dsiCantErrExterno;
    }

    /**
     * @param dsiCantErrExterno the dsiCantErrExterno to set
     */
    public void setDsiCantErrExterno(String dsiCantErrExterno) {
        this.dsiCantErrExterno = dsiCantErrExterno;
    }

    /**
     * @return the dsiPosicionTrabajo
     */
    public String getDsiPosicionTrabajo() {
        return dsiPosicionTrabajo;
    }

    /**
     * @param dsiPosicionTrabajo the dsiPosicionTrabajo to set
     */
    public void setDsiPosicionTrabajo(String dsiPosicionTrabajo) {
        this.dsiPosicionTrabajo = dsiPosicionTrabajo;
    }

    /**
     * @return the dsiTipo
     */
    public DiarioSenalTipo getDsiTipo() {
        return dsiTipo;
    }

    /**
     * @param dsiTipo the dsiTipo to set
     */
    public void setDsiTipo(DiarioSenalTipo dsiTipo) {
        this.dsiTipo = dsiTipo;
    }

    /**
     * @return the dsiLocalizacion
     */
    public DiarioSenalLocalizacion getDsiLocalizacion() {
        return dsiLocalizacion;
    }

    /**
     * @param dsiLocalizacion the dsiLocalizacion to set
     */
    public void setDsiLocalizacion(DiarioSenalLocalizacion dsiLocalizacion) {
        this.dsiLocalizacion = dsiLocalizacion;
    }

    /**
     * @return the dsiDiarioSenalFuncionario
     */
    public DiarioSenalFuncionario getDsiDiarioSenalFuncionario() {
        return dsiDiarioSenalFuncionario;
    }

    /**
     * @param dsiDiarioSenalFuncionario the dsiDiarioSenalFuncionario to set
     */
    public void setDsiDiarioSenalFuncionario(DiarioSenalFuncionario dsiDiarioSenalFuncionario) {
        this.dsiDiarioSenalFuncionario = dsiDiarioSenalFuncionario;
    }

    /**
     * @return the dsiHoraRegistro
     */
    public String getDsiHoraRegistro() {
        return dsiHoraRegistro;
    }

    /**
     * @param dsiHoraRegistro the dsiHoraRegistro to set
     */
    public void setDsiHoraRegistro(String dsiHoraRegistro) {
        this.dsiHoraRegistro = dsiHoraRegistro;
    }

    /**
     * @return the dsiHoraModificado
     */
    public String getDsiHoraModificado() {
        return dsiHoraModificado;
    }

    /**
     * @param dsiHoraModificado the dsiHoraModificado to set
     */
    public void setDsiHoraModificado(String dsiHoraModificado) {
        this.dsiHoraModificado = dsiHoraModificado;
    }

    /**
     * @return the dsiSerieA
     */
    public String getDsiSerieA() {
        return dsiSerieA;
    }

    /**
     * @param dsiSerieA the dsiSerieA to set
     */
    public void setDsiSerieA(String dsiSerieA) {
        this.dsiSerieA = dsiSerieA;
    }

    /**
     * @return the dsiSerieB
     */
    public String getDsiSerieB() {
        return dsiSerieB;
    }

    /**
     * @param dsiSerieB the dsiSerieB to set
     */
    public void setDsiSerieB(String dsiSerieB) {
        this.dsiSerieB = dsiSerieB;
    }

    /**
     * @return the dsiSerieC
     */
    public String getDsiSerieC() {
        return dsiSerieC;
    }

    /**
     * @param dsiSerieC the dsiSerieC to set
     */
    public void setDsiSerieC(String dsiSerieC) {
        this.dsiSerieC = dsiSerieC;
    }

    /**
     * @return the dsiSerieVa
     */
    public String getDsiSerieVa() {
        return dsiSerieVa;
    }

    /**
     * @param dsiSerieVa the dsiSerieVa to set
     */
    public void setDsiSerieVa(String dsiSerieVa) {
        this.dsiSerieVa = dsiSerieVa;
    }

    /**
     * @return the dsiCategoria
     */
    public DiarioSenalCategoria getDsiCategoria() {
        return dsiCategoria;
    }

    /**
     * @param dsiCategoria the dsiCategoria to set
     */
    public void setDsiCategoria(DiarioSenalCategoria dsiCategoria) {
        this.dsiCategoria = dsiCategoria;
    }

    public String getDsiSerieD() {
        return dsiSerieD;
    }

    public void setDsiSerieD(String dsiSerieD) {
        this.dsiSerieD = dsiSerieD;
    }
}
