package com.fis.bnk.studentmanagement.view.teacher;

import com.fis.bnk.studentmanagement.entity.Teacher;
import com.fis.bnk.studentmanagement.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "teachers", layout = MainView.class)
@ViewController("Teacher.list")
@ViewDescriptor("teacher-list-view.xml")
@LookupComponent("teachersDataGrid")
@DialogMode(width = "64em")
public class TeacherListView extends StandardListView<Teacher> {

}
