/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

import co.gov.aerocivil.controlt.entities.Menu;
import java.util.List;
import javax.el.ExpressionFactory;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Administrador
 */
@ManagedBean
@ViewScoped
public class MenuBBean {

    private MenuModel model;
    private FacesContext fc;
    private ExpressionFactory ef;
    private DefaultSubMenu menu;
    private Menu selectedOption;
    List<Menu> lista;

    public DefaultSubMenu getMenu() {
        return menu;
    }

    public MenuBBean() {
        fc = JsfUtil.getFacesContext();
        ef = fc.getApplication().getExpressionFactory();
        model = new DefaultMenuModel();

        //First submenu  
        //Submenu submenu = new Submenu();
        lista = JsfUtil.getListadosBBean().getListaMenu(JsfUtil.getFuncionarioSesion().getFuNivel());

        menu = new DefaultSubMenu();
        DefaultSubMenu parent = null;
        for (Menu m : lista) {
            if (m.getMenMetodo() == null || "".equals(m.getMenMetodo())) {
                if (parent != null) {
                    model.addElement(parent);
                }
                parent = new DefaultSubMenu(m.getMenLabel());
                parent.setExpanded(false);
            } else {
                DefaultMenuItem item = createMenuItem(m);
                parent.addElement(item);
            }
        }
        if (parent != null) {
            model.addElement(parent);
        }
//        panelMenu.setModel(model);
    }

    public MenuModel getModel() {
        return model;
    }

    private DefaultMenuItem createMenuItem(Menu m) {
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue(m.getMenLabel());
//        item.setUrl(ef.createMethodExpression(fc.getELContext(),
//                "#{" + m.getMenMetodo() + "}", String.class, new Class<?>[0]).getExpressionString());
        item.setCommand("#{" + m.getMenMetodo() + "}");
        item.setImmediate(true);
        item.setAjax(false);
        item.setAsync(false);
        item.setGlobal(false);
        item.setPartialSubmit(false);
        item.setResetValues(false);
        return item;
    }

    /**
     * Este metodo es ejecutado desde la página y ejecuta el método respectivo
     * en el BBean correspondiente
     *
     * @return
     */
    public String gotoOpcion() {
        removeFromSession();
        String[] regla = selectedOption.getMenMetodo().split("\\.");
        return JsfUtil.ejecutarNavegacion(regla[0], regla[1]);
    }

    private void removeFromSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("diarioSenalEspecialBBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("turnoEspFuncionarioBBean");
    }

    public Menu getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Menu selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        if (event != null && event.getTreeNode() != null) {
            event.getTreeNode().setExpanded(false);
        }
    }

    public String gotoInicio() {
        DefaultSubMenu main = (DefaultSubMenu) model.getElements().get(0);
        DefaultMenuItem item = (DefaultMenuItem) main.getElements().get(0);

        main.setExpanded(true);
        //selectedOption.setMenMetodo("vencimientosBBean.listar"); 
        selectedOption = lista.get(1);
//
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie[] cookies = request.getCookies();
        Cookie opentoken = null;
        for (Cookie c : cookies) {
            if(c.getName().contains("myTree")){
                opentoken = c;
                opentoken.setMaxAge(0);
                opentoken.setValue(""); // it is more elegant to clear the value but not necessary
                opentoken.setPath("/");
                response.addCookie(opentoken);
            }
        }
        try {
            String[] url = item.getCommand().replaceAll("#", "").replaceAll("\\{", "").replaceAll("}", "").split("\\.");
            return JsfUtil.ejecutarNavegacion(url[0], url[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return "logedIn";
        }
    }
}
