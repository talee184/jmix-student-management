package com.fis.bnk.studentmanagement.view.teacher;

import com.fis.bnk.studentmanagement.entity.Teacher;
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


@Route(value = "teachers", layout = MainView.class)
@ViewController("Teacher.list")
@ViewDescriptor("teacher-list-view.xml")
@LookupComponent("teachersDataGrid")
@DialogMode(width = "64em")
public class TeacherListView extends StandardListView<Teacher> {

  @ViewComponent
  private TypedTextField<String> nameField;        // Inject the nameField component

  @ViewComponent
  private TypedTextField<String> ageField;        // Inject the ageField component

  @ViewComponent
  private TypedTextField<String> addressField;        // Inject the addressField component

  @ViewComponent
  private TypedTextField<String> phoneNumberField;        // Inject the phoneNumberField component

  @ViewComponent
  private CollectionLoader<Teacher> teachersDl;


  @Subscribe("searchBtn")
  public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
    String nameText = nameField.getValue();         // Get search input for name
    String ageText = ageField.getValue();         // Get search input for age
    String addressText = addressField.getValue();         // Get search input for address
    String phoneNumberText = phoneNumberField.getValue();         // Get search input for phoneNumber

    if (nameText != null && !nameText.isEmpty()) {
      teachersDl.setParameter("name", "%" + nameText.toLowerCase() + "%");
    } else {
      teachersDl.removeParameter("name");
    }

    if (ageText != null && !ageText.isEmpty()) {
      Integer age = Integer.valueOf(ageText);
      teachersDl.setParameter("age", age);
    } else {
      teachersDl.removeParameter("age");
    }

    if (addressText != null && !addressText.isEmpty()) {
      teachersDl.setParameter("address", "%" + addressText.toLowerCase() + "%");
    } else {
      teachersDl.removeParameter("address");
    }

    if (phoneNumberText != null && !phoneNumberText.isEmpty()) {
      teachersDl.setParameter("phoneNumber", "%" + phoneNumberText.toLowerCase() + "%");
    } else {
      teachersDl.removeParameter("phoneNumber");
    }

    teachersDl.load();  // Reload the data with new search criteria
  }
}
