package university.models;

import university.enums.Language;
import university.enums.RequestStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TechSupportSpecialist extends Employee {

    private static final long serialVersionUID = 1L;

    private final List<SupportRequest> requests = new ArrayList<>();

    public TechSupportSpecialist(String id, String firstName, String lastName,
                                  String password, Language language,
                                  double salary, String department) {
        super(id, firstName, lastName, password, language, salary, department);
    }

    public void viewNewRequests() {
        System.out.println("=== New Requests ===");
        requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW)
                .forEach(r -> {
                    r.setStatus(RequestStatus.VIEWED);
                    System.out.println(r);
                });
    }

    public void acceptRequest(SupportRequest request) {
        request.setStatus(RequestStatus.ACCEPTED);
        System.out.println("[Accepted] " + request);
    }

    public void rejectRequest(SupportRequest request) {
        request.setStatus(RequestStatus.REJECTED);
        System.out.println("[Rejected] " + request);
    }

    public void markDone(SupportRequest request) {
        request.setStatus(RequestStatus.DONE);
        System.out.println("[Done] " + request);
    }

    public void addRequest(SupportRequest request) { requests.add(request); }
    public List<SupportRequest> getRequests()      { return Collections.unmodifiableList(requests); }

    @Override
    public String getInfo() {
        return String.format("TechSupport{%s, requests=%d}", getFullName(), requests.size());
    }
}