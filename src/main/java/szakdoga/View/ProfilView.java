package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
    Button ok = new Button();

    Image img = new Image("header.png", "banner");
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
            fieldsValidationSetUp();

            ok.addClickListener(click -> {
                if(textFieldIsValid()){
                    patient.setFirst_name(firstName.getValue());
                    patient.setLast_name(lastName.getValue());
                    patient.setEmail(email.getValue());
                    patient.setTb_number(tb_number.getValue());
                    patientService.save(patient);
                    Notification.show("Sikeres adatmódosítás!",2000,Notification.Position.MIDDLE);
                }
            });

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

        ok.setText("Mentés!");

        profilLayout.setColspan(titleField,4);
        profilLayout.setColspan(ok,4);
        profilLayout.add(titleField,firstName,lastName,email,tb_number,ok);
        add(profilLayout);
    }

    void fieldsValidationSetUp(){
        firstName.setMinLength(2);
        firstName.setMaxLength(20);
        firstName.setPattern("^[a-zA-Z_]*$");
        firstName.setErrorMessage("Hibás formátum!");

        lastName.setMinLength(2);
        lastName.setMaxLength(20);
        lastName.setPattern("^[a-zA-Z_]*$");
        lastName.setErrorMessage("Hibás formátum!");

        tb_number.setMinLength(9);
        tb_number.setMaxLength(9);
        tb_number.setPattern("^[0-9_]*$");
        tb_number.setErrorMessage("Pontosan 9 számjegy!");

        email.setPattern("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$");
        email.setErrorMessage("Hibás email formátum!");
    }

    boolean textFieldIsValid(){
        if(firstName.isInvalid() || lastName.isInvalid() || email.isInvalid() || tb_number.isInvalid()
                || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || tb_number.isEmpty()){
            return false;
        }else return true;
    }

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");
        verticalLayout.getStyle().set("border","double");
        verticalLayout.getStyle().set("align-self","center");
        verticalLayout.getStyle().set("width","90%");

        menuBar.getStyle().set("padding-left","20px");

        profilLayout.getStyle().set("background-color", "#f3f5f7");
        profilLayout.getStyle().set("border-radius", "15px");
        profilLayout.getStyle().set("padding","20px 20px 100px 20px");
        profilLayout.getStyle().set("border","double");
        profilLayout.getStyle().set("align-self","center");
        profilLayout.getStyle().set("width","87%");

        img.getStyle().set("width","1100px");
        img.getStyle().set("align-self","center");
        img.getStyle().set("border-radius","15px");
        img.getStyle().set("border","1px solid");

        ok.getStyle().set("margin-top","20px");
        ok.setMaxWidth("10%");
        ok.getStyle().set("width","10%");
    }

}
