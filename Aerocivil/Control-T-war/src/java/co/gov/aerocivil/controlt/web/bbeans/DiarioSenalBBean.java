/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.bbeans;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.DiarioSenal;
import co.gov.aerocivil.controlt.entities.DsTipo;
import co.gov.aerocivil.controlt.enums.RolEnum;
import co.gov.aerocivil.controlt.services.AeropuertoService;
import co.gov.aerocivil.controlt.services.DependenciaService;
import co.gov.aerocivil.controlt.services.DiarioSenalService;
import co.gov.aerocivil.controlt.services.DsTipoService;
import co.gov.aerocivil.controlt.services.ListasService;
import co.gov.aerocivil.controlt.web.enums.SortOrderEnum;
import co.gov.aerocivil.controlt.web.enums.ValoresLugarSuceso;
import co.gov.aerocivil.controlt.web.util.DateUtil;
import co.gov.aerocivil.controlt.web.util.JsfUtil;
import co.gov.aerocivil.controlt.web.util.LugarSucesoDto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Viviana
 */
@ManagedBean
@SessionScoped
public class DiarioSenalBBean {

    /**
     * Creates a new instance of DiarioSenalBBean
     *
     */
    @EJB
    private DiarioSenalService diarioSenalService;
    @EJB
    private ListasService listasService;
    @EJB
    private DependenciaService dependenciaService;
    private DiarioSenal diarioSenal;
    private DiarioSenal diarioSenalFiltro;
    private Jornada jornada;
    private List<Aeropuerto> listAeropuerto;
    private List<DsTipo> listTipoSenal;
    @EJB
    private DsTipoService tipoService;
    private List<Dependencia> listDependencia;
    private List<DiarioSenal> lista;
    private String Test;
    private String Test2;
    private String dependencia;
    private String dependenciaFiltro;
    private List<LugarSucesoDto> dependenciaList;
    private boolean show;
    @EJB
    private AeropuertoService aeropuertoService;

    public DiarioSenalBBean() {
        dependencia = null;
    }

    public String listarTodas() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        diarioSenalFiltro = new DiarioSenal();

        Regional regional = new Regional();
        Dependencia dependencia = new Dependencia();
        Aeropuerto aeropuerto = new Aeropuerto();
        DsTipo dsTipo = new DsTipo();

        aeropuerto.setRegional(regional);
        dependencia.setAeropuerto(aeropuerto);


        listAeropuerto = null;
        listDependencia = null;
        listTipoSenal = null;

