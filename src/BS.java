import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BS{

    static int _mutex = 0;
    static int _semaphore = 0;


    public static void main(String[] args) {
        JTextField textField = new JTextField();

        textField.addKeyListener(new MKeyListener());

        JFrame frame= new JFrame("Binary Semaphore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(textField);

        frame.setSize(400, 200);

        frame.setVisible(true);
    }
}

class MKeyListener extends KeyAdapter {

    ExecutorService readerPool = Executors.newFixedThreadPool(4);
    ExecutorService writerPool = Executors.newFixedThreadPool(4);

    int readerCount = 1;
    int writerCount = 1;

    @Override
    public void keyPressed(KeyEvent event) {

        char ch = event.getKeyChar();

        if (ch == 'r' || ch == 'R') {
            readerPool.execute(new Reader("reader " + readerCount++));

        } else if(ch == 'w' || ch == 'W'){
            writerPool.execute(new Writer("writer " + writerCount++));
        }

//        readerPool.shutdown();
//        writerPool.shutdown();

        if (event.getKeyCode() == KeyEvent.VK_HOME) {

            System.out.println("Key codes: " + event.getKeyCode());

        }
    }
}

// Java program to illustrate
// ThreadPool


// Task class to be executed (Step 1)
class Reader implements Runnable
{
    private String name;

    public Reader(String s)
    {
        name = s;
    }

    // Prints task name and sleeps for 1s
    // This Whole process is repeated 5 times
    public void run()
    {
        try
        {
//            while (BS._binarySemaphore == 1 && BS._mutex == 1) {
            while (BS._mutex == 1) {
                Date d = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                System.out.println(name + " is waiting to read file - " + ft.format(d));
                Thread.sleep(1000);
            }
            if (BS._mutex == 0 || BS._semaphore == 0) {
//            if (BS._binarySemaphore == 0) {
//                BS._binarySemaphore = 1;
                BS._semaphore++;
                Date d = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                System.out.println(name + " is reading file - " + ft.format(d));
//                this.wait(4000);
                Thread.sleep(4000);
                System.out.println(name + " complete");
//                BS._binarySemaphore = 0;
                BS._semaphore--;
                //prints the initialization time for every task
            }
        }

        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

class Writer implements Runnable
{
    private String name;

    public Writer(String s)
    {
        name = s;
    }

    // Prints task name and sleeps for 1s
    // This Whole process is repeated 5 times
    public void run()
    {
        try
        {
            while (BS._mutex == 1 || BS._semaphore > 0) {
                Date d = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                System.out.println(name + " is waiting to write to file - " + ft.format(d));
                Thread.sleep(1000);
            }
            if (BS._mutex == 0) {
                BS._mutex = 1;
                BS._semaphore = 1;
                Date d = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                System.out.println(name + " is writing to file - " + ft.format(d));
                Thread.sleep(4000);
                System.out.println(name + " complete");
                BS._mutex = 0;
                BS._semaphore = 0;
                //prints the initialization time for every task
            }
        }

        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
