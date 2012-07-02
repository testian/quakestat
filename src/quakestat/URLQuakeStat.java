/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package quakestat;

import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
/**
 *
 * @author testi
 */
public class URLQuakeStat extends QuakeStat {
    private URL masterserver;
    private MasterParser masterParser;
    private boolean useFactory;
    private ServerFetcherFactory factory;
    public URLQuakeStat(String gameName, URL masterserver, MasterParser masterParser, boolean useFactory) {
        super(gameName, (InetAddress)null);
        this.masterserver = masterserver;
        this.masterParser = masterParser;
        this.useFactory = useFactory;
        if (useFactory) factory = new ServerFetcherFactory();
        else
        factory = null;
    }
    public URLQuakeStat(String gameName, URL masterserver, MasterParser masterParser) {
    this(gameName, masterserver, masterParser, false);
    }
    public URLQuakeStat(String gameName, URL masterserver) {
    this(gameName, masterserver, null, false);
    }
    public URLQuakeStat(String gameName, URL masterserver, boolean useFactory) {
    this(gameName, masterserver, null, useFactory);
    }



    @Override
    public void parseServers(ServerListener sl) throws IOException {
        InputStream is = masterserver.openStream();
        

        List<String> msl;
        if (masterParser == null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        msl = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
        msl.add(line);
        }
        } else {
        msl = parseListByType(is);
        }
        List<SocketAddress> socketList = new ArrayList<SocketAddress>();
        for (String serverLine : msl) {

                    String hostname = serverLine;
        int port = -1;
        String[] fields = serverLine.split(":");
        if (fields.length == 2) {
        hostname = fields[0];
        try {port = Integer.parseInt(fields[1]);} catch (NumberFormatException ex){System.err.println(ex);}
        }
        try {
        socketList.add(new SocketAddress(InetAddress.getByName(hostname),port));
        } catch (UnknownHostException ex) {System.err.println(ex);}

        }

        final ArrayBlockingQueue<GameServer> serverQueue = new ArrayBlockingQueue<GameServer>(50);
        int threadCount = getMaxsim();
        if (threadCount>socketList.size()) threadCount = socketList.size();
        Thread[] threads = new Thread[threadCount];
        FetchTask[] tasks = new FetchTask[threadCount];
        for (int i = 0; i < threads.length;i++){
            FetchTask task = getRunnable(socketList, i, threadCount, serverQueue);
            tasks[i] = task;
            threads[i] = new Thread(task);
        }
        for (Thread t : threads) {
        t.start();
        }
        int totalServers = socketList.size();
        int serversCatched = 0;
        try {
        while (serversCatched < totalServers && sl.proceed()) {
            GameServer server;
            boolean allStop = true;
            while ((server = serverQueue.poll(1000, TimeUnit.MILLISECONDS)) == null) {
                for (FetchTask f : tasks) {
                if (!f.stopped()) allStop = false;
            }
            if (allStop) break;
            }
            if (server == null && allStop) break;
            sl.onServer(server);
            serversCatched++;

        }
        for (FetchTask f : tasks) {
        f.stop();
        }
        } catch(InterruptedException ex) {System.err.println(ex);}

        /*for (String serverLine : msl) {
        String hostname = serverLine;
        int port = -1;
        String[] fields = serverLine.split(":");
        if (fields.length == 2) {
        hostname = fields[0];
        try {port = Integer.parseInt(fields[1]);} catch (NumberFormatException ex){System.err.println(ex);}
        }
        

        
        try {
            if (!sl.proceed()) break;
            ServerFetcher quakeStat = instantiateQuakeStat(InetAddress.getByName(hostname), port);
            List<GameServer> list = quakeStat.getServers();
            if (!list.isEmpty())
                sl.onServer(list.get(0));
        }

        catch (UnknownHostException ex){System.err.println(ex);}
        }*/
        
    }

    private FetchTask getRunnable(List<SocketAddress> msl, int threadIndex,int threadCount, final ArrayBlockingQueue<GameServer> serverQueue) {
    final LinkedList<SocketAddress> subMsl = new LinkedList<SocketAddress>();
    for (int i = threadIndex; i < msl.size();i+=threadCount) {
    subMsl.add(msl.get(i));
    }

        return new FetchTask() {
        private boolean stop = false;
                public void run() {
                    for (SocketAddress sa : subMsl) {
                    {
                        if (stop) break;




        try {
            ServerFetcher quakeStat = instantiateQuakeStat(sa.getAddress(), sa.getPort());
            List<GameServer> list = quakeStat.getServers();
            if (!list.isEmpty())
                if (!serverQueue.offer(list.get(0))) { System.err.println("Queue full"); break; }
        }

        catch (IOException ex) {System.err.println(ex);}
        }
        
                    }
                    stop = true;

                }

            @Override
            public void stop() {
                stop = true;
            }
            public boolean stopped() {
            return stop;
            }


    };
    }

    private abstract class FetchTask implements Runnable {
    abstract public void run();
    abstract public void stop();
    abstract public boolean stopped();
    }

    private List<String> parseListByType(InputStream is) throws IOException {
    return masterParser.getServerList(is);
    }

    private ServerFetcher instantiateQuakeStat(InetAddress host, int port) {
    
    ServerFetcher quakeStat;
    if (useFactory)
    quakeStat = factory.createServerFetcher(this.getGameName(), host, port);
    else
    quakeStat = new QuakeStat(this.getGameName(), host, port);

    quakeStat.setIncludePlayers(this.isIncludePlayers());
    quakeStat.setInterval(this.getInterval());
    quakeStat.setMustBeNotEmpty(this.isMustBeNotEmpty());
    quakeStat.setMustBeNotFull(this.isMustBeNotFull());
    quakeStat.setMustBeUp(this.isMustBeUp());
    quakeStat.setRetry(this.getRetry());
    quakeStat.setSendinterval(this.getSendinterval());
    quakeStat.setCfg(this.getCfg());
    return quakeStat;
    }
    /*private ServerFetcher instantiateQuakeStat(InetAddress host) {
    return instantiateQuakeStat(host, -1);
    }*/
}
