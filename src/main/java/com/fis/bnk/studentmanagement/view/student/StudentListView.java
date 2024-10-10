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

    // Set search parameters based on the user's input
    setSearchParameter("code", codeText);
    setSearchParameter("name", nameText);
    setSearchParameter("address", addressText);
    setSearchParameter("phoneNumber", phoneText);

    studentsDl.load();  // Reload the data with new search criteria
  }
  /**
   * Helper method to set or remove the search parameter based on the input.
   * If the input is null or empty, it removes the parameter.
   * Otherwise, it sets the parameter using a wildcard search (LIKE '%input%').
   */
  private void setSearchParameter(String parameterName, String parameterValue) {
    if (parameterValue != null && !parameterValue.trim().isEmpty()) {
      studentsDl.setParameter(parameterName, "%" + parameterValue.trim().toLowerCase() + "%");
    } else {
      studentsDl.removeParameter(parameterName);
    }}
}
