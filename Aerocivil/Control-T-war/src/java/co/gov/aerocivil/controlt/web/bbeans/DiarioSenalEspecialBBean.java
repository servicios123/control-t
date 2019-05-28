/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DiarioSenalCategoria;
import co.gov.aerocivil.controlt.entities.DiarioSenalCierreTurno;
import co.gov.aerocivil.controlt.entities.DiarioSenalEspecial;
import co.gov.aerocivil.controlt.entities.DiarioSenalFuncionario;
import co.gov.aerocivil.controlt.entities.DiarioSenalInfo;
import co.gov.aerocivil.controlt.entities.DiarioSenalLocalizacion;
import co.gov.aerocivil.controlt.entities.DiarioSenalTipo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.services.DiarioSenalEspecialService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.lazylist.DiarioSenalEspecialLazyList;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class DiarioSenalEspecialBBean {

    @EJB
    private DiarioSenalEspecialService diarioSenalEspecialService;
    @EJB
    private ListasService listasService;
    /* -- / Registro / ---*/
    private DiarioSenalEspecial diarioSenalEspecial;
    private DiarioSenalFuncionario diarioSenalFuncionario;
    private DiarioSenalInfo diarioSenalInfo;
    private DiarioSenalInfo diarioSenalInfoSeleccionado;
    private String busqueda;
    private DiarioSenalEspecial dseAuxiliar;
    private DiarioSenalFuncionario dsfAuxiliar;
    private List<Posicion> posiciones;
    private List<DiarioSenalTipo> diarioSenalTipos;
    private List<DiarioSenalLocalizacion> diarioSenalLocalizaciones;
    private List<DiarioSenalCategoria> diarioSenalCategorias;
    private Jornada jornada;
    private Posicion posicion;
    private Boolean llega_tarde;
    private Boolean sale_temprano;
    private final int minutosDiferenciaTemprano = 10;
    private final int minutosDiferenciaSale = 10;
    private List<DiarioSenalFuncionario> funcionarios;
    private List<DiarioSenalInfo> dsiPorDti1;
    private List<DiarioSenalInfo> dsiPorDti2;
    private List<DiarioSenalInfo> dsiPorDti3;
    private List<DiarioSenalInfo> dsiPorDti4;
    private List<DiarioSenalInfo> dsiPorDti5;
    private List<DiarioSenalInfo> dsiPorDti6;
    private List<DiarioSenalInfo> dsiPorDti7;
    private List<Object[]> listaCierreTurno;
    private List<String> horasUTC;
    private List<String> minutosUTC;
    private String horaUTC;
    private String minutoUTC;
    private boolean disabled;
    /* -- / Consulta / ---*/
    private DiarioSenalEspecial diarioSenalEspecialFiltro;
    private LazyDataModel<DiarioSenalEspecial> lista;
    private List<Jornada> jornadas;
    private DiarioSenalEspecial verDiarioSenalEspecial;
    private List<DiarioSenalFuncionario> verFuncionarios;
    private List<DiarioSenalInfo> verDsiPorDti1;
    private List<DiarioSenalInfo> verDsiPorDti2;
    private List<DiarioSenalInfo> verDsiPorDti3;
    private List<DiarioSenalInfo> verDsiPorDti4;
    private List<DiarioSenalInfo> verDsiPorDti5;
    private List<DiarioSenalInfo> verDsiPorDti6;
    private List<DiarioSenalInfo> verDsiPorDti7;
    private List<Object[]> verListaCierreTurno;
    private DiarioSenalFuncionario verDsf;

    public DiarioSenalEspecialBBean() {
    }

    public String exportPDF() {

        ArrayList<PdfPTable> tables = new ArrayList<PdfPTable>();

        /* Info General */
        PdfPTable table = new PdfPTable(4);
        Boolean back = false;

        String proc = verDiarioSenalEspecial.getDseDependencia().getDepProcedencia() + "147";
        table.addCell(JsfUtil.celda("Principio de Procedencia", true));
        table.addCell(JsfUtil.celda(proc, true));
        table.addCell(JsfUtil.celda("Clave", true));
        table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseDependencia().getDepcategoria().getDcClaveDse(), true));

        table.addCell(JsfUtil.celda("Versión", false));
        table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseDependencia().getDepcategoria().getDcVersionDse(), false));
        table.addCell(JsfUtil.celda("Fecha", false));
        table.addCell(JsfUtil.celda(DateUtil.formatDate(verDiarioSenalEspecial.getDseDependencia().getDepcategoria().getDcFechaDse()), false));


        table.addCell(JsfUtil.celda("Fecha", true));
        table.addCell(JsfUtil.celda(DateUtil.formatDate(verDiarioSenalEspecial.getDseFecha()), true));
        table.addCell(JsfUtil.celda("Dependencia", true));
        table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseDependencia().getDepAbreviatura() + " - " + verDiarioSenalEspecial.getDseDependencia().getDepNombre(), true));

        //table.addCell(JsfUtil.celda("Estado", false));
        //table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseEstado(), false));
        table.addCell(JsfUtil.celda("Jornada", false));
        table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseJornadaOp().getJoAlias(), false));


        table.addCell(JsfUtil.celda("Hora Creado", true));
        table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseHoraCreado(), true));
        //table.addCell(JsfUtil.celda("Hora Cerrado", true));
        //table.addCell(JsfUtil.celda(verDiarioSenalEspecial.getDseHoraCerrado(), true));


        tables.add(table);



        /* -------------- */
        /* Funcionarios */
        table = null;

        PdfPTable func = new PdfPTable(3);
        func.addCell(JsfUtil.celdaTitulo("LISTADO DE FUNCIONARIOS", 3));
        func.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        func.addCell(JsfUtil.celdaSubTitulo("Posicion"));
        func.addCell(JsfUtil.celdaSubTitulo("H. Llegada"));
        /*func.addCell(JsfUtil.celdaSubTitulo("H. Receso"));
        func.addCell(JsfUtil.celdaSubTitulo("H. Regreso"));
        func.addCell(JsfUtil.celdaSubTitulo("H. Salida"));
        func.addCell(JsfUtil.celdaSubTitulo("Obs. Entrada"));
        func.addCell(JsfUtil.celdaSubTitulo("Obs. Salida"));
        func.addCell(JsfUtil.celdaSubTitulo("Estado"));*/
        back = true;
        if (verFuncionarios.size() > 0) {
            for (int i = 0; i < verFuncionarios.size(); i++) {
                back = !back;
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfFuncionario().getFunAlias(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfPosicion().getPosicionNacional().getPnAlias(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfHoraEntrada(), back));/*
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfHoraReceso(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfHoraRegreso(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfHoraSalida(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfObservacionEntrada(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfObservacionSalida(), back));
                func.addCell(JsfUtil.celda(verFuncionarios.get(i).getDsfEstado(), back));*/
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(3);
            func.addCell(celda);

        }

        tables.add(func);

        /* ------ */
        /* DSI 1*/
        func = null;

        PdfPTable ds1 = new PdfPTable(5);
        ds1.addCell(JsfUtil.celdaTitulo("NOTAM ESTIMADOS A REEMPLAZAR O CANCELAR", 5));
        ds1.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds1.addCell(JsfUtil.celdaSubTitulo("Localizacion"));
        ds1.addCell(JsfUtil.celdaSubTitulo("Serie y NR"));
        //ds1.addCell(JsfUtil.celdaSubTitulo("Solic / F - N"));
        ds1.addCell(JsfUtil.celdaSubTitulo("Codigo (Q)"));
        ds1.addCell(JsfUtil.celdaSubTitulo("Observacion"));

        back = true;
        if (verDsiPorDti1.size() > 0) {
            for (int i = 0; i < verDsiPorDti1.size(); i++) {
                back = !back;
                ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiLocalizacion().getDslAlias(), back));
                ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiSerie(), back));
                //ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiSolicitud(), back));
                ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiCodigo(), back));
                ds1.addCell(JsfUtil.celda(verDsiPorDti1.get(i).getDsiObservacion(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(5);
            ds1.addCell(celda);

        }
        tables.add(ds1);

        ds1 = null;

        /* ------ */
        /* DSI 2*/

        PdfPTable ds2 = new PdfPTable(6);
        ds2.addCell(JsfUtil.celdaTitulo("DOCUMENTACIÓN INTEGRADA PUBLICADA", 6));
        ds2.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds2.addCell(JsfUtil.celdaSubTitulo("Localizacion"));
        ds2.addCell(JsfUtil.celdaSubTitulo("Serie y NR"));
        //ds2.addCell(JsfUtil.celdaSubTitulo("Solic / F - N"));
        ds2.addCell(JsfUtil.celdaSubTitulo("Codigo (Q)"));
        //ds2.addCell(JsfUtil.celdaSubTitulo("Autorizado"));
        ds2.addCell(JsfUtil.celdaSubTitulo("EI"));
        ds2.addCell(JsfUtil.celdaSubTitulo("EE"));

        back = true;
        if (verDsiPorDti2.size() > 0) {
            for (int i = 0; i < verDsiPorDti2.size(); i++) {
                back = !back;
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiLocalizacion().getDslAlias(), back));
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiSerie(), back));
                //ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiSolicitud(), back));
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiCodigo(), back));
                //ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiAutorizado(), back));
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiErroresInternos(), back));
                ds2.addCell(JsfUtil.celda(verDsiPorDti2.get(i).getDsiErroresExternos(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(6);
            ds2.addCell(celda);

        }
        tables.add(ds2);

        /* ------ */
        /* DSI 3*/

        ds2 = null;

        PdfPTable ds3 = new PdfPTable(3);
        ds3.addCell(JsfUtil.celdaTitulo("ACTIVIDADES POSICIONES DE TRABAJO PUB / GIS", 3));
        ds3.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds3.addCell(JsfUtil.celdaSubTitulo("Posición De Trabajo"));
        ds3.addCell(JsfUtil.celdaSubTitulo("Actividades Desarrolladas"));

        back = true;
        if (verDsiPorDti3.size() > 0) {
            for (int i = 0; i < verDsiPorDti3.size(); i++) {
                back = !back;
                ds3.addCell(JsfUtil.celda(verDsiPorDti3.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds3.addCell(JsfUtil.celda(verDsiPorDti3.get(i).getDsiPosicionTrabajo(), back));
                ds3.addCell(JsfUtil.celda(verDsiPorDti3.get(i).getDsiActividad(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(3);
            ds3.addCell(celda);

        }
        tables.add(ds3);

        /* ------ */
        /* DSI 4*/

        ds3 = null;
        PdfPTable ds4 = new PdfPTable(4);
        ds4.addCell(JsfUtil.celdaTitulo("DOCUMENTACIÓN INTEGRADA PENDIENTE POR PUBLICAR", 4));
        ds4.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds4.addCell(JsfUtil.celdaSubTitulo("Localización"));
        ds4.addCell(JsfUtil.celdaSubTitulo("Documento"));
        ds4.addCell(JsfUtil.celdaSubTitulo("Observación"));

        back = true;
        if (verDsiPorDti4.size() > 0) {
            for (int i = 0; i < verDsiPorDti4.size(); i++) {
                back = !back;
                ds4.addCell(JsfUtil.celda(verDsiPorDti4.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds4.addCell(JsfUtil.celda(verDsiPorDti4.get(i).getDsiLocalizacion().getDslAlias(), back));
                ds4.addCell(JsfUtil.celda(verDsiPorDti4.get(i).getDsiDocumento(), back));
                ds4.addCell(JsfUtil.celda(verDsiPorDti4.get(i).getDsiObservacion(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(4);
            ds4.addCell(celda);

        }
        tables.add(ds4);

        /* ------ */
        /* DSI 5*/

        ds4 = null;

        PdfPTable ds5 = new PdfPTable(5);
        ds5.addCell(JsfUtil.celdaTitulo("OPERATIVIDAD DE EQUIPOS,SOFTWARE Y REDES", 5));
        ds5.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds5.addCell(JsfUtil.celdaSubTitulo("Equipo, Software o Red"));
        ds5.addCell(JsfUtil.celdaSubTitulo("Hora Utc"));
        ds5.addCell(JsfUtil.celdaSubTitulo("Falla"));
        ds5.addCell(JsfUtil.celdaSubTitulo("Observación"));

        back = true;
        if (verDsiPorDti5.size() > 0) {
            for (int i = 0; i < verDsiPorDti5.size(); i++) {
                back = !back;
                ds5.addCell(JsfUtil.celda(verDsiPorDti5.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds5.addCell(JsfUtil.celda(verDsiPorDti5.get(i).getDsiEquipoSoftware(), back));
                ds5.addCell(JsfUtil.celda(verDsiPorDti5.get(i).getDsiHoraUtc(), back));
                ds5.addCell(JsfUtil.celda(verDsiPorDti5.get(i).getDsiFalla(), back));
                ds5.addCell(JsfUtil.celda(verDsiPorDti5.get(i).getDsiObservacion(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(5);
            ds5.addCell(celda);

        }

        tables.add(ds5);

        /* ------ */
        /* DSI 5*/
        ds5 = null;

        PdfPTable ds6 = new PdfPTable(3);
        ds6.addCell(JsfUtil.celdaTitulo("NOVEDADES GENERALES DEL TURNO", 3));
        ds6.addCell(JsfUtil.celdaSubTitulo("Funcionario"));
        ds6.addCell(JsfUtil.celdaSubTitulo("Hora Utc"));
        ds6.addCell(JsfUtil.celdaSubTitulo("Observación"));

        back = true;
        if (verDsiPorDti6.size() > 0) {
            for (int i = 0; i < verDsiPorDti6.size(); i++) {
                back = !back;
                ds6.addCell(JsfUtil.celda(verDsiPorDti6.get(i).getDsiDiarioSenalFuncionario().getDsfFuncionario().getFunAlias(), back));
                ds6.addCell(JsfUtil.celda(verDsiPorDti6.get(i).getDsiHoraUtc(), back));
                ds6.addCell(JsfUtil.celda(verDsiPorDti6.get(i).getDsiObservacion(), back));
            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(3);
            ds6.addCell(celda);

        }
        tables.add(ds6);

        /* ------ */
        /* DSI 7*/




        PdfPTable ds7 = new PdfPTable(5);
        ds7.addCell(JsfUtil.celdaTitulo("CIERRE DEL TURNO", 5));
        ds7.addCell(JsfUtil.celdaSubTitulo("Producto"));
        ds7.addCell(JsfUtil.celdaSubTitulo("Serie y NR"));
        ds7.addCell(JsfUtil.celdaSubTitulo("Cantidad Publicadas"));
        ds7.addCell(JsfUtil.celdaSubTitulo("Cantidad EI"));
        ds7.addCell(JsfUtil.celdaSubTitulo("Cantidad EE"));
        back = true;
        if (verListaCierreTurno.size() > 0) {
            for (Object[] result : verListaCierreTurno) {
                back = !back;
                ds7.addCell(JsfUtil.celda(result[1].toString(), back));
                ds7.addCell(JsfUtil.celda(result[2].toString(), back));
                ds7.addCell(JsfUtil.celda(result[3].toString(), back));
                ds7.addCell(JsfUtil.celda(result[4].toString(), back));
                ds7.addCell(JsfUtil.celda(result[5].toString(), back));

            }
        } else {
            PdfPCell celda = JsfUtil.celda("No hay registros", false);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setColspan(5);
            ds7.addCell(celda);

        }




        tables.add(ds7);






        JsfUtil.generarPdfIText("Diario De Señales", tables);



        return null;
    }
    
    public String diariosOtrasDependencias(){
        DiarioSenalBBean dse = (DiarioSenalBBean) JsfUtil.getManagedBean(DiarioSenalBBean.class);
        return dse.listarTodas();
    }

    /*Consultar*/
    public String listar() {

        diarioSenalEspecialFiltro = new DiarioSenalEspecial();
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Dependencia d = new Dependencia();
        d.setDepId(306L);
        diarioSenalEspecialFiltro.setDseDependencia(d);
        jornadas = JsfUtil.getListadosBBean().getListaJornadaXDependencia(d.getDepId());



        return filtrar();
    }

    public String filtrar() {
        verDiarioSenalEspecial = new DiarioSenalEspecial();
        lista = new DiarioSenalEspecialLazyList(diarioSenalEspecialService, diarioSenalEspecialFiltro);
        return "FiltrarDiarioSenalEspecial";

    }

    public String ver() {
        //System.out.println("Seleccionado\t"+verDiarioSenalEspecial.getDseId());
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        verDsf = diarioSenalEspecialService.obtenerDsfPorDse(verDiarioSenalEspecial.getDseId(), logbbean.getFuncionarioTO().getFuncionario().getFunId());
        return "FiltrarDiarioSenalEspecial";
    }

    public String verDSF() {
        verDiarioSenalEspecial = verDsf.getDsfDiarioSenalEspecial();
        return "FiltrarDiarioSenalEspecial";
    }

    public String cerrarDseConsulta() {
        try {
            Calendar actual = Calendar.getInstance();
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            verDiarioSenalEspecial.setDseEstado("CERRADO");
            verDiarioSenalEspecial.setDseHoraCerrado(convertirHora(actual));
            verDiarioSenalEspecial.setDseFuncionarioCierra(logbbean.getFuncionarioTO().getFuncionario());
            guardarDse(verDiarioSenalEspecial, logbbean.getFuncionarioTO().getFuncionario());
            JsfUtil.addSuccessMessage("dseCerrado");
        } catch (Exception ex) {
            JsfUtil.addManualWarningMessage(ex.getMessage());
        }
        return ver();
    }

    public String salidaDsfConsulta() {
        Calendar actual = Calendar.getInstance();
        verDsf.setDsfHoraSalida(convertirHora(actual));
        verDsf.setDsfEstado("CERRADO");
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        guardarDsf(verDsf, logbbean.getFuncionarioTO().getFuncionario());
        JsfUtil.addSuccessMessage("dsfCerrado");
        return ver();
    }

    private void validar() {
        if (diarioSenalEspecial.getDseId() != null) {
            dseAuxiliar = diarioSenalEspecialService.obtenerPorId(diarioSenalEspecial.getDseId());
        }
        if (diarioSenalFuncionario.getDsfId() != null) {
            dsfAuxiliar = diarioSenalEspecialService.obtenerDsfPorId(diarioSenalFuncionario.getDsfId());
        }
    }

    public String eliminarDsi() {
        validar();

        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {
            if (dsfAuxiliar.getDsfEstado().equals("ABIERTO")) {
                //System.out.println("DSI: \t"+diarioSenalInfoSeleccionado.getDsiId()+"\t");
                diarioSenalEspecialService.eliminarDSI(diarioSenalInfoSeleccionado);
                JsfUtil.addSuccessMessage("dsiEliminadoExitosamente");
            } else {
                JsfUtil.addWarningMessage("dsfCerradoAviso");
            }
        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }

        diarioSenalInfoSeleccionado = new DiarioSenalInfo();
        actualizarPrincipales();
        return frontend();

        /**/
    }

    private void actualizarPrincipales() {
        Calendar actual = Calendar.getInstance();

        disabled = true;
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

        jornada = diarioSenalEspecialService.obtenerJornadaPorHora(actual.get(Calendar.HOUR_OF_DAY), logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
        if (jornada.getJoId() != null) {

            diarioSenalEspecial = diarioSenalEspecialService.obtenerDsePorFecha(actual.getTime(), jornada.getJoId());
            verDsf = diarioSenalEspecialService.obtenerDsfPorDseAnterior(logbbean.getFuncionarioTO().getFuncionario().getFunId());

            if (verDsf.getDsfId() != null) {
                //System.out.println("\tDSF = \t"+verDsf.getDsfDiarioSenalEspecial().getDseJornadaOp().getJoAlias());
            } else {
                //System.out.println("\tDSF = \tNULL");
            }



            /*if(diarioSenalEspecial.getDseId()==null)
             {
             diarioSenalEspecial.setDseFecha(actual.getTime());
             diarioSenalEspecial.setDseJornadaOp(jornada); 
             diarioSenalEspecial.setDseHoraCreado(convertirHora(actual));
             diarioSenalEspecial.setDseEstado("ABIERTO");
             diarioSenalEspecial.setDseDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
             diarioSenalEspecial= guardarDse(diarioSenalEspecial, logbbean.getFuncionarioTO().getFuncionario());
             }*/



            if (diarioSenalEspecial.getDseId() != null) {
                diarioSenalFuncionario = diarioSenalEspecialService.obtenerDsfPorDse(diarioSenalEspecial.getDseId(), logbbean.getFuncionarioTO().getFuncionario().getFunId());

                if (diarioSenalFuncionario.getDsfId() == null) {

                    posicion = diarioSenalEspecialService.obtenerPosicionPorTurnoFunc(diarioSenalEspecial.getDseFecha(), diarioSenalEspecial.getDseJornadaOp().getJoId(), logbbean.getFuncionarioTO().getFuncionario().getFunId());
                    if (posicion.getPosId() != null) {
                        diarioSenalFuncionario.setDsfPosicion(posicion);

                    }

                    diarioSenalFuncionario.setDsfEstado("ABIERTO");
                    diarioSenalFuncionario.setDsfFuncionario(logbbean.getFuncionarioTO().getFuncionario());
                    diarioSenalFuncionario.setDsfDiarioSenalEspecial(diarioSenalEspecial);
                }
            }

        } else {
            JsfUtil.addWarningMessage("dseJornadaAviso");
        }

    }

    public String registrarDse() {
        try {
            Calendar actual = Calendar.getInstance();
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            diarioSenalEspecial.setDseFecha(actual.getTime());
            diarioSenalEspecial.setDseJornadaOp(jornada);
            diarioSenalEspecial.setDseHoraCreado(convertirHora(actual));
            diarioSenalEspecial.setDseEstado("ABIERTO");
            diarioSenalEspecial.setDseDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
            diarioSenalEspecial = guardarDse(diarioSenalEspecial, logbbean.getFuncionarioTO().getFuncionario());
        } catch (Exception ex) {
            JsfUtil.addManualWarningMessage(ex.getMessage());
        }
        return registrar();
    }

    public String registrar() {
        reiniciar();
        actualizarPrincipales();
        return frontend();
    }

    public String volver() {
        diarioSenalInfoSeleccionado = new DiarioSenalInfo();
        return actualizar();
    }

    public String actualizar() {
        actualizarPrincipales();
        return frontend();
    }

    private String frontend() {
        return "DiarioSenalEspecial";
    }

    private void reiniciar() {
        jornada = new Jornada();
        diarioSenalEspecial = new DiarioSenalEspecial();
        diarioSenalFuncionario = new DiarioSenalFuncionario();
        diarioSenalInfo = new DiarioSenalInfo();
        posicion = new Posicion();
        diarioSenalInfoSeleccionado = new DiarioSenalInfo();

    }

    private DiarioSenalEspecial guardarDse(DiarioSenalEspecial dse, Funcionario func) throws Exception {
        return diarioSenalEspecialService.guardarDse(dse, func);
    }

    private DiarioSenalFuncionario guardarDsf(DiarioSenalFuncionario dsf, Funcionario func) {
        return diarioSenalEspecialService.guardarDsf(dsf, func);
    }

    /**
     * @return the diarioSenalEspecial
     */
    public DiarioSenalEspecial getDiarioSenalEspecial() {
        return diarioSenalEspecial;
    }

    /**
     * @param diarioSenalEspecial the diarioSenalEspecial to set
     */
    public void setDiarioSenalEspecial(DiarioSenalEspecial diarioSenalEspecial) {
        this.diarioSenalEspecial = diarioSenalEspecial;
    }

    /**
     * @return the diarioSenalFuncionario
     */
    public DiarioSenalFuncionario getDiarioSenalFuncionario() {
        return diarioSenalFuncionario;
    }

    /**
     * @param diarioSenalFuncionario the diarioSenalFuncionario to set
     */
    public void setDiarioSenalFuncionario(DiarioSenalFuncionario diarioSenalFuncionario) {
        this.diarioSenalFuncionario = diarioSenalFuncionario;
    }

    /**
     * @return the diarioSenalInfo
     */
    public DiarioSenalInfo getDiarioSenalInfo() {
        return diarioSenalInfo;
    }

    /**
     * @param diarioSenalInfo the diarioSenalInfo to set
     */
    public void setDiarioSenalInfo(DiarioSenalInfo diarioSenalInfo) {
        this.diarioSenalInfo = diarioSenalInfo;
    }

    /**
     * @return the posiciones
     */
    public List<Posicion> getPosiciones() {
        if (posiciones == null) {
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            posiciones = diarioSenalEspecialService.obtenerPosicionesDelFunc(logbbean.getFuncionarioTO().getFuncionario().getFunId());
        }
        return posiciones;
    }

    public String entradaDsf() {
        validar();
        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {

            Calendar actual = Calendar.getInstance();
            diarioSenalFuncionario.setDsfHoraEntrada(convertirHora(actual));
            diarioSenalFuncionario.setDsfEstado("ABIERTO");
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            verDsf = guardarDsf(diarioSenalFuncionario, logbbean.getFuncionarioTO().getFuncionario());

        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }

        actualizarPrincipales();
        return frontend();
    }

    public String recesoDsf() {
        validar();
        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {
            if (dsfAuxiliar.getDsfEstado().equals("ABIERTO")) {
                Calendar actual = Calendar.getInstance();
                diarioSenalFuncionario.setDsfHoraReceso(convertirHora(actual));
                LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
                guardarDsf(diarioSenalFuncionario, logbbean.getFuncionarioTO().getFuncionario());
            } else {
                JsfUtil.addWarningMessage("dsfCerradoAviso");
            }
        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }
        actualizarPrincipales();
        return frontend();
    }

    public String regresoDsf() {
        validar();
        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {
            if (dsfAuxiliar.getDsfEstado().equals("ABIERTO")) {
                Calendar actual = Calendar.getInstance();
                diarioSenalFuncionario.setDsfHoraRegreso(convertirHora(actual));
                LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
                guardarDsf(diarioSenalFuncionario, logbbean.getFuncionarioTO().getFuncionario());
            } else {
                JsfUtil.addWarningMessage("dsfCerradoAviso");
            }
        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }
        actualizarPrincipales();
        return frontend();
    }

    public String salidaDsf() {
        validar();
        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {
            if (dsfAuxiliar.getDsfEstado().equals("ABIERTO")) {
                if (getSale_temprano() && diarioSenalFuncionario.getDsfObservacionSalida().length() == 0) {
                    JsfUtil.addWarningMessage("dsfRequiereComentario");

                } else {
                    Calendar actual = Calendar.getInstance();
                    diarioSenalFuncionario.setDsfHoraSalida(convertirHora(actual));
                    diarioSenalFuncionario.setDsfEstado("CERRADO");
                    LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
                    guardarDsf(diarioSenalFuncionario, logbbean.getFuncionarioTO().getFuncionario());
                }

            } else {
                JsfUtil.addWarningMessage("dsfCerradoAviso");
            }
        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }
        actualizarPrincipales();
        return frontend();
    }

    /**
     * @return the jornada
     */
    public Jornada getJornada() {
        return jornada;
    }

    /**
     * @param jornada the jornada to set
     */
    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    /**
     * @return the posicion
     */
    public Posicion getPosicion() {
        return posicion;
    }

    /**
     * @param posicion the posicion to set
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    /**
     * @return the diarioSenalTipos
     */
    public List<DiarioSenalTipo> getDiarioSenalTipos() {
        diarioSenalTipos = diarioSenalEspecialService.obtenerTipos();
        return diarioSenalTipos;
    }

    /**
     * @param diarioSenalTipos the diarioSenalTipos to set
     */
    public void setDiarioSenalTipos(List<DiarioSenalTipo> diarioSenalTipos) {
        this.diarioSenalTipos = diarioSenalTipos;
    }

    private String convertirHora(Calendar c) {
        String hora = "";
        //c.add(Calendar.HOUR_OF_DAY, 5);
        if (c.get(Calendar.HOUR_OF_DAY) < 10) {
            hora += "0";
        }
        hora += c.get(Calendar.HOUR_OF_DAY);
        if (c.get(Calendar.MINUTE) < 10) {
            hora += "0";
        }
        hora += c.get(Calendar.MINUTE);
        return hora;
    }

    /**
     * @return the diarioSenalLocalizaciones
     */
    public List<DiarioSenalLocalizacion> getDiarioSenalLocalizaciones() {
        if (diarioSenalLocalizaciones == null) {
            diarioSenalLocalizaciones = diarioSenalEspecialService.obtenerDSL();

        }
        return diarioSenalLocalizaciones;
    }

    /**
     * @param diarioSenalLocalizaciones the diarioSenalLocalizaciones to set
     */
    public void setDiarioSenalLocalizaciones(List<DiarioSenalLocalizacion> diarioSenalLocalizaciones) {
        this.diarioSenalLocalizaciones = diarioSenalLocalizaciones;
    }

    public String actualizarDsi() {
        //System.out.println("Llegue\t\taqui");
        persistir(diarioSenalInfoSeleccionado);

        diarioSenalInfoSeleccionado = new DiarioSenalInfo();
        actualizarPrincipales();
        return frontend();
    }

    public String editarDsi() {
        if (diarioSenalInfoSeleccionado.getDsiHoraUtc() != null && diarioSenalInfoSeleccionado.getDsiHoraUtc().length() == 4) {
            horaUTC = diarioSenalInfoSeleccionado.getDsiHoraUtc().substring(0, 2);
            minutoUTC = diarioSenalInfoSeleccionado.getDsiHoraUtc().substring(2, 4);
            //System.out.println("Editando HORA UTC"+horaUTC+":"+minutoUTC);
        }
        return "EditDiarioSenalEspecial";
    }

    private void persistir(DiarioSenalInfo dsi) {
        validar();

        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {
            if (dsfAuxiliar.getDsfEstado().equals("ABIERTO")) {
                Calendar actual = Calendar.getInstance();
                dsi.setDsiHoraRegistro(convertirHora(actual));
                dsi.setDsiDiarioSenalFuncionario(diarioSenalFuncionario);
                LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

                if (dsi.getDsiTipo().getDstId() == 1 || dsi.getDsiTipo().getDstId() == 2) {
                    dsi.setDsiSerie("");
                    if (dsi.getDsiSerieC() != null && dsi.getDsiSerieC().length() != 0) {
                        if (dsi.getDsiSerie() == null) {
                            dsi.setDsiSerie("C" + dsi.getDsiSerieC());
                        } else {
                            dsi.setDsiSerie(dsi.getDsiSerie() + " - C" + dsi.getDsiSerieC());
                        }
                    }
                    if (dsi.getDsiSerieA() != null && dsi.getDsiSerieA().length() != 0) {
                        if (dsi.getDsiSerie() == null) {
                            dsi.setDsiSerie("A" + dsi.getDsiSerieA());
                        } else {
                            dsi.setDsiSerie(dsi.getDsiSerie() + " - A" + dsi.getDsiSerieA());
                        }
                    }
                    if (dsi.getDsiSerieB() != null && dsi.getDsiSerieB().length() != 0) {
                        if (dsi.getDsiSerie() == null) {
                            dsi.setDsiSerie("B" + dsi.getDsiSerieB());
                        } else {
                            dsi.setDsiSerie(dsi.getDsiSerie() + " - B" + dsi.getDsiSerieB());
                        }
                    }

                    if (dsi.getDsiSerieVa() != null && dsi.getDsiSerieVa().length() != 0) {
                        if (dsi.getDsiSerie() == null) {
                            dsi.setDsiSerie("VA" + dsi.getDsiSerieVa());
                        } else {
                            dsi.setDsiSerie(dsi.getDsiSerie() + " - VA" + dsi.getDsiSerieVa());
                        }
                    }
                }

                if (dsi.getDsiTipo().getDstId() == 5 || dsi.getDsiTipo().getDstId() == 6) {
                    dsi.setDsiHoraUtc(horaUTC + minutoUTC);
                    horaUTC = null;
                    minutoUTC = null;
                }

                diarioSenalEspecialService.guardarDsi(dsi, logbbean.getFuncionarioTO().getFuncionario());
                JsfUtil.addSuccessMessage("dsiRegistradoExitosamente");
            } else {
                JsfUtil.addWarningMessage("dsfCerradoAviso");
            }
        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }

    }

    public String guardarDsi() {

        persistir(diarioSenalInfo);

        diarioSenalInfo = new DiarioSenalInfo();
        actualizarPrincipales();
        return frontend();



    }

    public String cerrarDse() {

        validar();

        if (dseAuxiliar.getDseEstado().equals("ABIERTO")) {

            Calendar actual = Calendar.getInstance();
            LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
            diarioSenalEspecial.setDseEstado("CERRADO");
            diarioSenalEspecial.setDseHoraCerrado(convertirHora(actual));
            diarioSenalEspecial.setDseFuncionarioCierra(logbbean.getFuncionarioTO().getFuncionario());
            try {
                guardarDse(diarioSenalEspecial, logbbean.getFuncionarioTO().getFuncionario());
            } catch (Exception ex) {
                JsfUtil.addManualWarningMessage(ex.getMessage());
                return frontend();
            }

        } else {
            JsfUtil.addWarningMessage("dseCerradoAviso");
        }

        actualizarPrincipales();

        return frontend();
    }

    public String traerFormulario() {
        actualizarPrincipales();
        return frontend();
    }

    /**
     * @return the dsiPorDti1
     */
    public List<DiarioSenalInfo> getDsiPorDti1() {
        long cod = 1;

        dsiPorDti1 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);


        return dsiPorDti1;
    }

    /**
     * @param dsiPorDti1 the dsiPorDti1 to set
     */
    public void setDsiPorDti1(List<DiarioSenalInfo> dsiPorDti1) {
        this.dsiPorDti1 = dsiPorDti1;
    }

    /**
     * @return the dsiPorDti2
     */
    public List<DiarioSenalInfo> getDsiPorDti2() {
        long cod = 2;

        dsiPorDti2 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti2;
    }

    /**
     * @param dsiPorDti2 the dsiPorDti2 to set
     */
    public void setDsiPorDti2(List<DiarioSenalInfo> dsiPorDti2) {
        this.dsiPorDti2 = dsiPorDti2;
    }

    /**
     * @return the dsiPorDti3
     */
    public List<DiarioSenalInfo> getDsiPorDti3() {
        long cod = 3;

        dsiPorDti3 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti3;
    }

    /**
     * @param dsiPorDti3 the dsiPorDti3 to set
     */
    public void setDsiPorDti3(List<DiarioSenalInfo> dsiPorDti3) {
        this.dsiPorDti3 = dsiPorDti3;
    }

    /**
     * @return the dsiPorDti4
     */
    public List<DiarioSenalInfo> getDsiPorDti4() {
        long cod = 4;

        dsiPorDti4 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti4;
    }

    /**
     * @param dsiPorDti4 the dsiPorDti4 to set
     */
    public void setDsiPorDti4(List<DiarioSenalInfo> dsiPorDti4) {
        this.dsiPorDti4 = dsiPorDti4;
    }

    /**
     * @return the dsiPorDti5
     */
    public List<DiarioSenalInfo> getDsiPorDti5() {
        long cod = 5;

        dsiPorDti5 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti5;
    }

    /**
     * @param dsiPorDti5 the dsiPorDti5 to set
     */
    public void setDsiPorDti5(List<DiarioSenalInfo> dsiPorDti5) {
        this.dsiPorDti5 = dsiPorDti5;
    }

    /**
     * @return the dsiPorDti6
     */
    public List<DiarioSenalInfo> getDsiPorDti6() {
        long cod = 6;

        dsiPorDti6 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti6;
    }

    /**
     * @param dsiPorDti6 the dsiPorDti6 to set
     */
    public void setDsiPorDti6(List<DiarioSenalInfo> dsiPorDti6) {
        this.dsiPorDti6 = dsiPorDti6;
    }

    /**
     * @return the dsiPorDti7
     */
    public List<DiarioSenalInfo> getDsiPorDti7() {
        long cod = 7;

        dsiPorDti7 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(diarioSenalEspecial.getDseId(), cod);

        return dsiPorDti7;
    }

    /**
     * @param dsiPorDti7 the dsiPorDti7 to set
     */
    public void setDsiPorDti7(List<DiarioSenalInfo> dsiPorDti7) {
        this.dsiPorDti7 = dsiPorDti7;
    }

    /**
     * @return the diarioSenalCategorias
     */
    public List<DiarioSenalCategoria> getDiarioSenalCategorias() {
        if (diarioSenalCategorias == null) {
            diarioSenalCategorias = diarioSenalEspecialService.obtenerCategorias();

        }
        return diarioSenalCategorias;
    }

    /**
     * @param diarioSenalCategorias the diarioSenalCategorias to set
     */
    public void setDiarioSenalCategorias(List<DiarioSenalCategoria> diarioSenalCategorias) {
        this.diarioSenalCategorias = diarioSenalCategorias;
    }

    /**
     * @return the listaCierreTurno
     */
    public List<Object[]> getListaCierreTurno() {
        listaCierreTurno = diarioSenalEspecialService.cierreTurno(diarioSenalEspecial.getDseId());

        return listaCierreTurno;
    }

    /**
     * @param listaCierreTurno the listaCierreTurno to set
     */
    public void setListaCierreTurno(List<Object[]> listaCierreTurno) {
        this.listaCierreTurno = listaCierreTurno;
    }

    /**
     * @return the diarioSenalInfoSeleccionado
     */
    public DiarioSenalInfo getDiarioSenalInfoSeleccionado() {
        return diarioSenalInfoSeleccionado;
    }

    /**
     * @param diarioSenalInfoSeleccionado the diarioSenalInfoSeleccionado to set
     */
    public void setDiarioSenalInfoSeleccionado(DiarioSenalInfo diarioSenalInfoSeleccionado) {
        this.diarioSenalInfoSeleccionado = diarioSenalInfoSeleccionado;
    }

    /**
     * @return the busqueda
     */
    public String getBusqueda() {
        return busqueda;
    }

    /**
     * @param busqueda the busqueda to set
     */
    public void setBusqueda(String busqueda) {
        diarioSenalLocalizaciones = diarioSenalEspecialService.filtroLocalizaciones(busqueda);
        this.busqueda = busqueda;
    }

    /**
     * @return the diarioSenalEspecialFiltro
     */
    public DiarioSenalEspecial getDiarioSenalEspecialFiltro() {
        return diarioSenalEspecialFiltro;
    }

    /**
     * @param diarioSenalEspecialFiltro the diarioSenalEspecialFiltro to set
     */
    public void setDiarioSenalEspecialFiltro(DiarioSenalEspecial diarioSenalEspecialFiltro) {
        this.diarioSenalEspecialFiltro = diarioSenalEspecialFiltro;
    }

    /**
     * @return the lista
     */
    public LazyDataModel<DiarioSenalEspecial> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(LazyDataModel<DiarioSenalEspecial> lista) {
        this.lista = lista;
    }

    /**
     * @return the jornadas
     */
    public List<Jornada> getJornadas() {
        return jornadas;
    }

    /**
     * @param jornadas the jornadas to set
     */
    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    /**
     * @return the verDiarioSenalEspecial
     */
    public DiarioSenalEspecial getVerDiarioSenalEspecial() {
        return verDiarioSenalEspecial;
    }

    /**
     * @param verDiarioSenalEspecial the verDiarioSenalEspecial to set
     */
    public void setVerDiarioSenalEspecial(DiarioSenalEspecial verDiarioSenalEspecial) {
        this.verDiarioSenalEspecial = verDiarioSenalEspecial;
    }

    /**
     * @return the verDsiPorDti1
     */
    public List<DiarioSenalInfo> getVerDsiPorDti1() {
        long cod = 1;

        verDsiPorDti1 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti1;
    }

    /**
     * @param verDsiPorDti1 the verDsiPorDti1 to set
     */
    public void setVerDsiPorDti1(List<DiarioSenalInfo> verDsiPorDti1) {
        this.verDsiPorDti1 = verDsiPorDti1;
    }

    /**
     * @return the verDsiPorDti2
     */
    public List<DiarioSenalInfo> getVerDsiPorDti2() {
        long cod = 2;

        verDsiPorDti2 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti2;
    }

    /**
     * @param verDsiPorDti2 the verDsiPorDti2 to set
     */
    public void setVerDsiPorDti2(List<DiarioSenalInfo> verDsiPorDti2) {
        this.verDsiPorDti2 = verDsiPorDti2;
    }

    /**
     * @return the verDsiPorDti3
     */
    public List<DiarioSenalInfo> getVerDsiPorDti3() {
        long cod = 3;

        verDsiPorDti3 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti3;
    }

    /**
     * @param verDsiPorDti3 the verDsiPorDti3 to set
     */
    public void setVerDsiPorDti3(List<DiarioSenalInfo> verDsiPorDti3) {
        this.verDsiPorDti3 = verDsiPorDti3;
    }

    /**
     * @return the verDsiPorDti4
     */
    public List<DiarioSenalInfo> getVerDsiPorDti4() {
        long cod = 4;

        verDsiPorDti4 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti4;
    }

    /**
     * @param verDsiPorDti4 the verDsiPorDti4 to set
     */
    public void setVerDsiPorDti4(List<DiarioSenalInfo> verDsiPorDti4) {
        this.verDsiPorDti4 = verDsiPorDti4;
    }

    /**
     * @return the verDsiPorDti5
     */
    public List<DiarioSenalInfo> getVerDsiPorDti5() {
        long cod = 5;

        verDsiPorDti5 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti5;
    }

    /**
     * @param verDsiPorDti5 the verDsiPorDti5 to set
     */
    public void setVerDsiPorDti5(List<DiarioSenalInfo> verDsiPorDti5) {
        this.verDsiPorDti5 = verDsiPorDti5;
    }

    /**
     * @return the verDsiPorDti6
     */
    public List<DiarioSenalInfo> getVerDsiPorDti6() {
        long cod = 6;

        verDsiPorDti6 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti6;

    }

    /**
     * @param verDsiPorDti6 the verDsiPorDti6 to set
     */
    public void setVerDsiPorDti6(List<DiarioSenalInfo> verDsiPorDti6) {
        this.verDsiPorDti6 = verDsiPorDti6;
    }

    /**
     * @return the verDsiPorDti7
     */
    public List<DiarioSenalInfo> getVerDsiPorDti7() {
        long cod = 7;

        verDsiPorDti7 = diarioSenalEspecialService.diarioSenalInfosPorDseDst(verDiarioSenalEspecial.getDseId(), cod);

        return verDsiPorDti7;
    }

    /**
     * @param verDsiPorDti7 the verDsiPorDti7 to set
     */
    public void setVerDsiPorDti7(List<DiarioSenalInfo> verDsiPorDti7) {
        this.verDsiPorDti7 = verDsiPorDti7;
    }

    /**
     * @return the verListaCierreTurno
     */
    public List<Object[]> getVerListaCierreTurno() {
        verListaCierreTurno = diarioSenalEspecialService.cierreTurno(verDiarioSenalEspecial.getDseId());
        return verListaCierreTurno;

    }

    /**
     * @param verListaCierreTurno the verListaCierreTurno to set
     */
    public void setVerListaCierreTurno(List<Object[]> verListaCierreTurno) {
        this.verListaCierreTurno = verListaCierreTurno;
    }

    /**
     * @return the verFuncionarios
     */
    public List<DiarioSenalFuncionario> getVerFuncionarios() {
        verFuncionarios = diarioSenalEspecialService.obtenerDsfsPorDse(verDiarioSenalEspecial.getDseId(), null);

        return verFuncionarios;
    }

    /**
     * @param verFuncionarios the verFuncionarios to set
     */
    public void setVerFuncionarios(List<DiarioSenalFuncionario> verFuncionarios) {
        this.verFuncionarios = verFuncionarios;
    }

    /**
     * @return the funcionarios
     */
    public List<DiarioSenalFuncionario> getFuncionarios() {
        funcionarios = diarioSenalEspecialService.obtenerDsfsPorDse(diarioSenalEspecial.getDseId(), "ABIERTO");
        return funcionarios;
    }

    /**
     * @param funcionarios the funcionarios to set
     */
    public void setFuncionarios(List<DiarioSenalFuncionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    /**
     * @return the llega_tarde
     */
    public Boolean getLlega_tarde() {
        if (jornada.getJoId() != null) {
            Calendar actualTime = Calendar.getInstance();
            Calendar jornadaTime = Calendar.getInstance();

            jornadaTime.set(Calendar.HOUR_OF_DAY, jornada.getJoHoraInicio());
            jornadaTime.set(Calendar.MINUTE, jornada.getJoMinutoInicio());
            jornadaTime.add(Calendar.MINUTE, minutosDiferenciaTemprano);

            ////System.out.println("LLEGA TARDE? - Hora Jornada + tiempo establecido\t"+jornadaTime.get(Calendar.HOUR_OF_DAY)+":"+jornadaTime.get(Calendar.MINUTE)+"\t Milisegundos\t"+jornadaTime.getTimeInMillis());
            // //System.out.println("LLEGA TARDE? - Hora Actual\t"+actualTime.get(Calendar.HOUR_OF_DAY)+":"+actualTime.get(Calendar.MINUTE)+"\t Milisegundos\t"+actualTime.getTimeInMillis());


            if (actualTime.getTimeInMillis() > jornadaTime.getTimeInMillis()) {
                //System.out.println("LLEGA TARDE? TRUE");
                llega_tarde = true;
            } else {
                //System.out.println("LLEGA TARDE? FALSE");
                llega_tarde = false;
            }

        } else {
            llega_tarde = false;
        }
        return llega_tarde;
    }

    /**
     * @param llega_tarde the llega_tarde to set
     */
    public void setLlega_tarde(Boolean llega_tarde) {
        this.llega_tarde = llega_tarde;
    }

    public void operarSaleTemprano() {
        getSale_temprano();
    }

    /**
     * @return the sale_temprano
     */
    public Boolean getSale_temprano() {
        if (jornada.getJoId() != null) {
            Calendar actualTime = Calendar.getInstance();
            Calendar jornadaTime = Calendar.getInstance();

            jornadaTime.set(Calendar.HOUR_OF_DAY, jornada.getJoHoraFin());
            jornadaTime.set(Calendar.MINUTE, jornada.getJoMinutoFin());
            jornadaTime.add(Calendar.MINUTE, minutosDiferenciaSale * -1);

            ////System.out.println("SALE TEMPRANO? - Hora Jornada + tiempo establecido\t"+jornadaTime.get(Calendar.HOUR_OF_DAY)+":"+jornadaTime.get(Calendar.MINUTE)+"\t Milisegundos\t"+jornadaTime.getTimeInMillis());
            ////System.out.println("SALE TEMPRANO? - Hora Actual\t"+actualTime.get(Calendar.HOUR_OF_DAY)+":"+actualTime.get(Calendar.MINUTE)+"\t Milisegundos\t"+actualTime.getTimeInMillis());


            if (actualTime.getTimeInMillis() < jornadaTime.getTimeInMillis()) {
                ////System.out.println("SALE TEMPRANO? TRUE");
                sale_temprano = true;
            } else {
                ////System.out.println("SALE TEMPRANO? FALSE");
                sale_temprano = false;
            }

        } else {
            sale_temprano = false;
        }
        return sale_temprano;
    }

    /**
     * @param sale_temprano the sale_temprano to set
     */
    public void setSale_temprano(Boolean sale_temprano) {
        this.sale_temprano = sale_temprano;
    }

    /**
     * @return the horasUTC
     */
    public List<String> getHorasUTC() {
        horasUTC = new ArrayList<String>();
        int inicio = 0;
        int fin = 24;
        if (jornada.getJoId() != null) {
            inicio = (jornada.getJoHoraInicio() + 5) % 24;
            fin = (jornada.getJoHoraFin() + 5) % 24;
            //System.out.println("Inicio: "+inicio+"\tFin: "+fin);
        }
        if (inicio > fin) {
            fin = (jornada.getJoHoraFin() + 5);
            for (int i = inicio; i <= fin; i++) {
                int hora = i%24;
                if (hora < 10) {
                    horasUTC.add("0" + hora);
                } else {
                    horasUTC.add(hora + "");
                }
            }
        } else {
            for (int i = inicio; i <= fin; i++) {
                if (i < 10) {
                    horasUTC.add("0" + i);
                } else {
                    horasUTC.add(i + "");
                }
            }
        }
        return horasUTC;
    }

    /**
     * @param horasUTC the horasUTC to set
     */
    public void setHorasUTC(List<String> horasUTC) {
        this.horasUTC = horasUTC;
    }

    /**
     * @return the minutosUTC
     */
    public List<String> getMinutosUTC() {

        minutosUTC = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutosUTC.add("0" + i);
            } else {
                minutosUTC.add(i + "");
            }
        }
        return minutosUTC;
    }

    /**
     * @param minutosUTC the minutosUTC to set
     */
    public void setMinutosUTC(List<String> minutosUTC) {
        this.minutosUTC = minutosUTC;
    }

    /**
     * @return the horaUTC
     */
    public String getHoraUTC() {
        return horaUTC;
    }

    /**
     * @param horaUTC the horaUTC to set
     */
    public void setHoraUTC(String horaUTC) {
        this.horaUTC = horaUTC;
    }

    /**
     * @return the minutoUTC
     */
    public String getMinutoUTC() {
        return minutoUTC;
    }

    /**
     * @param minutoUTC the minutoUTC to set
     */
    public void setMinutoUTC(String minutoUTC) {
        this.minutoUTC = minutoUTC;
    }

    /**
     * @return the verDsf
     */
    public DiarioSenalFuncionario getVerDsf() {
        return verDsf;
    }

    /**
     * @param verDsf the verDsf to set
     */
    public void setVerDsf(DiarioSenalFuncionario verDsf) {
        this.verDsf = verDsf;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void disabledFalse() {
        if (diarioSenalInfo.getDsiTipo() != null) {
            setDisabled(false);
        } else {
            setDisabled(true);
        }

    }
}
