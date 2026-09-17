package ir.skymine.stats;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class WebApi {
    private final StatsManager stats; private HttpServer server; private final boolean auth; private final String token;
    public WebApi(StatsManager stats,int port,boolean auth,String token)throws IOException{this.stats=stats;this.auth=auth;this.token=token==null?"":token;server=HttpServer.create(new InetSocketAddress(port),0);server.createContext("/api/v1/status",this::status);server.createContext("/api/v1/players",this::players);server.createContext("/api/v1/leaderboard",this::leaderboard);}
    public void start(){server.start();} public void stop(){if(server!=null)server.stop(0);}
    private boolean authorized(HttpExchange e){if(!auth)return true;String a=e.getRequestHeaders().getFirst("Authorization");return a!=null&&a.equals("Bearer "+token)&&!token.isBlank();}
    private void status(HttpExchange e)throws IOException{if(!authorized(e)){send(e,401,"{\"error\":\"unauthorized\"}");return;}send(e,200,"{\"online\":"+stats.online()+",\"max_players\":"+stats.maxPlayers()+",\"tracked_players\":"+stats.all().size()+"}");}
    private void players(HttpExchange e)throws IOException{if(!authorized(e)){send(e,401,"{\"error\":\"unauthorized\"}");return;}StringBuilder b=new StringBuilder("[");boolean first=true;for(PlayerStats p:stats.all()){if(!first)b.append(',');first=false;b.append("{\"uuid\":\"").append(p.uuid()).append("\",\"name\":\"").append(q(p.name())).append("\",\"playtime\":").append(p.playtimeSeconds()).append(",\"kills\":").append(p.kills()).append(",\"deaths\":").append(p.deaths()).append(",\"blocks_broken\":").append(p.blocksBroken()).append("}");}b.append(']');send(e,200,b.toString());}
    private void leaderboard(HttpExchange e)throws IOException{if(!authorized(e)){send(e,401,"{\"error\":\"unauthorized\"}");return;}List<PlayerStats> list=new ArrayList<>(stats.all());String metric=Optional.ofNullable(query(e.getRequestURI().getQuery(),"metric")).orElse("playtime");Comparator<PlayerStats> c=switch(metric){case "kills"->Comparator.comparingLong(PlayerStats::kills);case "deaths"->Comparator.comparingLong(PlayerStats::deaths);case "blocks_broken"->Comparator.comparingLong(PlayerStats::blocksBroken);case "blocks_placed"->Comparator.comparingLong(PlayerStats::blocksPlaced);default->Comparator.comparingLong(PlayerStats::playtimeSeconds);};list.sort(c.reversed());int limit=Math.min(10,list.size());StringBuilder b=new StringBuilder("[");for(int i=0;i<limit;i++){if(i>0)b.append(',');PlayerStats p=list.get(i);long value=switch(metric){case "kills"->p.kills();case "deaths"->p.deaths();case "blocks_broken"->p.blocksBroken();case "blocks_placed"->p.blocksPlaced();default->p.playtimeSeconds();};b.append("{\"rank\":").append(i+1).append(",\"uuid\":\"").append(p.uuid()).append("\",\"name\":\"").append(q(p.name())).append("\",\"value\":").append(value).append("}");}b.append(']');send(e,200,b.toString());}
    private static String query(String q,String key){if(q==null)return null;for(String x:q.split("&")){String[] p=x.split("=",2);if(p.length==2&&p[0].equals(key))return URLDecoder.decode(p[1],StandardCharsets.UTF_8);}return null;}
    private static String q(String s){return s==null?"":s.replace("\\","\\\\").replace("\"","\\\"");}
    private static void send(HttpExchange e,int code,String body)throws IOException{byte[] d=body.getBytes(StandardCharsets.UTF_8);e.getResponseHeaders().set("Content-Type","application/json; charset=utf-8");e.getResponseHeaders().set("Access-Control-Allow-Origin","*");e.sendResponseHeaders(code,d.length);try(OutputStream o=e.getResponseBody()){o.write(d);}}
}
