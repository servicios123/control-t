/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Dependencia;

import co.gov.aerocivil.controlt.entities.DsTipo;

import co.gov.aerocivil.controlt.entities.DiaFestivo;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Menu;
import co.gov.aerocivil.controlt.entities.Posicion;
import co.gov.aerocivil.controlt.entities.Jornada;

import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Regional;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Stateless
public class ListasServiceBean implements ListasService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public List obtenerLista(Class klass) {
        return em.createNamedQuery(klass.getSimpleName() + ".findAll").getResultList();

    }

    @Override
    public List<Regional> obtenerListaRegionales() {
        List<Regional> lista = new ArrayList<Regional>(em.createNamedQuery("Regional.findAll").getResultList());
        return lista;
    }
    
  

    @Override
    public List<Aeropuerto> obtenerAeropuertosxRegional(Long regional) {

        return em.createNamedQuery("Aeropuerto.findByAeRegional").setParameter("regId", regional).getResultList();

    }

    
     @Override
    public List<PosNoAsig> obtenerPosNoAsigXdependencia(Long dependencia, Long programacion) {
        
        return em.createNamedQuery("PosNoAsigxDependencia.findAll")
                .setParameter("dep", dependencia)
                .setParameter("prog", programacion)
                .getResultList();
        
    }
     
     @Override
    public Programacion obtenerProgramacionXFecha(Date Fecha) {
        try{
        return (Programacion) em.createNamedQuery("Programacion.findByFecha")
                .setParameter("proFecha", Fecha)
                .getSingleResult();
        }catch(NoResultException e){
        return null;
        }
    }
  
    @Override
    public List<DsTipo> obtenerTipoSenal() {
        return em.createNamedQuery("DsTipo.findAll").getResultList();
    }

    @Override
    public List<Posicion> obtenerPosicionXDependencia(Long dependencia) {

        return em.createNamedQuery("Posicion.findByDependencia").setParameter("dep", dependencia).getResultList();

    }

    @Override
    public List<TurnoEspecial> obtenerTurnoEspeciaXDependencia(Long dependencia) {

        return em.createNamedQuery("TurnoEspecial.findByDependencia").setParameter("dep", dependencia).getResultList();

    }
    
    @Override
    public List<TurnoEspecial> obtenerTurnoEspeciaXDependenciaActivo(Long dependencia) {

        return em.createNamedQuery("TurnoEspecial.findByDependenciaAndEstado")
                .setParameter("teEstado", "Activo")
                .setParameter("dep", dependencia).getResultList();

    }

    @Override
    public List<Jornada> obtenerJornadaXDependencia(Long dependencia) {

        return em.createNamedQuery("Jornada.findByDependencia").setParameter("dep", dependencia).getResultList();

    }

    @Override
    public List<Dependencia> obtenerDepenXAerop(Long aeropuerto) {

        return em.createNamedQuery("Dependencia.findByDepAeropuerto").setParameter("depAeropuerto", aeropuerto).getResultList();

    }

    @Override
    public List<Dependencia> obtenerDepenXAeropXDepCat(Long aeropuerto, Long dcId) {
        List<Dependencia> lista = null;
        try {
            lista = em.createNamedQuery("Dependencia.findByDepAeropuertoDepCat").
                    setParameter("depAeropuerto", aeropuerto).
                    setParameter("categoria", dcId).
                    getResultList();
        } catch (NoResultException ne) {
            ne.printStackTrace();
        }
        return lista;

    }

    @Override
    public List<Funcionario> obtenerFuncionariosXPosicion(Long posicionJornada, Long funcId) {


        StringBuilder strQry = new StringBuilder("select f from Funcionario f, PosicionJornada pj, PosicionHabilitada p ");
        strQry.append(" where pj.posicion.posId=p.posicion.posId and pj.pjId= ").append(posicionJornada);
        if (funcId!=null){
            strQry.append(" and f.funId not in ( ").append(funcId).append(") ");
        }
        strQry.append(" and p.funcionario.funId=f.funId order by f.funAlias");
        Query q = em.createQuery(strQry.toString());
        return q.getResultList();

    }

    @Override
    public List<Funcionario> obtenerFuncionariosXPosicionXFecha(Long posicionJornada, Date fecha) {

        Calendar c = Calendar.getInstance();
        
        StringBuilder strQry = new StringBuilder("select f from Funcionario f, PosicionJornada pj, PosicionHabilitada p ");
        strQry.append(" where pj.posicion.posId=p.posicion.posId and pj.pjId= ").append(posicionJornada);
        strQry.append(" and p.phFvencimiento >=:fecha ");
        strQry.append(" and f.funFvCertmedico >=:actual ");
        strQry.append(" and f.funFvCurso >=:actual ");
        strQry.append(" and f.funFvEvaluacion >=:actual ");
        strQry.append(" and p.funcionario.funId=f.funId ");
        strQry.append(" and f.funId not in (select t.funcionario.funId  ");
        strQry.append(" from Turno t ").
        append(" where t.turFecha = :fecha ").
        append(" group by t.funcionario.funId "
                    + "having count(t.funcionario.funId) >= 2) "
                    + "and f.funId not in (select t1.funcionario.funId "
                    + "from Turno t1 "
                    + "where t1.turFecha = :fecha "
                    + "and t1.turHInicio = 0 and t1.turHFin = 23)"
                    + " order by f.funAlias ");
        //System.out.println(strQry.toString() + "-" + fecha);
        Query q = em.createQuery(strQry.toString());
        q.setParameter("fecha", fecha);
        q.setParameter("actual", c.getTime(), TemporalType.DATE);
        return q.getResultList();

    }
    
    @Override
    public List<Funcionario> obtenerFuncionariosXDependencia(Long dependencia) {


        StringBuilder strQry = new StringBuilder("select f from Funcionario f");
        strQry.append(" where f.dependencia.depId= ").append(dependencia);
        strQry.append(" and f.funEstado='Activo' order by f.funAlias");
        Query q = em.createQuery(strQry.toString());
        return q.getResultList();

    }

    @Override
    public Object obtenerObjById(Class klass, Long id) {
        //return em.createNamedQuery(klass.getSimpleName()+".findById").getSingleResult();
        return em.find(klass, id);
    }

    @Override
    public List<Menu> getListaMenu(Long rolId) {
        StringBuilder strQry = new StringBuilder("select MEN_ID, MEN_POSICION, MEN_LABEL, MEN_METODO from menu m, ");
        strQry.append(" menu_rol mr where mr.mr_rol=").append(rolId);
        strQry.append(" and mr.mr_menu=m.men_id order by m.men_posicion ");
        Query q = em.createNativeQuery(strQry.toString(), Menu.class);
        
        return q.getResultList();
    }

    @Override
    public void guardarFestivos(List<Date> festivos, List<Date> diasEliminar, Funcionario f) {
        StringBuilder strQry = new StringBuilder();
        Query query;
        for (Date d : festivos) {
            strQry = new StringBuilder();
            strQry.append("select d from DiaFestivo d where d.dfFecha =:fecha");
            query = em.createQuery(strQry.toString()).setParameter("fecha", d);
            try {
                query.getSingleResult();
            } catch (NoResultException nre) {
                DiaFestivo fest = new DiaFestivo(d);
                fest = (DiaFestivo) auditoria.auditar(fest, f);
            } catch (Exception ex) {
                //System.out.println("Excepción no controlada" + ex.getMessage());
            }
        }
        if (diasEliminar != null && diasEliminar.size() > 0) {
            strQry = new StringBuilder();
            strQry.append("delete from DiaFestivo d where d.dfFecha in :diasEliminar");
            query = em.createQuery(strQry.toString());
            query.setParameter("diasEliminar", diasEliminar);
            query.executeUpdate();
        }
    }

    @Override
    public List<DiaFestivo> getListaFestivos(Date start, Date end, Funcionario f) {
        StringBuilder strQry = new StringBuilder();

        strQry.append("Select * from Dia_Festivo d where (d.df_Fecha between to_date('");
        strQry.append(StringDateUtil.formatDate(start)).append("','dd/mm/yyyy') and to_date('").
                append(StringDateUtil.formatDate(end)).
                append("','dd/mm/yyyy')) and TO_CHAR(df_fecha,'D')=1");

        Query query = em.createNativeQuery(strQry.toString());
        if (query.getResultList() == null || query.getResultList().size() <= 0) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(start);

            int lastDay = end.getDate();
            for (int i = 1; i <= lastDay; i++) {
                c2.set(Calendar.DAY_OF_MONTH, i);
                if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    DiaFestivo d = new DiaFestivo(StringDateUtil.setCeroHoras(c2.getTime()));
                    auditoria.auditar(d, f);
                    i += 6;
                }
            }
        }
        strQry = new StringBuilder();
        strQry.append("Select d from DiaFestivo d where d.dfFecha between :start and :end");
        query = em.createQuery(strQry.toString());
        return query.setParameter("start", start).
                setParameter("end", end).getResultList();
    }

    @Override
    public List<PosicionJornada> obtenerPosicionJornadaXDependencia(Long dependencia) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select pj from PosicionJornada pj where pj.dependencia.depId = :depId and pj.pjEstado = :estado");
        strQry.append(" order by pj.pjAlias");
        Query query = em.createQuery(strQry.toString());
        return query.setParameter("depId", dependencia).setParameter("estado", "Activo").getResultList();
    }

    @Override
    public HashMap<String, String> getParametrosSistema() {
        HashMap<String, String> params = new HashMap<String, String>();
        String[] fields = {"PAR_ID", "PAR_ATRIBUTO", "PAR_VALOR"};
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select ");
        strQry.append(StringDateUtil.join(Arrays.asList(fields)));
        strQry.append(" from Parametro_Sistema ");
        Query query = em.createNativeQuery(strQry.toString());
        List<Object[]> list = query.getResultList();
        for (Object[] obj:list ) {
            params.put(StringDateUtil.getValue(obj[1], String.class), 
                    StringDateUtil.getValue(obj[2], String.class));
        }
        //System.out.println("Tamano\t"+params.size()+"\tTOSTRING \t"+params.toString());
        return params;
    }
    
    @Override
    public Connection getConecction() throws SQLException {
        try {
            
            /*InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource)initialContext.lookup("comp/env/jdbc/aerocivil");*/

            /*Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/aerocivil");*/
            //return dataSource.getConnection();
            
            /*Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/aerocivil");
            return dataSource.getConnection();*/
            //return em.unwrap(dataSource.getConnection().getClass());
            
            //return em.unwrap(java.sql.Connection.class);
            
            
            //String persistenceUnitName=properties.getProperty("persistenceUnitName");
            //Persistence p = em.ge
            EntityManagerFactory factory = em.getEntityManagerFactory();
            EntityManager manager = factory.createEntityManager();
            return manager.unwrap(java.sql.Connection.class);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage(), e);
        }
    }
    
    @Override
    public EntityManager getEntManager() {

            
            /*InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource)initialContext.lookup("comp/env/jdbc/aerocivil");*/

            /*Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/aerocivil");*/
            //return dataSource.getConnection();
            
            return em;
        
    }

    @Override
    public Boolean isDiaFestivo(Date fecha) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("Select d from DiaFestivo d where d.dfFecha =:fecha");
        Query query = em.createQuery(strQry.toString());
        try{
            DiaFestivo df = (DiaFestivo) query.setParameter("fecha", 
                    StringDateUtil.setCeroHoras(fecha)).getSingleResult();
            return df!=null;
        }
        catch(NoResultException nre){
        }
        return Boolean.FALSE;
    }

    @Override
    public TurnoEspecial obtenerTurnoXDepAlias(TurnoEspecial te) {
        Query q = em.createNamedQuery("TurnoEspecial.findByTeSigla");
        q.setParameter("teSigla", te.getTeSigla());
        q.setParameter("depId", te.getDependencia().getDepId());
        te = (TurnoEspecial) q.getSingleResult();
        return te;
    }

}