package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import szakdoga.Names;

import javax.annotation.PostConstruct;

@Route(value = "bookings")
@PageTitle("Foglalasok")
public class BookingsView extends VerticalLayout {

    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    VerticalLayout verticalLayout = new VerticalLayout();

    Image img = new Image("https://cdn.pixabay.com/photo/2014/12/10/20/56/medical-563427_960_720.jpg", "banner");
    MenuBar menuBar = new MenuBar();

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
            add(new H1("TODO"));


        } else {
            UI.getCurrent().navigate(Login.class);
            UI.getCurrent().getPage().executeJs("location.reload();");
        }
    }

    void setStyle() {
        UI.getCurrent().getElement().getStyle().set("width", "100%");
        verticalLayout.getStyle().set("background-color", "#f3f5f7");
        verticalLayout.getStyle().set("border-radius", "15px");
    }
}
