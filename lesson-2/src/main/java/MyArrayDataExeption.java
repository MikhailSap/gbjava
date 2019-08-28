public class MyArrayDataExeption extends Exception {
    private int iCell;
    private int jCell;

    public void setiCell(int iCell) {
        this.iCell = iCell;
    }

    public void setjCell(int jCell) {
        this.jCell = jCell;
    }

    @Override
    public String getMessage() {
        return "Сell [" +iCell+ "]" + "[" +jCell+ "]" + " contains not a number.";
    }
}
