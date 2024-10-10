package com.fis.bnk.studentmanagement.view.subject;

import com.fis.bnk.studentmanagement.entity.Subject;
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


@Route(value = "subjects", layout = MainView.class)
@ViewController("Subject.list")
@ViewDescriptor("subject-list-view.xml")
@LookupComponent("subjectsDataGrid")
@DialogMode(width = "64em")
public class SubjectListView extends StandardListView<Subject> {

  @ViewComponent
  private TypedTextField<String> codeField;        // Inject the codeField component

  @ViewComponent
  private TypedTextField<String> nameField;        // Inject the nameField component

  @ViewComponent
  private CollectionLoader<Subject> subjectsDl;
  @Subscribe("searchBtn")
  public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
    String codeText = codeField.getValue();         // Get search input for code
    String nameText = nameField.getValue();         // Get search input for name

    if (codeText != null && !codeText.isEmpty()) {
      subjectsDl.setParameter("code", "%" + codeText.toLowerCase() + "%");
    } else {
      subjectsDl.removeParameter("code");
    }

    if (nameText != null && !nameText.isEmpty()) {
      subjectsDl.setParameter("name", "%" + nameText.toLowerCase() + "%");
    } else {
      subjectsDl.removeParameter("name");
    }

    subjectsDl.load();  // Reload the data with new search criteria
  }
}
