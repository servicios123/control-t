/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.manager;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.services.ProgramacionTotalService;
import co.gov.aerocivil.controlt.to.FuncionarioTO;

/**
 *
 * @author juank
 */
public class ProgrammeHandler implements Runnable{

    private Programacion programacion;
    private ProgramacionTotalService programacionTotalService;
    private boolean debug;
    private Funcionario funcionario;

    public ProgrammeHandler(Programacion programacion, ProgramacionTotalService programacionTotalService, boolean debug, Funcionario funcionario) {
        this.programacion = programacion;
        this.programacionTotalService = programacionTotalService;
        this.debug = debug;
        this.funcionario = funcionario;
    }
    
    
    
    @Override
    public void run() {
        programacionTotalService.run(programacion, funcionario, debug);
    }
    
}
