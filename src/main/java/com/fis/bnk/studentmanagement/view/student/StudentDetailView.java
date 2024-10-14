package com.fis.bnk.studentmanagement.view.student;

import com.fis.bnk.studentmanagement.entity.Grade;
import com.fis.bnk.studentmanagement.entity.Student;
import com.fis.bnk.studentmanagement.entity.Subject;
import com.fis.bnk.studentmanagement.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.Notifications.Type;
import io.jmix.flowui.app.inputdialog.DialogActions;
import io.jmix.flowui.app.inputdialog.DialogOutcome;
import io.jmix.flowui.app.inputdialog.InputParameter;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.data.grid.ContainerDataGridItems;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "students/:id", layout = MainView.class)
@ViewController("Student.detail")
@ViewDescriptor("student-detail-view.xml")
@EditedEntityContainer("studentDc")
public class StudentDetailView extends StandardDetailView<Student> {

  @Autowired
  private Dialogs dialogs;
  @Autowired
  private DataManager dataManager;
  @Autowired
  private Notifications notifications;
  @ViewComponent
  private TypedTextField<Object> scoreField;
  @ViewComponent
  private InstanceContainer<Student> studentDc;
  @ViewComponent
  private CollectionContainer<Grade> gradeDc;
  @ViewComponent
  private JmixComboBox<String> subjectComboBox;
  @ViewComponent
  private DataContext dataContext;
  @ViewComponent
  private CollectionLoader<Grade> gradeDl;  // Sử dụng @ViewComponent để lấy loader từ XML
  @ViewComponent
  private DataGrid<Grade> gradesTable;  // Reference to DataGrid



  @Subscribe("addGradeBtn")
  public void onAddGradeClick(final ClickEvent<JmixButton> event) {
    dialogs.createInputDialog(this)
        .withHeader("Nhập điểm cho sinh viên")
        .withParameter(
            InputParameter.entityParameter("subject", Subject.class)
                .withLabel("Chọn môn học")
        )
        .withParameter(
            InputParameter.doubleParameter("score")
                .withLabel("Nhập điểm")
        )
        .withActions(DialogActions.OK_CANCEL)
        .withCloseListener(closeEvent -> {
          if (closeEvent.closedWith(DialogOutcome.OK)) {
            Subject selectedSubject = closeEvent.getValue("subject");
            Double scoreDouble = closeEvent.getValue("score");
            // Chuyển đổi Double sang BigDecimal
            BigDecimal score = scoreDouble != null ? BigDecimal.valueOf(scoreDouble) : null;
            // Kiểm tra giá trị
            if (selectedSubject != null && score != null) {

              // Check if the subject already exists in the current gradeDc
              boolean subjectExists = gradeDc.getItems().stream()
                  .anyMatch(grade -> grade.getSubject().equals(selectedSubject));

              if (subjectExists) {
                // Notify that the grade for the subject already exists
                notifications.create("Đã có điểm cho môn học này.")
                    .withType(Notifications.Type.WARNING)
                    .withDuration(3000)
                    .show();
                return;  // Exit the method to prevent saving
              }
              // Tạo đối tượng Grade mới và lưu vào dataContext
              Grade grade = dataContext.create(Grade.class);
              grade.setStudent(studentDc.getItem()); // Lấy student từ data container
              grade.setSubject(selectedSubject);
              grade.setScore(score);

              gradeDc.getMutableItems().add(grade);

            } else {
              // Thông báo nếu thiếu thông tin
              notifications.create("Vui lòng chọn môn học và nhập điểm hợp lệ.")
                  .withType(Type.WARNING)
                  .withDuration(3000)
                  .show();
            }
          }
        }).open();
  }

  @Subscribe(id = "studentDc", target = Target.DATA_CONTAINER)
  public void onStudentDcItemChange(InstanceContainer.ItemChangeEvent<Student> event) {
    Student student = event.getItem();  // Lấy sinh viên hiện tại từ studentDc

    if (student != null) {
      // Thiết lập tham số cho gradeDl loader và tải lại dữ liệu
      gradeDl.setParameter("student", student);
      gradeDl.load();
    }
  }
  // Helper method to get the single selected item from the DataGrid
  private Optional<Grade> getSingleSelectedGrade() {
    return gradesTable.getSelectedItems().stream().findFirst();  // Use stream to get a single item
  }
  // Handler for editing a grade
  @Subscribe("editBtn")
  public void onEditBtnClick(final ClickEvent<JmixButton> event) {
    getSingleSelectedGrade().ifPresent(selectedGrade -> {
      gradesTable.getEditor().editItem(selectedGrade);  // Correct usage of editor to edit the selected item
    });
  }

  @Subscribe("removeBtn")
  public void onRemoveBtnClick(final ClickEvent<JmixButton> event) {
    getSingleSelectedGrade().ifPresent(selectedGrade -> {
      dataContext.remove(selectedGrade);  // Mark for removal in DataContext
      gradeDc.getMutableItems().remove(selectedGrade);  // Remove from gradeDc

      // Update the DataGrid items after removal
      gradesTable.setItems(gradeDc.getItems());
    });
  }
  // Handler for canceling grade edits (Rollback when cancel)
  @Subscribe("cancelBtn")
  public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
      gradesTable.getEditor().cancel();  // Hủy bỏ các thay đổi trong chế độ chỉnh sửa
      reloadGradeData();  // Reload original data from the database to rollback changes

      notifications.create("Thay đổi đã bị hủy!")
          .withType(Type.WARNING)
          .withDuration(3000)  // Show the notification for 3 seconds
          .show();
  }
  // Reload grades from the database to rollback unsaved changes
  private void reloadGradeData() {
    // Clear the current items in gradeDc
    gradeDc.getMutableItems().clear();

    // Get the currently selected student
    Student student = studentDc.getItem();


    if (student != null) {
      // Query the database for grades associated with the current student
      List<Grade> grades = dataManager.load(Grade.class)
          .query("select g from Grade g join fetch g.subject left join fetch g.subject.students where g.student = :student")
          .parameter("student", student)
          .list();

      // Merge each grade into DataContext and add to gradeDc
      for (Grade grade : grades) {
        grade.setStudent(student);
        Grade grade1 = dataContext.merge(grade);
        gradeDc.getMutableItems().add(grade1);
      }
    }

    // Reset the DataGrid items to reflect the reloaded data
    gradesTable.setItems(new ContainerDataGridItems<>(gradeDc));
  }
}
