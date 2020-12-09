package tickets;

import credentials.Credential;

public class TicketDispenser {
    private final TicketQueue<Ticket> ticketQueue;
    private int tact = 1250;
    private int id;
    private boolean running = true;

    public TicketDispenser(TicketQueue<Ticket> ticketQueue, Credential credential) {
        this.ticketQueue = ticketQueue;
        Thread thread = new Thread(() -> {
            while (running){
                try {
                    Thread.sleep(tact);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createTicket(credential);
                System.out.println("New Ticket created");
            }
        });
        thread.start();
    }

    public synchronized Ticket getTicket(){
        if (!ticketQueue.isEmpty()){
            return ticketQueue.poll();
        }
        return null;
    }

    public int getStatus(){
        if (!ticketQueue.isEmpty()){
            return ticketQueue.size();
        }
        return 0;
    }

    private synchronized void createTicket(Credential credential){
        ticketQueue.add(new Ticket(createID(), credential));
    }

    public synchronized int createID()
    {
        return id++;
    }

    public void setTact(int newTact){
        tact = newTact;
    }

    public void stop(){
        running = false;
    }

}
