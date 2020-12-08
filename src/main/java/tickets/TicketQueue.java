package tickets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class TicketQueue<Ticket> implements Queue {
    private final ArrayList<Ticket> ticketQueue;

    public TicketQueue(){
        ticketQueue = new ArrayList<>();
    }

    @Override
    public int size() {
        return ticketQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return ticketQueue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {

        return ticketQueue.contains(o);
    }

    @Override
    public Iterator iterator() {
        return ticketQueue.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        ticketQueue.add(0, (Ticket)o);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return ticketQueue.remove(o);
    }

    @Override
    public boolean addAll(Collection c) {
        return ticketQueue.addAll(c);
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public Ticket remove() {
        Ticket ticket = ticketQueue.get(ticketQueue.size()-1);
        ticketQueue.remove(ticketQueue.size()-1);
        return ticket;
    }

    @Override
    public Ticket poll() {
        if (!ticketQueue.isEmpty()){
            Ticket ticket = ticketQueue.get(ticketQueue.size()-1);
            ticketQueue.remove(ticketQueue.size()-1);
            return ticket;
        }
        return null;
    }

    @Override
    public Ticket element() {
        return null;
    }

    @Override
    public Ticket peek() {
        return null;
    }
}
