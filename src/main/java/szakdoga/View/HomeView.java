package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;
import szakdoga.Names;
import szakdoga.entity.Appointment;
import szakdoga.entity.Doctor;
import szakdoga.entity.Patient;
import szakdoga.entity.Timetable;
import szakdoga.service.AppointmentService;
import szakdoga.service.DoctorService;
import szakdoga.service.PatientService;
import szakdoga.service.TimetableService;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Route(value = "home")
public class HomeView extends VerticalLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    ComboBox<Doctor> doctorDropdown = new ComboBox<>();
    FullCalendar calendar;
    Button confirmButton = new Button();
    Button cancelButton = new Button();

    Image img = new Image("https://cdn.pixabay.com/photo/2014/12/10/20/56/medical-563427_960_720.jpg", "banner");


    H2 foglalasH2 = new H2("Foglalás:");
    Button foglalasButton = new Button("Időpont lefoglalása!");

    int choosenDoctorId;

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    TimetableService timetableService;

    @Autowired
    AppointmentService appointmentService;

    Optional<Timetable> timetable = Optional.of(new Timetable());
    Patient patient = new Patient();

    Appointment appointment=new Appointment();

    public HomeView() {
    }

    @PostConstruct
    public void init() {
        wrappedSession = session.getSession();
        if (wrappedSession.getAttribute(Names.USERNAME) != null) {
            menuBar.setOpenOnHover(true);

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

            doctorDropdown.setLabel("Orvosok:");
            List<Doctor> doctorList = doctorService.getAll();
            doctorDropdown.setItemLabelGenerator(Doctor::getFullName);
            doctorDropdown.setItems(doctorList);
            doctorDropdown.setValue(doctorList.get(0));

            doctorDropdown.addValueChangeListener(event -> {
                if (event.getValue() == null) {
                    addCalendar();
                } else {
                    addCalendar();
                }
            });
            setStyle();
            addCalendar();
            //horizontalLayout.add(calendar,foglalasH2,foglalasButton);
            add(verticalLayout);
            add(doctorDropdown, calendar);

        } else {
            UI.getCurrent().navigate(Login.class);
            UI.getCurrent().getPage().executeJs("location.reload();");
        }
    }

    void addCalendar() {
        remove(verticalLayout);
        remove(doctorDropdown, calendar);
        calendar = FullCalendarBuilder.create().build();
        calendar.setLocale(CalendarLocale.HUNGARIAN);
        calendar.setFirstDay(DayOfWeek.MONDAY);
        calendar.setMinTime(LocalTime.of(6, 00));
        calendar.setMaxTime(LocalTime.of(21, 00));
        calendar.setWeekNumbersVisible(false);

        if (choosenDoctorId == 0) {
            addTimetableToCalendar(calendar, doctorDropdown.getValue().getId());
        } else {
            addTimetableToCalendar(calendar, choosenDoctorId);
        }

        calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK);
        BusinessHours businessHours = new BusinessHours(LocalTime.of(6, 00), LocalTime.of(18, 00));
        calendar.setBusinessHours(businessHours);
        calendar.setMaxWidth("70%");
        calendar.setMinHeight("600px");
        calendar.getStyle().set("aligment","center");

        calendar.addEntryClickedListener(entryClickedEvent -> {
            if (entryClickedEvent.getEntry().getColor()!="#ff0000"){
                Dialog dialog = new Dialog();
                H2 foglalas = new H2("Foglalás:");
                H3 doctorH3 = new H3("Orvos neve:");
                H4 doctorName = new H4(doctorDropdown.getValue().getFullName());
                H2 idopont = new H2("Időpont:");

                H3 ido=new H3();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.DAY_OF_WEEK));
                String day= String.valueOf(entryClickedEvent.getEntry().getRecurringDaysOfWeeks()).replaceAll("\\[","").replaceAll("\\]","");
                cal.set(Calendar.DAY_OF_WEEK, getDayNumber(day));
                String date=sdf.format(cal.getTime()) + "  "+ entryClickedEvent.getEntry().getRecurringStartTime() +"-"+ entryClickedEvent.getEntry().getRecurringEndTime();
                ido.setText(date);
                dialog.setHeight("300px");
                dialog.setWidth("400px");
                dialog.open();
                dialog.setCloseOnEsc(false);
                dialog.setCloseOnOutsideClick(false);
                confirmButton = new Button("Foglalás!", event -> {
                    Entry redEntry = entryClickedEvent.getEntry();
                    redEntry.setColor("#ff0000");
                    calendar.removeEntry(entryClickedEvent.getEntry());
                    calendar.addEntry(redEntry);
                    calendar.render();
                    dialog.close();
                    appointment.setPatient_id(patient.getId());
                    appointment.setDoctor_id(doctorDropdown.getValue().getId());
                    appointment.setDate(cal.getTime());
                    appointment.setStartapp(String.valueOf(entryClickedEvent.getEntry().getRecurringStartTime()));
                    appointment.setEndapp(String.valueOf(entryClickedEvent.getEntry().getRecurringEndTime()));
                    appointmentService.save(appointment);
                    Notification.show("Sikeres foglalás", 4000, Notification.Position.MIDDLE);
                });

                cancelButton = new Button("Vissza!", event -> {
                    dialog.close();
                });

                dialog.add(foglalas);
                dialog.add(doctorH3,doctorName);
                dialog.add(idopont,ido);
                dialog.add(confirmButton, cancelButton);
            }

        });

        img.setMaxHeight("300 px");
        verticalLayout.add(img, menuBar);
        //verticalLayout.add(nev);

        add(verticalLayout);
        add(doctorDropdown, calendar);
    }

    void addTimetableToCalendar(FullCalendar calendar, Integer id) {
        timetable = timetableService.getTimetable(id);
        String monday = timetable.get().getMonday();
        String tuesday = timetable.get().getTuesday();
        String wednesday = timetable.get().getWednesday();
        String thursday = timetable.get().getThursday();
        String friday = timetable.get().getFriday();

        addEntryToCalendar(monday, DayOfWeek.MONDAY, calendar);
        addEntryToCalendar(tuesday, DayOfWeek.TUESDAY, calendar);
        addEntryToCalendar(wednesday, DayOfWeek.WEDNESDAY, calendar);
        addEntryToCalendar(thursday, DayOfWeek.THURSDAY, calendar);
        addEntryToCalendar(friday, DayOfWeek.FRIDAY, calendar);
    }

    void addEntryToCalendar(String day, DayOfWeek dayOfWeek, FullCalendar calendar) {
        String[] parts = day.split("-");
        String part1 = parts[0];
        String part2 = parts[1];
        LocalTime from = LocalTime.parse(part1);
        LocalTime to = LocalTime.parse(part2);
        List<Entry> idopontok = new LinkedList<>();

        while (from.isBefore(to)) {
            Entry entry = new Entry();
            entry.setColor("#009900");
            entry.setRecurringDaysOfWeeks(Collections.singleton(dayOfWeek));
            entry.setRecurringEndDate(LocalDate.now().plusWeeks(2),calendar.getTimezone());
            entry.setRecurringStartTime(from);
            entry.setRecurringEndTime(from.plusMinutes(30));
            entry.setRecurringStartDate(LocalDate.now(),calendar.getTimezone());
            entry.setTitle("");
            entry.setEditable(false);
            from = from.plusMinutes(30);
            idopontok.add(entry);
        }

        for (Entry e : idopontok) {
            calendar.addEntry(e);
        }
    }

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        //UI.getCurrent().getElement().getStyle().set("background-color", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");
        foglalasH2.getStyle().set("margin-top", "0px");
        confirmButton.getStyle().set("margin", "20px");
        cancelButton.getStyle().set("margin", "20px");

    }

    int getDayNumber(String day){
        switch (day){
            case "MONDAY": return 2;
            case "TUESDAY": return  3;
            case "WEDNESDAY": return 4;
            case "THURSDAY": return 5;
            case "FRIDAY": return 6;
        }
        return 2;
    }
}
