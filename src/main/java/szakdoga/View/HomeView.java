package szakdoga.View;

import szakdoga.entity.Doctor;
import szakdoga.entity.Patient;
import szakdoga.entity.Timetable;
import szakdoga.service.DoctorService;
import szakdoga.service.PatientService;
import szakdoga.service.TimetableService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.*;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Route(value = "home")
public class HomeView extends VerticalLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    MenuBar menuBar = new MenuBar();
    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    ComboBox<Doctor> doctorDropdown = new ComboBox<>();
    FullCalendar calendar;
    int choosenDoctorId;

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    TimetableService timetableService;

    Optional<Timetable> timetable = Optional.of(new Timetable());

    Patient patient = new Patient();

    public HomeView() {
    }

    @PostConstruct
    public void init() {
        wrappedSession = session.getSession();
        //if (wrappedSession.getAttribute(Names.USERNAME) !=null){
        menuBar.setOpenOnHover(true);

        MenuItem foglalas = menuBar.addItem("Időpont foglalás");
        MenuItem lista = menuBar.addItem("Foglalásaim");
        MenuItem profil = menuBar.addItem("Profilom");
        MenuItem loguot = menuBar.addItem("Kijelentkezes");
        //patient=patientService.findByEmail(wrappedSession.getAttribute(Names.USERNAME).toString());
        loguot.addClickListener(click->{
            Notification.show("Sikeres kijelentkezes!",1500,Notification.Position.MIDDLE);
            UI.getCurrent().navigate(Login.class);
                });

        doctorDropdown.setLabel("Orvosok");
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
        add(verticalLayout);
        add(doctorDropdown, calendar);
    }

    void addCalendar(){
        remove(verticalLayout);
        remove(doctorDropdown,calendar);
        calendar = FullCalendarBuilder.create().build();
        calendar.setLocale(CalendarLocale.HUNGARIAN);
        calendar.setFirstDay(DayOfWeek.MONDAY);
        if(choosenDoctorId==0){
            addTimetableToCalendar(calendar, doctorDropdown.getValue().getId());
        }else{
            addTimetableToCalendar(calendar,choosenDoctorId);
        }

        calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK);
        BusinessHours businessHours = new BusinessHours(LocalTime.of(6, 00), LocalTime.of(18, 00));
        calendar.setBusinessHours(businessHours);
        calendar.setMaxWidth("50%");
        calendar.setMinHeight("600px");
        Image img=new Image("https://cdn.pixabay.com/photo/2014/12/10/20/56/medical-563427_960_720.jpg","banner");
        img.setMaxHeight("300 px");
        verticalLayout.add(img,menuBar);
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

        calendar.addEntry(addEntryToCalendar(monday, DayOfWeek.MONDAY));
        calendar.addEntry(addEntryToCalendar(tuesday, DayOfWeek.TUESDAY));
        calendar.addEntry(addEntryToCalendar(wednesday, DayOfWeek.WEDNESDAY));
        calendar.addEntry(addEntryToCalendar(thursday, DayOfWeek.THURSDAY));
        calendar.addEntry(addEntryToCalendar(friday, DayOfWeek.FRIDAY));
    }

    Entry addEntryToCalendar(String day, DayOfWeek dayOfWeek) {
        String[] parts = day.split("-");
        String part1 = parts[0];
        String part2 = parts[1];
        LocalTime from = LocalTime.parse(part1);
        LocalTime to = LocalTime.parse(part2);

        Entry entry = new Entry();
        entry.setColor("#009900");
        entry.setRecurringDaysOfWeeks(Collections.singleton(dayOfWeek));
        entry.setRecurringStartTime(from);
        entry.setRecurringEndTime(to);

        entry.setTitle("Rendelesi ido");
        entry.setEditable(false);
        return entry;
    }

    void setStyle(){
        UI.getCurrent().getElement().getStyle().set("width","100%");
        verticalLayout.getStyle().set("background-color","#f3f5f7");
        verticalLayout.getStyle().set("border-radius","15px");
    }
}
