/* 
 * To change this template, choose Tools | Templates 
 * and open the template in the editor. 
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Resumen;
import co.gov.aerocivil.controlt.entities.TempVerProgramacion;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.FuncionarioService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.services.ProgramacionTurnosSession;
import co.gov.aerocivil.controlt.services.TempVerProgramacionService;
import co.gov.aerocivil.controlt.to.ProgramacionTO;
import co.gov.aerocivil.controlt.web.enums.Months;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class TempVerProgramacionBBean {

    @EJB
    private TempVerProgramacionService tempVerProgramacionService;
    @EJB
    private ListasService listasService;
    @EJB
    FuncionarioService funcionarioService;
    @EJB
    private ProgramacionTurnosSession programacionService;
    private TempVerProgramacion tempVerProgramacion;
    private TempVerProgramacion tempVerProgramacionFiltro;
    private Programacion programacion;
    private List<TempVerProgramacion> lista;
    private Long[] numDiasProg;
    private Long diasMostrar;
    private List<ColumnModel> columns;
    private List<ColumnModel> columnsFecha;
    private List<PosNoAsig> listPosNoAsig;
    private List<Resumen> resumenList;
    private List<String[]> dynamicCars;
    private List<String[]> reportData;
    private boolean flagVal;
    private String excludedColumns;
    private Funcionario funcionario;
    private List<Vistaprogramacion> comprobacion;
    private List<Vistaprogramacion> fechas;
    private List<ColumnModel> columnsComprobar;
    private List<ColumnModel> columnsFechaComprobacion;
    private List<String[]> dynamicComprobacion;
    private boolean isEmptyListPosNoAsig;
    private String procesoResumen;

    public List<String[]> getDynamicCars() {
        return dynamicCars;
    }

    private void filtrando() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        funcionario = logbbean.getFuncionarioTO().getFuncionario();
        diasMostrar = DateUtil.daysBetween(programacion.getProFechaFin(), programacion.getProFechaInicio());
        columns = new ArrayList<ColumnModel>();
        columnsFecha = new ArrayList<ColumnModel>();
        String FechaS = new String();

        Date fechaf = new Date();
        for (int i = 0; i <= diasMostrar; i++) {
            fechaf = DateUtil.addDays(programacion.getProFechaInicio(), i);
            columnsFecha.add(new ColumnModel(DateUtil.formatDateProg(fechaf), "fecha" + i));
        }

        String nomCol = new String();

        columns.add(new ColumnModel("Funcionario", "Func"));
        for (int i = 1; i <= ((diasMostrar + 1) * 2); i++) {

            if (i % 2 == 0) {
                nomCol = "Ext";
            } else {
                nomCol = "Ord";
            }

            columns.add(new ColumnModel(nomCol, "Dia" + i));
            //numDiasProg[i]= (long)i; 

        }
        columns.add(new ColumnModel("Funcionario.", "Func."));
        columns.add(new ColumnModel("THRX", "THRX"));
        tempVerProgramacionService.consultarTurnos(programacion);
        lista = tempVerProgramacionService.getLista(programacion);
        dynamicCars = new ArrayList<String[]>();
        reportData = new ArrayList<String[]>();

        int size = columns.size();

        for (int i = 0; i < lista.size(); i++) {
            ArrayList<String> lista_posiciones = new ArrayList<String>();
            String[] datos_posiciones = lista.get(i).getProg();
            lista_posiciones.add(lista.get(i).getFuncionario().getFunAlias());
            //System.out.println("datos pr1: "+lista.get(i).getFuncionario().getFunAlias()); 
            for (int j = 0; j < datos_posiciones.length; j++) {
                lista_posiciones.add(datos_posiciones[j]);
            }
            //System.out.println("datos pr2: "+lista.get(i).getFuncionario().getFunAlias()); 
            String[] posicionesReporte = new String[lista_posiciones.size()];
            posicionesReporte = lista_posiciones.toArray(posicionesReporte);
            reportData.add(posicionesReporte);
            lista_posiciones.add(lista.get(i).getFuncionario().getFunAlias());
            Long tohrex = tempVerProgramacionService.totalHrEx(programacion.getProId(), lista.get(i).getFuncionario());
            lista_posiciones.add(tohrex.toString());
            String[] posiciones = new String[lista_posiciones.size()];
            posiciones = lista_posiciones.toArray(posiciones);
            dynamicCars.add(posiciones);
        }

        /* 
         for (int i = 0; i < lista.size(); i++) { 
         String[] posiciones = new String[size]; 

         for (int j = 1; j < size; j++) { 
         if (j == 1) { 
         posiciones[j - 1] = lista.get(i).getFuncionario().getFunAlias(); 
         }                
         if((j-1)>lista.get(i).getProg().length) 
         {                    
         posiciones[j]=""; 
         } 
         else 
         { 
         try 
         { 
         posiciones[j] = lista.get(i).getProg()[j - 1]; 
         }catch(ArrayIndexOutOfBoundsException e){ 
         e.printStackTrace(); 
         } 
         }                
         } 

         dynamicCars.add(posiciones); 
         }*/


        comprobar();
        System.out.println("Fin de la programacion " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
        //resumenList = tempVerProgramacionService.obtenerResumen(programacion.getProId());
    }

    public String filtrar() {

        flagVal = true;
        filtrando();
        listPosNoAsig = null;
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A1)) {
            listPosNoAsig = JsfUtil.getListadosBBean().getListaPosNoAsig(programacion.getDependencia().getDepId(),
                    programacion.getProId());
        } else {
            listPosNoAsig = JsfUtil.getListadosBBean().getListaPosNoAsig(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId(),
                    programacion.getProId());
        }
        resumenList = null;
        isEmptyListPosNoAsig = false;
        if (listPosNoAsig.isEmpty()) {
            isEmptyListPosNoAsig = true;
        }


        return "verProgramacion";
    }

    public void obtenerResumen() {
        resumenList = tempVerProgramacionService.obtenerResumen(programacion.getProId());
    }

    public void obtenerProcesoResumen() {
        FileWriter fileWriter = null;
        try {
            String registros = tempVerProgramacionService.obtenerResumenProceso(programacion.getProId(), funcionario);
            
            HttpServletResponse response =
                    (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=programacion"+programacion.getProId()+".txt");
            response.getOutputStream().write(registros.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(TempVerProgramacionBBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(TempVerProgramacionBBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String validarProgramacion() {
        ProgramacionTurnosBBean bbean = ((ProgramacionTurnosBBean) JsfUtil.getManagedBean(ProgramacionTurnosBBean.class));
        bbean.setProgramacion(programacion);

        return bbean.validar();



    }

    public String filtrarValidacion() {

        flagVal = false;
        filtrando();
        return "verProgramacion";
    }

    public String enviarAprobar() {
        programacion.setProEstado(2);
        programacionService.guardar(programacion, JsfUtil.getFuncionarioSesion());
        return null;
    }

    public String aprobar() {
        programacion.setProEstado(1);
        programacionService.guardar(programacion, JsfUtil.getFuncionarioSesion());
        return null;
    }

    public String volverEditar() {
        programacion.setProEstado(0);
        programacionService.guardar(programacion, JsfUtil.getFuncionarioSesion());
        return null;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public List<TempVerProgramacion> getLista() {
        return lista;
    }

    public void setLista(List<TempVerProgramacion> lista) {
        this.lista = lista;
    }

    public TempVerProgramacionService getTempVerProgramacionService() {
        return tempVerProgramacionService;
    }

    public void setTempVerProgramacionService(TempVerProgramacionService tempVerProgramacionService) {
        this.tempVerProgramacionService = tempVerProgramacionService;
    }

    public TempVerProgramacion getTempVerProgramacion() {
        return tempVerProgramacion;
    }

    public void setTempVerProgramacion(TempVerProgramacion tempVerProgramacion) {
        this.tempVerProgramacion = tempVerProgramacion;
    }

    public TempVerProgramacion getTempVerProgramacionFiltro() {
        return tempVerProgramacionFiltro;
    }

    public void setTempVerProgramacionFiltro(TempVerProgramacion tempVerProgramacionFiltro) {
        this.tempVerProgramacionFiltro = tempVerProgramacionFiltro;
    }

    public Long[] getNumDiasProg() {
        return numDiasProg;
    }

    public void setNumDiasProg(Long[] numDiasProg) {
        this.numDiasProg = numDiasProg;
    }

    public Long getDiasMostrar() {
        return diasMostrar;
    }

    public void setDiasMostrar(Long diasMostrar) {
        this.diasMostrar = diasMostrar;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public List<ColumnModel> getColumnsFecha() {
        return columnsFecha;
    }

    public void setColumnsFecha(List<ColumnModel> columnsFecha) {
        this.columnsFecha = columnsFecha;
    }

    public List<PosNoAsig> getListPosNoAsig() {
        return listPosNoAsig;
    }

    public void setListPosNoAsig(List<PosNoAsig> listPosNoAsig) {
        this.listPosNoAsig = listPosNoAsig;
    }

    public boolean isFlagVal() {
        return flagVal;
    }

    public void setFlagVal(boolean flagVal) {
        this.flagVal = flagVal;
    }

    private void excluirColumnas(Long diasMostrar) {
        excludedColumns = "";
        diasMostrar = 10L;
        for (int i = 31; i > 31 - diasMostrar; i--) {
            excludedColumns += "" + i + ",";
        }
        excludedColumns = excludedColumns.substring(0, excludedColumns.length() - 1);
    }

    /**
     * @return the comprobacion
     */
    public List<Vistaprogramacion> getComprobacion() {
        return comprobacion;
    }

    /**
     * @param comprobacion the comprobacion to set
     */
    public void setComprobacion(List<Vistaprogramacion> comprobacion) {
        this.comprobacion = comprobacion;
    }

    /**
     * @return the fechas
     */
    public List<Vistaprogramacion> getFechas() {
        return fechas;
    }

    /**
     * @param fechas the fechas to set
     */
    public void setFechas(List<Vistaprogramacion> fechas) {
        this.fechas = fechas;
    }

    /**
     * @return the columnsComprobar
     */
    public List<ColumnModel> getColumnsComprobar() {
        return columnsComprobar;
    }

    /**
     * @param columnsComprobar the columnsComprobar to set
     */
    public void setColumnsComprobar(List<ColumnModel> columnsComprobar) {
        this.columnsComprobar = columnsComprobar;
    }

    /**
     * @return the dynamicComprobacion
     */
    public List<String[]> getDynamicComprobacion() {
        return dynamicComprobacion;
    }

    /**
     * @param dynamicComprobacion the dynamicComprobacion to set
     */
    public void setDynamicComprobacion(List<String[]> dynamicComprobacion) {
        this.dynamicComprobacion = dynamicComprobacion;
    }

    /**
     * @return the columnsFechaComprobacion
     */
    public List<ColumnModel> getColumnsFechaComprobacion() {
        return columnsFechaComprobacion;
    }

    /**
     * @param columnsFechaComprobacion the columnsFechaComprobacion to set
     */
    public void setColumnsFechaComprobacion(List<ColumnModel> columnsFechaComprobacion) {
        this.columnsFechaComprobacion = columnsFechaComprobacion;
    }

    /**
     * @return the funcionario
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return the isEmptyListPosNoAsig
     */
    public boolean isIsEmptyListPosNoAsig() {
        return isEmptyListPosNoAsig;
    }

    /**
     * @param isEmptyListPosNoAsig the isEmptyListPosNoAsig to set
     */
    public void setIsEmptyListPosNoAsig(boolean isEmptyListPosNoAsig) {
        this.isEmptyListPosNoAsig = isEmptyListPosNoAsig;
    }

    static public class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    public String getExcludedColumns() {
        return excludedColumns;
    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        //Rectangle lettHoriz = new Rectangle(PageSize.LETTER); 
        Rectangle lettHoriz = new Rectangle(0, 0, 279.4F, 215.9F);
        //lettHoriz.rotate();        
        pdf.setPageSize(lettHoriz);
        pdf.setMargins(0, 0, 0, 0);
        //HeaderFooter hf = new HeaderFooter(null, null); 
        //pdf.setHeader(); 
        ////System.out.println("Rotando"); 
        /*ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext(); 
         String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "prime_logo.png"; 

         pdf.add(Image.getInstance(logo));*/
    }

    public String exportPDF() {

        double paginas = Math.ceil((diasMostrar + 1) / 16) + 1;
        ////System.out.println("\n\nPaginas:"+paginas); 


        String reportFileName = "programacionTurnos";
        ArrayList<JasperPrint> lista_reporte = new ArrayList<JasperPrint>();



        for (int pagina = 0; pagina < paginas; pagina++) {
            ////System.out.println("\n\nPagina:"+pagina+"\n\n"); 

            // grupos de 16 
            List<ProgramacionTO> listaProg = new ArrayList<ProgramacionTO>();

            for (String[] str : reportData) {
                ProgramacionTO progTO = new ProgramacionTO();
                ArrayList<String> contenidoLista = new ArrayList<String>();
                contenidoLista.add(str[0]);

                int desde = (pagina * 32) + 1;
                int hasta = ((pagina + 1) * 32);

                for (int actual = desde; actual <= hasta; actual++) {
                    if (actual >= str.length) {
                        //sobrantes 
                        contenidoLista.add("");
                    } else {
                        contenidoLista.add(str[actual]);
                    }
                }

                int thorex = 0;
                for (int i = 1; i < (paginas * 32); i++) {
                    if (i < str.length) {
                        if (i % 2 == 0 && str[i].length() > 1) {
                            thorex++;
                        }
                    }
                }

                contenidoLista.add(thorex + "");

                String[] contenido = new String[contenidoLista.size()];
                contenido = contenidoLista.toArray(contenido);
                progTO.setArray(contenido);
                ////System.out.println(""); 
                for (int i = 0; i < contenido.length; i++) {
                    ////System.out.print(contenido[i]+"\t"); 
                }
                listaProg.add(progTO);
            }

            HashMap<String, Object> map = new HashMap();
            String[][] fechasArr = new String[16][2];

            ////System.out.println("\n\nFechas de la pagina "+pagina+" :\n"); 
            int j = 0;
            for (int i = (pagina * 16); i < 16 * (pagina + 1); i++) {

                Date fechaf = DateUtil.addDays(programacion.getProFechaInicio(), i);
                fechasArr[j][0] = DateUtil.formatDate(fechaf, "EE-dd").toUpperCase();
                fechasArr[j][1] = JsfUtil.getListadosBBean().isDiaFestivo(fechaf) ? "1" : null;
                j++;
            }
            ////System.out.println(""); 
            /*for(int i=0; i<16;i++) 
             { 
             ////System.out.print(fechasArr[i][0]+"\t"); 
             } 
             */






            HashMap<String, String> params = listasService.getParametrosSistema();
            map.put("fechasArr", fechasArr);

            map.put("clave", programacion.getDependencia().getDepcategoria().getDcClaveP());
            map.put("version", programacion.getDependencia().getDepcategoria().getDcVersionP());
            map.put("fechaFormato", DateUtil.formatDate(programacion.getDependencia().getDepcategoria().getDcFechaP()));
            map.put("consecutivo", programacion.getDependencia().getDepProcedencia() + "425");


            map.put("dependencia", programacion.getDependencia().getDepNombre());
            map.put("depAbrev", programacion.getDependencia().getDepAbreviatura());
            map.put("aeropuerto", programacion.getDependencia().getAeropuerto().getAeNombre());
            map.put("piePagina", "Clave: " + programacion.getDependencia().getDepcategoria().getDcClaveP() + "\nVersión: " + programacion.getDependencia().getDepcategoria().getDcVersionP() + "\nFecha: " + DateUtil.formatDate(programacion.getDependencia().getDepcategoria().getDcFechaP()));



            Funcionario usuario = JsfUtil.getFuncionarioSesion();
            if (programacion.getFuncionarioGenera() == null) {

                map.put("adminNiv4", usuario.getFunNombre() + "<br/>Coordinador Dependencia");
            } else {

                map.put("adminNiv4", programacion.getFuncionarioGenera().getFunNombre());
            }


            if (programacion.getFuncionarioAprueba() == null) {

                map.put("adminNiv3", "Jefe Regional");
            } else {

                map.put("adminNiv3", programacion.getFuncionarioAprueba().getFunNombre());
            }


            map.put("adminNiv2", "Director Regional");

            Calendar inicio_c = Calendar.getInstance();
            inicio_c.setTime(programacion.getProFechaInicio());

            Calendar fin_c = Calendar.getInstance();
            fin_c.setTime(programacion.getProFechaFin());

            String periodo = inicio_c.get(Calendar.DATE) + " de " + Months.getMonth(inicio_c.get(Calendar.MONTH)).getLabel() + " a " + fin_c.get(Calendar.DATE) + " de " + Months.getMonth(fin_c.get(Calendar.MONTH)).getLabel();

            map.put("periodo", periodo);

            lista_reporte.add(JsfUtil.generacionPagina(reportFileName, map, listaProg));


        }

        JsfUtil.salidaReporte(lista_reporte, reportFileName);


        return null;
    }

    public String exportXLS() {

        double paginas = Math.ceil((diasMostrar + 1) / 16) + 1;
        ////System.out.println("\n\nPaginas:"+paginas); 


        String reportFileName = "programacionTurnosXls";
        ArrayList<JasperPrint> lista_reporte = new ArrayList<JasperPrint>();



        for (int pagina = 0; pagina < paginas; pagina++) {
            ////System.out.println("\n\nPagina:"+pagina+"\n\n"); 

            // grupos de 16 
            List<ProgramacionTO> listaProg = new ArrayList<ProgramacionTO>();

            for (String[] str : reportData) {
                ProgramacionTO progTO = new ProgramacionTO();
                ArrayList<String> contenidoLista = new ArrayList<String>();
                contenidoLista.add(str[0]);

                int desde = (pagina * 32) + 1;
                int hasta = ((pagina + 1) * 32);

                for (int actual = desde; actual <= hasta; actual++) {
                    if (actual >= str.length) {
                        //sobrantes 
                        contenidoLista.add("");
                    } else {
                        contenidoLista.add(str[actual]);
                    }
                }

                int thorex = 0;
                for (int i = 1; i < (paginas * 32); i++) {
                    if (i < str.length) {
                        if (i % 2 == 0 && str[i].length() > 1) {
                            thorex++;
                        }
                    }
                }

                contenidoLista.add(thorex + "");

                String[] contenido = new String[contenidoLista.size()];
                contenido = contenidoLista.toArray(contenido);
                progTO.setArray(contenido);
                ////System.out.println(""); 
                for (int i = 0; i < contenido.length; i++) {
                    ////System.out.print(contenido[i]+"\t"); 
                }
                listaProg.add(progTO);
            }

            HashMap<String, Object> map = new HashMap();
            String[][] fechasArr = new String[16][2];

            ////System.out.println("\n\nFechas de la pagina "+pagina+" :\n"); 
            int j = 0;
            for (int i = (pagina * 16); i < 16 * (pagina + 1); i++) {

                Date fechaf = DateUtil.addDays(programacion.getProFechaInicio(), i);
                fechasArr[j][0] = DateUtil.formatDate(fechaf, "EE-dd").toUpperCase();
                fechasArr[j][1] = JsfUtil.getListadosBBean().isDiaFestivo(fechaf) ? "1" : null;
                j++;
            }
            ////System.out.println(""); 
            /*for(int i=0; i<16;i++) 
             { 
             ////System.out.print(fechasArr[i][0]+"\t"); 
             } 
             */






            HashMap<String, String> params = listasService.getParametrosSistema();
            map.put("fechasArr", fechasArr);

            map.put("clave", programacion.getDependencia().getDepcategoria().getDcClaveP());
            map.put("version", programacion.getDependencia().getDepcategoria().getDcVersionP());
            map.put("fechaFormato", DateUtil.formatDate(programacion.getDependencia().getDepcategoria().getDcFechaP()));
            map.put("consecutivo", programacion.getDependencia().getDepProcedencia() + "425");


            map.put("dependencia", programacion.getDependencia().getDepNombre());
            map.put("depAbrev", programacion.getDependencia().getDepAbreviatura());
            map.put("aeropuerto", programacion.getDependencia().getAeropuerto().getAeNombre());
            map.put("piePagina", "Clave: " + programacion.getDependencia().getDepcategoria().getDcClaveP() + "\nVersión: " + programacion.getDependencia().getDepcategoria().getDcVersionP() + "\nFecha: " + DateUtil.formatDate(programacion.getDependencia().getDepcategoria().getDcFechaP()));



            Funcionario usuario = JsfUtil.getFuncionarioSesion();
            if (programacion.getFuncionarioGenera() == null) {

                map.put("adminNiv4", usuario.getFunNombre() + "<br/>Coordinador Dependencia");
            } else {

                map.put("adminNiv4", programacion.getFuncionarioGenera().getFunNombre());
            }


            if (programacion.getFuncionarioAprueba() == null) {

                map.put("adminNiv3", "Jefe Regional");
            } else {

                map.put("adminNiv3", programacion.getFuncionarioAprueba().getFunNombre());
            }


            map.put("adminNiv2", "Director Regional");

            Calendar inicio_c = Calendar.getInstance();
            inicio_c.setTime(programacion.getProFechaInicio());

            Calendar fin_c = Calendar.getInstance();
            fin_c.setTime(programacion.getProFechaFin());

            String periodo = inicio_c.get(Calendar.DATE) + " de " + Months.getMonth(inicio_c.get(Calendar.MONTH)).getLabel() + " a " + fin_c.get(Calendar.DATE) + " de " + Months.getMonth(fin_c.get(Calendar.MONTH)).getLabel();

            map.put("periodo", periodo);

            lista_reporte.add(JsfUtil.generacionPagina(reportFileName, map, listaProg));


        }

        JsfUtil.salidaReporteXls(lista_reporte, reportFileName);


        return null;
    }

    public void comprobar() {
        columnsComprobar = new ArrayList<ColumnModel>();
        columnsFechaComprobacion = new ArrayList<ColumnModel>();
        dynamicComprobacion = new ArrayList<String[]>();
        comprobacion = tempVerProgramacionService.comprobacion(programacion.getProId());
        fechas = tempVerProgramacionService.fechasProgramacion(programacion.getProId());
        Map<String, String> fechaColumnasDefault = new HashMap<String, String>();
        Map<String, String> fechaColumnas = new HashMap<String, String>();
        fechaColumnasDefault.put("- Posicion", "-");
        for (int i = 0; i < fechas.size(); i++) {
            fechaColumnasDefault.put(fechas.get(i).toString(), "-");
        }
        imprimirColumnas(fechaColumnasDefault);
        String pivote = "";
        int j;
        for (j = 0; j < comprobacion.size(); j++) {
            if (!pivote.equals(comprobacion.get(j).getPjAlias())) {
                if (j != 0) {
                    imprimirComprobacion(fechaColumnas);

                }


                Iterator it = fechaColumnasDefault.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    fechaColumnas.put((String) e.getKey(), (String) e.getValue());
                }


                fechaColumnas.put("- Posicion", comprobacion.get(j).getPjAlias());

                pivote = comprobacion.get(j).getPjAlias();
            }

            String fecha = comprobacion.get(j).toString();
            if (fechaColumnas.get(fecha).equals("-")) {
                fechaColumnas.put(fecha, comprobacion.get(j).getFunAlias() + ",");
            } else {
                String cadena = fechaColumnas.get(fecha);
                fechaColumnas.put(fecha, cadena + " " + comprobacion.get(j).getFunAlias() + ",");
            }


        }
        if (j != 0) {
            imprimirComprobacion(fechaColumnas);

        }



    }

    private String miFormato(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        int mes = c.get(Calendar.MONTH) + 1;
        return c.get(Calendar.YEAR) + "/" + mes + "/" + c.get(Calendar.DAY_OF_MONTH);
    }

    private void imprimirComprobacion(Map<String, String> columnas) {
        Set<String> keys = columnas.keySet();
        String[] funcionarios = new String[columnas.size()];

        int i = 0;
        for (String key : keys) {
            ////System.out.print(columnas.get(key) +"\t"); 
            if (i == 0) {
                funcionarios[i] = columnas.get(key);
            } else {
                funcionarios[i] = columnas.get(key).substring(0, columnas.get(key).length() - 1);
            }


            i++;
            String nomCol = "Ord";
            if (i % 2 == 0) {
                nomCol = "Ext";
            }


        }
        // //System.out.println(funcionarios.length); 
        dynamicComprobacion.add(funcionarios);


    }

    private void imprimirColumnas(Map columnas) {
        Set<String> keys = columnas.keySet();

        for (String key : keys) {
            // //System.out.print(key+"\t"); 
            if (key.equals("- Posicion")) {
                columnsFechaComprobacion.add(new ColumnModel("Posición", "Posición"));
            } else {
                columnsFechaComprobacion.add(new ColumnModel(key, key));
            }


        }
        // //System.out.print("\n");       

    }

    public List<Resumen> getResumenList() {
        return resumenList;
    }

    public void setResumenList(List<Resumen> resumenList) {
        this.resumenList = resumenList;
    }

    public String getProcesoResumen() {
        return procesoResumen;
    }

    public void setProcesoResumen(String procesoResumen) {
        this.procesoResumen = procesoResumen;
    }
}
