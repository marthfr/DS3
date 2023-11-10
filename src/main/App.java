package main;

import main.comms.Comms;
import main.member.Member;
import main.server.Server;

import java.util.ArrayList;
import java.util.List;

public class App {
  public static void main(String[] args) {
    Server voteServer = new Server();
    Comms serverCommunication = new Comms(voteServer);
    voteServer.setCommunication(serverCommunication);

    // creation of new members with unique identifiers 
    List<Member> members = new ArrayList<>();
    List<Integer> ports = new ArrayList<>();
    int basePort = 6000;
    for (int i = 1; i <= 9; i++) {
      String memberId = "" + i;
      Comms communication = new Comms(voteServer);
      Member member = new Member(memberId, communication, voteServer);
      int port = basePort + i;
      ports.add(port);
      member.getCommunication().startServer(port, member.getMemberId());
      members.add(member);
    }

    voteServer.setMembers(members, ports);
    members.get(0).sendPrepareRequest();
  }
}
