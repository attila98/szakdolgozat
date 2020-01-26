package szakdoga.View;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "About")
@PageTitle("About")
public class AboutHome extends VerticalLayout {

    public AboutHome() {

        add(new H1("About oldal"));
    }

}
