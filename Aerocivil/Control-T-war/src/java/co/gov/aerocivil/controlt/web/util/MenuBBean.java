/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

import co.gov.aerocivil.controlt.entities.Menu;
import java.util.List;
import javax.el.ExpressionFactory;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.event.NodeCollapseEvent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class MenuBBean {

    private MenuModel model;
    private FacesContext fc;
    private ExpressionFactory ef;
    private TreeNode menu;
    private Menu selectedOption;
    List<Menu> lista;

    public TreeNode getMenu() {
        return menu;
    }

    public MenuBBean() {
        fc = JsfUtil.getFacesContext();
        ef = fc.getApplication().getExpressionFactory();
        model = new DefaultMenuModel();

        //First submenu  
        //Submenu submenu = new Submenu();
        lista = JsfUtil.getListadosBBean().getListaMenu(JsfUtil.getFuncionarioSesion().getFuNivel());

        menu = new DefaultTreeNode("Root", null);
        TreeNode father = null;
        for (Menu m : lista) {
            if (m.getMenMetodo() == null || "".equals(m.getMenMetodo())) {
                TreeNode nodo = new DefaultTreeNode(m, menu);
                father = nodo;
            } else {

                HtmlCommandLink edLink = new HtmlCommandLink();
                edLink.setValueExpression("value", JsfUtil.createValueExpression(m.getMenLabel(), String.class));
                edLink.setActionExpression(ef.createMethodExpression(fc.getELContext(),
                        "#{" + m.getMenMetodo() + "}", String.class, new Class<?>[0]));
                /*edLink.addActionListener(new SetPropertyActionListener(JsfUtil.createValueExpression("#{registroBBean.registro}", RegistroTO.class), 
                 JsfUtil.createValueExpression("#{registro}", RegistroTO.class)	));*/
                TreeNode nodo2 = new DefaultTreeNode(m, father);
            }
        }


    }

    public MenuModel getModel() {
        return model;
    }

    private MenuItem createMenuItem(Menu m) {
        MenuItem item = new MenuItem();
        item.setValue(m.getMenLabel());
        item.setActionExpression(ef.createMethodExpression(fc.getELContext(),
                "#{" + m.getMenMetodo() + "}", String.class, new Class<?>[0]));
        item.setImmediate(true);
        item.setAjax(false);
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
        List<TreeNode> menuOpt = menu.getChildren();
        menuOpt.get(0).setExpanded(true);
        /*List<TreeNode> subMenu = menuOpt.get(0).getChildren();
         subMenu.get(0).setSelected(true);*/
        //this.selectedOption

        selectedOption = lista.get(1);
        //selectedOption.setMenMetodo("vencimientosBBean.listar");            
        try {
            String[] regla = selectedOption.getMenMetodo().split("\\.");
            return JsfUtil.ejecutarNavegacion(regla[0], regla[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return "logedIn";
        }
    }
}