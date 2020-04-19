package szakdoga.View;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
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

@PageTitle("Profil")
@Route(value = "doctorprofile")
public class DoctorProfileView extends VerticalLayout {
    VerticalLayout verticalLayout = new VerticalLayout();
    VaadinSession session = UI.getCurrent().getSession();
    WrappedSession wrappedSession;
    MenuBar menuBar = new MenuBar();

    Image img = new Image("header.png","banner");

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
        verticalLayout.setHeight("800px");

        menuBar.getStyle().set("padding-left","20px");

        img.getStyle().set("width","1100px");
        img.getStyle().set("align-self","center");
        img.getStyle().set("border-radius","15px");
        img.getStyle().set("border","1px solid");

    }

}
