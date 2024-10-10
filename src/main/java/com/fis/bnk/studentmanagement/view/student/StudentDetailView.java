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
import io.jmix.flowui.component.textfield.TypedTextField;
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
              // Lấy student từ data container
              Student student = studentDc.getItem();

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

  @Subscribe("saveBtn")
  public void onSaveGrade() {
    // Kiểm tra nhập liệu
    String subjectName = subjectComboBox.getValue(); // Lấy tên môn học từ comboBox
    String scoreText = scoreField.getValue(); // Lấy giá trị dưới dạng String

    // Chuyển đổi String sang BigDecimal
    if (subjectName == null || scoreText == null || scoreText.isEmpty()) {
      notifications.create("Vui lòng nhập đủ thông tin")
          .withType(Type.WARNING)
          .show();
      return;
    }

    // Tra cứu đối tượng Subject từ cơ sở dữ liệu dựa trên tên môn học
    Subject selectedSubject = dataManager.load(Subject.class)
        .query("select s from Subject s where s.name = :name")
        .parameter("name", subjectName)
        .optional()
        .orElse(null);

    try {
      BigDecimal score = new BigDecimal(scoreText); // Chuyển đổi sang BigDecimal

      // Tạo đối tượng Grade mới và lưu vào dataContext
      Grade grade = dataManager.create(Grade.class);
      grade.setStudent(studentDc.getItem()); // Lấy student từ data container
      grade.setSubject(selectedSubject);
      grade.setScore(score);

      // Thêm điểm vào danh sách và cập nhật giao diện
      gradeDc.getMutableItems().add(grade);
      dataManager.save(grade); // Lưu dữ liệu

      notifications.create("Lưu thành công!")
          .withType(Type.SUCCESS)
          .show();

      // Đóng popup sau khi lưu
//      closePopup();
    } catch (NumberFormatException e) {
      notifications.create("Vui lòng nhập số hợp lệ cho điểm.")
          .withType(Type.WARNING)
          .show();
    }
  }

//  @Subscribe("cancelBtn")
//  public void onCancelGrade() {
//    // Đóng popup khi nhấn Hủy
//    closePopup();
//  }
//
//  private void closePopup() {
//    // Đóng popup (giả định bạn đang sử dụng InputDialog)
//    // Nếu bạn sử dụng InputDialog, bạn cần nắm bắt đối tượng InputDialog và gọi close()
//    // Ví dụ:
//    if (dialogs != null) {
//      dialogs.c(DialogOutcome.OK); // Đóng popup
//    }
//  }
}
