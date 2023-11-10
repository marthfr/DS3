package main.member;

import main.comms.Comms;
import main.server.Server;

public class Member {
  private final String memberId;
  private final Comms communication;
  private int cnt = 0;
  private String topProposalNum = null;
  private AcceptedProposalPair acceptedProposalPair;
  private final Server voteServer;
  private final String memberProposalValue;
  private int delayTime;
  private boolean isForcedOffline;
  private boolean isRandomOffline;

  public Member(String memberId, Comms communication, Server voteServer) {
    this.memberId = memberId;
    this.communication = communication;
    this.voteServer = voteServer;
    this.memberProposalValue = memberId;

    switch (Integer.parseInt(memberId)) {
      case 1:
        this.delayTime = 0;
        break;
      case 2:
        this.delayTime = 10000;
        break;
      case 3:
        this.delayTime = (int) (Math.random() * 5000) + 4000;
        break;
      default:
        this.delayTime = (int) (Math.random() * 5000) + 4000;
        break;
    }
  }

  public String getMemberId() {
    return this.memberId;
  }

  public Comms getCommunication() {
    return this.communication;
  }

  public String generateProposalNumber() {
    cnt++;
    return memberId + ":" + cnt;
  }

  public void setHighestSeenProposalNumber(String proposalNumber) {
    this.topProposalNum = proposalNumber;
  }

  public String getHighestSeenProposalNumber() {
    return this.topProposalNum;
  }

  public String getMemberProposalValue() {
    return this.memberProposalValue;
  }

  public AcceptedProposalPair getAcceptedProposalPair() {
    return this.acceptedProposalPair;
  }

  public void setAcceptedProposalPair(String currentProposalNumber, AcceptedProposalPair acceptedProposalPair) {
    if (this.acceptedProposalPair == null) {
      this.acceptedProposalPair = acceptedProposalPair;
    } else {
      if (voteServer.compareProposalNumbers(acceptedProposalPair.getProposalID(), currentProposalNumber)) {
        this.acceptedProposalPair = acceptedProposalPair;
      }
    }
  }

  public void setDelayTime(int delayTime) {
    this.delayTime = delayTime;
  }

    public int getDelayTime() {
    return this.delayTime;
  }

  private boolean goOffline() {
    if (isForcedOffline) {
      return true;
    }
    if (isRandomOffline && Integer.parseInt(this.memberId) == 3) {
      int random = (int) (Math.random() * 100);
      if (random <= 5) {
        System.out.println("Member " + memberId + " is offline");
        return true;
      }
      return false;
    }
    return false;
  }

  public void turnOnRandomOffline() {
    this.isRandomOffline = true;
  }

  public void forceOffline() {
    this.isForcedOffline = true;
  }

  public void sendPrepareRequest() {
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String proposalNumber = generateProposalNumber();
    String message = "PREPARE " + proposalNumber;
    voteServer.broadcast(message);
  }

  public void sendPromise(String proposerId, String proposalNumber, AcceptedProposalPair acceptedProposalPair) {
    if (goOffline()) {
      return;
    }
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (acceptedProposalPair == null) {
      communication.sendMessage(proposerId, "PROMISE " + proposalNumber);
    } else {
      communication.sendMessage(proposerId,
        "PROMISE " +
        proposalNumber + " " +
        acceptedProposalPair.getProposalID() + " " + 
        acceptedProposalPair.getProposalValue());
    }
  }

  public synchronized void sendAcceptRequest(String proposalNumber, String proposalValue, String currentMemberId) {
    if (goOffline()) {
      return;
    }
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (proposalValue == null) {
      proposalValue = currentMemberId;
    }
    voteServer.broadcast("ACCEPT " + proposalNumber + " " + proposalValue);
  }

  public void sendReject(String proposalNumber) {
    if (goOffline()) {
      return;
    }
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    communication.sendMessage(memberId, "REJECT " + proposalNumber);
  }

  public void sendAccepted(String proposerId, String proposalNumber, String comingAcceptedValue) {
    if (goOffline()) {
      return;
    }
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    communication.sendMessage(proposerId,
        "ACCEPTED " + proposalNumber + " "
            + comingAcceptedValue);
  }

  public void sendResult(String proposalNumber, String proposalValue) {
    if (goOffline()) {
      return;
    }
    try {
      Thread.sleep(delayTime);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    voteServer.broadcast("RESULT " + proposalNumber + " " + proposalValue);
  }
}
