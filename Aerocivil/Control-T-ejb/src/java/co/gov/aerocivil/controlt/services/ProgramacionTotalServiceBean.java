package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DetSecuencia;
import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaRestriccion;
import co.gov.aerocivil.controlt.entities.ParametroSistema;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.entities.PosicionInactiva;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.ProcesoProgramacion;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import co.gov.aerocivil.models.Day;
import co.gov.aerocivil.models.Functionary;
import co.gov.aerocivil.models.Period;
import co.gov.aerocivil.models.Position;
import co.gov.aerocivil.models.Setting;
import co.gov.aerocivil.models.Turn;
import co.gov.aerocivil.models.Week;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Stateless
public class ProgramacionTotalServiceBean
        implements ProgramacionTotalService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private ArrayList<Day> days;
    private ArrayList<Functionary> functionaries;
    private List<PosicionHabilitada> enabledPositions;
    private List<JornadaRestriccion> restrictivePeriod;
    private Setting setting;
    private Programacion programming;
    private List<DetSecuencia> sequences;
    private Day lastDay;
    private ArrayList<String> log;
    private ArrayList<String> data;
    private Boolean debug;
    private List<Jornada> jornadas;
    private ArrayList<Week> weeks;
    private List<DiaFestivo> holydays;
    private List<ProcesoProgramacion> procesoProgramacionList;
    private Funcionario funcionario;
    private List<Jornada> jornadasDependencia;
    private HashMap<Long, List<Long>> jornadasNoLaboralesPorUsuario;
    @EJB
    private ListasService listasService;
    @EJB
    private JornadaNoLaboralService jornadaNoLaboralService;
    @EJB
    private AuditoriaService auditoriaService;
    private Map<Long, List<Long>> enabledPositionsFunctionaryMap;
    private HashMap<Long, List<Week>> pendingTrops;

    public class TargetDay {

        public static final int PREVIOUS = -1;
        public static final int CURRENT = 0;
        public static final int NEXT = 1;
        public static final int PREVIOUS_2_DAYS = -2;
    }

    public class Type {

        public static final int ANY = -1;
        public static final int ORDINARY = 1;
        public static final int EXTRAORDINARY = 2;
        public static final int SPECIAL = 3;
        public static final int REST = 5;
        public static final int LD = 4;
        public static final int TROP = 8;
    }

    @Override
    public String validateTurn(Turno turno, Long cambio) {
        //System.out.println("Llegar a validateTurn ");
        String result = null;
        this.programming = turno.getProgramacion();
        List<Turno> turnos = getTurnosFunPro(turno.getFuncionario().getFunId().longValue(), turno.getProgramacion().getProId().longValue());

        this.log = new ArrayList();
        this.days = new ArrayList();

        Calendar current = Calendar.getInstance();
        current.setTime(this.programming.getProFechaInicio());
        while (current.getTime().compareTo(this.programming.getProFechaFin()) <= 0) {
            this.days.add(new Day(current.getTime()));
            current.add(5, 1);
        }
        Functionary fun = new Functionary(turno.getFuncionario());
        fun.setAvailable(Boolean.valueOf(true));
        for (Day day : this.days) {
            ArrayList<Turn> turns = new ArrayList();
            for (Turno turno1 : turnos) {
                if (turno1.getTurFecha().compareTo(day.getDate()) == 0) {
                    if (cambio == null) {
                        turns.add(convertTurno2Turn(turno1, fun));
                    } else if (cambio != turno1.getTurId()) {
                        turns.add(convertTurno2Turn(turno1, fun));
                    }
                }
            }
            if (turno.getTurFecha().compareTo(day.getDate()) == 0) {
                Turn turn = convertTurno2Turn(turno, fun);
                turn.setFunctionary(null);
                turns.add(turn);
            }
            day.setTurns(turns);
        }
        this.debug = Boolean.valueOf(true);
        this.enabledPositions = getEnabledPositions();
        this.restrictivePeriod = getRestrictivePeriod();
        this.sequences = getSequences();
        this.holydays = getDiaFestivo();
        this.jornadasNoLaboralesPorUsuario = this.jornadaNoLaboralService.getJornadasNoLaborales();

        prepareSetting();

        this.weeks = new ArrayList();
        Week week = new Week();
        for (Day day : this.days) {
            current.setTime(day.getDate());
            if ((day.getDate().compareTo(this.programming.getProFechaInicio()) != 0) && (current.get(7) == 1) && (!isHolyDay(day.getDate()).booleanValue())) {
                this.weeks.add(week);
                week = new Week();
            }
            week.addDay(day);
        }
        if (week.getDays().size() > 0) {
            this.weeks.add(week);
        }
        int tipo = 0;
        for (Day day : this.days) {
            if (day.getDate().compareTo(turno.getTurFecha()) == 0) {
                for (Turn turn : day.getTurns()) {
                    if (turn.getFunctionary() == null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(day.getDate());

                        Turn ordinary = getTurnOfFunctionary(fun, c.getTime(), 0, -1);
                        if (ordinary == null) {
                            validateOrdinaryTurn(fun, turn, day.getDate());
                            tipo = 1;
                        } else if ((turn.getPeriod().getStart() < ordinary.getPeriod().getEnd()) && (ordinary.getPeriod().getTotal() + turn.getPeriod().getTotal() < 24)) {

                            turn.setFunctionary(fun);
                            validateExtraordinaryTurn(turn, ordinary, day.getDate(), ordinary.getFunctionary().getMaxHoursExtra(), true);
                            tipo = 2;
                        } else {
                            validateExtraordinaryTurn(ordinary, turn, day.getDate(), ordinary.getFunctionary().getMaxHoursExtra(), true);
                            tipo = 2;
                        }
                    }
                }
            }
        }
        if (this.log.size() > 0) {
            result = (String) this.log.get(0);
            result += tipo + ";";
        }
        return result;
    }

    private Turn convertTurno2Turn(Turno turno1, Functionary fun) {
        try {
            if ((turno1.getTurTipo().longValue() == 1L) || (turno1.getTurTipo().longValue() == 2L)) {
                PosicionJornada pj = getPositionPeriod(turno1.getTurPosicionJornada());

                Position pos = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                Period per = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                if (pj.getJornada().getJornadaObligatoria() != null) {
                    per.setRequiredPeriod(pj.getJornada().getJornadaObligatoria().getJoId().longValue());
                }
                Turn turn = new Turn(per, pos, fun, turno1.getTurPosicionJornada().longValue());
                if (turno1.getTurTipo().longValue() == 1L) {
                    turn.setType(1);
                } else {
                    turn.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                }
                return turn;
            }
            if (turno1.getTurTipo().longValue() == 3L) {
                TurnoEspFuncionario te = getSpecialTurn(turno1.getTurPosicionJornada());
                //System.out.println("turno id\t" + turno1.getTurId() + "\tpj\t" + turno1.getTurPosicionJornada());
                if (te == null) {
                    //System.out.println("fue nulo :( ");
                } else {
                    Position pos = new Position(0L, te.getTurnoEspecial().getTeSigla());
                    Period per = new Period(0L, "", te.getTurnoEspecial().getTeHinicio().byteValue(), te.getTurnoEspecial().getTeHfin().byteValue() + 1);
                    Turn turn = new Turn(per, pos, fun, turno1.getTurPosicionJornada().longValue());
                    turn.setPermiteHorasExtra(false);
                    turn.setType(ProgramacionTotalServiceBean.Type.SPECIAL);
                    return turn;
                }
            }
            Period period = new Period(0L, "", 0, 24);
            Position position = new Position(0L, "DES");
            Turn turn = new Turn(period, position, fun, 0L);
            turn.setType(ProgramacionTotalServiceBean.Type.REST);
            turn.setPermiteHorasExtra(false);
            return turn;
        } catch (Exception e) {
            Period period = new Period(0L, "", 0, 24);
            Position position = new Position(0L, "DES");
            Turn turn = new Turn(period, position, fun, 0L);
            turn.setType(ProgramacionTotalServiceBean.Type.REST);
            turn.setPermiteHorasExtra(false);
            return turn;
        }
    }

    public void run(Programacion programacion, Funcionario fun, Boolean debug) {
        this.funcionario = fun;
        this.procesoProgramacionList = new ArrayList<ProcesoProgramacion>();
        init(programacion, fun, debug);
        programme();
        save();
        finish();
    }

    private void init(Programacion programacion, Funcionario fun, Boolean debug) {
        this.log = new ArrayList();
        this.data = new ArrayList();

        this.debug = debug;

        this.programming = programacion;
        this.days = new ArrayList();
        this.functionaries = new ArrayList();
        this.enabledPositions = getEnabledPositions();

        this.lastDay = getLastDay();
        this.holydays = getDiaFestivo();
        this.restrictivePeriod = getRestrictivePeriod();
        this.sequences = getSequences();

        this.jornadasDependencia = listasService.obtenerJornadaXDependencia(this.programming.getDependencia().getDepId());

        Calendar c = Calendar.getInstance();
        c.setTime(this.programming.getProFechaFin());

        Calendar a = Calendar.getInstance();

        write_data("Dependencia;Mes;A?o;Func;Fecha;");
        write_data(this.programming.getDependencia().getDepAbreviatura() + ";" + (c.get(2) + 1) + ";" + c.get(1) + ";" + fun.getFunNombre() + ";" + a.get(5) + "/" + (a.get(2) + 1) + "/" + a.get(1) + " " + a.get(10) + ":" + a.get(12));
        write_data("");

        prepareSetting();
        prepareDays();
        prepareFunctionaries();
        this.jornadasNoLaboralesPorUsuario = this.jornadaNoLaboralService.getJornadasNoLaborales();
        enabledPositionsFunctionaryMap = getEnabledPositionsFunctionaryMap(this.programming.getDependencia().getDepId());

        write("Tipo;Fecha;Funcionario;Ordinario;ExtraOrdinario;Resultado;Detalles;");
    }

    private void prepareDays() {
        List<PosicionJornada> positionPeriods = getPositionPeriods();

        Calendar current = Calendar.getInstance();
        current.setTime(this.programming.getProFechaInicio());

        Boolean print = Boolean.valueOf(true);
        while (current.getTime().compareTo(this.programming.getProFechaFin()) <= 0) {
            ArrayList<Turn> turns = new ArrayList();
            for (PosicionJornada pj : positionPeriods) {
                Period period = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                if (pj.getJornada().getJornadaObligatoria() != null) {
                    period.setRequiredPeriod(pj.getJornada().getJornadaObligatoria().getJoId().longValue());
                }
                Position position = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                Turn turn = new Turn(period, position, null, pj.getPjId().longValue());
                turn.setNumEnables(numEnables(pj.getPosicion().getPosId().longValue()));

                turns.add(turn);
            }
            orderTurns(turns);
            if (print.booleanValue()) {
                write_data("Posiciones Habilitadas");
                write_data("Posici?n;N. Funcionarios;");
                long pos = 0L;
                for (Turn t : turns) {
                    if (pos != t.getPosition().getId()) {
                        write_data(t.getPosition().getAlias() + ";" + t.getNumEnables());
                        pos = t.getPosition().getId();
                    }
                }
                write_data("");
                write_data("");

                print = Boolean.valueOf(false);
            }
            this.days.add(new Day(current.getTime(), turns));
            current.add(5, 1);
        }
        this.weeks = new ArrayList();
        Week week = new Week();
        int holidays = 0;
        for (Day day : this.days) {
            current.setTime(day.getDate());
            Calendar calSunday = Calendar.getInstance();
            calSunday.setTime(day.getDate());
            if (isHolyDay(day.getDate()) || calSunday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                holidays++;
            }
            if ((day.getDate().compareTo(this.programming.getProFechaInicio()) != 0) && (current.get(7) == 1)) {
                week.setTotalDays(7 - holidays);
                this.weeks.add(week);
                week = new Week();
                holidays = 0;
            }
            week.addDay(day);
        }
        if (week.getDays().size() > 0) {
            if (holidays == 0) {
                holidays++;
            }
            week.setTotalDays(7 - holidays);
            this.weeks.add(week);
        }
    }

    private void prepareSetting() {
        this.setting = new Setting();
        List<RestriccionDependencia> restricciones = getRestrictions();
        for (RestriccionDependencia rd : restricciones) {
            if (rd.getRdValor() != null) {
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 4L) {
                    this.setting.setPeriodId(rd.getRdValor().longValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 10L) {
                    boolean max = false;
                    if (rd.getRdValor().longValue() > 0L) {
                        max = true;
                    }
                    this.setting.setMaxHoursExtra(max);
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 5L) {
                    this.setting.setTimeRecess(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 1L) {
                    this.setting.setMaxWorkedHoursByDay(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 2L) {
                    this.setting.setMaxContinuosDaysExtra(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 7L) {
                    this.setting.setMaxContinuosPeriod(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 9L) {
                    this.setting.setMaxContinuosDaysExtraModification(rd.getRdValor().intValue());
                }
            }
        }
        this.jornadas = getJornadas();
    }

    private void prepareFunctionaries() {
        List<Funcionario> funcionarios = getFunctionaries();
        for (Funcionario fun : funcionarios) {
            this.functionaries.add(new Functionary(fun));
        }
        for (Functionary fun : this.functionaries) {
            int count = 0;
            for (PosicionHabilitada ph : this.enabledPositions) {
                if (ph.getFuncionario().getFunId().longValue() == fun.getId()) {
                    count++;
                }
            }
            fun.setNumHabilities(count);
        }
        List<Turno> rest = getRestLastWeek();
        if (rest != null) {
            for (Functionary fun : this.functionaries) {
                for (Turno r : rest) {
                    if (r.getFuncionario().getFunId().longValue() == fun.getId()) {
                        //System.out.println(fun.getAlias() + "\tDescanso la ultima semana\t");
                        fun.setRestedLastWeek(Boolean.valueOf(true));
                        break;
                    }
                }
            }
        }
        orderFunctionaries();
    }

    private ProcesoProgramacion getProcesoProgramacion(String proceso, Date inicio, Date fin) {
        ProcesoProgramacion procesoProgramacion = new ProcesoProgramacion();
        procesoProgramacion.setProgramacion(this.programming);
        procesoProgramacion.setProceso(proceso);
        procesoProgramacion.setInicio(inicio);
        procesoProgramacion.setFin(fin);
        return procesoProgramacion;

    }

    private void programme() {
        ParametroSistema sec = getSystemParameter(new BigDecimal(100));
        boolean excludeMorning = false;
        Jornada morning = getJornadaM();
        if (sec == null) {
            write_data("\nSecuencia Por Defecto\n1.Especiales\n2.Empalme mes anterior\n3.Top 5 posiciones con menor funcionarios habilitados\n4.Jornadas con jornada anterior obligatoria\n5.Descansos\n6.Turnos ordinarios ordenados por jornada ASC\n7.Turnos ordinarios full\n8.Turnos extraordinarios\n\n");
            solveSpecialTurns();

            solveFirstDayAgainstLastMonth();

            solvePeriodsRecessAndRequired();
            solveRestByWeekPeriodRecess();
            /*if (funcionario.getDependencia() != null && funcionario.getDependencia().getDepcategoria() != null && funcionario.getDependencia().getDepcategoria().getDcId() != null && funcionario.getDependencia().getDepcategoria().getDcId() == 1L) {
             solveTropByWeekPeriodRecess();
             }*/
            solveDescByWeekPeriodRecessFull();

//            solveTop(10);
            for (Jornada jornada : this.jornadas) {
                solveOrdinaryTurnsByPeriod(jornada.getJoId().longValue());
            }
            solveOrdinaryTurnsComplete();
            //solveOrdinaryTurnsAvaible();
            solveExtraordinaryTurnsDistribuied(Boolean.valueOf(false));

        } else {
            String[] order = sec.getParValor().split(",");
            String print_sec = "\nSecuencia Definida " + sec.getParValor() + "\n";
            for (String num : order) {
                if (num.equals("1")) {
                    print_sec = print_sec + "1.Especiales\n";
                    Date inicio = new Date();
                    System.out.println("[PROCESO] Especiales Inicio: " + new SimpleDateFormat("HH:mm:ss aa").format(inicio));
                    solveSpecialTurns();
                    this.auditoriaService.auditar(getProcesoProgramacion("Especiales", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Especiales Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("2")) {
                    print_sec = print_sec + "2.Empalme mes anterior\n";
                    Date inicio = new Date();
                    System.out.println("[PROCESO] Empalme mes anterior Inicio: " + new SimpleDateFormat("HH:mm:ss aa").format(inicio));
                    solveFirstDayAgainstLastMonth();
                    this.auditoriaService.auditar(getProcesoProgramacion("Empalme mes anterior", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Empalme mes anterior Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("3")) {
                    Date inicio = new Date();
                    print_sec = print_sec + "3.Top 5 posiciones con menor funcionarios habilitados\n";
//                    solveTop(5);
                    this.auditoriaService.auditar(getProcesoProgramacion("Top 5 posiciones con menor funcionarios habilitados", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Top 5 Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("4")) {
                    Date inicio = new Date();
                    print_sec = print_sec + "4.Jornadas con jornada anterior obligatoria\n";
                    solvePeriodsRecessAndRequired();
                    if (morning != null && this.programming.getDependencia().getDepId() != 290) {
                        solveOrdinaryTurnsOnlyMorning(morning.getJoId().longValue());
                        excludeMorning = !excludeMorning;
                    }
                    this.auditoriaService.auditar(getProcesoProgramacion("Jornadas con jornada anterior obligatoria", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Jornada anerior obl Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("5")) {
                    Date inicio = new Date();
                    print_sec = print_sec + "5.Descansos\n";

                    solveRestByWeekPeriodRecess();

                    solveDescByWeekPeriodRecessFull();
                    this.auditoriaService.auditar(getProcesoProgramacion("Descansos", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Descansos Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("6")) {
                    print_sec = print_sec + "6.Turnos ordinarios ordenados por jornada ASC\n";
                    Date inicio = new Date();

                    for (Jornada jornada : this.jornadas) {
                        if (excludeMorning && !jornada.getJoAlias().equalsIgnoreCase("M")) {
                            solveOrdinaryTurnsByPeriod(jornada.getJoId().longValue());
                        }
                    }
                    this.auditoriaService.auditar(getProcesoProgramacion("Turnos ordinarios ordenados por jornada ASC", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] oridnarios Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("7")) {
                    print_sec = print_sec + "7.Turnos ordinarios full\n";
                    Date inicio = new Date();
                    solveOrdinaryTurnsComplete();
                    //solveOrdinaryTurnsAvaible();
                    this.auditoriaService.auditar(getProcesoProgramacion("Turnos ordinarios full", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Ordinarios Full Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
                if (num.equals("8")) {
                    print_sec = print_sec + "8.Turnos extraordinarios\n";
                    Date inicio = new Date();
                    solveExtraordinaryTurnsDistribuied(Boolean.valueOf(false));
                    this.auditoriaService.auditar(getProcesoProgramacion("Turnos extraordinarios", inicio, new Date()), funcionario);
                    System.out.println("[PROCESO] Extras Fin: " + new SimpleDateFormat("HH:mm:ss aa").format(new Date()));
                }
            }
            write_data(print_sec + "\n");
        }
        Date inicio = new Date();
        refiningByDay();
        refiningSequence();
        this.auditoriaService.auditar(getProcesoProgramacion("Refinamiento", inicio, new Date()), funcionario);
//        solveExtraordinaryTurnsDistribuied(Boolean.valueOf(false));
        debug_missed();
    }

    private void refiningSequence() {

        Calendar c = Calendar.getInstance();
        for (Day day : days) {
            c.setTime(day.getDate());
            ArrayList<Turn> todays = new ArrayList<Turn>();
            ArrayList<Turn> others = new ArrayList<Turn>();

            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() != null && turn.getType() == ProgramacionTotalServiceBean.Type.ORDINARY) {
                    Turn previous = getTurnOfFunctionary(turn.getFunctionary(), day.getDate(), ProgramacionTotalServiceBean.TargetDay.PREVIOUS, ProgramacionTotalServiceBean.Type.ORDINARY);

                    if (previous != null && previous.getPeriod().getId() == turn.getPeriod().getId()) {
                        todays.add(turn);

                    } else {
                        if (turn.getType() == ProgramacionTotalServiceBean.Type.ORDINARY) {
                            others.add(turn);
                        }
                    }
                }
            }

            //System.out.println("Cambios " + c.get(Calendar.DATE) + "/" + (c.get(Calendar.MONTH) + 1));
            //System.out.println("\n\tRepetidos\n");
            for (Turn today : todays) {
                //System.out.println("\t\t" + today.getFunctionary().getAlias() + "\t" + today.getPeriod().getAlias() + today.getPosition().getAlias());
            }

            //System.out.println("\n\tNoRepetidos\n");
            for (Turn other : others) {
                //System.out.println("\t\t" + other.getFunctionary().getAlias() + "\t" + other.getPeriod().getAlias() + other.getPosition().getAlias());
            }

            setFunctionariesAvailable();

            for (Turn today : todays) {
                if (today.getFunctionary().isAvailable()) {
                    for (Turn other : others) {
                        if (other.getFunctionary().isAvailable()) {
                            if (other.getPeriod().getId() != today.getPeriod().getId()) {
                                if (validateAllTurn(today, other.getFunctionary(), day.getDate(), ProgramacionTotalServiceBean.Type.ORDINARY)) {
                                    Functionary aux = other.getFunctionary();
                                    other.setFunctionary(today.getFunctionary());
                                    today.setFunctionary(aux);

                                    today.getFunctionary().setAvailable(false);
                                    other.getFunctionary().setAvailable(false);

                                }
                            }
                        }

                    }
                }
            }

        }

    }

    private boolean validateAllTurn(Turn turn, Functionary fun, Date date, int type) {

        Functionary actual = turn.getFunctionary();
        turn.setFunctionary(fun);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(date);
        tomorrow.add(Calendar.DATE, 1);

        boolean result = false;

        if (turn.getPeriod().getId() == -1) {
            if (fun != null) {

                Turn next = getTurnOfFunctionary(fun, date, ProgramacionTotalServiceBean.TargetDay.NEXT, ProgramacionTotalServiceBean.Type.ORDINARY);
                Turn other = null;

                if (type == ProgramacionTotalServiceBean.Type.ORDINARY) {
                    other = getTurnOfFunctionary(fun, date, ProgramacionTotalServiceBean.TargetDay.CURRENT, ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                }
                if (type == ProgramacionTotalServiceBean.Type.EXTRAORDINARY) {
                    other = getTurnOfFunctionary(fun, date, ProgramacionTotalServiceBean.TargetDay.CURRENT, ProgramacionTotalServiceBean.Type.ORDINARY);
                }

                if (next == null || (next != null && next.getPeriod().getRequiredPeriod() == -1)) {
                    if (type == ProgramacionTotalServiceBean.Type.ORDINARY) {
                        if (validateOrdinaryTurn(fun, turn, date)) {

                            if (other == null || (other != null && validateExtraordinaryTurn(turn, other, date, fun.getMaxHoursExtra(), false))) {
                                result = true;
                            }
                        }
                    } else {
                        if (other != null && validateExtraordinaryTurn(other, turn, date, fun.getMaxHoursExtra(), false)) {
                            result = true;
                        }
                    }
                }

            }
        }
        turn.setFunctionary(actual);

        return result;
    }

    private void solveTop(int top) {
        List<Functionary> funs = functionaries;
        if (top * 2 < functionaries.size()) {
            funs = functionaries.subList(0, top * 2);
        }
        Calendar c = Calendar.getInstance();

        for (Functionary f : funs) {
            //System.out.println("Funcionario entre funs\t" + f.getAlias() + "\t" + f.getCountRestSunday());
        }

        setFunctionariesAvailable();
        for (Day day : days) {
            setFunctionariesBusy(day.getTurns());
            c.setTime(day.getDate());
            //System.out.print("\n\nDia: " + c.get(Calendar.DATE) + "\t");
            int i = 0;
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() == null && i <= top) {
                    Collections.shuffle(funs);
                    for (Functionary fun : funs) {
                        Turn ayer = getTurnOfFunctionary(fun, day.getDate(), ProgramacionTotalServiceBean.TargetDay.PREVIOUS, ProgramacionTotalServiceBean.Type.ORDINARY);
                        Turn antier = getTurnOfFunctionary(fun, day.getDate(), ProgramacionTotalServiceBean.TargetDay.PREVIOUS_2_DAYS, ProgramacionTotalServiceBean.Type.ORDINARY);

                        if (!(ayer != null && antier != null && ayer.getPosition().getId() == antier.getPosition().getId() && ayer.getPeriod().getId() == antier.getPeriod().getId())) {
                            if (this.solvingOrdinaryTurn(fun, turn, day.getDate())) {
                                i++;
                                //System.out.print("[" + fun.getAlias() + " -> " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + "]\t");
                                break;
                            }
                        }

                    }

                }
            }
            setFunctionariesAvailable();
        }

        orderFunctionaries();
    }

    private void solvePeriodsRecessAndRequired() {
        for (Jornada jornada : this.jornadas) {
            if (jornada.getJornadaObligatoria() != null) {
                long required = jornada.getJornadaObligatoria().getJoId().longValue();
                long period = jornada.getJoId().longValue();
                if (jornada.getJoAlias().contains("A")) {
                    solvePeriodRequired(period, required);
                } else {
                    solvePeriodRequiredAditionalRestrictions(period, required);
                }
            }
        }
        orderFunctionaries();
    }

    private void solveSpecialTurns() {
        List<TurnoEspFuncionario> tes = getSpecialTurns();
        for (TurnoEspFuncionario te : tes) {
            Calendar start = Calendar.getInstance();
            start.setTime(te.getTefFini());
            start = StringDateUtil.setCeroHoras(start);
            while (start.getTime().compareTo(te.getTefFfin()) <= 0) {
                if (start.getTime().compareTo(this.programming.getProFechaFin()) > 0) {
                    break;
                }
                for (Day day : this.days) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(day.getDate());
                    if (start.getTime().compareTo(day.getDate()) == 0) {
                        Period period = new Period(0L, "", te.getTurnoEspecial().getTeHinicio().byteValue(), te.getTurnoEspecial().getTeHfin().byteValue() + 1);
                        Position position = new Position(0L, te.getTurnoEspecial().getTeSigla());
                        Functionary func = new Functionary(te.getFuncionario());
                        Turn turn = new Turn(period, position, func, te.getTefId().longValue());
                        turn.setType(ProgramacionTotalServiceBean.Type.SPECIAL);
                        turn.setPermiteHorasExtra(te.getTurnoEspecial().getTePermiteExtras());

                        if (te.getTurnoEspecial().getTeComision() != null && te.getTurnoEspecial().getTeComision() == 2) {
                            turn.setPermiteDescanso(false);
                            //System.out.println("no permite descanso = " + turn.getFunctionary().getAlias());
                        } else {
                            turn.setPermiteDescanso(true);
                        }
                        day.addTurn(turn);
                        write("Especial;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + func.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");

                        break;
                    }
                }
                start.add(Calendar.DATE, 1);
            }
        }
        // DOMINGOS

        Calendar c = Calendar.getInstance();

        ArrayList<Day> sundays = new ArrayList<Day>();

        for (Day day : days) {
            c.setTime(day.getDate());
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isHolyDay(day.getDate())) {
                sundays.add(day);
            }
        }
        int count_sunday = sundays.size();
        write_data("");
        write_data("");
        write_data("Total Domingos en el mes;" + count_sunday);
        write_data("Funcionario;No Domingos Trabaja;No Domingos Libre;Domingos Libre");

        Random rg = new Random();

        for (Functionary fun : functionaries) {
//            if (fun.getCountRestSunday() != 0) {
            if (fun.getCountRestSunday() != null) {
                int num_ld = count_sunday - fun.getCountRestSunday();

                if (num_ld > 0) {
                    ArrayList<Integer> lds = new ArrayList<Integer>();
                    String fechas = "";

                    final Random random = new Random();
                    final Set<Integer> intSet = new HashSet<Integer>();
                    while (intSet.size() < num_ld) {
                        intSet.add(random.nextInt(sundays.size()));
                    }

                    for (Integer index : intSet) {
                        Day day = sundays.get(index);
                        c.setTime(day.getDate());

                        Turn turn = getTurnOfFunctionary(fun, day.getDate(), ProgramacionTotalServiceBean.TargetDay.CURRENT, ProgramacionTotalServiceBean.Type.ANY);
                        if (turn == null) {

                            fechas += c.get(Calendar.DATE) + "/" + (c.get(Calendar.MONTH) + 1) + ", ";
                            Period period = new Period((long) 0, "", 0, 24);
                            Position position = new Position((long) 0, "LXR");
                            Turn ld = new Turn(period, position, fun, 0);
                            ld.setType(ProgramacionTotalServiceBean.Type.LD);
                            ld.setPermiteHorasExtra(false);
                            day.addTurn(ld);
                            lds.add(index);
                            write("LD;" + c.get(Calendar.DATE) + "/" + (c.get(Calendar.MONTH) + 1) + ";" + fun.getAlias() + ";" + ld.getPeriod().getAlias() + ld.getPosition().getAlias() + ";;OK;");

                        }

                    }

                    write_data(fun.getAlias() + ";" + fun.getCountRestSunday() + ";" + num_ld + ";" + fechas);

                }
            }

        }
        write_data("");
        write_data("");

    }

    private void solveFirstDayAgainstLastMonth() {
        Day day = (Day) this.days.get(0);

        Calendar c = Calendar.getInstance();
        c.setTime(day.getDate());
        setFunctionariesAvailable();
        for (Functionary fun : this.functionaries) {
            setFunctionariesBusy(day.getTurns());
            if (fun.isAvailable().booleanValue()) {
                if (!fun.getRestedLastWeek().booleanValue()) {
                    Turn previous = getTurnLastDayFromLastProg(fun);
                    if (previous != null) {
                        if ((previous.getPeriod().getId() == this.setting.getPeriodId()) && (c.get(7) != 1) && (!isHolyDay(day.getDate()))) {
                            Period period = new Period(0L, "", 0, 24);
                            Position position = new Position(0L, "DES");
                            Turn rest = new Turn(period, position, fun, 0L);
                            rest.setType(ProgramacionTotalServiceBean.Type.REST);
                            rest.setPermiteHorasExtra(false);
                            day.addTurn(rest);
                            write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;Empalmando mes anterior");
                            fun.setAvailable(Boolean.valueOf(false));
                            fun.setRestedLastWeek(Boolean.valueOf(true));
                        }
                    }
                } else {
                    write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";DES;;Descans? la ultima semana del mes anterior;Empalmando mes anterior");
                }
            }
        }
        setFunctionariesAvailable();
        setFunctionariesBusy(day.getTurns());
        for (Jornada jornada : this.jornadas) {
            if (jornada.getJornadaObligatoria() != null) {
                long required = jornada.getJornadaObligatoria().getJoId().longValue();
                long period = jornada.getJoId().longValue();
                for (Turn turn : day.getTurns()) {
                    if (turn.getPeriod().getId() == period) {
                        for (Functionary fun : this.functionaries) {
                            if (fun.isAvailable().booleanValue()) {
                                Turn previous = getTurnLastDayFromLastProgPrevMonth(fun);
                                if ((previous != null) && (previous.getPeriod().getId() == required) && (previous.getPosition().getId() == turn.getPosition().getId())) {
                                    turn.setFunctionary(fun);
                                    write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;Empalmando mes anterior");
                                    fun.setAvailable(Boolean.valueOf(false));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void solveRestByWeekPeriodRecess() {
        write_data("Descansos");
        String head = "Funcionario;";
        Calendar cal = Calendar.getInstance();
        for (Week week : this.weeks) {
            cal.setTime(((Day) week.getDays().get(0)).getDate());
            head = head + "De " + cal.get(5) + " A ";
            cal.setTime(((Day) week.getDays().get(week.getDays().size() - 1)).getDate());
            head = head + cal.get(5) + ";";
        }
        for (Week week : this.weeks) {
            int days = week.getDays().size() == 1 ? Calendar.SATURDAY - 1 : week.getDays().size();
            double limit = (double) functionaries.size() / days;
            int maxTurns = (int) Math.round(limit);
            Collections.shuffle(this.functionaries);
            for (Functionary fun : this.functionaries) {
                if (fun.isRestWeek().booleanValue()) {
                    if (!isRestWeek(fun, week)) {
                        for (Day day : week.getDays()) {
                            if (day.getTotalDesc() < maxTurns) {
                                Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                                if (des == null) {
                                    if (!isHolyDay(day.getDate()).booleanValue()) {
                                        Turn previous = null;

                                        previous = getTurnOfFunctionary(fun, day.getDate(), -1, 1);
                                        if ((previous != null) && (previous.getPeriod().getId() == this.setting.getPeriodId())) {
                                            if (isTurnDayMax(day, maxTurns, Type.REST)) {
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(day.getDate());
                                                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                                                    Calendar backward = Calendar.getInstance();
                                                    backward.setTime(c.getTime());
                                                    while (backward.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY
                                                            && backward.get(Calendar.DAY_OF_MONTH) > 1) {
                                                        backward.add(Calendar.DATE, -1);
                                                        Turn turn = getTurnOfFunctionary(fun, backward.getTime(), 0, -1);
                                                        if (turn == null) {
                                                            Period period = new Period(0L, "", 0, 24);
                                                            Position position = new Position(0L, "DES");
                                                            Turn rest = new Turn(period, position, fun, 0L);
                                                            rest.setType(ProgramacionTotalServiceBean.Type.REST);
                                                            rest.setPermiteHorasExtra(false);
                                                            day.addTurn(rest);
                                                            day.setTotalDesc(day.getTotalDesc() + 1);
                                                            write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    Period period = new Period(0L, "", 0, 24);
                                                    Position position = new Position(0L, "DES");
                                                    Turn rest = new Turn(period, position, fun, 0L);
                                                    rest.setType(ProgramacionTotalServiceBean.Type.REST);
                                                    rest.setPermiteHorasExtra(false);
                                                    day.addTurn(rest);
                                                    day.setTotalDesc(day.getTotalDesc() + 1);
                                                    write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        float max_count;
        int count;
        int i = 0;
        for (Week week : this.weeks) {
            double limit = (double) this.functionaries.size() / week.getTotalDays();
            int maxTurns = (int) Math.round(limit);
            float percent = week.getDays().size() / 7.0F;
            max_count = percent * this.functionaries.size();
            count = 0;
            Collections.shuffle(this.functionaries);
            for (Functionary fun : this.functionaries) {
                if (fun.isRestWeek().booleanValue()) {
                    if ((i == 0) || (count <= max_count)) {
                        if (!isRestWeek(fun, week)) {
                            week.setRandomDays();
                            for (Day d : week.getDays()) {
                                if (d.getTotalDesc() < maxTurns) {
                                    if ((isRestSunday(d.getDate(), fun)) && (!isHolyDay(d.getDate()).booleanValue())) {
                                        Turn des = getTurnOfFunctionary(fun, d.getDate(), 0, -1);
                                        if (des == null && isTurnDayMax(d, maxTurns, Type.REST)) {
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(d.getDate());

                                            Period period = new Period(0L, "", 0, 24);
                                            Position position = new Position(0L, "DES");
                                            Turn turn = new Turn(period, position, fun, 0L);
                                            turn.setType(ProgramacionTotalServiceBean.Type.REST);
                                            turn.setPermiteHorasExtra(false);
                                            d.addTurn(turn);
                                            d.setTotalDesc(d.getTotalDesc() + 1);
                                            write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                            count++;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            count++;
                        }
                    }
                }
            }
            i++;
        }
        write_data(head);
        for (Functionary fun : this.functionaries) {
            String row = fun.getAlias() + ";";
            for (Week week : this.weeks) {
                Boolean existe = Boolean.valueOf(false);
                for (Day day : week.getDays()) {
                    Turn aux = getTurnOfFunctionary(fun, day.getDate(), 0, 5);
                    if (aux == null) {
                        Turn special = getTurnOfFunctionary(fun, day.getDate(), 0, 3);
                        if ((special != null) && ((special.getPeriod().getAlias() + special.getPosition().getAlias()).equals("DESC"))) {
                            Calendar ca = Calendar.getInstance();
                            ca.setTime(day.getDate());

                            row = row + ca.get(5) + ";";
                            existe = Boolean.valueOf(true);
                            break;
                        }
                    } else {
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(day.getDate());

                        row = row + ca.get(5) + ";";
                        existe = Boolean.valueOf(true);
                        break;
                    }
                }
                if (!existe.booleanValue()) {
                    row = row + ";";
                }
            }
            write_data(row);
        }
        write_data("");
        write_data("");
    }

    private void solveRestByWeekPeriodRecessFull() {
        write_data("Descansos");
        String head = "Funcionario;";
        Calendar cal = Calendar.getInstance();
        for (Week week : this.weeks) {
            cal.setTime(((Day) week.getDays().get(0)).getDate());
            head = head + "De " + cal.get(5) + " A ";
            cal.setTime(((Day) week.getDays().get(week.getDays().size() - 1)).getDate());
            head = head + cal.get(5) + ";";
        }
        for (Week week : this.weeks) {
            for (Functionary fun : this.functionaries) {
                if (fun.isRestWeek().booleanValue()) {
                    if (!isRestWeek(fun, week)) {
                        for (Day day : week.getDays()) {
                            Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                            if (des == null) {
                                if ((isRestSunday(day.getDate(), fun)) && (!isHolyDay(day.getDate()).booleanValue())) {
                                    Turn previous = null;

                                    previous = getTurnOfFunctionary(fun, day.getDate(), -1, 1);
                                    if ((previous != null) && (previous.getPeriod().getId() == this.setting.getPeriodId())) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(day.getDate());
                                        Period period = new Period(0L, "", 0, 24);
                                        Position position = new Position(0L, "DES");
                                        Turn rest = new Turn(period, position, fun, 0L);
                                        rest.setType(ProgramacionTotalServiceBean.Type.REST);
                                        rest.setPermiteHorasExtra(false);
                                        day.addTurn(rest);
                                        write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        float max_count;
        int count;
        for (int i = 0; i < this.weeks.size(); i++) {
            float percent = ((Week) this.weeks.get(i)).getDays().size() / 7.0F;
            max_count = percent * this.functionaries.size();
            count = 0;
            for (Functionary fun : this.functionaries) {
                if (fun.isRestWeek().booleanValue()) {
                    if ((i == 0) || (count <= max_count)) {
                        if (!isRestWeek(fun, (Week) this.weeks.get(i))) {
                            ((Week) this.weeks.get(i)).setRandomDays();
                            for (Day d : ((Week) this.weeks.get(i)).getDays()) {
                                if ((isRestSunday(d.getDate(), fun)) && (!isHolyDay(d.getDate()).booleanValue())) {
                                    Turn des = getTurnOfFunctionary(fun, d.getDate(), 0, -1);
                                    if (des == null) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(d.getDate());

                                        Period period = new Period(0L, "", 0, 24);
                                        Position position = new Position(0L, "DES");
                                        Turn turn = new Turn(period, position, fun, 0L);
                                        turn.setType(ProgramacionTotalServiceBean.Type.REST);
                                        turn.setPermiteHorasExtra(false);
                                        d.addTurn(turn);
                                        write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                        count++;
                                        break;
                                    }
                                }
                            }
                        } else {
                            count++;
                        }
                    }
                }
            }
        }
        write_data(head);
        for (Functionary fun : this.functionaries) {
            String row = fun.getAlias() + ";";
            for (Week week : this.weeks) {
                Boolean existe = Boolean.valueOf(false);
                for (Day day : week.getDays()) {
                    Turn aux = getTurnOfFunctionary(fun, day.getDate(), 0, 5);
                    if (aux == null) {
                        Turn special = getTurnOfFunctionary(fun, day.getDate(), 0, 3);
                        if ((special != null) && ((special.getPeriod().getAlias() + special.getPosition().getAlias()).equals("DESC"))) {
                            Calendar ca = Calendar.getInstance();
                            ca.setTime(day.getDate());

                            row = row + ca.get(5) + ";";
                            existe = Boolean.valueOf(true);
                            break;
                        }
                    } else {
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(day.getDate());

                        row = row + ca.get(5) + ";";
                        existe = Boolean.valueOf(true);
                        break;
                    }
                }
                if (!existe.booleanValue()) {
                    row = row + ";";
                }
            }
            write_data(row);
        }
        write_data("");
        write_data("");
    }

    private void solveTropByWeekPeriodRecess() {
        write_data("Trop");
        String head = "Funcionario;";
        Calendar cal = Calendar.getInstance();
        for (Week week : this.weeks) {
            cal.setTime(((Day) week.getDays().get(0)).getDate());
            head = head + "De " + cal.get(5) + " A ";
            cal.setTime(((Day) week.getDays().get(week.getDays().size() - 1)).getDate());
            head = head + cal.get(5) + ";";
        }
        for (Week week : this.weeks) {
            double limit = (double) this.functionaries.size() / week.getTotalDays();
            int maxTurns = (int) Math.round(limit);
            for (Functionary fun : this.functionaries) {
                //if (fun.isRestWeek().booleanValue()) {
                if (!isTropWeek(fun, week)) {
                    for (Day day : week.getDays()) {
                        if (day.getTotalTrop() < maxTurns) {
                            Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                            if (des == null) {
                                cal.setTime(day.getDate());
                                if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !isHolyDay(day.getDate()).booleanValue()) {

                                    if (isTurnDayMax(day, maxTurns, Type.TROP)) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(day.getDate());
                                        Period period = new Period(0L, "", 0, 24);
                                        Position position = new Position(0L, "TROP");
                                        Turn rest = new Turn(period, position, fun, 0L);
                                        rest.setType(ProgramacionTotalServiceBean.Type.TROP);
                                        rest.setPermiteHorasExtra(false);
                                        day.addTurn(rest);
                                        day.setTotalTrop(day.getTotalTrop() + 1);
                                        write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //}
            }
        }
        float max_count;
        int count;
        for (int i = 0; i < this.weeks.size(); i++) {
            Week week = this.weeks.get(i);
            double limit = (double) this.functionaries.size() / week.getTotalDays();
            int maxTurns = (int) Math.round(limit);
            float percent = this.functionaries.size() / week.getDays().size();
            count = 0;
            for (Functionary fun : this.functionaries) {
                // if (fun.isRestWeek().booleanValue()) {
                if ((i == 0) || (count <= percent)) {
                    if (!isTropWeek(fun, week)) {
                        week.setRandomDays();
                        for (Day d : week.getDays()) {
                            if (d.getTotalDesc() < maxTurns) {
                                if ((isRestSunday(d.getDate(), fun)) && (!isHolyDay(d.getDate()).booleanValue())) {
                                    Turn des = getTurnOfFunctionary(fun, d.getDate(), 0, -1);
                                    if (des == null && isTurnDayMax(d, maxTurns, Type.TROP)) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(d.getDate());

                                        Period period = new Period(0L, "", 0, 24);
                                        Position position = new Position(0L, "TROP");
                                        Turn turn = new Turn(period, position, fun, 0L);
                                        turn.setType(ProgramacionTotalServiceBean.Type.TROP);
                                        turn.setPermiteHorasExtra(false);
                                        d.addTurn(turn);
                                        d.setTotalTrop(d.getTotalTrop() + 1);
                                        write("Trop;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                        count++;
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        count++;
                    }
                }
                //}
            }
        }
        write_data(head);
        for (Functionary fun : this.functionaries) {
            String row = fun.getAlias() + ";";
            for (Week week : this.weeks) {
                Boolean existe = Boolean.valueOf(false);
                for (Day day : week.getDays()) {
                    Turn aux = getTurnOfFunctionary(fun, day.getDate(), 0, 8);
                    if (aux == null) {
                        Turn special = getTurnOfFunctionary(fun, day.getDate(), 0, 3);
                        if ((special != null) && ((special.getPeriod().getAlias() + special.getPosition().getAlias()).equals("TROP"))) {
                            Calendar ca = Calendar.getInstance();
                            ca.setTime(day.getDate());

                            row = row + ca.get(5) + ";";
                            existe = Boolean.valueOf(true);
                            break;
                        }
                    } else {
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(day.getDate());

                        row = row + ca.get(5) + ";";
                        existe = Boolean.valueOf(true);
                        break;
                    }
                }
                if (!existe.booleanValue()) {
                    row = row + ";";
                }
            }
            write_data(row);
        }
        write_data("");
        write_data("");
    }

    private void solveDescByWeekPeriodRecessFull() {
        write_data("DESC SATURDAY");
        String head = "Funcionario;";
        Calendar cal = Calendar.getInstance();
        for (Week week : this.weeks) {
            cal.setTime(((Day) week.getDays().get(0)).getDate());
            head = head + "De " + cal.get(5) + " A ";
            cal.setTime(((Day) week.getDays().get(week.getDays().size() - 1)).getDate());
            head = head + cal.get(5) + ";";
        }
        for (Week week : this.weeks) {
            for (Functionary fun : this.functionaries) {
                if (fun.isRestWeek().booleanValue()) {
                    if (!isDescAsigned(fun, week)) {
                        for (Day day : week.getDays()) {

                            Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                            if (des == null) {
                                Calendar c = Calendar.getInstance();
                                c.setTime(day.getDate());

                                int nD = c.get(Calendar.DAY_OF_WEEK);

                                if (nD == 7) {

                                    Period period = new Period(0L, "", 0, 24);
                                    Position position = new Position(0L, "DESC");
                                    Turn rest = new Turn(period, position, fun, 0L);
                                    rest.setType(ProgramacionTotalServiceBean.Type.REST);
                                    rest.setPermiteHorasExtra(false);
                                    day.addTurn(rest);
                                    write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                    break;
                                }
                            }

                        }
                    }
                }
            }
        }

    }

    private boolean isRestSunday(Date date, Functionary fun) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int count;

        if ((fun.getCountRestSunday() != null) && (fun.getCountRestSunday() == 0) && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {

            count = 0;
            for (Day day : this.days) {
                c.setTime(day.getDate());
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    for (Turn turn : day.getTurns()) {
                        if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (turn.getType() == 5)) {
                            count++;
                            if (count > fun.getCountRestSunday()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void solveOrdinaryTurnsEmpty() {
        setFunctionariesAvailable();
        for (Day day : this.days) {
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() == null) {
                    for (Functionary fun : this.functionaries) {
                        if (solvingOrdinaryTurn(fun, turn, day.getDate())) {
                            break;
                        }
                    }
                }
            }
            setFunctionariesBusy(day.getTurns());
        }
    }

    private void solveOrdinaryTurnsComplete() {
        setFunctionariesAvailable();
        Collections.shuffle(this.functionaries);
        for (Day day : this.days) {
            setFunctionariesBusy(day.getTurns());
            for (Functionary fun : this.functionaries) {
                for (Turn turn : day.getTurns()) {
                    if ((turn.getFunctionary() == null)
                            && (solvingOrdinaryTurn(fun, turn, day.getDate()))) {
                        break;
                    }
                }
            }
            Functionary fun;
            Collections.shuffle(this.functionaries);

            setFunctionariesAvailable();
        }
    }

    private void solveOrdinaryTurnsByPeriod(long period) {
        setFunctionariesAvailable();
        Collections.shuffle(this.functionaries);
        for (Day day : this.days) {
            setFunctionariesBusy(day.getTurns());
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                    for (Functionary fun : this.functionaries) {
                        if (solvingOrdinaryTurn(fun, turn, day.getDate())) {
                            break;
                        }
                    }
                }
                Collections.shuffle(this.functionaries);
            }
            setFunctionariesAvailable();
        }
    }

    private void solveOrdinaryTurnsOnlyMorning(long period) {
        setFunctionariesAvailable();
        Collections.shuffle(this.functionaries);
        for (Day day : this.days) {
            setFunctionariesBusy(day.getTurns());
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                    for (Functionary fun : this.functionaries) {
                        if (solvingOrdinaryTurnMorning(fun, turn, day.getDate())) {
                            break;
                        }
                    }
                }
                Collections.shuffle(this.functionaries);
            }
            setFunctionariesAvailable();
        }
    }

    private void solveOrdinaryTurnsAvaible() {
        setFunctionariesAvailable();
        for (Day day : this.days) {
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            setFunctionariesBusy(day.getTurns());
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() == null) {
                    for (Functionary fun : this.functionaries) {
                        if (solvingOrdinaryTurn(fun, turn, day.getDate())) {
                            break;
                        }
                    }
                }
            }
            setFunctionariesAvailable();
        }
    }

    private void solveExtraordinaryTurnsDistribuied(Boolean exceeded) {
        setFunctionariesAvailable();
        Collections.shuffle(this.functionaries);
        for (Day day : this.days) {
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            setFunctionariesBusy(day.getTurns());
            for (Turn extra : day.getTurns()) {
                if (extra.getFunctionary() == null) {
                    for (Functionary fun : this.functionaries) {
                        if (solvingOrdinaryTurn(fun, extra, day.getDate())) {
                            break;
                        } else if (solvingExtraOrdinaryTurn(fun, extra, exceeded, day.getDate())) {
                            break;
                        }
                    }
                }
            }
            setFunctionariesAvailable();
        }
    }

    private void save() {
        for (Day day : this.days) {
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() != null) {
                    saveAssignedPosition(turn, day);
                } else {
                    saveNoAssignedPosition(turn, day);
                }
            }
        }
    }

    private void saveNoAssignedPosition(Turn turn, Day day) {
        PosNoAsig pos = new PosNoAsig();
        pos.setFecha(day.getDate());
        pos.setDependencia(this.programming.getDependencia().getDepId());
        pos.setJornada(Long.valueOf(turn.getPeriod().getId()));
        pos.setPosicion(Long.valueOf(turn.getPosition().getId()));
        pos.setProgramacion(this.programming.getProId());
        pos.setPosicionJornada(new PosicionJornada(Long.valueOf(turn.getPositionPeriod())));

        persist(pos);
    }

    private void saveAssignedPosition(Turn turn, Day day) {
        Turno turn_raw = new Turno();
        turn_raw.setFuncionario(new Funcionario(Long.valueOf(turn.getFunctionary().getId()), null, null, null, null));
        turn_raw.setProgramacion(this.programming);
        turn_raw.setTurEstado(Long.valueOf(0L));
        turn_raw.setTurFecha(day.getDate());
        turn_raw.setTurTipo(Long.valueOf(turn.getType()));
        turn_raw.setTurPosicionJornada(Long.valueOf(turn.getPositionPeriod()));
        turn_raw.setTurHInicio(Long.valueOf(turn.getPeriod().getStart()));
        turn_raw.setTurHFin(Long.valueOf(turn.getPeriod().getEnd() - 1L));
        turn_raw.setTurMInicio(Long.valueOf(0L));
        turn_raw.setTurMFin(Long.valueOf(59L));

        persist(turn_raw);
    }

    private boolean solvingOrdinaryTurn(Functionary fun, Turn turn, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (validateOrdinaryTurn(fun, turn, date).booleanValue()) {
            turn.setFunctionary(fun);
            fun.setAvailable(Boolean.valueOf(false));
            return true;
        }
        return false;
    }

    private boolean solvingOrdinaryTurnMorning(Functionary fun, Turn turn, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (validateOrdinaryTurnMorningNigth(fun, turn, date).booleanValue()) {
            setNextTurnToExtra(fun, date, TargetDay.CURRENT, Type.ORDINARY);

            turn.setFunctionary(fun);
            turn.setType(Type.ORDINARY);
            fun.setAvailable(Boolean.valueOf(false));
            return true;
        }
        return false;
    }

    private boolean solvingExtraOrdinaryTurn(Functionary fun, Turn extra, Boolean exceeded, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (!fun.isAvailable().booleanValue()) {
            Turn turn = getTurnOfFunctionary(fun, date, 0, -1);

            if ((turn != null) && (!turn.getHaveExtra().booleanValue())) {
                int hourEval = fun.getMaxHoursExtra();
                if (exceeded.booleanValue()) {
                    hourEval = fun.getMaxHoursExtraExceeded();
                }
                if (hourEval < extra.getPeriod().getTotal()) {
                    return false;
                }
                if (turn.getType() == ProgramacionTotalServiceBean.Type.SPECIAL) {
                    return false;
                }
                int extraType = extra.getType();
                int turnType = turn.getType();
                if ((extra.getPeriod().getStart() < turn.getPeriod().getEnd()) && (turn.getPeriod().getTotal() + extra.getPeriod().getTotal() < 24)) {
                    extra.setFunctionary(fun);
                    extra.setType(ProgramacionTotalServiceBean.Type.ORDINARY);
                    turn.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                    if (validateExtraordinaryTurn(extra, turn, date, hourEval, exceeded.booleanValue()).booleanValue()) {
                        extra.setType(ProgramacionTotalServiceBean.Type.ORDINARY);
                        turn.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                        turn.setHaveExtra(Boolean.valueOf(true));
                        extra.setHaveExtra(Boolean.valueOf(true));
                        //System.out.print("Fun: " + fun.getAlias() + "\tAntes: " + fun.getMaxHoursExtra());
                        if (exceeded.booleanValue()) {
                            fun.decreaseMaxHoursExtraExceeded(extra.getPeriod().getTotal());
                        } else {
                            fun.decreaseMaxHoursExtra(extra.getPeriod().getTotal());
                        }
                        //System.out.println("\tAhora: " + fun.getMaxHoursExtra());
                        Functionary aux = fun;
                        this.functionaries.remove(fun);
                        this.functionaries.add(aux);
                        extra.setFunctionary(fun);
                        turn.setFunctionary(fun);
                        return true;
                    } else {
                        turn.setType(turnType);
                        extra.setType(extraType);
                    }
                    extra.setFunctionary(null);
                } else if (validateExtraordinaryTurn(turn, extra, date, hourEval, exceeded.booleanValue()).booleanValue()) {
                    extra.setFunctionary(fun);
                    extra.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                    fun.addCountExtras();
//                    if(turn.getPosition().getAlias().startsWith("N")){
//                        fun.addCoeuntExtrasNocturnas();
//                    }
                    turn.setHaveExtra(Boolean.valueOf(true));
                    extra.setHaveExtra(Boolean.valueOf(true));
                    //System.out.print("Fun: " + fun.getAlias() + "\tAntes: " + fun.getMaxHoursExtra());
                    if (exceeded.booleanValue()) {
                        fun.decreaseMaxHoursExtraExceeded(extra.getPeriod().getTotal());
                    } else {
                        fun.decreaseMaxHoursExtra(extra.getPeriod().getTotal());
                    }
                    //System.out.println("\tAhora: " + fun.getMaxHoursExtra());

                    Functionary aux = fun;
                    this.functionaries.remove(fun);
                    this.functionaries.add(aux);
                    turn.setFunctionary(fun);
                    extra.setFunctionary(fun);
                    return true;
                }
            }
        }
        return false;
    }

    private void orderFunctionaries() {
        Collections.sort(functionaries, new Comparator<Functionary>() {
            @Override
            public int compare(Functionary o1, Functionary o2) {
                return o1.getNumHabilities() < o2.getNumHabilities() ? -1
                        : o1.getNumHabilities() > o2.getNumHabilities() ? 1
                        : 0;
            }
        });
    }

    private void orderTurns(ArrayList<Turn> turns) {
        Collections.sort(turns, new Comparator<Turn>() {
            @Override
            public int compare(Turn o1, Turn o2) {
                return o1.getNumEnables() < o2.getNumEnables() ? -1
                        : o1.getNumEnables() > o2.getNumEnables() ? 1
                        : 0;
            }
        });
    }

    private void solvePeriodRequired(long period, long required) {
        setFunctionariesAvailable();
        for (Day day : this.days) {
            setFunctionariesBusy(day.getTurns());
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                    for (Functionary fun : this.functionaries) {
                        if ((fun.isAvailable().booleanValue()) && (satisfiesEnabledPosition(fun.getId(), turn.getPosition().getId()).booleanValue())) {
//                            if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                            Turn previous = null;
                            if (day.getDate().compareTo(this.programming.getProFechaInicio()) == 0) {
                                previous = getTurnLastDayFromLastProg(fun);

                                if ((previous != null) && (previous.getPeriod().getId() == required) && (previous.getPosition().getId() == turn.getPosition().getId())) {
                                    turn.setFunctionary(fun);
                                    write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                    fun.setAvailable(Boolean.valueOf(false));

                                    Functionary aux = fun;
                                    this.functionaries.remove(fun);
                                    this.functionaries.add(aux);

                                    break;
                                }
                            } else {
                                previous = getTurnOfFunctionary(fun, day.getDate(), -1, -1);
                                if (previous == null) {
                                    Turn next = getTurnOfFunctionary(fun, day.getDate(), 1, -1);
                                    if (next == null || next.getPosition().getId() == 0L || satisfiesRecessPeriod(turn, next, false, day.getDate())) {
                                        previous = getTurnDateByPeriodPosition(day.getDate(), -1, required, turn.getPosition().getId());
                                        Turn previous_of_previous = getTurnOfFunctionary(fun, day.getDate(), -2, -1);
                                        if ((previous != null) && ((previous_of_previous == null) || (previous_of_previous.getPeriod().getId() != this.setting.getPeriodId()))) {
                                            if (previous.getPeriod().getId() == 168 && turn.getPeriod().getId() == 166) {
                                                //System.out.println("current = " + previous.getPeriod().getAlias() + previous.getPosition().getAlias());
                                            }
                                            if (satisfiesPetitions(turn, day.getDate(), fun) && satisfiesPetitions(previous, day.getDate(), fun)) {
                                                if (satisfiesInactivePosition(turn, day.getDate())) {
                                                    if (!isJornadaNoLaboral(turn, fun)) {
                                                        turn.setFunctionary(fun);
                                                        write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                                        previous.setFunctionary(fun);
                                                        c.add(5, -1);
                                                        write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";;OK;");
                                                        c.setTime(day.getDate());

                                                        fun.setAvailable(Boolean.valueOf(false));

                                                        Functionary aux = fun;
                                                        this.functionaries.remove(fun);
                                                        this.functionaries.add(aux);
                                                        break;
                                                    } else {
                                                        write("Ordinario;" + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                                    }
                                                } else {
                                                    write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(day.getDate()));
                                                }
                                            } else {
                                                write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";;Tiene una peticion aprobada este dia;");
                                            }
                                        } else {
                                            write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;Problemas de restriccion por el dia anterior o 2 dias anteriores;");
                                        }
                                    } else {
                                        write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;Nu cumple con el tiempo de receso despues de la jornada: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";");
                                    }
                                }
                            }
                        } else {
                            write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;El funcionario no tiene la posicion habilitada;");
                        }
                    }
                }
            }
            Turn turn;
            setFunctionariesAvailable();
        }
    }

    private void solvePeriodRequiredAditionalRestrictions(long period, long required) {
        setFunctionariesAvailable();
        for (Day day : this.days) {
            setFunctionariesBusy(day.getTurns());
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            String fecha = new SimpleDateFormat("ddMMyy").format(day.getDate());
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                    for (Functionary fun : this.functionaries) {
                        if ((fun.isAvailable().booleanValue()) && (satisfiesEnabledPosition(fun.getId(), turn.getPosition().getId()).booleanValue())) {
//                            if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period)) {
                            Turn actual = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                            if (actual == null) {
                                actual = getTurnDateByPeriodPosition(day.getDate(), 0, required, turn.getPosition().getId());
                                Turn previous = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                                if ((actual != null) && ((previous == null) || (previous.getPeriod().getId() != this.setting.getPeriodId()))) {
                                    if (satisfiesPetitions(turn, day.getDate(), fun) && satisfiesPetitions(actual, day.getDate(), fun)) {
                                        if (satisfiesInactivePosition(turn, day.getDate())) {
                                            if (!isJornadaNoLaboral(turn, fun)) {
                                                turn.setFunctionary(fun);
                                                write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;OK;");
                                                actual.setFunctionary(fun);
                                                c.add(5, -1);
                                                write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + actual.getPeriod().getAlias() + actual.getPosition().getAlias() + ";;OK;");
                                                c.setTime(day.getDate());

                                                fun.setAvailable(Boolean.valueOf(false));

                                                Functionary aux = fun;
                                                this.functionaries.remove(fun);
                                                this.functionaries.add(aux);
                                                break;
                                            } else {
                                                write("Ordinario;" + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                            }
                                        } else {
                                            write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(day.getDate()));
                                        }
                                    } else {
                                        write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + actual.getPeriod().getAlias() + actual.getPosition().getAlias() + ";;Tiene una peticion aprobada este dia;");
                                    }
                                } else {
                                    write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;Problemas de restriccion por el dia anterior o 2 dias anteriores;");
                                }
                            }
                        } else {
                            write("Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;El funcionario no tiene la posicion habilitada;");
                        }
                    }
                }
            }
            Turn turn;
            setFunctionariesAvailable();
        }
    }

    private boolean isRestWeek(Functionary fun, Week week) {
        for (Day day : week.getDays()) {
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            if (day.getDate().compareTo(this.programming.getProFechaInicio()) == 0) {
                if (fun.getRestedLastWeek().booleanValue()) {
                    return true;
                }
            }
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (turn.getType() == Type.REST || (turn.getType() == Type.SPECIAL && turn.getPosition().getAlias().equals("DESC")))) {
                    return true;
                }
            }

            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && !turn.getPermiteDescanso()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTropWeek(Functionary fun, Week week) {
        for (Day day : week.getDays()) {
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            int totalCea = 0;
            for (Turn turn : day.getTurns()) {
                String alias = turn.getPosition().getAlias();
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (((turn.getType() == Type.TROP || turn.getType() == Type.SPECIAL) && (alias.equals("TROP"))))) {
                    return true;
                }

                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (((turn.getType() == Type.SPECIAL) && (alias.endsWith("VAC") || alias.endsWith("LIC"))))) {
                    return true;
                }

                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (((turn.getType() == Type.SPECIAL) && (alias.equals("CEA") || alias.equals("SIN") || alias.equals("SIND") || alias.equals("PERS"))))) {
                    totalCea++;
                    if (totalCea > 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void fullAssignTrops(boolean trops) {
        pendingTrops = new HashMap<Long, List<Week>>();

        for (Functionary fun : this.functionaries) {
            for (Week week : weeks) {
                int totalTRops = 0;
                int totalDes = 0;
                for (Day day : week.getDays()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(day.getDate());
                    for (Turn turn : day.getTurns()) {
                        String alias = turn.getPosition().getAlias();
                        if (turn.getFunctionary() != null && turn.getFunctionary().getId() == fun.getId() && ((turn.getType() == Type.TROP || turn.getType() == Type.SPECIAL) && (alias.equals("TROP")))) {
                            totalTRops++;
                        }

                        if (turn.getFunctionary() != null && turn.getFunctionary().getId() == fun.getId() && ((turn.getType() == Type.REST) && (alias.equals("DES")))) {
                            totalDes++;
                        }
                    }
                }

                double limit = (double) this.functionaries.size() / week.getTotalDays();
                int maxTurns = (int) Math.round(limit);
                if (totalTRops == 0 && trops) {
                    for (Day day : week.getDays()) {
                        Calendar cal = Calendar.getInstance();
                        Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                        if (des == null) {
                            cal.setTime(day.getDate());
                            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && !isHolyDay(day.getDate()).booleanValue()) {
                                if (isTurnDayMax(day, maxTurns, Type.TROP)) {
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(day.getDate());
                                    Period period = new Period(0L, "", 0, 24);
                                    Position position = new Position(0L, "TROP");
                                    Turn rest = new Turn(period, position, fun, 0L);
                                    rest.setType(ProgramacionTotalServiceBean.Type.TROP);
                                    rest.setPermiteHorasExtra(false);
                                    day.addTurn(rest);
                                    day.setTotalTrop(day.getTotalTrop() + 1);
                                    write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                    break;
                                }
                            }
                        }
                    }
                }
                if (totalDes == 0) {
                    for (Day day : week.getDays()) {
                        Calendar cal = Calendar.getInstance();
                        Turn des = getTurnOfFunctionary(fun, day.getDate(), 0, -1);
                        if (des == null) {
                            if ((isRestSunday(day.getDate(), fun)) && (!isHolyDay(day.getDate()).booleanValue())) {
                                Turn previous = null;

                                previous = getTurnOfFunctionary(fun, day.getDate(), -1, 1);
                                if ((previous != null) && (previous.getPeriod().getId() == this.setting.getPeriodId())) {
                                    if (isTurnDayMax(day, maxTurns, Type.REST)) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(day.getDate());
                                        Period period = new Period(0L, "", 0, 24);
                                        Position position = new Position(0L, "DES");
                                        Turn rest = new Turn(period, position, fun, 0L);
                                        rest.setType(ProgramacionTotalServiceBean.Type.REST);
                                        rest.setPermiteHorasExtra(false);
                                        day.addTurn(rest);
                                        day.setTotalDesc(day.getTotalDesc() + 1);
                                        write("Descanso;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + rest.getPeriod().getAlias() + rest.getPosition().getAlias() + ";;OK;");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private boolean isTurnDayMax(Day day, int max, int type) {
        int contador = 0;
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(day.getDate());
        for (Turn turn : day.getTurns()) {
            if ((turn.getFunctionary() != null) && ((turn.getType() == type))) {
                contador++;
            }
        }
        return contador < max;
    }

    private boolean isDescAsigned(Functionary fun, Week week) {
        int spacialDays = 0;
        for (Day day : week.getDays()) {
            Calendar c = Calendar.getInstance();
            c.setTime(day.getDate());
            for (Turn turn : day.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (((turn.getType() == ProgramacionTotalServiceBean.Type.REST)))) {
                    return true;
                }
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId()) && (((turn.getType() == ProgramacionTotalServiceBean.Type.SPECIAL)))) {
                    int time = (turn.getPeriod().getEnd() - turn.getPeriod().getStart());
                    if (time <= 12) {
                        spacialDays++;
                    }
                }
            }
        }
        return !(spacialDays == 5);
    }

    private boolean areWeBusy() {
        for (Functionary fun : this.functionaries) {
            if (fun.isAvailable().booleanValue()) {
                return false;
            }
        }
        return true;
    }

    private Turn getTurnDateByPeriodPosition(Date current, int amountDay, long period, long pos) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : day.getTurns()) {
                    if ((turn.getFunctionary() == null) && (turn.getPeriod().getId() == period) && (turn.getPosition().getId() == pos)) {
                        return turn;
                    }
                }
            }
        }
        return null;
    }

    private Boolean validateExtraordinaryTurn(Turn turn, Turn extra, Date current, int hourEval, boolean exceeded) {
        Calendar c = Calendar.getInstance();
        c.setTime(current);


        String s = "ExtraOrdinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + turn.getFunctionary().getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";" + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";";
        if (satisfiesInitialConditionalExtra(s, turn, extra, current, hourEval, exceeded).booleanValue()) {
            if (((satisfiesEnabledPosition(turn.getFunctionary().getId(), turn.getPosition().getId()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), extra.getPosition().getId()).booleanValue()))
                    || ((satisfiesEnabledSpecialPosition(turn.getFunctionary().getId(), turn.getPositionPeriod()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), extra.getPosition().getId()).booleanValue()))
                    || ((satisfiesEnabledSpecialPosition(turn.getFunctionary().getId(), extra.getPositionPeriod()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), turn.getPosition().getId()).booleanValue()))) {

                Turn next = getTurnOfFunctionary(turn.getFunctionary(), current, 1, 1);
                if (next != null && turn != null) {
                    if (!satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                if (next != null && extra != null) {
                    if (!satisfiesRestrictionPreviousPeriod(extra, next, true).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                Turn previous = getTurnOfFunctionary(turn.getFunctionary(), current, -1, Type.EXTRAORDINARY);
                
                if(previous==null){
                    previous = getTurnOfFunctionary(turn.getFunctionary(), current, -1, Type.ANY);
                }
                if (previous != null && turn != null) {
                    if (!satisfiesRestrictionPreviousPeriod(previous, turn, true).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                if (satisfiesMaxContinuosPeriod(turn, extra, next, current).booleanValue()) {
                    if ((satisfiesRequiredPeriod(turn, extra).booleanValue()) && (satisfiesRequiredPeriod(extra, next).booleanValue())) {
                        if ((satisfiesRestrictionPreviousPeriod(turn, extra, true).booleanValue()) && (satisfiesRestrictionPreviousPeriod(extra, next, true).booleanValue()) && ((satisfiesRestrictionPreviousPeriod(turn, next, true).booleanValue()))) {
                            if ((satisfiesRecessPeriod(turn, extra, Boolean.valueOf(true)).booleanValue()) && (satisfiesRecessPeriod(extra, next, Boolean.valueOf(false)).booleanValue())) {
                                if ((satisfiesSequence(turn, extra).booleanValue()) && (satisfiesSequence(extra, next).booleanValue())) {
                                    if (!satisfiesInactivePosition(extra, current)) {
                                        write(s + "La posicion: " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                    } else if (!satisfiesInactivePosition(turn, current)) {
                                        write(s + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                    } else {
                                        if (!satisfiesPetitions(turn, current, turn.getFunctionary())) {
                                            write(s + "El funcionario tiene un peticion para la jornada: " + turn.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        } else if (!satisfiesPetitions(extra, current, turn.getFunctionary())) {
                                            write(s + "El funcionario tiene un peticion para la jornada: " + extra.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        } else {

                                            if (isJornadaNoLaboral(turn, turn.getFunctionary())) {
                                                write("Ordinario;" + "El funcionario " + turn.getFunctionary().getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                            } else if (isJornadaNoLaboral(extra, turn.getFunctionary())) {
                                                write("Ordinario;" + "El funcionario " + turn.getFunctionary().getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                            } else {
                                                write(s + "OK;");
                                                return Boolean.valueOf(true);
                                            }
                                        }
                                    }
                                }
                                write(s + "No cumple con secuencias;");
                            } else if (next == null) {
                                write(s + "No cumple con la restricion de jornada receso anterior;");
                            } else {
                                write(s + "No cumple con la restricion de jornada receso anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                            }
                        } else if (next == null) {
                            write(s + "No cumple con la restricion de jornada restrictivas anterior;");
                        } else {
                            write(s + "No cumple con la restricion de jornada restrictivas anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                        }
                    } else if (next == null) {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior;");
                    } else {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                    }
                } else if (next != null) {
                    write(s + "No cumple con la regla de maximo numero de jornadas continuas (" + this.setting.getMaxContinuosPeriod() + ");" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " - " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + " - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + " ;");
                } else {
                    write(s + "No cumple con la regla de maximo numero de jornadas continuas (" + this.setting.getMaxContinuosPeriod() + ");" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " - " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";");
                }
            } else if (!satisfiesEnabledPosition(turn.getFunctionary().getId(), extra.getPosition().getId()).booleanValue()) {
                write(s + "Funcionario No Tiene Posicion Habilitada;" + extra.getPosition().getAlias() + ";");
            } else {
                write(s + "Funcionario No Tiene Posicion Habilitada;" + turn.getPosition().getAlias() + ";");
            }
        }
        return Boolean.valueOf(false);
    }

    private Boolean validateOrdinaryTurn(Functionary fun, Turn turn, Date current) {
        Calendar c = Calendar.getInstance();
        c.setTime(current);

        String s = "Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;";
        if (!isJornadaNoLaboral(turn, fun)) {
            if (satisfiesRestByWeek(fun, current)) {
                if (fun.isAvailable().booleanValue()) {
                    if (satisfiesEnabledPosition(fun.getId(), turn.getPosition().getId()).booleanValue()) {
                        Turn previous = getTurnOfFunctionary(fun, current, -1, -1);
                        if (current.compareTo(this.programming.getProFechaInicio()) == 0) {
                            previous = getTurnLastDayFromLastProg(fun);
                        }
                        Turn next = getTurnOfFunctionary(fun, current, ProgramacionTotalServiceBean.TargetDay.NEXT, ProgramacionTotalServiceBean.Type.ANY);
                        if (next != null && turn != null) {
                            if (!satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()) {
                                write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                                return false;
                            }
                        }
                        if (satisfiesRequiredPeriod(previous, turn).booleanValue()) {
                            if (satisfiesRestrictionPreviousPeriod(previous, turn, false).booleanValue()) {
                                if (satisfiesRecessPeriod(previous, turn, Boolean.valueOf(false), current).booleanValue()) {
                                    if (satisfiesSequence(previous, turn).booleanValue()) {
                                        if (satisfiesInactivePosition(turn, current)) {
                                            if (satisfiesPetitions(turn, current, fun)) {
                                                write(s + "OK;");
                                                return Boolean.valueOf(true);
                                            } else {
                                                write(s + "El funcionario tiene un peticion para la jornada: " + turn.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                            }
                                        } else {
                                            write(s + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + "esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        }
                                    }
                                    write(s + "No cumple con secuencia;");
                                } else {
                                    write(s + "No cumple con la restricion de jornada de receso;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                                }
                            } else {
                                write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                            }
                        } else if (previous == null) {
                            write(s + "No cumple con la restricion de jornada obligatoria anterior por que no existe;");
                        } else {
                            write(s + "No cumple con la restricion de jornada obligatoria anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                        }
                    } else {
                        write(s + "Funcionario No Tiene Posicion Habilitada;" + turn.getPosition().getAlias() + ";");
                    }
                } else {
                    write(s + "Funcionario ya tiene turno programado para ese dia;");
                }
            } else {
                write(s + "No cumple con las restricci?n de los descansos;");
            }
        } else {
            System.out.println(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;");
            write(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
        }
        return Boolean.valueOf(false);
    }

    private Boolean validateOrdinaryTurnMorningNigth(Functionary fun, Turn turn, Date current) {
        Calendar c = Calendar.getInstance();
        c.setTime(current);

        String s = "Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;";
        if (!isJornadaNoLaboral(turn, fun)) {
            if (satisfiesRestByWeek(fun, current)) {
                if (fun.isAvailable().booleanValue()) {
                    if (satisfiesEnabledPosition(fun.getId(), turn.getPosition().getId()).booleanValue()) {
                        Turn previous = getTurnOfFunctionary(fun, current, -1, -1);
                        if (current.compareTo(this.programming.getProFechaInicio()) == 0) {
                            previous = getTurnLastDayFromLastProg(fun);
                        }
                        Turn next = getTurnOfFunctionary(fun, current, TargetDay.NEXT, Type.ORDINARY);
                        Turn actual = getTurnOfFunctionary(fun, current, TargetDay.CURRENT, Type.ORDINARY);
                        if (next != null && turn != null) {
                            if (!satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()) {
                                write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                                return false;
                            }
                        }
                        if (satisfiesRequiredPeriod(previous, turn).booleanValue()) {
                            if (satisfiesRestrictionPreviousPeriod(previous, turn, false).booleanValue()) {
                                if (satisfiesRecessPeriod(previous, turn, Boolean.valueOf(false), current).booleanValue()) {
                                    if (satisfiesSequence(previous, turn).booleanValue()) {
                                        if (satisfiesInactivePosition(turn, current)) {
                                            if (satisfiesPetitions(turn, current, fun)) {
                                                if (turn != null && turn.getPeriod().getAlias().equalsIgnoreCase("M")) {
                                                    if (actual != null && actual.getPeriod().getAlias().equalsIgnoreCase("N")) {
                                                        if (next != null && next.getPeriod().getAlias().equalsIgnoreCase("A")) {
                                                            write(s + "OK;");
                                                            return Boolean.valueOf(true);
                                                        } else {
                                                            write("La jornada siguiente no es amanece");
                                                        }
                                                    } else {
                                                        write(s + "La jornada actual no es Nocturna");
                                                    }
                                                } else {
                                                    write(s + "La jornada no es mañana");
                                                }
                                            } else {
                                                write(s + "El funcionario tiene un peticion para la jornada: " + turn.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                            }
                                        } else {
                                            write(s + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + "esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        }
                                    }
                                    write(s + "No cumple con secuencia;");
                                } else {
                                    write(s + "No cumple con la restricion de jornada de receso;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                                }
                            } else {
                                write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                            }
                        } else if (previous == null) {
                            write(s + "No cumple con la restricion de jornada obligatoria anterior por que no existe;");
                        } else {
                            write(s + "No cumple con la restricion de jornada obligatoria anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                        }
                    } else {
                        write(s + "Funcionario No Tiene Posicion Habilitada;" + turn.getPosition().getAlias() + ";");
                    }
                } else {
                    write(s + "Funcionario ya tiene turno programado para ese dia;");
                }
            } else {
                write(s + "No cumple con las restricci?n de los descansos;");
            }
        } else {
            System.out.println(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;");
            write(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
        }
        return Boolean.valueOf(false);
    }

    private boolean satisfiesRestByWeek(Functionary fun, Date current_day) {
        return true;
    }

    private Boolean isHolyDay(Date date) {
        for (DiaFestivo day : this.holydays) {
            if (date.compareTo(day.getDfFecha()) == 0) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    private Boolean satisfiesInitialConditionalExtra(String s, Turn turn, Turn extra, Date current, int hourEval, boolean exceeded) {
        extra.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
        if (turn.getFunctionary().getCanExtra().booleanValue()) {
            if ((!this.setting.isMaxHoursExtra()) || ((hourEval > 0) && (turn.getFunctionary().getMaxHoursExtra() - extra.getPeriod().getTotal() > 0))) {
                if (extra.getPeriod().getStart() >= turn.getPeriod().getEnd()) {
                    if ((this.setting.getMaxWorkedHoursByDay() == -1) || (turn.getPeriod().getTotal() + extra.getPeriod().getTotal() <= this.setting.getMaxWorkedHoursByDay() + 1)) {
                        if (satisfiesMaxContinuosDaysExtra(turn, current, exceeded).booleanValue()) {
                            return Boolean.valueOf(true);
                        }
                        if (exceeded) {
                            write(s + "No cumple con la regla de maximo de dias continuos con horas extras;maximo de dias: " + (this.setting.getMaxContinuosDaysExtra() + this.setting.getMaxContinuosDaysExtraModification()) + ";");
                        } else {
                            write(s + "No cumple con la regla de maximo de dias continuos con horas extras;maximo de dias: " + this.setting.getMaxContinuosDaysExtra() + ";");
                        }
                    } else {
                        write(s + "El funcionario ha sobrepasado el numero de horas maximo que se trabaja al dia;" + this.setting.getMaxWorkedHoursByDay() + ";");
                    }
                } else {
                    write(s + "La jornada del turno extra es igual o anterior a la del turno ordinario;");
                }
            } else if (exceeded) {
                write(s + "El maximo numero de horas extras excedidas, permitido al funcionario se ha sobrepasado;Programadas: " + turn.getFunctionary().getInitialMaxHoursExtraExceeded() + " - Restantes:   " + turn.getFunctionary().getMaxHoursExtraExceeded() + ";");
            } else {
                write(s + "El maximo numero de horas extras, permitido al funcionario se ha sobrepasado;Programadas: " + turn.getFunctionary().getInitialMaxHoursExtra() + " - Restantes:   " + turn.getFunctionary().getMaxHoursExtra() + ";");
            }
        } else {
            write(s + "El funcionario no puede hacer horas extras;");
        }
        return Boolean.valueOf(false);
    }

    private Boolean satisfiesMaxContinuosPeriod(Turn turn, Turn extra, Turn next, Date date) {

        if ((this.setting.getMaxContinuosPeriod() > 0) && (turn != null) && (extra != null) && (turn.getType() == 1)) {
            if (turn.getPeriod().getEnd() == extra.getPeriod().getStart()) {
                if (this.setting.getMaxContinuosPeriod() >= 1) {
                    if ((next != null) && (extra.getPeriod().getEnd() >= 24) && (extra.getPeriod().getEnd() % 24 == next.getPeriod().getStart())) {
                        return Boolean.valueOf(false);
                    }
                } else {
                    return Boolean.valueOf(false);
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesMaxContinuosDaysExtra(Turn turn, Date date) {
        if ((this.setting.getMaxContinuosDaysExtra() > 0) && (turn != null)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int totalLeft = this.setting.getMaxContinuosDaysExtra();
            for (int i = 0; i < this.setting.getMaxContinuosDaysExtra(); i++) {
                Turn previous = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), -1, 2);
                if (previous == null) {
//                    return Boolean.valueOf(true);
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, -1);
            }
            calendar.setTime(date);
            for (int i = 0; i < this.setting.getMaxContinuosDaysExtra(); i++) {
                Turn next = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), 1, 2);
                if (next == null) {
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, 1);
            }
            if (totalLeft > 0) {
                return true;
            }
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesMaxContinuosDaysExtra(Turn turn, Date date, boolean exceed) {
        if ((this.setting.getMaxContinuosDaysExtra() > 0) && (turn != null)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int totalLeft = this.setting.getMaxContinuosDaysExtra();
            if (exceed) {
                totalLeft += this.setting.getMaxContinuosDaysExtraModification();
            }
            int limit = totalLeft;
            for (int i = 0; i < limit; i++) {
                Turn previous = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), -1, 2);
                if (previous == null) {
//                    return Boolean.valueOf(true);
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, -1);
            }
            calendar.setTime(date);
            for (int i = 0; i < limit; i++) {
                Turn next = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), 1, 2);
                if (next == null) {
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, 1);
            }
            if (totalLeft > 0) {
                return true;
            }
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRequiredPeriod(Turn previous, Turn current) {
        if (current != null) {
            if (current.getPeriod().getRequiredPeriod() != -1L) {
                if (previous != null) {
                    if ((previous.getPeriod().getId() == current.getPeriod().getRequiredPeriod()) && (previous.getPosition().getId() == current.getPosition().getId())) {
                        return Boolean.valueOf(true);
                    }
                }
                return Boolean.valueOf(false);
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRestrictionPreviousPeriod(Turn previous, Turn current, boolean validateDiff) {
        if ((previous != null) && (current != null)) {
            if (previous.getPeriod().getAlias().equalsIgnoreCase("N") && current.getPeriod().getAlias().equalsIgnoreCase("M")) {
                System.out.println("validateDiff = " + validateDiff);
            }
            if (previous.getPeriod().getAlias().equalsIgnoreCase("M") && current.getPeriod().getAlias().equalsIgnoreCase("N")) {
                System.out.println("validateDiff = " + validateDiff);
            }

            if ("MRA".equals(previous.getPeriod().getAlias() + previous.getPosition().getAlias()) || "MRA".equals(current.getPeriod().getAlias() + current.getPosition().getAlias())) {
                System.out.println("validateDiff = " + validateDiff);
            }
            JornadaRestriccion result = getRestrictivePeriod(previous.getPeriod().getId(), current.getPeriod().getId());
//          
            if (previous.getPeriod().getAlias().equalsIgnoreCase("N") && current.getPeriod().getAlias().equalsIgnoreCase("M") && result!=null) {
                return false;
            }
            int diffHrs = current.getPeriod().getStart() - (previous.getPeriod().getEnd() - 1);

            if (result != null) {
                if (validateDiff) {
                    return false;
                }

                if (diffHrs < 1) {
                    return false;
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRecessPeriod(Turn previous, Turn current, Boolean same_day) {
        if ((previous != null) && (current != null)) {
            if ((this.setting.getPeriodId() != -1L) && (this.setting.getTimeRecess() != -1)) {
                if (previous.getPeriod().getId() == this.setting.getPeriodId()) {
                    int start = previous.getPeriod().getEnd() + this.setting.getTimeRecess();
                    if (!same_day.booleanValue()) {
                        start %= 24;
                    }
                    if (current.getPeriod().getStart() < start) {
                        return Boolean.valueOf(false);
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRecessPeriod(Turn previous, Turn current, Boolean same_day, Date currentDate) {
        Calendar r = Calendar.getInstance();
        r.setTime(currentDate);

        if ((previous != null) && (current != null)) {
            if ((this.setting.getPeriodId() != -1L) && (this.setting.getTimeRecess() != -1)) {
                if (previous.getPeriod().getId() == this.setting.getPeriodId()) {
                    int start = this.setting.getTimeRecess();
                    if (!same_day.booleanValue()) {
                        start %= 24;
                    }
                    Calendar today = Calendar.getInstance();
                    int year = r.get(Calendar.YEAR);
                    int month = r.get(Calendar.MONTH);
                    int day = r.get(Calendar.DATE);
                    today.set(year, month, day, current.getPeriod().getStart(), 0);

                    Calendar yesterday = Calendar.getInstance();
                    yesterday.setTime(currentDate);
                    yesterday.add(Calendar.DATE, -1);
                    yesterday.set(Calendar.HOUR_OF_DAY, previous.getPeriod().getEnd());
                    yesterday.set(Calendar.MINUTE, 0);

                    long resultado = diferenciaHorasDias(yesterday, today);
                    if (resultado < start) {
                        return Boolean.valueOf(false);
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private long diferenciaHorasDias(Calendar fechaInicial, Calendar fechaFinal) {
        long diferenciaHoras = 0;
        long milisegundos_dia = 24 * 60 * 60 * 1000;
        diferenciaHoras = (fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / milisegundos_dia;
        if (diferenciaHoras > 0) { // Lo Multiplicaos por 24 por que estamos utilizando el formato militar 
            diferenciaHoras *= 24;
        }
        return diferenciaHoras;
    }

    private Boolean satisfiesSequence(Turn previous, Turn current) {
        if ((previous != null) && (current != null)) {
            long sec = -1L;
            long back = -1L;
            for (DetSecuencia ds : this.sequences) {
                if (ds.getSecuencia().getSecuId().longValue() != sec) {
                    sec = ds.getSecuencia().getSecuId().longValue();
                    back = -1L;
                }
                if ((ds.getJornada().getJoId().longValue() == current.getPeriod().getId()) && (previous.getPeriod().getId() == back)) {
                    return Boolean.valueOf(true);
                }
                back = ds.getJornada().getJoId().longValue();
            }
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesInactivePosition(Turn current, Date fecha) {
        if ((current != null)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);

            //System.out.println("pj -> " + current.getPositionPeriod());
            //System.out.println("new SimpleDateFormat() = " + new SimpleDateFormat("dd/MM/yyyy").format(fecha));
            List<PosicionInactiva> inactive = getInactivePositions(current.getPositionPeriod(), fecha);
            if (inactive != null) {
                for (PosicionInactiva inactiva : inactive) {
                    String fechaBd = new SimpleDateFormat("dd/MM/yyyy").format(inactiva.getFecha());
                    String actual = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
                    if (actual.equalsIgnoreCase(fechaBd)) {
                        return false;
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private boolean isJornadaNoLaboral(Turn turn, Functionary fun) {
        List<Long> ids = this.jornadasNoLaboralesPorUsuario.get(fun.getId());
        if (ids != null && ids.size() == this.jornadasDependencia.size()) {
            return true;
        }
        if (ids != null && ids.size() < this.jornadasDependencia.size()) {
            for (Long noLabJoId : ids) {
                long currentJoId = turn.getPeriod().getId();
                if (currentJoId == noLabJoId) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean satisfiesPetitions(Turn current, Date fecha, Functionary fun) {

        /*if (fun.getAlias().equalsIgnoreCase("ICG") && current != null && current.getPeriod().getId() == 427) {
         System.out.println("poscision");
         }*/
        if ((current != null)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            if (current.getFunctionary() == null) {
                List<PermisoEspecial> petitions = getPetitions(fun.getId(), fecha);
                for (PermisoEspecial especial : petitions) {
                    List<Jornada> listaJornadas = especial.getListaJornadas();
                    if (listaJornadas.size() == this.jornadasDependencia.size()) {
                        return false;
                    }
                    if (listaJornadas.size() < this.jornadasDependencia.size()) {
                        for (Jornada j : listaJornadas) {
                            if (current.getPeriod().getId() == j.getJoId()) {
                                return false;
                            }
                        }
                    }
                }
                //valida si tiene jornadas no laborales
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesEnabledPosition(long funId, long posId) {
        if (this.enabledPositionsFunctionaryMap == null) {
            enabledPositionsFunctionaryMap = getEnabledPositionsFunctionaryMap(this.programming.getDependencia().getDepId());
        }
        List<Long> positionsIdsList = this.enabledPositionsFunctionaryMap.get(funId);
        if (positionsIdsList != null) {
            for (Long idEp : positionsIdsList) {
                if (idEp == posId) {
                    return Boolean.valueOf(true);
                }
            }
        }
        return Boolean.valueOf(false);
    }

    private Boolean satisfiesEnabledSpecialPosition(long funId, long posId) {
        List<TurnoEspFuncionario> tes = getSpecialTurns();
        for (TurnoEspFuncionario te : tes) {
            if ((te.getTefId().longValue() == posId) && (te.getFuncionario().getFunId().longValue() == funId)) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    private Turn getTurnLastDayFromLastProg(Functionary fun) {
        this.lastDay = getLastDay();
        if (this.lastDay != null) {
            for (Turn turn : this.lastDay.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                    return turn;
                }
            }
        }
        return null;
    }

    private Turn getTurnLastDayFromLastProgPrevMonth(Functionary fun) {
        this.lastDay = getLastDayPrevMonth();
        if (this.lastDay != null) {
            for (Turn turn : this.lastDay.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                    return turn;
                }
            }
        }
        return null;
    }

    private Turn getTurnOfFunctionary(Functionary fun, Date current, int amountDay, int type) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        String d = new SimpleDateFormat("dd/MM/yyyy").format(eval.getTime());
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : turnSortByType(day.getTurns())) {
                    if (type == -1) {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.ANY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((turn.getType() == ProgramacionTotalServiceBean.Type.ORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((turn.getType() == ProgramacionTotalServiceBean.Type.EXTRAORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                    } else if (type == -2) {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                            return turn;
                        }
                    } else {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.ORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.TROP) || (turn.getType() == type))) {
                            return turn;
                        }

                    }
                }
            }
        }
        return null;
    }

    private void setNextTurnToExtra(Functionary fun, Date current, int amountDay, int type) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : turnSortByType(day.getTurns())) {
                    if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((turn.getType() == Type.ORDINARY) || (turn.getType() == type))) {
                        turn.setType(Type.EXTRAORDINARY);
                        return;
                    }
                }
            }
        }
    }

    private void replaceTurnOfFunctionary(Functionary fun, Date current, int amountDay, int type, Turn newTurn) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : turnSortByType(day.getTurns())) {
                    if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && (turn.getType() == type)) {
                        turn = newTurn;
                    }
                }
            }
        }
    }

    private Turn getTurnTropOfFunctionary(Functionary fun, Date current, int amountDay, int type) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : turnSortByType(day.getTurns())) {

                    if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && (type == ProgramacionTotalServiceBean.Type.TROP) && (turn.getPosition().getAlias().equals("TROP"))) {
                        return turn;
                    }

                }
            }
        }
        return null;
    }

    private void setFunctionariesBusy(ArrayList<Turn> turns) {
        for (Turn turn : turns) {
            if (turn.getFunctionary() != null) {
                for (Functionary fun : this.functionaries) {
                    if (fun.getId() == turn.getFunctionary().getId()) {
                        fun.setAvailable(Boolean.valueOf(false));
                        break;
                    }
                }
            }
        }
        Turn turn;
    }

    private void setFunctionariesAvailable() {
        for (Functionary fun : this.functionaries) {
            fun.setAvailable(Boolean.valueOf(true));
        }
    }

    private void setFunctionariesAvailable(List<Functionary> funcionaries) {
        for (Functionary fun : funcionaries) {
            fun.setAvailable(Boolean.valueOf(true));
        }
    }

    private int numEnables(long pos) {
        int cont = 0;
        for (PosicionHabilitada ph : this.enabledPositions) {
            if (ph.getPosicion().getPosId().longValue() == pos) {
                cont++;
            }
        }
        return cont;
    }

    private List<Jornada> getJornadas() {
        try {
            Query query = this.em.createQuery("SELECT j FROM Jornada j WHERE j.dependencia.depId = :depId and j.joEstado = :estado ORDER BY j.joHoraInicio ASC");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            //System.out.println("queryJornadas = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Jornada getJornadaM() {
        try {
            Query query = this.em.createQuery("SELECT j FROM Jornada j WHERE j.joAlias = :alias and j.dependencia.depId = :depId and j.joEstado = :estado ORDER BY j.joHoraInicio ASC");
            query.setParameter("alias", "M");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            //System.out.println("queryJornadas = " + query.toString());
            return (Jornada) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    private List<PosicionJornada> getPositionPeriods() {
        try {
            Query query = this.em.createQuery("SELECT p FROM PosicionJornada p WHERE p.dependencia.depId = :depId and p.jornada.dependencia.depId = :depId and p.posicion.dependencia.depId = :depId and p.pjEstado= :estado and p.posicion.posEstado= :estado");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private PosicionJornada getPositionPeriod(Long id) {
        return (PosicionJornada) this.em.find(PosicionJornada.class, id);
    }

    private ParametroSistema getSystemParameter(BigDecimal id) {
        return (ParametroSistema) this.em.find(ParametroSistema.class, id);
    }

    private TurnoEspFuncionario getSpecialTurn(Long id) {
        return (TurnoEspFuncionario) this.em.find(TurnoEspFuncionario.class, id);
    }

    private List<PosicionHabilitada> getEnabledPositions() {
        try {
            Query query = this.em.createQuery("SELECT p FROM PosicionHabilitada p WHERE p.funcionario.dependencia.depId = :depId and p.posicion.posEstado= :estado and p.posicion.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            //System.out.println("queryPH = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<PosicionHabilitada> getEnabledPositionsFunctionary(Long funId, Long posId) {
        try {
            Query query = em.createQuery("SELECT p FROM PosicionHabilitada p WHERE p.funcionario.funId = :funId and p.posicion.posEstado= :estado and p.posicion.posId = :posId ");
            query.setParameter("funId", funId);
            query.setParameter("estado", "Activo");
            query.setParameter("posId", posId);
            return (List<PosicionHabilitada>) query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PosicionHabilitada>();
        }

    }

    private Map<Long, List<Long>> getEnabledPositionsFunctionaryMap(Long depId) {
        try {
            Map<Long, List<Long>> enabledPositions = new HashMap<Long, List<Long>>();
            String query = "SELECT ph.ph_funcionario,LISTAGG(ph.ph_posicion,',') within group(order by ph.ph_posicion) FROM POSICION_HABILITADA ph, "
                    + "POSICION p, "
                    + "funcionario f "
                    + "WHERE p.POS_ESTADO = 'Activo' "
                    + "AND p.POS_ID = ph.PH_POSICION "
                    + "AND ph.ph_funcionario = f.fun_id "
                    + "AND f.fun_dependencia = " + depId + " "
                    + "GROUP BY ph.ph_funcionario";
            //Query query = em.createQuery("SELECT p FROM PosicionHabilitada p WHERE p.funcionario.funId = :funId and p.posicion.posEstado= :estado and p.posicion.posId = :posId ");
            Query q = em.createNativeQuery(query);
            List<Object[]> result = q.getResultList();
            for (Object[] row : result) {
                List<Long> ids = new ArrayList<Long>();
                String[] data = ((String) row[1]).split(",");
                for (String id : data) {
                    ids.add(new BigDecimal(id).longValue());
                }
                enabledPositions.put(((BigDecimal) row[0]).longValue(), ids);
            }
            return enabledPositions;
        } catch (NoResultException nre) {
            return new HashMap<Long, List<Long>>();
        }

    }

    private List<Funcionario> getFunctionaries() {
        try {
            Calendar actual = Calendar.getInstance();
            Query query = this.em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId and f.funEstado= :estado and f.funFvCertmedico > :fecha and f.funFvCurso> :fecha and f.funFvEvaluacion> :fecha and f.fuNivel IN (2,3,4,5,6) and  f.funId IN (SELECT p.funcionario.funId FROM PosicionHabilitada p WHERE p.funcionario.dependencia.depId = :depId and p.posicion.posEstado= :estado and p.posicion.dependencia.depId = :depId)  ");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<PermisoEspecial> getPetitions(Long funId, Date current) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(current);
            Query query = this.em.createNamedQuery("PermisoEspecial.findByUser");
            query.setParameter("funId", funId);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<PosicionInactiva> getInactivePositions(Long pjId, Date fecha) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(fecha);
            Query query = this.em.createQuery("SELECT p FROM PosicionInactiva p WHERE p.posicionJornada.pjId = :pjId");
            query.setParameter("pjId", pjId);
            //System.out.println("queryIP = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<JornadaRestriccion> getRestrictivePeriod() {
        try {
            Query query = this.em.createQuery("SELECT j FROM JornadaRestriccion j WHERE j.jornada.dependencia.depId = :depId AND j.jornadaEv.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private JornadaRestriccion getRestrictivePeriod(Long prev, Long current) {
        try {
            Query query = this.em.createNamedQuery("JornadaRestriccion.getRestrictions");
            query.setParameter("prev", prev);
            query.setParameter("current", current);

            return (JornadaRestriccion) query.getSingleResult();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<TurnoEspFuncionario> getSpecialTurns() {
        try {
            Calendar actual = Calendar.getInstance();
            Query query = this.em.createQuery("SELECT t FROM TurnoEspFuncionario t WHERE t.turnoEspecial.dependencia.depId= :depId AND t.tefEstado= :estado AND t.funcionario.dependencia.depId = :depId AND t.funcionario.funFvCertmedico > :fecha AND t.funcionario.funFvCurso > :fecha AND t.funcionario.funFvEvaluacion > :fecha AND t.funcionario.funEstado = 'Activo' AND ((t.tefFini between :start and :finish) or (t.tefFfin between :start and :finish)) ");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Programado");
            query.setParameter("start", this.programming.getProFechaInicio(), TemporalType.DATE);
            query.setParameter("finish", this.programming.getProFechaFin(), TemporalType.DATE);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            //System.out.println("query = " + query.toString());

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<RestriccionDependencia> getRestrictions() {
        try {
            Query query = this.em.createQuery("SELECT r FROM RestriccionDependencia r WHERE r.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<Turno> getRestLastWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.programming.getProFechaInicio());
        c.set(7, 2);
        c.add(5, -1);

        //System.out.println("Entre " + c.get(5) + "/" + (c.get(2) + 1));
        try {
            Query query = this.em.createQuery("SELECT t FROM Turno t WHERE t.programacion.proEstado=1 and t.programacion.dependencia.depId = :depId AND t.turTipo= :turTipo AND (t.turFecha between :start and :finish) ");

            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("turTipo", Integer.valueOf(5));
            query.setParameter("start", c.getTime(), TemporalType.DATE);
            query.setParameter("finish", this.programming.getProFechaInicio(), TemporalType.DATE);

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<Turno> getTurnosFunPro(long funId, long proId) {
        try {
            this.em.clear();
            this.em.flush();

            Query q1 = this.em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId = :funId AND t.programacion.proId = :proId");

            q1.setParameter("proId", Long.valueOf(proId));
            q1.setParameter("funId", Long.valueOf(funId));

            return q1.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<DiaFestivo> getDiaFestivo() {
        try {
            Query query = this.em.createQuery("SELECT d FROM DiaFestivo d WHERE d.dfFecha between :start and :finish");
            query.setParameter("start", this.programming.getProFechaInicio(), TemporalType.DATE);
            query.setParameter("finish", this.programming.getProFechaFin(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<DetSecuencia> getSequences() {
        try {
            Query query = this.em.createQuery("SELECT s FROM DetSecuencia s WHERE s.secuencia.dependencia.depId=:depId AND s.secuencia.secuEstado = :estado ORDER BY s.secuencia.secuId, s.dsOrden");
            query.setParameter("estado", "Activo");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Day getLastDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.programming.getProFechaInicio());
        c.add(5, -1);
        try {
            Query query = this.em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId is not null and t.programacion.dependencia.depId = :depId AND t.turFecha= :fecha AND t.programacion.proEstado = 1 AND (t.turTipo =2 OR ( t.turTipo =1 AND 0=(SELECT count(tt) FROM Turno tt WHERE tt.funcionario.funId is not null and t.funcionario.funId = tt.funcionario.funId AND t.turFecha=tt.turFecha AND t.turId <> tt.turId ) ) )");

            query.setParameter("fecha", c.getTime(), TemporalType.DATE);
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            List<Turno> turnos = query.getResultList();

            ArrayList<Turn> turns = new ArrayList();
            for (Turno turno : turnos) {
                if (turno.getFuncionario() != null) {
                    Calendar f = Calendar.getInstance();
                    f.setTime(turno.getTurFecha());

                    PosicionJornada pj = (PosicionJornada) this.em.find(PosicionJornada.class, turno.getTurPosicionJornada());
                    Period period = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                    Position position = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                    Functionary fun = new Functionary(turno.getFuncionario());
                    Turn turn = new Turn(period, position, fun, turno.getTurPosicionJornada().longValue());
                    turns.add(turn);
                }
            }
            return new Day(c.getTime(), turns);
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Day getLastDayPrevMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.programming.getProFechaInicio());
        c.add(5, -1);
        try {
            Query query = this.em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId is not null and t.programacion.dependencia.depId = :depId AND t.turFecha= :fecha AND t.programacion.proEstado = 1 AND (t.turTipo =2 OR t.turTipo =1)");

            query.setParameter("fecha", c.getTime(), TemporalType.DATE);
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            List<Turno> turnos = query.getResultList();

            ArrayList<Turn> turns = new ArrayList();
            for (Turno turno : turnos) {
                if (turno.getFuncionario() != null) {
                    Calendar f = Calendar.getInstance();
                    f.setTime(turno.getTurFecha());

                    PosicionJornada pj = (PosicionJornada) this.em.find(PosicionJornada.class, turno.getTurPosicionJornada());
                    Period period = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                    Position position = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                    Functionary fun = new Functionary(turno.getFuncionario());
                    Turn turn = new Turn(period, position, fun, turno.getTurPosicionJornada().longValue());
                    turns.add(turn);
                }
            }
            return new Day(c.getTime(), turns);
        } catch (NoResultException nre) {
        }
        return null;
    }

    public void persist(Object object) {
        this.em.persist(object);
    }

    private void debug_completed() {
        //System.out.print("\n\tHorario Ordinario\n\n\tFun");
        for (Day day : this.days) {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(day.getDate());
            //System.out.print("\t" + fecha.get(5) + "/" + fecha.get(2));
        }
        //System.out.println("");
        for (Functionary fun : this.functionaries) {
            //System.out.print("\t" + fun.getAlias());
            for (Day day : this.days) {
                boolean flag = false;
                for (Turn turn : day.getTurns()) {
                    if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                        if (turn.getType() != 2) {
                            flag = true;
                            if (turn.getType() == 1) {
                                //System.out.print("\t" + turn.getPeriod().getAlias() + turn.getPosition().getAlias());
                            }
                            if (turn.getType() == 3) {
                                //System.out.print("\t(Esp)");
                            }
                            if (turn.getType() == 5) {
                                //System.out.print("\t(Des)");
                            }
                        }
                    }
                }
                if (!flag) {
                    //System.out.print("\t   ");
                }
            }
            //System.out.println("");
        }
        //System.out.println("\n");

        //System.out.print("\n\tHorario ExtraOrdinario\n\n\tFun");
        for (Day day : this.days) {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(day.getDate());
            //System.out.print("\t" + fecha.get(5) + "/" + fecha.get(2));
        }
        //System.out.println("");
        for (Functionary fun : this.functionaries) {
            //System.out.print("\t" + fun.getAlias());
            for (Day day : this.days) {
                boolean flag = false;
                for (Turn turn : day.getTurns()) {
                    if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                        if (turn.getType() == 2) {
                            flag = true;

                            //System.out.print("\t" + turn.getPeriod().getAlias() + turn.getPosition().getAlias());
                        }
                    }
                }
                if (!flag) {
                    //System.out.print("\t   ");
                }
            }
            //System.out.println("");
        }
        //System.out.println("\n");
    }

    private void debug_missed() {
        write_data("No Asignados");
        write_data("Fecha;Posiciones;");
        int missed = 0;
        int ordinary = 0;
        int extra = 0;
        int special = 0;
        int rest = 0;
        int total = 0;
        for (Day day : this.days) {
            Calendar date = Calendar.getInstance();
            date.setTime(day.getDate());
            String row = date.get(5) + "/" + (date.get(2) + 1) + ";";
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() == null) {
                    row = row + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";";
                    missed++;
                } else {
                    if (turn.getType() == 1) {
                        ordinary++;
                    }
                    if (turn.getType() == 2) {
                        extra++;
                    }
                    if (turn.getType() == 3) {
                        special++;
                    }
                    if (turn.getType() == 5) {
                        rest++;
                    }
                }
                total++;
            }
            write_data(row);
        }
        write_data("");
        write_data("Tipo;Total;");
        write_data("Ordinarios;" + ordinary);
        write_data("Extra;" + extra);
        write_data("Especiales;" + special);
        write_data("Descansos;" + rest);
        write_data("No Asignados;" + missed);
        write_data("Total;" + total);
        write_data("");
        write_data("");
    }

    private void write(String s) {
        if (this.debug.booleanValue()) {
            this.log.add(s);
        }
    }

    private void write_data(String s) {
        if (this.debug.booleanValue()) {
            this.data.add(s);
        }
    }

    private void finish() {
        if (this.debug.booleanValue()) {
            try {
                File fout = new File("out_" + this.programming.getDependencia().getDepAbreviatura() + ".csv");
                FileOutputStream fous = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fous));
                for (String s : this.data) {
                    bw.write(s, 0, s.length());
                    bw.newLine();
                }
                for (String s : this.log) {
                    bw.write(s, 0, s.length());
                    bw.newLine();
                }
                bw.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProgramacionTotalServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ProgramacionTotalServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void refiningByDay() {
        /**
         * .|.
         */
        Calendar c = Calendar.getInstance();
        for (Day day : this.days) {

            ArrayList<Turn> no_asigns = new ArrayList();
            ArrayList<Functionary> no_ordins = new ArrayList();

            c.setTime(day.getDate());
            for (Turn turn : day.getTurns()) {
                if (turn.getFunctionary() == null) {
                    no_asigns.add(turn);
                }
            }
            for (Functionary fun : this.functionaries) {
                boolean es_nulo = true;
                for (Turn turn : day.getTurns()) {

                    if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                        es_nulo = false;
                        break;
                    }
                }
                if (es_nulo) {
                    fun.setAvailable(true);
                    no_ordins.add(fun);
                }
            }

            for (Turn no_asign : no_asigns) {
                if (no_asign.getFunctionary() == null) {
                    for (Functionary fun : no_ordins) {
                        if (fun.isAvailable()) {
                            Turn next = getTurnOfFunctionary(fun, day.getDate(), ProgramacionTotalServiceBean.TargetDay.NEXT, ProgramacionTotalServiceBean.Type.ORDINARY);
                            if (next == null || (next != null && next.getPeriod().getRequiredPeriod() == ProgramacionTotalServiceBean.Type.ANY)) {
                                if (validateOrdinaryTurn(fun, no_asign, day.getDate()).booleanValue()) {
                                    no_asign.setFunctionary(fun);
                                    no_asign.setType(ProgramacionTotalServiceBean.Type.ORDINARY);
                                    fun.setAvailable(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Turn> turnSortByType(List<Turn> turns) {
        Turn[] auxArr = new Turn[turns.size()];
        auxArr = turns.toArray(auxArr);

        Turn temp = null;

        for (int i = 0; i < (auxArr.length - 1); i++) {
            for (int j = 1; j < (auxArr.length - i - 1); j++) {
                if (auxArr[j + 1].getType() < auxArr[j].getType()) {
                    //swap the elements!
                    temp = auxArr[j + 1];
                    auxArr[j + 1] = auxArr[j];
                    auxArr[j] = temp;
                }
            }
        }
        List<Turn> respuesta = Arrays.asList(auxArr);
        return respuesta;
    }
}
