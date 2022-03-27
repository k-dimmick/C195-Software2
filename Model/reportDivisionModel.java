package Model;

public class reportDivisionModel {

    private int divisionID;
    private String division;
    private int total;

    public static reportDivisionModel currentReport;

    /**
     * Created model for divisions to be able to generate reports -
     * this is for the following method: reportsController/locationTable
     * @param divisionID
     * @param division
     * @param total
     */
        public reportDivisionModel (int divisionID, String division, int total){
            this.divisionID=divisionID;
            this.division=division;
            this.total=total;
        }

    /**
     * All getters and setters
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static reportDivisionModel getCurrentReport() {
        return currentReport;
    }

    public static void setCurrentReport(reportDivisionModel currentReport) {
        reportDivisionModel.currentReport = currentReport;
    }
}
