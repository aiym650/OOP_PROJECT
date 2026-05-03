package university.enums;
public enum ManagerType {
    OR("Office Registrar"),
    DEPARTMENT("Department Manager"),
    DEAN("Dean"),
    RECTOR("Rector");

    private final String title;

    ManagerType(String title) { this.title = title; }

    public String getTitle() { return title; }

    @Override
    public String toString() { return title; }
}