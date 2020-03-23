package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
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
import szakdoga.entity.Patient;
import szakdoga.service.AppointmentService;
import szakdoga.service.PatientService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "bookings")
@PageTitle("Foglalasok")
public class BookingsView extends VerticalLayout {

    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    VerticalLayout verticalLayout = new VerticalLayout();
    VerticalLayout verticalLayout2 = new VerticalLayout();

    Image img = new Image("header.png", "banner");
    MenuBar menuBar = new MenuBar();

    H1 title=new H1("Foglalásaim:");

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PatientService patientService;

    Patient patient = new Patient();


    List<Appointment> appointmentList=new ArrayList<>();

    Grid<Appointment> appointmentGrid= new Grid<>();

    public BookingsView() {
    }

    @PostConstruct
    public void init() {
        wrappedSession = session.getSession();
        if (wrappedSession.getAttribute(Names.USERNAME) != null) {
            wrappedSession = session.getSession();
            MenuItem foglalas = menuBar.addItem("Időpont foglalás");
            MenuItem lista = menuBar.addItem("Foglalásaim");
            MenuItem profil = menuBar.addItem("Profilom");
            MenuItem loguot = menuBar.addItem("Kijelentkezes");
            patient=patientService.findByEmail(wrappedSession.getAttribute(Names.USERNAME).toString());
            profil.addClickListener(click -> {
                UI.getCurrent().navigate(ProfilView.class);
            });

            loguot.addClickListener(click -> {
                Notification.show("Sikeres kijelentkezes!", 1500, Notification.Position.MIDDLE);
                wrappedSession.setAttribute(Names.USERNAME, null);
                UI.getCurrent().navigate(Login.class);
            });

            lista.addClickListener(click -> {
                UI.getCurrent().navigate(BookingsView.class);
            });

            foglalas.addClickListener(click -> {
                UI.getCurrent().navigate(HomeView.class);
            });

            setStyle();

            verticalLayout.add(img, menuBar);
            add(verticalLayout);
            add(title);


            appointmentList = appointmentService.getAll().stream().filter(app -> app.getPatient_id()==patient.getId()).collect(Collectors.toList());
            appointmentGrid.setItems(appointmentList);
            appointmentGrid.addColumn(Appointment::getFormattedDate).setHeader("Dátum");
            appointmentGrid.addColumn(Appointment::getStartapp).setHeader("Hány órától");
            appointmentGrid.addColumn(Appointment::getEndapp).setHeader("Meddig");
            appointmentGrid.addColumn(Appointment::getDoctor_id).setHeader("Orvos neve");
            appointmentGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
            verticalLayout2.add(title,appointmentGrid);
            add(verticalLayout2);
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

        menuBar.getStyle().set("padding-left","20px");

        appointmentGrid.getStyle().set("background-color", "#f3f5f7");

        img.getStyle().set("width","1100px");
        img.getStyle().set("align-self","center");
        img.getStyle().set("border-radius","15px");
        img.getStyle().set("border","1px solid");
    }
}
