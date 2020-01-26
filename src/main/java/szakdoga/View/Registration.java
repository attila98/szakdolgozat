package szakdoga.View;

import szakdoga.entity.Doctor;
import szakdoga.entity.Patient;
import szakdoga.entity.Timetable;
import szakdoga.service.DoctorService;
import szakdoga.service.PatientService;
import szakdoga.service.TimetableService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;

@Route(value = "registration")
@PageTitle("Regisztráció")
public class Registration extends VerticalLayout {
    FormLayout patientFormLayout= new FormLayout();
    FormLayout doctorFormLayout= new FormLayout();
    FormLayout doctorFormLayout2= new FormLayout();
    VerticalLayout verticalLayout=new VerticalLayout();

    TextField firstName= new TextField();
    TextField lastName =new TextField();
    TextField email =new TextField();
    TextField password =new TextField();
    TextField tb_number =new TextField();

    Button ok=new Button();
    Button back=new Button();


    TextField firstNameD= new TextField();
    TextField lastNameD =new TextField();
    TextField emailD =new TextField();
    TextField passwordD =new TextField();
    TextField cityD =new TextField();
    Button okD=new Button();
    Button backD=new Button();

    TimePicker mondayFrom = new TimePicker();
    TimePicker mondayTo = new TimePicker();
    TimePicker tuesdayFrom = new TimePicker();
    TimePicker tuesdayTo = new TimePicker();
    TimePicker wednesdayFrom = new TimePicker();
    TimePicker wednesdayTo = new TimePicker();
    TimePicker thursdayFrom = new TimePicker();
    TimePicker thursdayTo = new TimePicker();
    TimePicker fridayFrom = new TimePicker();
    TimePicker fridayTo = new TimePicker();

    Date szul=new Date();

    Patient patient=new Patient();
    Doctor doctor=new Doctor();
    Timetable timetable=new Timetable();

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    TimetableService timetableService;

    public Registration(){
    }

