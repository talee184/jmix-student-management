package com.fis.bnk.studentmanagement.view.subject;

import com.fis.bnk.studentmanagement.entity.Subject;
import com.fis.bnk.studentmanagement.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "subjects/:id", layout = MainView.class)
@ViewController("Subject.detail")
@ViewDescriptor("subject-detail-view.xml")
@EditedEntityContainer("subjectDc")
public class SubjectDetailView extends StandardDetailView<Subject> {

}
