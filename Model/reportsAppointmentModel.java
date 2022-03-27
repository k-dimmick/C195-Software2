package Model;

import java.time.LocalDate;
import java.util.Locale;

public class reportsAppointmentModel {

    private String monthAndYear;
    private int total;
    private String type;

    public static reportsAppointmentModel currentReport;


    /**
     * Created this Appointment Model to be able to generate required report more freely
     * Model is used at reportsController/appointmentTime
     * @param monthAndYear
     * @param total
     * @param type
     */
    public reportsAppointmentModel(String monthAndYear, int total, String type){
        this.monthAndYear=monthAndYear;
        this.total=total;
        this.type=type;
    }


    /**
     * All Getters and Setters
     * @return
     */
    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static reportsAppointmentModel getCurrentReport() {
        return currentReport;
    }

    public static void setCurrentReport(reportsAppointmentModel currentReport) {
        reportsAppointmentModel.currentReport = currentReport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
