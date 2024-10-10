package com.fis.bnk.studentmanagement.view.school;

import com.fis.bnk.studentmanagement.entity.School;
import com.fis.bnk.studentmanagement.entity.Student;
import com.fis.bnk.studentmanagement.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "schools", layout = MainView.class)
@ViewController("School.list")
@ViewDescriptor("school-list-view.xml")
@LookupComponent("schoolsDataGrid")
@DialogMode(width = "64em")
public class SchoolListView extends StandardListView<School> {

  @ViewComponent
  private TypedTextField<String> codeField;        // Inject the codeField component

  @ViewComponent
  private TypedTextField<String> nameField;        // Inject the nameField component

  @ViewComponent
  private CollectionLoader<School> schoolsDl;


  @Subscribe("searchBtn")
  public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
    String codeText = codeField.getValue();         // Get search input for code
    String nameText = nameField.getValue();         // Get search input for name

    if (codeText != null && !codeText.isEmpty()) {
      schoolsDl.setParameter("code", "%" + codeText.toLowerCase() + "%");
    } else {
      schoolsDl.removeParameter("code");
    }

    if (nameText != null && !nameText.isEmpty()) {
      schoolsDl.setParameter("name", "%" + nameText.toLowerCase() + "%");
    } else {
      schoolsDl.removeParameter("name");
    }

    schoolsDl.load();  // Reload the data with new search criteria
  }
}
