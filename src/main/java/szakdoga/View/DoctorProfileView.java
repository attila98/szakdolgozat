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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import szakdoga.Names;
import szakdoga.entity.Doctor;
import szakdoga.entity.Timetable;
import szakdoga.service.DoctorService;
import szakdoga.service.TimetableService;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Optional;

@PageTitle("Profil")
@Route(value = "doctorprofile")
public class DoctorProfileView extends VerticalLayout {
    VerticalLayout verticalLayout = new VerticalLayout();
    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    FormLayout profilLayout = new FormLayout();
    FormLayout timetableLayout = new FormLayout();
    MenuBar menuBar = new MenuBar();

    H2 titleField = new H2();
    H2 titleField2 = new H2();

    TextField firstName= new TextField();
    TextField lastName =new TextField();
    TextField email =new TextField();
    TextField city =new TextField();
    Button ok = new Button();

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
    Button ok2 = new Button();

    Image img = new Image("header.png","banner");

    Doctor doctor = new Doctor();

    Optional<Timetable> timetable = Optional.of(new Timetable());

    @Autowired
    DoctorService doctorService;

    @Autowired
    TimetableService timetableService;

    public DoctorProfileView()
    {
        UI.getCurrent().getPage().addStyleSheet("/back.css");
    }

    @PostConstruct
    public void init() {
        wrappedSession = session.getSession();
        if (wrappedSession.getAttribute(Names.DOCTOR) != null) {
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
            verticalLayout.add(img, menuBar);
            add(verticalLayout);

            doctor=doctorService.findByEmail(wrappedSession.getAttribute(Names.DOCTOR).toString());
            loadProfil();
            fieldsValidationSetUp();

            timetable=timetableService.getTimetable(doctor.getId());
            loadTimetable();

            ok.addClickListener(click -> {
                if(textFieldIsValid()){
                    doctor.setFirst_name(firstName.getValue());
                    doctor.setLast_name(lastName.getValue());
                    doctor.setEmail(email.getValue());
                    doctor.setCity(city.getValue());
                    doctorService.save(doctor);
                    Notification.show("Sikeres adatmódosítás!",2000,Notification.Position.MIDDLE);
                }
            });

            ok2.addClickListener(c -> {
                if(doctorTimetableIsValid()){
                    timetable.get().setMonday(mondayFrom.getValue().toString() +"-"+mondayTo.getValue().toString());
                    timetable.get().setTuesday(tuesdayFrom.getValue().toString() +"-"+ tuesdayTo.getValue().toString());
                    timetable.get().setWednesday(wednesdayFrom.getValue().toString()+"-"+wednesdayTo.getValue().toString());
                    timetable.get().setThursday(thursdayFrom.getValue().toString() +"-"+ thursdayTo.getValue().toString());
                    timetable.get().setFriday(fridayFrom.getValue().toString() +"-"+ fridayTo.getValue().toString());
                    doctorService.save(doctor);
                    Timetable timetable2=timetable.get();
                    timetableService.save(timetable2);
                    Notification.show("Sikeres regisztráció!",2000,Notification.Position.MIDDLE);
                }else{
                    Notification.show("Hibás adatok!",2000,Notification.Position.MIDDLE);
                }});

        } else {
            UI.getCurrent().navigate(Login.class);
            UI.getCurrent().getPage().executeJs("location.reload();");
        }
    }

    void loadProfil(){
        titleField.setText("Adataim:");
        firstName.focus();
        firstName.setValue(doctor.getFirst_name());
        firstName.setLabel("Vezetéknév");

        lastName.setValue(doctor.getLast_name());
        lastName.setLabel("Keresztnév");

        email.setValue(doctor.getEmail());
        email.setLabel("Email cím");

        city.setValue(doctor.getCity());
        city.setLabel("Város");

        ok.setText("Mentés!");

        profilLayout.setColspan(titleField,4);
        profilLayout.setColspan(ok,4);
        profilLayout.add(titleField,firstName,lastName,email,city,ok);
        add(profilLayout);
    }

