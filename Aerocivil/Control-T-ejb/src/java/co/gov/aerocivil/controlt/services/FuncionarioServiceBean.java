/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.CursoRecurrente;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.EvaluacionCompetencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.enums.ParametrosEnum;
import co.gov.aerocivil.controlt.util.QueryUtil;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class FuncionarioServiceBean implements FuncionarioService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    @EJB
    private AuditoriaService auditoria;
    @EJB
    private ListasService listasService;
    @EJB
    private EvaluacionCompetenciaService evaluacionService;
    @EJB
    private CursoRecurrenteService cursoRecurrenteService;

    @Override
    public Funcionario guardar(Funcionario funcionario, Funcionario fSession) throws SQLIntegrityConstraintViolationException {
        funcionario.setDependencia(em.find(Dependencia.class, funcionario.getDependencia().getDepId()));
        return (Funcionario) auditoria.auditar(funcionario, fSession);
    }

    @Override
    public Funcionario getFuncionarioById(Funcionario funcionario) {
        try {
            if (funcionario.getFunId() == null && funcionario.getFunAlias() != null
                    && funcionario.getDependencia() != null) {
                funcionario =
                        (Funcionario) em.createNamedQuery("Funcionario.findByFunAliasDep")
                        .setParameter("funAlias", funcionario.getFunAlias().toUpperCase())
                        .setParameter("dep", funcionario.getDependencia().getDepId())
                        .getSingleResult();
            } else if (funcionario.getFunId() != null) {
                funcionario = em.find(Funcionario.class, funcionario.getFunId());
            } else {
                return null;
            }
            em.refresh(funcionario);
        } catch (Exception ex) {
            return null;
        }

        return funcionario;
    }

    @Override
    public Funcionario getPersonaByLoginPassword(String login, String clave) {
        try {
            StringBuilder strQry = new StringBuilder("Select o from Funcionario o ");
            strQry.append("where o.funUsuario =:login and ");
            strQry.append(" o.funEstado = 'Activo' ");
            String paramAuthDB = listasService.getParametrosSistema().get(ParametrosEnum.autenticacion_db.name());
            boolean authDB = paramAuthDB != null && "true".equals(paramAuthDB);
            //System.out.println(":::::paramAuthDB:" + paramAuthDB + " authDB: "  + authDB);

            //NoLDAP Ambiente de pruebas
            if (authDB) {

                strQry.append("and o.funClave =:clave ");
                Query query = em.createQuery(strQry.toString());
                query.setParameter("login", login);
                query.setParameter("clave", clave);
                Funcionario f = (Funcionario) query.getSingleResult();
                f.setIntentosFallidos(0L);
                return em.merge(f);
            } else {
                //LDAP Ambiente de producción Aerocivil
                Query query = em.createQuery(strQry.toString());
                query.setParameter("login", login);
                Funcionario f = (Funcionario) query.getSingleResult();
                if (autenticacionLDAP(login, clave)) {
                    f.setIntentosFallidos(0L);
                    return em.merge(f);
                }
            }
        } catch (NoResultException nre) {
            nre.printStackTrace();
        }
        return null;
    }

    @Override
    public Funcionario bloquearUsuario(String login) {

        StringBuilder strQry = new StringBuilder("Select o from Funcionario o ");
        strQry.append("where o.funUsuario =:login ");
        //strQry.append("o.funEstado = 'Activo' ");
        Query query = em.createQuery(strQry.toString());
        query.setParameter("login", login);

        try {
            Funcionario f = (Funcionario) query.getSingleResult();
            f.setIntentosFallidos(f.getIntentosFallidos() != null ? f.getIntentosFallidos() + 1L : 1);
            if (3L == f.getIntentosFallidos()) {
                f.setFunEstado("Bloqueado");
            }
            //return em.merge(f);
            Funcionario fSess = null;
            return (Funcionario) auditoria.auditar(f, fSess);
        } catch (NoResultException nre) {
            nre.printStackTrace();
        }
        return null;
    }

    private boolean autenticacionLDAP(String usuario, String password) {
        Hashtable<String, String> auth = new Hashtable<String, String>(11);
        //String base = "OU=Usuarios,OU=Bogota,DC=aerocivil,DC=gov,DC=co";		
        HashMap<String, String> params = listasService.getParametrosSistema();

        String[] ldapServers = new String[]{params.get(ParametrosEnum.ldap_server.name()),
            params.get(ParametrosEnum.ldap_server2.name()),
            params.get(ParametrosEnum.ldap_server3.name())};

        for (String ldapURL : ldapServers) {
            String dn = usuario + "@aerocivil.gov.co";
            auth.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            auth.put(Context.PROVIDER_URL, ldapURL);
            auth.put(Context.SECURITY_AUTHENTICATION, "simple");
            auth.put(Context.SECURITY_PRINCIPAL, dn);

            try {
                auth.put(Context.SECURITY_CREDENTIALS, new String(password.getBytes("UTF-8"), "UTF-8"));//"{MD5}"+
                //auth.put(Context.SECURITY_CREDENTIALS, password);//"{MD5}"+
                DirContext authContext = new InitialDirContext(auth);
                //System.out.println("LA AUTENTICACION SE REALIZAO CORRECTAMENTE ANTE EL LDAP!");
                return true;
            } catch (AuthenticationException authEx) {
                //System.out.println("NO SE ENCONTRO ESTOS DATOS!");
                authEx.printStackTrace();
                return false;
            } catch (NamingException namEx) {
                //System.out.println("SUCEDIO ALGO!");
                namEx.printStackTrace();

            } catch (UnsupportedEncodingException uex) {
                uex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Obtiene la lista de funcionarios limitados por pagesize, si first y
     * pageSize son nulos retorna todos los registros
     *
     * @param funcionario objeto con los filtros a aplicar
     * @param first página a retornar
     * @param pageSize tamaño de la página a retornar,
     * @param sortField campo por el cual se ordena
     * @param sortOrder tipo de orden (ASC, DESC)
     * @return
     */
    @Override
    public List<Funcionario> getListaPag(Funcionario funcionario, Integer first, Integer pageSize,
            String sortField, String sortOrder) {

        Query query = createQueryFilter(funcionario, sortField, sortOrder);
        if (first != null && pageSize != null) {
            query.setFirstResult(first).setMaxResults(pageSize);
        }
        try {
            List<Funcionario> result = (List<Funcionario>) query.getResultList();
            for(Funcionario f : result){
                try {
                    EvaluacionCompetencia ev = evaluacionService.consultarEvaluacion(f);
                    CursoRecurrente cr = cursoRecurrenteService.consultarMaxima(f.getFunId());
                    f.setEvaluacionCompetencia(ev);
                    f.setCursoRecurrente(cr);
                } catch (Exception e) {
                    f.setEvaluacionCompetencia(null);
                }
            }
            return result;
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    private Query createQueryFilter(Funcionario funcionario, String sortField, String sortOrder) {
        List<String> condiciones = new ArrayList<String>();

        Map<String, Object> params = new HashMap<String, Object>();

        if (funcionario.getFechaini() != null && funcionario.getFechafin() != null) {

            switch (funcionario.getEvalFecha()) {
                case 1:
                    condiciones.add("(f.funFvCertmedico between :fini and :ffin or f.funFvCertmedico IS NULL )");
                    params.put("fini", funcionario.getFechaini());
                    params.put("ffin", funcionario.getFechafin());
                    break;
                case 2:
                    condiciones.add("(f.funFvCurso between :fini and :ffin or f.funFvCurso IS NULL)");
                    params.put("fini", funcionario.getFechaini());
                    params.put("ffin", funcionario.getFechafin());
                    break;
                case 3:
                    condiciones.add("(f.funFvEvaluacion between :fini and :ffin or f.funFvEvaluacion IS NULL)");
                    params.put("fini", funcionario.getFechaini());
                    params.put("ffin", funcionario.getFechafin());
                    break;
            }

        }

        if (funcionario.getFechaini() != null && funcionario.getFechafin() == null) {

            switch (funcionario.getEvalFecha()) {
                case 1:
                    condiciones.add("(f.funFvCertmedico >= :fini  or f.funFvCertmedico IS NULL ) ");
                    params.put("fini", funcionario.getFechaini());

                    break;
                case 2:
                    condiciones.add("(f.funFvCurso >= :fini  or f.funFvCurso IS NULL )");
                    params.put("fini", funcionario.getFechaini());

                    break;
                case 3:
                    condiciones.add("(f.funFvEvaluacion >= :fini  or f.funFvEvaluacion IS NULL )");
                    params.put("fini", funcionario.getFechaini());

                    break;
            }

        }

        if (funcionario.getFechaini() == null && funcionario.getFechafin() != null) {

            switch (funcionario.getEvalFecha()) {
                case 1:
                    condiciones.add("(f.funFvCertmedico <= :ffin or f.funFvCertmedico IS NULL )");
                    params.put("ffin", funcionario.getFechafin());

                    break;
                case 2:
                    condiciones.add("(f.funFvCurso <= :ffin  or f.funFvCurso IS NULL )");
                    params.put("ffin", funcionario.getFechafin());

                    break;
                case 3:
                    condiciones.add("(f.funFvEvaluacion <= :ffin  or f.funFvEvaluacion IS NULL )");
                    params.put("ffin", funcionario.getFechafin());

                    break;
            }

        }

        if (funcionario.getRoles() != null && funcionario.getRoles().length > 0) {
            condiciones.add(" f.fuNivel in :roles ");
            params.put("roles", Arrays.asList(funcionario.getRoles()));
        }

        if (funcionario.getFunNombre() != null && !"".equals(funcionario.getFunNombre())) {
            condiciones.add("upper(f.funNombre) like :nombre ");
            params.put("nombre", "%" + funcionario.getFunNombre().toUpperCase() + "%");
        }
        if (funcionario.getFunAlias() != null && !"".equals(funcionario.getFunAlias())) {
            condiciones.add("upper(f.funAlias) like :alias ");
            params.put("alias", "%" + funcionario.getFunAlias().toUpperCase() + "%");
        }

        if (funcionario.getFunId() != null && !"".equals(funcionario.getFunId())) {
            condiciones.add("f.funId = :id ");
            params.put("id", funcionario.getFunId());
        }

        if (funcionario.getFunEstado() != null && !"".equals(funcionario.getFunEstado())) {
            condiciones.add("f.funEstado = :estado ");
            params.put("estado", funcionario.getFunEstado());
        }

        if (funcionario.getDependencia() != null
                && funcionario.getDependencia().getDepcategoria() != null
                && funcionario.getDependencia().getDepcategoria().getDcId() != null) {
            condiciones.add("f.dependencia.depcategoria.dcId = :depCat ");
            params.put("depCat", funcionario.getDependencia().getDepcategoria().getDcId());
        }
        if (funcionario.getDependencia() != null && funcionario.getDependencia().getDepId() != null) {
            condiciones.add("f.dependencia.depId = :dep ");
            params.put("dep", funcionario.getDependencia().getDepId());
        }

        if (funcionario.getDependencia().getAeropuerto() != null && funcionario.getDependencia().getAeropuerto().getAeId() != null) {
            condiciones.add("f.dependencia.aeropuerto.aeId = :aero ");
            params.put("aero", funcionario.getDependencia().getAeropuerto().getAeId());
        } else if (funcionario.getDependencia().getAeropuerto().getRegional() != null && funcionario.getDependencia().getAeropuerto().getRegional().getRegId() != null) {
            condiciones.add("f.dependencia.aeropuerto.regional.regId = :reg ");
            params.put("reg", funcionario.getDependencia().getAeropuerto().getRegional().getRegId());
        }

        if (funcionario.getFuNivel() != null) {
            condiciones.add("f.fuNivel = :rol ");
            params.put("rol", funcionario.getFuNivel());
        }

        if (funcionario.getFunEstado() != null) {
            condiciones.add("f.funEstado = :estado ");
            params.put("estado", funcionario.getFunEstado());
        }
        
        if(funcionario.getFunFvCertmedico()!=null){
            condiciones.add("f.funFvCertmedico = :fvCertmedico");
            params.put("fvCertmedico", funcionario.getFunFvCertmedico());
        }
        
        if(funcionario.getFunFvCurso()!=null){
            condiciones.add("f.funFvCurso = :fvCurso");
            params.put("fvCurso", funcionario.getFunFvCurso());
        }
        
        if(funcionario.getFunFvEvaluacion()!=null){
            condiciones.add("f.funFvEvaluacion = :fvEvaluacion");
            params.put("fvEvaluacion", funcionario.getFunFvEvaluacion());
        }

        StringBuilder strQry = new StringBuilder();

        if (condiciones.size() > 0) {
            strQry.append(" where ");
            strQry.append(QueryUtil.joinWithAnd(condiciones));
        }

        Query query = em.createQuery("Select count(f) from Funcionario f  " + strQry.toString());
        QueryUtil.setParameters(query, params);
        count = (Long) query.getSingleResult();

        StringBuilder strQryFinal = new StringBuilder("Select f from Funcionario f ").
                append(strQry.toString());
        if (sortField != null && sortOrder != null) {
            strQryFinal.append(" order by f.").append(sortField).append(" ").append(sortOrder);
        }else if(funcionario.getSortField()!=null && !funcionario.getSortField().equalsIgnoreCase("")){
            strQryFinal.append(" order by f.").append(funcionario.getSortField()).append(" DESC");
        }
        //System.out.println(strQryFinal);
        query = em.createQuery(strQryFinal.toString());
        QueryUtil.setParameters(query, params);

        return query;

    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public List<Funcionario> getFuncionarios600am(Dependencia dependencia, Date d, Long hInicio, Long mInicio, Long hFin, Long mFin) {
        StringBuilder strQry = new StringBuilder();

        //strQry.append("new co.gov.aerocivil.controlt.entities.Funcionario");
        strQry.append("select f.fun_id, f.fun_alias, f.fun_nombre, f.fun_direccion, tr.tra_sector ");
        strQry.append(" from turno t, programacion p, funcionario f ");
        strQry.append(" left join transporte tr on f.fun_id=tr.tra_funcionario ");
        strQry.append(" and tr.tra_fecha = to_date('").append(StringDateUtil.formatDate(d));
        strQry.append("','dd/mm/yyyy') ");
        strQry.append(" where f.fun_id = t.tur_funcionario ");
        strQry.append(" and t.tur_fecha = to_date('").append(StringDateUtil.formatDate(d));
        strQry.append("','dd/mm/yyyy') and t.tur_h_inicio = ").append(hInicio);
        strQry.append(" and t.tur_m_inicio = ").append(mInicio);
        strQry.append("and t.tur_h_fin = ").append(hFin);
        strQry.append(" and t.tur_m_fin = ").append(mFin);
        strQry.append(" and p.pro_id=t.tur_programacion and p.pro_estado=1 ");
        strQry.append(" and t.tur_estado=0 order by f.fun_alias ");
        Query query = em.createNativeQuery(strQry.toString());
        List<Funcionario> funcionarios = new ArrayList<Funcionario>();
        Iterator<Object[]> resultado = query.getResultList().iterator();
        while (resultado.hasNext()) {
            Object[] reg = resultado.next();
            Funcionario f = new Funcionario(
                    reg[0] != null ? ((BigDecimal) reg[0]).longValue() : null,
                    reg[1] != null ? reg[1].toString() : null,
                    reg[2] != null ? reg[2].toString() : null,
                    reg[3] != null ? reg[3].toString() : null,
                    reg[4] != null ? reg[4].toString() : null);
            funcionarios.add(f);
        }
        return funcionarios;
    }

    @Override
    public Funcionario buscarSITAH(Long funId) {

        String[] fields = {"FUN_ID", "FUN_NOMBRE", "FUN_DIRECCION",
            "FUN_TELEFONO", "FUN_CELULAR", "FUN_FU_TRASLADO",
            "FUN_F_INGRESO", "FUN_CORREO_ELECTRONICO", "FUN_CARGO", "FUN_SALARIO"};

        StringBuilder strQry = new StringBuilder("SELECT ")
                .append(StringDateUtil.join(Arrays.asList(fields)));
        strQry.append(" FROM Funcionario_Sitah_view where FUN_ID =").append(funId);
        Query q = em.createNativeQuery(strQry.toString());
        //System.out.println("Sql SITAH: "+strQry.toString());
        q.setMaxResults(1);
        Object[] f;
        /*Query q = em.createNamedQuery("FuncionarioSitah.byFunId");
         FuncionarioSitah f=new FuncionarioSitah();        */
        try {
            f = (Object[]) q.getSingleResult();
            //f = (FuncionarioSitah) q.setParameter("funId", funId).getSingleResult();
        } catch (NonUniqueResultException e) {
            f = (Object[]) q.getResultList().get(0);
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Funcionario f2 = new Funcionario();
        f2.setFunId(StringDateUtil.getValue(f[0], Long.class));
        f2.setFunUsuario(f2.getFunId().toString());
        f2.setFunNombre(StringDateUtil.getValue(f[1], String.class));
        f2.setFunDireccion(StringDateUtil.getValue(f[2], String.class));
        f2.setFunTelefono(StringDateUtil.getValue(f[3], String.class));
        f2.setFunCelular(StringDateUtil.getValue(f[4], String.class));
        f2.setFunFuTraslado(StringDateUtil.getValue(f[5], Date.class));
        f2.setFunFIngreso(StringDateUtil.getValue(f[6], Date.class));
        f2.setFunCorreoElectronico(StringDateUtil.getValue(f[7], String.class));
        f2.setFunCargo(StringDateUtil.getValue(f[8], String.class));
        f2.setFunSueldo(StringDateUtil.getValue(f[9], Long.class));
        
        /*
         * f2.setFunId(StringDateUtil.getValue(1033727226L, Long.class));
        f2.setFunUsuario("1033727226");
        f2.setFunNombre(StringDateUtil.getValue("Juan Camilo Sosa Suarez", String.class));
        f2.setFunDireccion(StringDateUtil.getValue("Cr con Cll", String.class));
        f2.setFunTelefono(StringDateUtil.getValue("7725634", String.class));
        f2.setFunCelular(StringDateUtil.getValue("3005094182", String.class));
        f2.setFunFuTraslado(StringDateUtil.getValue(new Date(), Date.class));
        f2.setFunFIngreso(StringDateUtil.getValue(new Date(), Date.class));
        f2.setFunCorreoElectronico(StringDateUtil.getValue("dark.sosa1304@gmail.com", String.class));
        f2.setFunCargo(StringDateUtil.getValue("vice", String.class));
        f2.setFunSueldo(StringDateUtil.getValue(9000000L, Long.class));
         */
        return f2;

    }

    @Override
    public Funcionario getJefeDependencia(Long depId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" Select f from Funcionario f where f.dependencia.depId =:depId ");
            sb.append(" and f.fuNivel=4 and f.funEstado='Activo' ");
            Query q = em.createQuery(sb.toString());
            q.setParameter("depId", depId);
            q.setMaxResults(1);
            return (Funcionario) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Funcionario getJefeRegional(Long regId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Select f from Funcionario f where ");
        sb.append(" f.dependencia.aeropuerto.regional.regId=:regId ");
        sb.append(" and f.fuNivel=3 and f.funEstado='Activo' ");
        Query q = em.createQuery(sb.toString());
        q.setParameter("regId", regId);
        q.setMaxResults(1);
        //Modificación 19 septiembre se agrega try catch por si la consulta no obtiene el resultado
        try {
            return (Funcionario) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public boolean validarAlias(Funcionario func, Dependencia dep) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Select f from Funcionario f where ");
        sb.append(" f.dependencia.depcategoria.dcId=:dcId ");
        sb.append(" and f.funAlias= :funAlias");
        sb.append(" and f.funId!=:funId");
        Query q = em.createQuery(sb.toString());
        q.setParameter("dcId", dep.getDepcategoria().getDcId());
        q.setParameter("funAlias", func.getFunAlias());
        q.setParameter("funId", func.getFunId());
        q.setMaxResults(1);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return false;//Ya se encuentra el alias asignado
        }
        return true;//puede asignarlo
    }
}
