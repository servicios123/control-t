package co.gov.aerocivil.controlt.web.util;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.to.FuncionarioTO;
import co.gov.aerocivil.controlt.web.bbeans.TempVerProgramacionBBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.primefaces.context.RequestContext;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String messageId, String... params) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageId, params);
    }

    public static void addSuccessMessage(String messageId, String... params) {
        addMessage(FacesMessage.SEVERITY_INFO, messageId, params);
        //FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addManualSuccessMessage(String messageDescription) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, messageDescription, messageDescription);
        getFacesContext().addMessage(null, mensaje);
    }

    public static void addWarningMessage(String messageId, String... params) {
        addMessage(FacesMessage.SEVERITY_WARN, messageId, params);
    }

    public static void addManualWarningMessage(String messageDescription) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, messageDescription, messageDescription);
        getFacesContext().addMessage(null, mensaje);
    }

    private static void addMessage(FacesMessage.Severity severity, String messageId, String... params) {
        String messageDescription = formatMessage(messageId, params);
        FacesMessage mensaje = new FacesMessage(severity, messageDescription, messageDescription);
        getFacesContext().addMessage(null, mensaje);
    }

    /**
     * retorna un mensaje formateado para ser mostrado en la vista
     *
     * @param messageId
     * @param params
     * @return
     */
    public static String formatMessage(String messageId, String... params) {
        String messageDescription = getMessageFromBundle(messageId);
        if (params != null) {
            MessageFormat mssgFormat = new MessageFormat(messageDescription);
            messageDescription = mssgFormat.format(params);
        }
        return messageDescription;
    }

    public static String getMessageFromBundle(String messageId) {
        return ResourceBundle.getBundle("/messages").getString(messageId);
    }

    public static String getRequestParameter(String key) {
        return getFacesContext().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(getFacesContext(), component, theId);
    }

    public static HttpServletRequest getServletRequest() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
        return httpServletRequest;
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static Funcionario getFuncionarioSesion() {
        return ((FuncionarioTO) getServletRequest().getSession().getAttribute("FuncionarioTO")).getFuncionario();
    }

    public static void forceRefresh() {
        FacesContext context = getFacesContext();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);
        root.setViewId(viewId);
        context.setViewRoot(root);
    }

    public static Object getManagedBean(Class classBBean) {
        String klassBBeanFacesCtx = classBBean.getSimpleName().substring(0, 1).toLowerCase()
                + classBBean.getSimpleName().substring(1);
        return getManagedBean(klassBBeanFacesCtx);
    }

    private static String getJsfEl(String value) {
        return (new StringBuilder()).append("#{").append(value).append("}").toString();
    }

    private static ValueBinding getValueBinding(String el) {
        return getApplication().createValueBinding(el);
    }

    private static Application getApplication() {
        ApplicationFactory appFactory =
                (ApplicationFactory) FactoryFinder.getFactory("javax.faces.application.ApplicationFactory");
        return appFactory.getApplication();
    }

    public static ListadosBBean getListadosBBean() {
        return ((ListadosBBean) JsfUtil.getManagedBean(ListadosBBean.class));
    }

    public static ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), valueExpression, valueType);
    }

    public static Object getManagedBean(String bBname) {
        return JsfUtil.getValueBinding(
                JsfUtil.getJsfEl(bBname)).
                getValue(FacesContext.getCurrentInstance());
    }

    public static RequestContext getRequestContext() {
        return RequestContext.getCurrentInstance();
    }

    public static String ejecutarNavegacion(String bBname, String method) {
        String retorno = null;
        if (bBname != null) {
            Object bbean = JsfUtil.getManagedBean(bBname);

            if (bbean != null) {
                for (Method m : bbean.getClass().getMethods()) {
                    if (m.getName().equals(method)) {
                        try {
                            Object ret = m.invoke(bbean);
                            if (ret instanceof String) {
                                retorno = (String) ret;
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        JsfUtil.forceRefresh();
        return retorno;
    }

    public static Object getConnection() {
        /*Connection connection = null;
         try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         String oracleURL = "jdbc:oracle:thin:@localhost:1521:aerocivil2";
         connection = DriverManager.getConnection(oracleURL, "us_aerocivil", "W3bfactory");
         connection.setAutoCommit(false);
         } catch (SQLException exception) {
         exception.printStackTrace();
         }
         catch(Exception e){
         e.printStackTrace();
         }
         return connection;*/

        return getListadosBBean().getConnection();

    }

    public static String getReportPath(String reportFileName) {


        String reportPath;


        // -- original -- //
        reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/" + reportFileName + ".jasper");
        if (reportPath == null || reportPath.equals("")) {
            //-- plan A --//
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            reportPath = servletContext.getRealPath("/jasper/" + reportFileName + ".jasper");

            if (reportPath == null || reportPath.equals("")) {
                //-- plan B -- //
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                reportPath = session.getServletContext().getRealPath("/jasper/" + reportFileName + ".jasper");

                if (reportPath == null || reportPath.equals("")) {
                    // -- plan C -- //
                    HttpSession sess = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    reportPath = sess.getServletContext().getRealPath("/jasper/" + reportFileName + ".jasper");

                    if (reportPath == null || reportPath.equals("")) {
                        // -- plan D -- //
                        HttpSession se = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
                        reportPath = se.getServletContext().getRealPath("/jasper/" + reportFileName + ".jasper");

                        if (reportPath == null || reportPath.equals("")) {
                            // -- local -- //
                            //reportPath = "D:\\controlT\\Control-T\\Control-T-war\\web\\jasper\\" + reportFileName + ".jasper";
                            //reportPath = "D:\\Oracle\\Produccion\\domains\\Aplicaciones_ADF_Produccion\\servers\\AppCtrlTSvr1\\tmp\\_WL_user\\Control-T\\6nfji8\\war\\jasper\\"+reportFileName+".jasper";
                            //reportPath = "D:\\controlT\\Control-T\\dist\\wldeploy\\C546546ontrol-T\\Control-T-war.war\\jasper\\" + reportFileName +  ".jasper";
                            // reportPath= "D:\\Oracle\\Middleware\\user_projects\\domains\\aplicaciones_ADF\\servers\\AppCtrlTSvr1\\tmp\\_WL_user\\Control-T\\6nfji8\\war\\jasper\\"+reportFileName+".jasper";
                            // reportPath="D:\\Oracle\\domains\\Aplicaciones_ADF_Pruebas\\servers\\AppCtrlTSvr1\\tmp\\_WL_user\\Control-T\\6nfji8\\war\\jasper\\"+reportFileName+".jasper";
                            //reportPath= "D:\\Oracle\\Middleware\\user_projects\\domains\\aplicaciones_ADF\\servers\\AdminServerADF\\tmp\\.appmergegen_1426886770819\\Control-T.ear\\le5i9l\\jasper\\"+reportFileName+".jasper";
                            //reportPath= "D:\\Oracle\\Produccion\\domains\\aplicaciones_ADF_Produccion\\servers\\AdminServerADF\\tmp\\.appmergegen_1426171493662\\Control-T.ear\\le5i9l\\jasper\\"+reportFileName+".jasper";
                            reportPath = "D:\\Oracle\\Produccion\\domains\\aplicaciones_ADF_Produccion\\servers\\AdminServerADF\\tmp\\.appmergegen_1425063840511\\Control-T.ear\\le5i9l\\jasper\\" + reportFileName + ".jasper";

                            //System.out.println("Ruta Local\t"+reportPath);                            
                        } else {
                            //System.out.println("Ruta Plan D\t"+reportPath);
                        }
                    } else {
                        //System.out.println("Ruta Plan C\t"+reportPath);
                    }
                } else {
                    //System.out.println("Ruta Plan B\t"+reportPath);
                }

            } else {
                //System.out.println("Ruta Plan A\t"+reportPath);
            }

        } else {
            //System.out.println("Ruta Original\t"+reportPath);
        }


        return reportPath;

    }

    public static void salidaReporte(ArrayList<JasperPrint> listado, String reportFileName) {
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + reportFileName + "(" + DateUtil.formatDate(new Date()) + ").pdf\"");
        OutputStream os = null;
        try {
            os = response.getOutputStream();

            JRExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, listado);
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

            exporter.exportReport();
            os.flush();
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            //FacesUtils.addErrorMessage("Archivo no encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            //FacesUtils.addErrorMessage("I/O Problem");
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e2) {
            }
        }

        getFacesContext().responseComplete();


    }

    public static void salidaReporteXls(ArrayList<JasperPrint> listado, String reportFileName) {
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        response.setContentType("application/xls");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + reportFileName + "(" + DateUtil.formatDate(new Date()) + ").xls\"");
        OutputStream os = null;
        try {
            os = response.getOutputStream();

            JRExporter exporter = new JRXlsExporter();

            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, listado);
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

            exporter.exportReport();
            os.flush();
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            //FacesUtils.addErrorMessage("Archivo no encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            //FacesUtils.addErrorMessage("I/O Problem");
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e2) {
            }
        }

        getFacesContext().responseComplete();


    }

    public static void generaReporte(String reportFileName, HashMap<String, Object> map, List listaDS) {
        try {
            map.put("CABEZOTE_DIR",
                    FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/img/"));
            printMap(map);
            // Definimos cual sera nuestra fuente de datos
            JRBeanCollectionDataSource beanCollectionDataSource =
                    new JRBeanCollectionDataSource(listaDS);
            //String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/" + reportFileName + ".jasper");
            String reporPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/" + reportFileName + ".jasper");
            /*String reportPath = JsfUtil.getReportPath(reportFileName);*/

            JasperPrint print;
            // Rellenamos el informe con la conexion creada y sus parametros establecidos
            //print = JasperFillManager.fillReport(report, map, beanCollectionDataSource);//listasService.getConecction());
            // Exportamos el informe a formato PDF
            //JasperExportManager.exportReportToPdfFile(print, REPORT_EXPORT_PATH);
            print = JasperFillManager.fillReport(reporPath, map, beanCollectionDataSource);

            byte[] pdf = JasperExportManager.exportReportToPdf(print);

            HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + reportFileName + "(" + DateUtil.formatDate(new Date()) + ").pdf\"");
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os.write(pdf);
                os.flush();
            } catch (FileNotFoundException e) {
                //FacesUtils.addErrorMessage("Archivo no encontrado");
                e.printStackTrace();
            } catch (IOException e) {
                //FacesUtils.addErrorMessage("I/O Problem");
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (Exception e2) {
                }
            }

            getFacesContext().responseComplete();

            /*} catch (SQLException ex) {
             Logger.getLogger(TempVerProgramacionBBean.class.getName()).log(Level.SEVERE, null, ex);
             */
        } catch (JRException jr) {
            Logger.getLogger(TempVerProgramacionBBean.class.getName()).log(Level.SEVERE, null, jr);
        }
    }

    public static void printMap(HashMap hashMap) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            //System.out.println(e.getKey() + " " + e.getValue());
        }
    }

    public static boolean isFuncionarioDepInformatica(Long rolSelected) {
        if (rolSelected.equals(0L) || rolSelected.equals(7L) || rolSelected.equals(8L)) {
            return true;
        }
        return false;
    }

    public static JasperPrint generacionPagina(String nombreArchivoJasper, HashMap<String, Object> map, List listaDS) {
        //System.out.println("Comienzo Generacion PDF\tOK");
        JasperPrint print = new JasperPrint();
        try {
            //System.out.println("Comienzo Generacion lectura logo\tOK");
            BufferedImage image = ImageIO.read(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/img/logo.png"));
            map.put("LOGO_CONTROL_T", image);
            //System.out.println("Finaliza Generacion lectura logo\tOK\t"+image.getWidth());
            /*map.put("CABEZOTE_DIR", 
             FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/img/"));*/
        } catch (IOException ex) {
            //System.out.println("Error Generacion lectura logo\tERROR");
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        InputStream reporteStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/" + nombreArchivoJasper + ".jasper");
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaDS);
        try {
            print = JasperFillManager.fillReport(reporteStream, map, beanCollectionDataSource);
        } catch (JRException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);

        }
        return print;
    }

    public static void generarPdfIText(String filename, ArrayList<PdfPTable> tables) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {

            PdfWriter.getInstance(document, baos);
            document.setMargins(10, 10, 30, 20);
            document.setMarginMirroringTopBottom(true);

            document.open();

            PdfPTable table = new PdfPTable(2);
            BufferedImage image = ImageIO.read(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/img/logo.png"));
            Image logo = Image.getInstance(image, null);
            logo.scaleAbsolute(60, 60);
            PdfPCell cell = new PdfPCell();
            cell.addElement(logo);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setRowspan(2);
            table.addCell(cell);

            PdfPCell cell2 = JsfUtil.celdaTitulo(filename, 1);
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);

            PdfPCell cell3 = JsfUtil.celda(DateUtil.formatDate(new Date()), false);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);


            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            for (int i = 0; i < tables.size(); i++) {
                document.add(tables.get(i));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
            }
            document.close();

            HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "(" + DateUtil.formatDate(new Date()) + ").pdf\"");
            response.setContentLength(baos.size());
            OutputStream os;

            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (DocumentException ex) {
            //System.out.println("Error en document.add(tables.get(i));");
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        getFacesContext().responseComplete();

    }

    public static PdfPCell celdaSubTitulo(String texto) {
        if (texto == null) {
            texto = "";
        }
        Font font = new Font(FontFamily.UNDEFINED, 6, Font.BOLD);
        font.setColor(BaseColor.WHITE);


        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        BaseColor color = new BaseColor(37, 57, 121);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(color);

        return cell;
    }

    public static PdfPCell celdaTitulo(String texto, int colspan) {
        if (texto == null) {
            texto = "";
        }
        Font font = new Font(FontFamily.UNDEFINED, 8, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setColspan(colspan);

        return cell;
    }

    public static PdfPCell celda(String texto, Boolean gris) {
        if (texto == null) {
            texto = "";
        }
        Font font = new Font(FontFamily.UNDEFINED, 6, Font.NORMAL);

        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        if (gris) {
            BaseColor color = new BaseColor(245, 245, 245);
            cell.setBackgroundColor(color);
        }

        return cell;
    }

    public static Date getFirstDayMonth(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        int minDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);

        //Esto es para garantizar que va a guardar en la clave el mes-año que se está visualizando
        c.set(Calendar.DATE, minDay);
        return c.getTime();
    }

    public static Date getLastDayMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        //Esto es para garantizar que va a guardar en la clave el mes-año que se está visualizando
        c.set(Calendar.DATE, maxDay);
        return c.getTime();

    }

    public static void saveInSession(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(key, value);
    }
    
    public static Object getFromSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(key);
    }
    
    public static void deleteFromSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(key);
    }
}