    void loadTimetable(){
        titleField2.setText("Rendelési idő:");

        mondayFrom.setLabel("Hetfő");
        tuesdayFrom.setLabel("Kedd");
        wednesdayFrom.setLabel("Szerda");
        thursdayFrom.setLabel("Csütörtök");
        fridayFrom.setLabel("Péntek");

        String monday=timetable.get().getMonday();
        String tuesday=timetable.get().getTuesday();
        String wednesday=timetable.get().getWednesday();
        String thursday=timetable.get().getThursday();
        String friday=timetable.get().getFriday();

        mondayFrom.setValue(LocalTime.parse(monday.substring(0,monday.indexOf("-"))));
        mondayTo.setValue(LocalTime.parse(monday.substring(monday.indexOf("-")+1)));
        tuesdayFrom.setValue(LocalTime.parse(tuesday.substring(0,tuesday.indexOf("-"))));
        tuesdayTo.setValue(LocalTime.parse(tuesday.substring(tuesday.indexOf("-")+1)));
        wednesdayFrom.setValue(LocalTime.parse(wednesday.substring(0,wednesday.indexOf("-"))));
        wednesdayTo.setValue(LocalTime.parse(wednesday.substring(wednesday.indexOf("-")+1)));
        thursdayFrom.setValue(LocalTime.parse(thursday.substring(0,thursday.indexOf("-"))));
        thursdayTo.setValue(LocalTime.parse(thursday.substring(thursday.indexOf("-")+1)));
        fridayFrom.setValue(LocalTime.parse(friday.substring(0,friday.indexOf("-"))));
        fridayTo.setValue(LocalTime.parse(friday.substring(friday.indexOf("-")+1)));

        ok2.setText("Mentés!");

        timetableLayout.setColspan(titleField2,4);
        timetableLayout.setColspan(ok2,4);
        timetableLayout.add(titleField2,mondayFrom,mondayTo,tuesdayFrom,tuesdayTo,wednesdayFrom,wednesdayTo,thursdayFrom,
                thursdayTo,fridayFrom,fridayTo,ok2);
        add(timetableLayout);
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

        city.setMinLength(3);
        city.setMaxLength(20);
        city.setPattern("^[a-zA-Z_]*$");
        city.setErrorMessage("Hibás formátum!");

        email.setPattern("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$");
        email.setErrorMessage("Hibás email formátum!");
    }

    boolean textFieldIsValid(){
        if(firstName.isInvalid() || lastName.isInvalid() || email.isInvalid() || city.isInvalid()
                || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || city.isEmpty()){
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

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");
        verticalLayout.getStyle().set("border","double");
        verticalLayout.getStyle().set("align-self","center");
        verticalLayout.getStyle().set("width","90%");

        menuBar.getStyle().set("padding-left","20px");

        img.getStyle().set("width","1100px");
        img.getStyle().set("align-self","center");
        img.getStyle().set("border-radius","15px");
        img.getStyle().set("border","1px solid");

        profilLayout.getStyle().set("background-color", "#f3f5f7");
        profilLayout.getStyle().set("border-radius", "15px");
        profilLayout.getStyle().set("padding","20px 20px 100px 20px");
        profilLayout.getStyle().set("border","double");
        profilLayout.getStyle().set("align-self","center");
        profilLayout.getStyle().set("width","87%");

        timetableLayout.getStyle().set("background-color", "#f3f5f7");
        timetableLayout.getStyle().set("border-radius", "15px");
        timetableLayout.getStyle().set("padding","20px 20px 100px 20px");
        timetableLayout.getStyle().set("border","double");
        timetableLayout.getStyle().set("align-self","center");
        timetableLayout.getStyle().set("width","87%");

        ok.getStyle().set("margin-top","20px");
        ok.setMaxWidth("10%");
        ok.getStyle().set("width","10%");

        ok2.getStyle().set("margin-top","20px");
        ok2.setMaxWidth("10%");
        ok2.getStyle().set("width","10%");
    }

}
