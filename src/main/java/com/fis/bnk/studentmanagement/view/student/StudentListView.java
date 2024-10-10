package com.fis.bnk.studentmanagement.view.student;

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


@Route(value = "students", layout = MainView.class)
@ViewController("Student.list")
@ViewDescriptor("student-list-view.xml")
@LookupComponent("studentsDataGrid")
@DialogMode(width = "64em")
public class StudentListView extends StandardListView<Student> {

  @ViewComponent
  private CollectionLoader<Student> studentsDl;

  @ViewComponent
  private TypedTextField<String> codeField;        // Inject the codeField component

  @ViewComponent
  private TypedTextField<String> nameField;        // Inject the nameField component

  @ViewComponent
  private TypedTextField<String> addressField;     // Inject the addressField component

  @ViewComponent
  private TypedTextField<String> phoneNumberField; // Inject the phoneNumberField component

  @Subscribe("searchBtn")
  public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
    String codeText = codeField.getValue();         // Get search input for code
    String nameText = nameField.getValue();         // Get search input for name
    String addressText = addressField.getValue();   // Get search input for address
    String phoneText = phoneNumberField.getValue(); // Get search input for phone number

    if (codeText != null && !codeText.isEmpty()) {
      studentsDl.setParameter("code", "%" + codeText.toLowerCase() + "%");
    } else {
      studentsDl.removeParameter("code");
    }

    if (nameText != null && !nameText.isEmpty()) {
      studentsDl.setParameter("name", "%" + nameText.toLowerCase() + "%");
    } else {
      studentsDl.removeParameter("name");
    }

    if (addressText != null && !addressText.isEmpty()) {
      studentsDl.setParameter("address", "%" + addressText.toLowerCase() + "%");
    } else {
      studentsDl.removeParameter("address");
    }

    if (phoneText != null && !phoneText.isEmpty()) {
      studentsDl.setParameter("phoneNumber", "%" + phoneText.toLowerCase() + "%");
    } else {
      studentsDl.removeParameter("phoneNumber");
    }

    studentsDl.load();  // Reload the data with new search criteria
  }
}
