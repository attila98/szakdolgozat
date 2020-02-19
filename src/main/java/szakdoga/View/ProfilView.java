package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import szakdoga.Names;
import szakdoga.entity.Patient;
import szakdoga.service.PatientService;

import javax.annotation.PostConstruct;

@Route(value = "profil")
@PageTitle("Profil")
public class ProfilView extends VerticalLayout {

    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    VerticalLayout verticalLayout = new VerticalLayout();
    FormLayout profilLayout = new FormLayout();
    H2 titleField = new H2();

    TextField firstName= new TextField();
    TextField lastName =new TextField();
    TextField email =new TextField();
    TextField tb_number =new TextField();

    Image img = new Image("https://cdn.pixabay.com/photo/2014/12/10/20/56/medical-563427_960_720.jpg", "banner");
    MenuBar menuBar = new MenuBar();

    @Autowired
    PatientService patientService;

    Patient patient=new Patient();

    public ProfilView(){
    }

    @PostConstruct
    public void init(){
        wrappedSession=session.getSession();
        if (wrappedSession.getAttribute(Names.USERNAME) != null) {
            menuBar.setOpenOnHover(true);
            MenuItem foglalas = menuBar.addItem("Időpont foglalás");
            MenuItem lista = menuBar.addItem("Foglalásaim");
            MenuItem profil = menuBar.addItem("Profilom");
            MenuItem loguot = menuBar.addItem("Kijelentkezes");
            //patient=patientService.findByEmail(wrappedSession.getAttribute(Names.USERNAME).toString());
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
            loadProfil();

        } else {
            UI.getCurrent().navigate(Login.class);
            UI.getCurrent().getPage().executeJs("location.reload();");
        }
    }

    void loadProfil(){
        patient=patientService.findByEmail(wrappedSession.getAttribute(Names.USERNAME).toString());

        titleField.setText("Adataim:");
        firstName.focus();
        firstName.setValue(patient.getFirst_name());
        firstName.setLabel("Vezetéknév");

        lastName.setValue(patient.getLast_name());
        lastName.setLabel("Keresztnév");

        email.setValue(patient.getEmail());
        email.setLabel("Email cím");

        tb_number.setValue(patient.getTb_number());
        tb_number.setLabel("TB szám");

        profilLayout.setColspan(titleField,4);
        profilLayout.add(titleField,firstName,lastName,email,tb_number);
        add(profilLayout);
    }

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");

        profilLayout.getStyle().set("background-color", "#f3f5f7");
        profilLayout.getStyle().set("border-radius", "15px");
        profilLayout.getStyle().set("padding","20px");
    }

}
