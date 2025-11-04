// ===============================================
// Customer Support Ticket System (Java - Simple Version)
// Save as: Main.java
// Compile: javac Main.java
// Run:     java Main
// ===============================================
import java.util.*;

// ---------------- TICKET CLASS ----------------
class Ticket {
    int id;
    String customerName;
    String issue;
    int priority; // 1 = highest priority

    Ticket(int id, String customerName, String issue, int priority) {
        this.id = id;
        this.customerName = customerName;
        this.issue = issue;
        this.priority = priority;
    }

    public String toString() {
        return "Ticket ID: " + id + " | Name: " + customerName + " | Issue: " + issue + " | Priority: " + priority;
    }
}

// ---------------- LINKED LIST FOR TICKETS ----------------
class TicketList {
    class Node { Ticket data; Node next; Node(Ticket t){ data = t; } }
    Node head;

    void addTicket(Ticket t) {
        Node n = new Node(t);
        n.next = head;
        head = n;
    }

    boolean deleteTicket(int id) {
        Node prev = null, current = head;
        while (current != null) {
            if (current.data.id == id) {
                if (prev == null) head = current.next;
                else prev.next = current.next;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    Ticket findTicket(int id) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.id == id) return temp.data;
            temp = temp.next;
        }
        return null;
    }

    void showAll() {
        Node temp = head;
        if (temp == null) System.out.println("No Tickets Found.");
        while (temp != null) { System.out.println(temp.data); temp = temp.next; }
    }
}

// ---------------- STACK FOR UNDO ----------------
class UndoStack {
    Stack<Integer> undoIds = new Stack<>();
    void push(int id){ undoIds.push(id); }
    Integer pop(){ return undoIds.isEmpty()? null : undoIds.pop(); }
}

// ---------------- PRIORITY QUEUE ----------------
class PriorityTicketQueue {
    Queue<Ticket> urgent = new LinkedList<>();  // priority 1
    Queue<Ticket> normal = new LinkedList<>();   // priority 2,3,4,5

    void add(Ticket t) {
        if (t.priority == 1) urgent.add(t);
        else normal.add(t);
    }

    Ticket remove() {
        if (!urgent.isEmpty()) return urgent.remove();
        if (!normal.isEmpty()) return normal.remove();
        return null;
    }
}

// ---------------- CIRCULAR QUEUE (ROUND ROBIN) ----------------
class RoundRobinQueue {
    Queue<Ticket> queue = new LinkedList<>();
    void add(Ticket t){ queue.add(t); }
    Ticket next(){ return queue.poll(); }
}

// ---------------- MAIN SYSTEM ----------------
public class Main {
    static Scanner sc = new Scanner(System.in);

    static TicketList ticketList = new TicketList();
    static UndoStack undo = new UndoStack();
    static PriorityTicketQueue priorityQueue = new PriorityTicketQueue();
    static RoundRobinQueue roundQueue = new RoundRobinQueue();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== CUSTOMER SUPPORT TICKET SYSTEM ====");
            System.out.println("1. Add Ticket");
            System.out.println("2. Delete Ticket");
            System.out.println("3. Find Ticket");
            System.out.println("4. Show All Tickets");
            System.out.println("5. Undo Last Add");
            System.out.println("6. Priority Processing");
            System.out.println("7. Round Robin Processing");
            System.out.println("0. Exit");
            System.out.print("Enter Option: ");
            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1: addTicket(); break;
                case 2: deleteTicket(); break;
                case 3: findTicket(); break;
                case 4: ticketList.showAll(); break;
                case 5: undoLast(); break;
                case 6: priorityProcess(); break;
                case 7: roundRobin(); break;
                case 0: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid Option!");
            }
        }
    }

    static void addTicket() {
        System.out.print("Enter Ticket ID: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Customer Name: "); String name = sc.nextLine();
        System.out.print("Enter Issue: "); String issue = sc.nextLine();
        System.out.print("Priority (1 = urgent, 2/3/4/5 = normal): "); int p = sc.nextInt();

        Ticket t = new Ticket(id, name, issue, p);
        ticketList.addTicket(t);
        undo.push(id);
        priorityQueue.add(t);
        roundQueue.add(t);
        System.out.println("Ticket Added Successfully.");
    }

    static void deleteTicket() {
        System.out.print("Enter Ticket ID to Remove: ");
        int id = sc.nextInt();
        System.out.println(ticketList.deleteTicket(id) ? "Deleted" : "Not Found");
    }

    static void findTicket() {
        System.out.print("Enter Ticket ID: ");
        int id = sc.nextInt();
        Ticket t = ticketList.findTicket(id);
        System.out.println(t != null ? t : "Ticket Not Found");
    }

    static void undoLast() {
        Integer id = undo.pop();
        if (id == null) System.out.println("Nothing to Undo");
        else {
            ticketList.deleteTicket(id);
            System.out.println("Undo Successful. Last Ticket Removed.");
        }
    }

    static void priorityProcess() {
        Ticket t = priorityQueue.remove();
        System.out.println(t == null ? "No Tickets to Process" : "Processing: " + t);
    }

    static void roundRobin() {
        Ticket t = roundQueue.next();
        System.out.println(t == null ? "No Tickets to Process (Round Robin)" : "Processing: " + t);
    }
}