        diarioSenalFiltro.setDsTipo(dsTipo);


        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A2)) {

            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());

            diarioSenalFiltro.setDependencia(dependencia);
            cargarDependenciaListado();

        }
        if (logbbean.isFuncionarioNivel(RolEnum.NIVEL_A3)) {


            regional.setRegId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getAeropuerto().getRegional().getRegId());
            aeropuerto.setRegional(regional);
            dependencia.setDepcategoria(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria());
            dependencia.setAeropuerto(aeropuerto);
            diarioSenalFiltro.setDependencia(dependencia);
            cargarAeropuertoListado();

        }
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A4, RolEnum.NIVEL_U1, RolEnum.NIVEL_U2})) {

            dependencia.setDepId(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId());
            diarioSenalFiltro.setDependencia(dependencia);


        }
        diarioSenalFiltro.setDependencia(dependencia);
        if (diarioSenalFiltro.getDependencia().getDepcategoria() == null) {
            diarioSenalFiltro.getDependencia().setDepcategoria(new DepCategoria());
        }
        cargarDsTipo();
        cargarDependencias();
        return filtrar();

    }

    public String listar() {

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId() == 306) {
            return diarioespecial();
        } else {
            return listarTodas();
        }
    }

    public String diarioespecial() {
        DiarioSenalEspecialBBean dse = (DiarioSenalEspecialBBean) JsfUtil.getManagedBean(DiarioSenalEspecialBBean.class);
        return dse.listar();
    }

    public String filtrar() {
        /*cargarAeropuerto();
         cargarDependencia();*/
        if (dependenciaFiltro != null) {
            if (dependenciaFiltro.contains("-")) {
                diarioSenalFiltro.setDsenLugarSuceso(dependenciaFiltro.split("-")[0]);
            } else {
                diarioSenalFiltro.setDsenLugarSuceso(dependenciaFiltro);
            }
        } else {
            diarioSenalFiltro.setDsenLugarSuceso(null);
        }
        lista = diarioSenalService.getLista(diarioSenalFiltro);
        return "listarDiarioSenal";
    }

    public String editar() {
        cargarDependencias();
        cargarDsTipo();
        return "editDiarioSenal";
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String crear() {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepId() == 306) {
            DiarioSenalEspecialBBean dse = (DiarioSenalEspecialBBean) JsfUtil.getManagedBean(DiarioSenalEspecialBBean.class);
            return dse.registrar();
        } else {
            cargarDependencias();
            Dependencia dependencia = new Dependencia();
            Regional regional = new Regional();
            Aeropuerto aeropuerto = new Aeropuerto();
            DsTipo dsTipo = new DsTipo();

            diarioSenal = new DiarioSenal();
            diarioSenal.setDsenFechaSuceso(new Date());
            aeropuerto.setRegional(regional);
            dependencia.setAeropuerto(aeropuerto);
            diarioSenal.setDependencia(dependencia);
            diarioSenal.setDsTipo(dsTipo);
            listAeropuerto = null;
            listDependencia = null;
            cargarDsTipo();
            return "editDiarioSenal";
        }
    }

    public void cargarDependencias() {
        dependenciaList = new ArrayList<LugarSucesoDto>();
        Dependencia dep = JsfUtil.getFuncionarioSesion().getDependencia();
        DepCategoria categoria = dep.getDepcategoria();
        Aeropuerto aeropuerto = dep.getAeropuerto();
        Regional regional = aeropuerto.getRegional();
        if (categoria.getDcNombre().equalsIgnoreCase(ValoresLugarSuceso.AIS.toString())) {
            if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.CUNDINAMARCA.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.ELDORADO.toString())))) {
                Aeropuerto alterno = aeropuertoService.findOneById(new Long(ValoresLugarSuceso.FLAMINIO_SUAREZ_CAMACHO.toString()));
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(aeropuerto.getAeNombre());
                opcion.setValue(aeropuerto.getAeNombre());

                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel(alterno.getAeNombre());
                opcion2.setValue(alterno.getAeNombre());

                dependenciaList.add(opcion);
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.CUNDINAMARCA.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.FLAMINIO_SUAREZ_CAMACHO.toString())))) {
                Aeropuerto alterno = aeropuertoService.findOneById(new Long(ValoresLugarSuceso.ELDORADO.toString()));
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(aeropuerto.getAeNombre());
                opcion.setValue(aeropuerto.getAeNombre());

                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel(alterno.getAeNombre());
                opcion2.setValue(alterno.getAeNombre());

                dependenciaList.add(opcion);
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.ANTIOQUIA.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.OLAYA_HERRERA.toString())))) {
                Aeropuerto alterno = aeropuertoService.findOneById(new Long(ValoresLugarSuceso.JOSE_MARIA_CORDOBA.toString()));
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(aeropuerto.getAeNombre());
                opcion.setValue(aeropuerto.getAeNombre());

                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel(alterno.getAeNombre());
                opcion2.setValue(alterno.getAeNombre());

                dependenciaList.add(opcion);
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.ANTIOQUIA.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.JOSE_MARIA_CORDOBA.toString())))) {
                Aeropuerto alterno = aeropuertoService.findOneById(new Long(ValoresLugarSuceso.OLAYA_HERRERA.toString()));
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(aeropuerto.getAeNombre());
                opcion.setValue(aeropuerto.getAeNombre());

                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel(alterno.getAeNombre());
                opcion2.setValue(alterno.getAeNombre());

                dependenciaList.add(opcion);
                dependenciaList.add(opcion2);
                show = true;
            } else {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(aeropuerto.getAeNombre());
                opcion.setValue(aeropuerto.getAeNombre());

                dependenciaList.add(opcion);
                show = true;
            }
        } else if (categoria.getDcNombre().equalsIgnoreCase(ValoresLugarSuceso.ATS.toString())) {
            if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.CUNDINAMARCA.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.ELDORADO.toString())))) {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel("SKBOZTZX");
                opcion.setValue("SKBOZTZX");
                dependenciaList.add(opcion);
                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel("SKEDZGZA");
                opcion2.setValue("SKEDZGZA");
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.ATLANTICO.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.CORTISSOZ.toString())))) {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel("SKBQZTZX");
                opcion.setValue("SKBQZTZX");
                dependenciaList.add(opcion);
                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel("SKECZGZA");
                opcion2.setValue("SKECZGZA");
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.NORTE_SANTANDER.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.CAMILO_DAZA.toString())))) {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel("SKCCZTZX");
                opcion.setValue("SKCCZTZX");
                dependenciaList.add(opcion);
                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel("SKCCZAZX");
                opcion2.setValue("SKCCZAZX");
                dependenciaList.add(opcion2);
                show = true;
            } else if (regional.getRegNombre().equalsIgnoreCase(ValoresLugarSuceso.NORTE_SANTANDER.toString())
                    && (aeropuerto.getAeId().equals(new Long(ValoresLugarSuceso.PALONEGRO.toString())))) {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel("SKBGZTZX");
                opcion.setValue("SKBGZTZX");
                dependenciaList.add(opcion);
                LugarSucesoDto opcion2 = new LugarSucesoDto();
                opcion2.setLabel("SKBGZAZX");
                opcion2.setValue("SKBGZAZX");
                dependenciaList.add(opcion2);
                show = true;
            } else {
                LugarSucesoDto opcion = new LugarSucesoDto();
                opcion.setLabel(dep.getDepAbreviatura());
                opcion.setValue(dep.getDepAbreviatura());

                dependenciaList.add(opcion);
                show = true;
            }

        } else {
            LugarSucesoDto opcion = new LugarSucesoDto();
            opcion.setLabel(aeropuerto.getAeNombre());
            opcion.setValue(aeropuerto.getAeNombre());

            dependenciaList.add(opcion);
            show = true;
        }

    }

    public String guardar() {

        /*LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
         Calendar cf = Calendar.getInstance();
         cf.set(Calendar.HOUR_OF_DAY, diarioSenal.getDsenHoraSuceso());
         cf.set(Calendar.MINUTE, diarioSenal.getDsenMinSuceso());
         Calendar cal = Calendar.getInstance();
         cal.add(Calendar.HOUR_OF_DAY, 5);
            
         diarioSenal.setDsenFechaSuceso(cf.getTime());
         diarioSenal.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
         diarioSenal.setDsenFechaRegistro(cal.getTime());
         diarioSenal.setFuncionario(logbbean.getFuncionarioTO().getFuncionario());
         diarioSenalService.guardar(diarioSenal, 
         JsfUtil.getFuncionarioSesion());
         JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
         //((ListadosBBean)JsfUtil.getListadosBBean()).setListaSectores(null);
         return registrar(); */


        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        Calendar cf = Calendar.getInstance();
        cf.add(Calendar.HOUR_OF_DAY, 5);
        //System.out.println("Test");
        cf.set(Calendar.HOUR_OF_DAY, diarioSenal.getDsenHoraSuceso());
        cf.set(Calendar.MINUTE, diarioSenal.getDsenMinSuceso());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 5);
        if (cf.getTimeInMillis() > cal.getTimeInMillis()) {
            JsfUtil.addWarningMessage("dsenHoraNoAceptada");
            return null;
        } else {
            diarioSenal.setDsenFechaSuceso(cf.getTime());
            diarioSenal.setDependencia(logbbean.getFuncionarioTO().getFuncionario().getDependencia());
            diarioSenal.setDsenFechaRegistro(cal.getTime());
            diarioSenal.setFuncionario(logbbean.getFuncionarioTO().getFuncionario());
            if (dependencia.contains("-")) {
                diarioSenal.setDsenLugarSuceso(dependencia.split("-")[0]);
            } else {
                diarioSenal.setDsenLugarSuceso(dependencia);
            }
            diarioSenalService.guardar(diarioSenal,
                    JsfUtil.getFuncionarioSesion());
            JsfUtil.addSuccessMessage("genericSingleSaveSuccess");
            //((ListadosBBean)JsfUtil.getListadosBBean()).setListaSectores(null);
            dependencia = null;
            return listar();
        }


    }

    public void cargarAeropuerto() {
        cargarAeropuerto(diarioSenal);
    }

    public void cargarAeropuertoListado() {

        cargarAeropuerto(diarioSenalFiltro);
    }

    private void cargarAeropuerto(DiarioSenal f) {
        listDependencia = null;
        listAeropuerto = JsfUtil.getListadosBBean().getListaAeroXRegional(f.getDependencia().getAeropuerto().getRegional().getRegId());
    }

    public void cargarDependencia() {
        cargarDependencia(diarioSenal);
    }

    public void cargarDependenciaListado() {
        cargarDependencia(diarioSenalFiltro);
    }

    private void cargarDependencia(DiarioSenal f) {
        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);
        if (logbbean.isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3})) {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId(),
                    JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria().getDcId());
        } else {
            listDependencia = JsfUtil.getListadosBBean().getListaDepenXAerop(f.getDependencia().getAeropuerto().getAeId());
        }

    }

    public void cargarDsTipo() {
        DsTipo tipoFiltro = new DsTipo();
        tipoFiltro.setCategoria(JsfUtil.getFuncionarioSesion().getDependencia().getDepcategoria());
        listTipoSenal = tipoService.getLista(tipoFiltro);
    }

    public DiarioSenalService getDiarioSenalService() {
        return diarioSenalService;
    }

    public List<DsTipo> getListTipoSenal() {
        return listTipoSenal;
    }

    public void setListTipoSenal(List<DsTipo> listTipoSenal) {
        this.listTipoSenal = listTipoSenal;
    }

    public void setDiarioSenalService(DiarioSenalService diarioSenalService) {
        this.diarioSenalService = diarioSenalService;
    }

    public DiarioSenal getDiarioSenal() {
        return diarioSenal;
    }

    public void setDiarioSenal(DiarioSenal diarioSenal) {
        this.diarioSenal = diarioSenal;
    }

    public DiarioSenal getDiarioSenalFiltro() {
        return diarioSenalFiltro;
    }

    public void setDiarioSenalFiltro(DiarioSenal diarioSenalFiltro) {
        this.diarioSenalFiltro = diarioSenalFiltro;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public List<Aeropuerto> getListAeropuerto() {
        return listAeropuerto;
    }

    public void setListAeropuerto(List<Aeropuerto> listAeropuerto) {
        this.listAeropuerto = listAeropuerto;
    }

    public List<Dependencia> getListDependencia() {
        return listDependencia;
    }

    public void setListDependencia(List<Dependencia> listDependencia) {
        this.listDependencia = listDependencia;
    }

    public List<DiarioSenal> getLista() {
        return lista;
    }

    public void setLista(List<DiarioSenal> lista) {
        this.lista = lista;
    }

    public List<DsTipo> getListDsTipo() {
        return listTipoSenal;
    }

    public void setListDsTipo(List<DsTipo> listTipoSenal) {
        this.listTipoSenal = listTipoSenal;
    }
    private List<DiarioSenal> listaDiarios;

    /**
     * Genera el pdf para el informe
     *
     * @param aux = Lista de objetos que mostraremos en el informe
     * @param map = Resto de parametros que viajan al informe
     * @throws JRException
     */
    public String exportPDF() {
        if (dependenciaFiltro != null) {
            if (dependenciaFiltro.contains("-")) {
                diarioSenalFiltro.setDsenLugarSuceso(dependenciaFiltro.split("-")[0]);
            } else {
                diarioSenalFiltro.setDsenLugarSuceso(dependenciaFiltro);
            }
        } else {
            diarioSenalFiltro.setDsenLugarSuceso(null);
        }

        LoginBBean logbbean = (LoginBBean) JsfUtil.getManagedBean(LoginBBean.class);

        HashMap<String, String> params = listasService.getParametrosSistema();
        listaDiarios = diarioSenalService.getLista(diarioSenalFiltro, null, null, "dsenFechaSuceso", SortOrderEnum.ASC.getOrder());

        List<DiarioSenal> lista2 = diarioSenalService.getLista(diarioSenalFiltro, null, null, "dsenFechaSuceso", SortOrderEnum.ASC.getOrder());
        HashMap<String, Object> map = new HashMap();

        if (diarioSenalFiltro.getDependencia() != null && diarioSenalFiltro.getDependencia().getDepId() != null) {
            Dependencia dep = (Dependencia) listasService.obtenerObjById(Dependencia.class, diarioSenalFiltro.getDependencia().getDepId());
            map.put("depAbrev", dep.getDepAbreviatura());
            map.put("depId", diarioSenalFiltro.getDependencia().getDepId());
        }
        Calendar fecha = Calendar.getInstance();
        map.put("fecha", fecha.getTime());

        map.put("claveFormato", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcClaveDs());
        map.put("versFormato", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcVersionDs());
        map.put("fechaFormato", DateUtil.formatDate(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcFechaDs()));
        map.put("piePagina", "Clave: " + logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcClaveDs() + "\nVersión: " + logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcVersionDs() + "\nFecha: " + DateUtil.formatDate(logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepcategoria().getDcFechaDs()));
        map.put("consecutivo", logbbean.getFuncionarioTO().getFuncionario().getDependencia().getDepProcedencia() + "147");
        map.put("lugar", diarioSenalFiltro.getDsenLugarSuceso());



        ArrayList<JasperPrint> lista_jasper = new ArrayList<JasperPrint>();
        lista_jasper.add(JsfUtil.generacionPagina("DiarioSenales", map, lista2));
        JsfUtil.salidaReporte(lista_jasper, "DiarioSenales");

        return null;
    }

    public boolean isCrearHabilitado() {
        return ((LoginBBean) JsfUtil.getManagedBean(LoginBBean.class)).isFuncionarioEnNivel(new RolEnum[]{RolEnum.NIVEL_A2, RolEnum.NIVEL_A3, RolEnum.NIVEL_U1});
    }

    /**
     * @return the Test
     */
    public String getTest() {
        Test = JsfUtil.getReportPath("DiarioSenales");
        //System.out.println("Test\t" + Test);
        return Test;
    }

    /**
     * @param Test the Test to set
     */
    public void setTest(String Test) {
        this.Test = Test;
    }

    /**
     * @return the Test2
     */
    public String getTest2() {
        Test2 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\jasper\\DiarioSenales.jasper");
        if (Test2 == null) {
            Test2 = System.getProperty("user.home");
            Test2 += " (M2)";
        } else {
            Test2 += " (M1)";
        }

        return Test2;
    }

    /**
     * @param Test2 the Test2 to set
     */
    public void setTest2(String Test2) {
        this.Test2 = Test2;
    }

    public List<LugarSucesoDto> getDependenciaList() {
        return dependenciaList;
    }

    public void setDependenciaList(List<LugarSucesoDto> dependenciaList) {
        this.dependenciaList = dependenciaList;
    }

    public boolean isShow() {
        return show;
    }

    public String getDependenciaFiltro() {
        return dependenciaFiltro;
    }

    public void setDependenciaFiltro(String dependenciaFiltro) {
        this.dependenciaFiltro = dependenciaFiltro;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
