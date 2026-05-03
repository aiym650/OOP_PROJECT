package university.interfaces;
public interface JournalObserver {
    void onNewPaperPublished(String journalName, String paperTitle);
}