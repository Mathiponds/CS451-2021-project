package cs451.links;

import cs451.Message;

import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class OutputLink extends Link{
    private Link link;
    private List<String> output;
    private String path;

    public OutputLink(Link link, String path){
        this.link = link;
        this.path = path;
        output = new ArrayList<>();
    }
    @Override
    public Message deliver() throws SocketTimeoutException {
        Message m = link.deliver();
        output.add("d "+m.getSrcPort()+" "+m.getSeqNumber());
        return m;
    }

    @Override
    public void send(List<Message> lm) {
        link.send(lm);
        for(Message m : lm)
            output.add("b "+m.getSeqNumber());
    }

    @Override
    public void close() {
        link.close();
        write();

    }
    public void write(){
        try {
            FileWriter writer = new FileWriter(path);
            for(String s : output){
                writer.write(s+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
