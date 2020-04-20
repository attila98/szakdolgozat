package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import szakdoga.Names;
import szakdoga.entity.Appointment;
import szakdoga.entity.Doctor;
import szakdoga.service.AppointmentService;
import szakdoga.service.DoctorService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Főoldal")
@Route(value = "doctorhome")
public class DoctorHomeView extends VerticalLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    VerticalLayout verticalLayout2 = new VerticalLayout();
    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    MenuBar menuBar = new MenuBar();
    H3 noBook = new H3("Nincs megjeleníthető foglalás az ön számára.");

    Image img = new Image("header.png","banner");

    @Autowired
    DoctorService doctorService;

    @Autowired
    AppointmentService appointmentService;

    List<Appointment> appointmentList=new ArrayList<>();

    Grid<Appointment> appointmentGrid= new Grid<>();

    Doctor doctor;

    public DoctorHomeView()
    {
        UI.getCurrent().getPage().addStyleSheet("/back.css");
    }

    @PostConstruct
    public void init() {
        wrappedSession = session.getSession();
        if (wrappedSession.getAttribute(Names.DOCTOR) != null) {
            doctor=doctorService.findByEmail(Names.DOCTOR);

            menuBar.setOpenOnHover(true);

            MenuItem lista = menuBar.addItem("Időpontok");
            MenuItem profil = menuBar.addItem("Profilom");
            MenuItem loguot = menuBar.addItem("Kijelentkezes");

            profil.addClickListener(click -> {
                UI.getCurrent().navigate(DoctorProfileView.class);
            });

            loguot.addClickListener(click -> {
                Notification.show("Sikeres kijelentkezes!", 1500, Notification.Position.MIDDLE);
                wrappedSession.setAttribute(Names.DOCTOR, null);
                UI.getCurrent().navigate(Login.class);
            });

            lista.addClickListener(click -> {
                UI.getCurrent().navigate(DoctorHomeView.class);
            });

            setStyle();
            doctor=doctorService.findByEmail(wrappedSession.getAttribute(Names.DOCTOR).toString());


            H2 title=new H2("Foglalalások");
            appointmentList = appointmentService.getAll().stream().filter(app -> app.getDoctor_id()==doctor.getId()).collect(Collectors.toList());

            appointmentGrid.setItems(appointmentList);
            appointmentGrid.addColumn(Appointment::getFormattedDate).setHeader("Dátum");
            appointmentGrid.addColumn(Appointment::getStartapp).setHeader("Hány órától");
            appointmentGrid.addColumn(Appointment::getEndapp).setHeader("Meddig");
            appointmentGrid.addColumn(Appointment::getPatientName).setHeader("Beteg neve");
            appointmentGrid.addColumn(Appointment::getPatientTBNumber).setHeader("Beteg TB száma");
            appointmentGrid.addColumn(Appointment::getPatientBDay).setHeader("Beteg születési dátuma");
            appointmentGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);

            verticalLayout.add(img, menuBar);
            if (appointmentList.isEmpty()){
                verticalLayout2.add(title,noBook);
            }else{
                verticalLayout2.add(title,appointmentGrid);
            }

            add(verticalLayout,verticalLayout2);

        } else {
            UI.getCurrent().navigate(Login.class);
            UI.getCurrent().getPage().executeJs("location.reload();");
        }
    }

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");
        verticalLayout.getStyle().set("border","double");
        verticalLayout.getStyle().set("align-self","center");
        verticalLayout.getStyle().set("width","90%");

        verticalLayout2.getStyle().set("background-color", "#f3f5f7");
        verticalLayout2.getStyle().set("border-radius", "15px");
        verticalLayout2.getStyle().set("border","double");
        verticalLayout2.getStyle().set("align-self","center");
        verticalLayout2.getStyle().set("width","90%");

        appointmentGrid.getStyle().set("background-color", "#f3f5f7");

        menuBar.getStyle().set("padding-left","20px");

        img.getStyle().set("width","1100px");
        img.getStyle().set("align-self","center");
        img.getStyle().set("border-radius","15px");
        img.getStyle().set("border","1px solid");

        noBook.getStyle().set("margin-bottom","50px");
    }
}