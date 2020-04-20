package szakdoga.View;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import szakdoga.entity.Doctor;
import szakdoga.entity.Patient;
import szakdoga.entity.Timetable;
import szakdoga.service.DoctorService;
import szakdoga.service.PatientService;
import szakdoga.service.TimetableService;

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

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Registration(){
        UI.getCurrent().getPage().addStyleSheet("/back.css");
    }

    @PostConstruct
    public void init(){
        setStyle();
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);
        verticalLayout.setHeight("800px");
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

        fieldsValidationSetUp();
    }

    public void patientReg(){
        patientFormLayout.setVisible(true);
        doctorFormLayout.setVisible(false);
        doctorFormLayout2.setVisible(false);
        patientFormLayout.remove(firstName,lastName,email,password,tb_number,ok,back);

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
        back.setText("Vissza a bejelentkező oldalra!");
        back.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(Login.class));
        patientFormLayout.add(firstName,lastName,email,password,tb_number,ok,back);
        ok.addClickListener(buttonClickEvent ->    {
            if(patientIsValid()){
                patient.setFirst_name(firstName.getValue());
                patient.setLast_name(lastName.getValue());
                patient.setBirth_day(szul);
                patient.setEmail(email.getValue());
                patient.setPassword(passwordEncoder.encode(password.getValue()));
                patient.setTb_number(tb_number.getValue());
                patientService.save(patient);
                Notification.show("Sikeres regisztráció!",2000,Notification.Position.MIDDLE);
                UI.getCurrent().navigate(Login.class);
            }else {

                Notification.show("Hibás adat!",1000,Notification.Position.MIDDLE);
            }
        });

    }

    public void doctorReg(){
        patientFormLayout.setVisible(false);
        doctorFormLayout.setVisible(true);
        doctorFormLayout2.setVisible(false);
        doctorFormLayout2.removeAll();
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
            if (doctorIsValid()){
                doctor.setFirst_name(firstNameD.getValue());
                doctor.setLast_name(lastNameD.getValue());
                doctor.setEmail(emailD.getValue());
                doctor.setPassword(passwordEncoder.encode(passwordD.getValue()));
                doctor.setCity(cityD.getValue());

                doctorFormLayout.setVisible(false);
                doctorFormLayout2.setVisible(true);
            }else {
                Notification.show("Hibás adatok!",2000,Notification.Position.MIDDLE);
            }

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
            if(doctorTimetableIsValid()){
                timetable.setMonday(mondayFrom.getValue().toString() +"-"+mondayTo.getValue().toString());
                timetable.setTuesday(tuesdayFrom.getValue().toString() +"-"+ tuesdayTo.getValue().toString());
                timetable.setWednesday(wednesdayFrom.getValue().toString()+"-"+wednesdayTo.getValue().toString());
                timetable.setThursday(thursdayFrom.getValue().toString() +"-"+ thursdayTo.getValue().toString());
                timetable.setFriday(fridayFrom.getValue().toString() +"-"+ fridayTo.getValue().toString());
                doctorService.save(doctor);
                timetable.setId(doctor.getId());
                timetableService.save(timetable);
                Notification.show("Sikeres regisztráció!",2000,Notification.Position.MIDDLE);
                UI.getCurrent().navigate(Login.class);
            }else{
                Notification.show("Hibás adatok!",2000,Notification.Position.MIDDLE);
            }
            });

        doctorFormLayout2.setColspan(title,4);
        doctorFormLayout2.setColspan(reg,4);
        title.getStyle().set("text-align","center");

        doctorFormLayout2.add(title,mondayFrom,mondayTo,tuesdayFrom,tuesdayTo,wednesdayFrom,wednesdayTo,thursdayFrom
                ,thursdayTo,fridayFrom,fridayTo,reg);

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

        password.setMinLength(6);
        password.setMaxLength(20);
        password.setErrorMessage("Minimum 6 karakter!");

        tb_number.setMinLength(9);
        tb_number.setMaxLength(9);
        tb_number.setPattern("^[0-9_]*$");
        tb_number.setErrorMessage("Pontosan 9 számjegy!");

        email.setPattern("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$");
        email.setErrorMessage("Hibás email formátum!");

        firstNameD.setMinLength(2);
        firstNameD.setMaxLength(20);
        firstNameD.setPattern("^[a-zA-Z_]*$");
        firstNameD.setErrorMessage("Hibás formátum!");

        lastNameD.setMinLength(2);
        lastNameD.setMaxLength(20);
        lastNameD.setErrorMessage("Hibás formátum!");

        passwordD.setMinLength(6);
        passwordD.setMaxLength(20);
        passwordD.setErrorMessage("Minimum 6 karakter!");

        emailD.setPattern("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$");
        emailD.setErrorMessage("Hibás email formátum!");

        cityD.setMinLength(3);
        cityD.setMaxLength(20);
        cityD.setPattern("^[a-zA-Z_]*$");
        cityD.setErrorMessage("Hibás formátum!");
    }

    boolean patientIsValid(){
        if(firstName.isInvalid() || lastName.isInvalid() || email.isInvalid() || tb_number.isInvalid()
                || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || tb_number.isEmpty()){
            return false;
        }else return true;
    }

    boolean doctorIsValid(){
        if(firstNameD.isInvalid() || lastNameD.isInvalid() || emailD.isInvalid() || cityD.isInvalid()
                || firstNameD.isEmpty() || lastNameD.isEmpty() || emailD.isEmpty() || cityD.isEmpty()){
            return false;
        }else return true;
    }

    boolean doctorTimetableIsValid(){
        if (mondayFrom.isEmpty() || mondayTo.isEmpty() || mondayFrom.getValue().isAfter(mondayTo.getValue()) || tuesdayFrom.isEmpty() || tuesdayTo.isEmpty()
            || tuesdayFrom.getValue().isAfter(tuesdayTo.getValue()) || wednesdayFrom.isEmpty() || wednesdayTo.isEmpty() || wednesdayFrom.getValue().isAfter(wednesdayTo.getValue()) || thursdayFrom.isEmpty() || thursdayTo.isEmpty()
            || thursdayFrom.getValue().isAfter(thursdayTo.getValue()) || fridayFrom.isEmpty() || fridayTo.isEmpty() || fridayFrom.getValue().isAfter(fridayTo.getValue())){
            return false;
        }else{
            return true;
        }
    }

    void setStyle(){
        UI.getCurrent().getElement().getStyle().set("width","100%");

        patientFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("25%",1));
        patientFormLayout.setMaxWidth("70%");
        patientFormLayout.getStyle().set("display","block");
        patientFormLayout.getStyle().set("margin-left","auto");
        patientFormLayout.getStyle().set("margin-right","auto");
        patientFormLayout.getStyle().set("background-color","#f6f7f7");
        patientFormLayout.getStyle().set("padding","20px");
        patientFormLayout.getStyle().set("border-radius","10px");
        patientFormLayout.getStyle().set("border","double");

        doctorFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("25%",1));
        doctorFormLayout.setMaxWidth("70%");
        doctorFormLayout.getStyle().set("display","block");
        doctorFormLayout.getStyle().set("margin-left","auto");
        doctorFormLayout.getStyle().set("margin-right","auto");
        doctorFormLayout.getStyle().set("background-color","#f6f7f7");
        doctorFormLayout.getStyle().set("padding","20px");
        doctorFormLayout.getStyle().set("border-radius","10px");
        doctorFormLayout.getStyle().set("border","double");

        doctorFormLayout2.setMaxWidth("70%");
        doctorFormLayout2.getStyle().set("display","block");
        doctorFormLayout2.getStyle().set("margin-left","auto");
        doctorFormLayout2.getStyle().set("margin-right","auto");
        doctorFormLayout2.getStyle().set("background-color","#f6f7f7");
        doctorFormLayout2.getStyle().set("padding","20px");
        doctorFormLayout2.getStyle().set("border-radius","10px");
        doctorFormLayout2.getStyle().set("border","double");
    }
}