    @PostConstruct
    public void init(){
        setStyle();
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);
        setDefaultHorizontalComponentAlignment(Alignment.END);
        H1 title=new H1("Orvosi időpontfoglaló rendszer");
        Button beteg=new Button("Betegként");
        Button orvos=new Button("Orvosként");
        H2 regH2=new H2("Regisztráció: ");
        Label space=new Label("  ");
        regH2.add(beteg,space,orvos);
        doctorFormLayout.setVisible(false);
        doctorFormLayout2.setVisible(false);
        patientFormLayout.setVisible(false);
        verticalLayout.add(title,regH2,patientFormLayout,doctorFormLayout,doctorFormLayout2);
        add(verticalLayout);
        beteg.addClickListener(c->patientReg());
        orvos.addClickListener(c->doctorReg());
    }

    public void patientReg(){
        patientFormLayout.setVisible(true);
        doctorFormLayout.setVisible(false);
        doctorFormLayout2.setVisible(false);

        firstName.focus();
        firstName.setPlaceholder("Nagy");
        firstName.setLabel("Vezetéknév");

        lastName.setPlaceholder("Gábor");
        lastName.setLabel("Keresztnév");

        email.setPlaceholder("valami@email.com");
        email.setLabel("Email cím");

        password.setPlaceholder("******");
        password.setLabel("Jelszó");

        tb_number.setPlaceholder("123456789");
        tb_number.setLabel("TB szám");

        ok.setText("Regisztráció");
        back.setText("Vissza");
        back.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(Login.class));
        patientFormLayout.add(firstName,lastName,email,password,tb_number,ok,back);

        ok.addClickListener(buttonClickEvent ->    {patient.setFirst_name(firstName.getValue());
            patient.setLast_name(lastName.getValue());
            patient.setBirth_day(szul);
            patient.setEmail(email.getValue());
            patient.setPassword(password.getValue());
            patient.setTb_number(tb_number.getValue());
            patientService.save(patient);
            Notification.show("Sikeres regisztráció!",2000,Notification.Position.MIDDLE);
            UI.getCurrent().navigate(Login.class);
        });

    }

    public void doctorReg(){
        patientFormLayout.setVisible(false);
        doctorFormLayout.setVisible(true);
        doctorFormLayout2.setVisible(false);
        doctorFormLayout.remove(firstNameD,lastNameD,emailD,passwordD,cityD,okD,backD);

        firstNameD.focus();
        firstNameD.setPlaceholder("Nagy");
        firstNameD.setLabel("Vezetéknév");

        lastNameD.setPlaceholder("Gábor");
        lastNameD.setLabel("Keresztnév");

        emailD.setPlaceholder("valami@email.com");
        emailD.setLabel("Email cím");

        passwordD.setPlaceholder("******");
        passwordD.setLabel("Jelszó");

        cityD.setPlaceholder("Debrecen");
        cityD.setLabel("Város");

        okD.setText("Tovább");
        backD.setText("Vissza a bejelentkező oldalra!");
        backD.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(Login.class));
        doctorFormLayout.add(firstNameD,lastNameD,emailD,passwordD,cityD,okD,backD);

        okD.addClickListener(buttonClickEvent ->    {
            doctor.setFirst_name(firstNameD.getValue());
            doctor.setLast_name(lastNameD.getValue());
            doctor.setEmail(emailD.getValue());
            doctor.setPassword(passwordD.getValue());
            doctor.setCity(cityD.getValue());
            doctorService.save(doctor);
            doctorFormLayout.setVisible(false);
            doctorFormLayout2.setVisible(true);
        });

        H3 title =new H3("Adja meg a rendelési idejét!");
        mondayFrom.setLabel("Hetfő");
        tuesdayFrom.setLabel("Kedd");
        wednesdayFrom.setLabel("Szerda");
        thursdayFrom.setLabel("Csütörtök");
        fridayFrom.setLabel("Péntek");


        Button reg=new Button("Regisztráció!");

        doctorFormLayout2.remove(title,mondayFrom,mondayTo,tuesdayFrom,tuesdayTo,wednesdayFrom,wednesdayTo,thursdayFrom
                ,thursdayTo,fridayFrom,fridayTo,reg);
        reg.addClickListener(c -> {
            timetable.setMonday(mondayFrom.getValue().toString() +"-"+mondayTo.getValue().toString());
            timetable.setTuesday(tuesdayFrom.getValue().toString() +"-"+ tuesdayTo.getValue().toString());
            timetable.setWednesday(wednesdayFrom.getValue().toString()+"-"+wednesdayTo.getValue().toString());
            timetable.setThursday(thursdayFrom.getValue().toString() +"-"+ thursdayTo.getValue().toString());
            timetable.setFriday(fridayFrom.getValue().toString() +"-"+ fridayTo.getValue().toString());
            timetable.setId(doctor.getId());
            timetableService.save(timetable);
            Notification.show("Sikeres regisztráció!",2000,Notification.Position.MIDDLE);
            UI.getCurrent().navigate(Login.class);
            });

        doctorFormLayout2.setColspan(title,4);
        doctorFormLayout2.setColspan(reg,4);
        title.getStyle().set("text-align","center");

        doctorFormLayout2.add(title,mondayFrom,mondayTo,tuesdayFrom,tuesdayTo,wednesdayFrom,wednesdayTo,thursdayFrom
                ,thursdayTo,fridayFrom,fridayTo,reg);

    }

    void setStyle(){
        UI.getCurrent().getElement().getStyle().set("width","100%");

        patientFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("25%",1));
        patientFormLayout.setMaxWidth("70%");
        patientFormLayout.getStyle().set("display","block");
        patientFormLayout.getStyle().set("margin-left","auto");
        patientFormLayout.getStyle().set("margin-right","auto");

        doctorFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("25%",1));
        doctorFormLayout.setMaxWidth("70%");
        doctorFormLayout.getStyle().set("display","block");
        doctorFormLayout.getStyle().set("margin-left","auto");
        doctorFormLayout.getStyle().set("margin-right","auto");

        doctorFormLayout2.setMaxWidth("70%");
        doctorFormLayout2.getStyle().set("display","block");
        doctorFormLayout2.getStyle().set("margin-left","auto");
        doctorFormLayout2.getStyle().set("margin-right","auto");
    }
}
