package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import szakdoga.Names;
import szakdoga.entity.Doctor;
import szakdoga.entity.Patient;
import szakdoga.service.DoctorService;
import szakdoga.service.PatientService;

import javax.annotation.PostConstruct;

@Route(value = "login")
@PageTitle("Login")
public class Login extends VerticalLayout {
    final LoginI18n i18n = LoginI18n.createDefault();
    LoginForm PatientLoginForm = new LoginForm();
    LoginForm DoctorLoginForm = new LoginForm();
    VerticalLayout verticalLayout=new VerticalLayout();
    VaadinSession vaadinSession = UI.getCurrent().getSession();
    WrappedSession wrappedSession;

    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;

    Patient patient=new Patient();
    Doctor doctor=new Doctor();

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Login() {
        UI.getCurrent().getPage().addStyleSheet("/back.css");
    }

    @PostConstruct
    public void startLogin(){
        wrappedSession = vaadinSession.getSession();
        setStyle();
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);
        verticalLayout.setHeight("800px");
        H1 title=new H1("Orvosi időpontfoglaló rendszer");
        Button beteg=new Button("Betegként");
        Button orvos=new Button("Orvosként");
        H2 login=new H2("Bejelentkezés: ");
        Label space=new Label("  ");
        Button regButton=new Button("Regisztráció!");
        H3 registration=new H3("Szeretnék regisztrálni: ");
        registration.getStyle().set("margin-top","10px");
        registration.add(regButton);
        login.add(beteg,space,orvos);

        PatientLoginForm.setVisible(true);
        DoctorLoginForm.setVisible(false);

        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        PatientLoginForm.addLoginListener(loginEvent -> {
            patient=patientService.findByEmail(loginEvent.getUsername());
            if ((patientService.findByEmail(loginEvent.getUsername()) != null) && passwordEncoder.matches(loginEvent.getPassword(),patient.getPassword())){
                    Notification.show("Sikeres bejelentkezés!",2000,Notification.Position.MIDDLE);
                    wrappedSession.setAttribute(Names.USERNAME,patient.getEmail());
                    UI.getCurrent().navigate(HomeView.class);
            }else {
                Notification.show("Hibas felhsznalonev vagy jelszo!");
                PatientLoginForm.setEnabled(true);
            }
        });

        DoctorLoginForm.addLoginListener(loginEvent -> {
            doctor=doctorService.findByEmail(loginEvent.getUsername());
            if (doctorService.findByEmail(loginEvent.getUsername()) != null && doctor.getPassword().equals(loginEvent.getPassword())){
                Notification.show("Sikeres bejelentkezés!",2000,Notification.Position.MIDDLE);
                wrappedSession.setAttribute(Names.USERNAME,doctor.getEmail());
                UI.getCurrent().navigate(HomeView.class);
            }else{
                Notification.show("Hibas felhsznalonev vagy jelszo!");
                DoctorLoginForm.setEnabled(true);
            }
        });

        beteg.addClickListener(c -> setBetegFormVisible());
        orvos.addClickListener(c -> setOrvosFormVisible() );
        regButton.addClickListener(c -> UI.getCurrent().navigate(Registration.class));
        verticalLayout.add(title,login,registration);
        add(verticalLayout);
    }

    private LoginI18n createHungarianBeteg() {
        i18n.getForm().setUsername("Email cím");
        i18n.getForm().setTitle("Bejelentkezés betegként");
        i18n.getForm().setSubmit("Bejelentkezés");
        i18n.getForm().setPassword("Jelszó");
        i18n.getForm().setForgotPassword("Elfelejtettem a jelszót");
        return i18n;
    }

    private LoginI18n createHungarianOrvos() {
        i18n.getForm().setUsername("Email cím");
        i18n.getForm().setTitle("Bejelentkezés orvosként");
        i18n.getForm().setSubmit("Bejelentkezés");
        i18n.getForm().setPassword("Jelszó");
        i18n.getForm().setForgotPassword("Elfelejtettem a jelszót");
        return i18n;
    }

    private void setBetegFormVisible(){
        DoctorLoginForm.setVisible(false);
        PatientLoginForm.setVisible(true);
        PatientLoginForm.setI18n(createHungarianBeteg());
        verticalLayout.add(PatientLoginForm);
    }

    private void setOrvosFormVisible(){
        PatientLoginForm.setVisible(false);
        DoctorLoginForm.setVisible(true);
        DoctorLoginForm.setI18n(createHungarianOrvos());
        verticalLayout.add(DoctorLoginForm);
    }

    void setStyle(){
        PatientLoginForm.getElement().getStyle().set("background-color","#f6f7f7");
        PatientLoginForm.getElement().getStyle().set("border-radius","10px");
        PatientLoginForm.getElement().getStyle().set("border","double");

        DoctorLoginForm.getElement().getStyle().set("background-color","#f6f7f7");
        DoctorLoginForm.getElement().getStyle().set("border-radius","10px");
        DoctorLoginForm.getElement().getStyle().set("border","double");
    }
}
