package App;

import Base.Decree;
import Networking.PeerNode;

import java.util.Scanner;

public class FileSystemPeer {
    private PeerNode node;

    public void run() {
        node = new PeerNode();
        Runnable runnable = node::run;
        Thread t = new Thread(runnable);
        t.start();
        try {
            Thread.sleep(4000);
            //prompt user input
            executeInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeInput() {
        String o;
        Scanner input = new Scanner(System.in);


        while (true) {
            System.out.println("Input an operation >>");
            o = input.nextLine();

            if(o.equals("exit")) {
                input.close();
                break;
            }

            Decree op = new FileOperation(o);
            node.localRequest(op);
        }
    }
}
