package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import main.comms.Comms;
import main.member.AcceptedProposalPair;
import main.member.Member;
import main.server.Server;

public class MemberTest {
  @Test
  public void testGenerateProposalNumber() {
    Member member = new Member("1", mock(Comms.class), mock(Server.class));
    assertEquals("1:1", member.generateProposalNumber());
    assertEquals("1:2", member.generateProposalNumber());
  }

  @Test
  public void testGetMemberId() {
    Member member = new Member("1", mock(Comms.class), mock(Server.class));
    assertEquals("1", member.getMemberId());
  }

  @Test
  public void testSetAndGetHighestSeenProposalNumber() {
    Member member = new Member("1", mock(Comms.class), mock(Server.class));
    member.setHighestSeenProposalNumber("1:3");
    assertEquals("1:3", member.getHighestSeenProposalNumber());
  }

  @Test
  public void testSendPrepareRequest() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);

    member.sendPrepareRequest();
    verify(mockServer).broadcast(anyString());
  }

  @Test
  public void testDelayTimeInitialization() {
    // Member ID 1
    Member member1 = new Member("1", mock(Comms.class), mock(Server.class));
    assertEquals(0, member1.getDelayTime());

    // Member ID 2
    Member member2 = new Member("2", mock(Comms.class), mock(Server.class));
    assertEquals(10000, member2.getDelayTime());

    // Member ID 3
    Member member3 = new Member("3", mock(Comms.class), mock(Server.class));
    assertTrue(member3.getDelayTime() >= 4000 && member3.getDelayTime() <= 9000);

    // Default case
    Member memberDefault = new Member("999", mock(Comms.class), mock(Server.class));
    assertTrue(memberDefault.getDelayTime() >= 4000 && memberDefault.getDelayTime() <= 9000);
  }

  @Test
  public void testSendRejectWhenOffline() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);
    member.forceOffline();
    member.sendReject("1:4");

    verify(mockComm, never()).sendMessage(anyString(), anyString());
  }

  @Test
  public void testSendPromiseWhenOnline() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);
    member.sendPromise("2", "1:1", null);

    // Verifying that a message is sent when the member is online
    verify(mockComm).sendMessage(eq("2"), anyString());
  }

  @Test
  public void testSetAcceptedProposalPair() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);
    AcceptedProposalPair pair = new AcceptedProposalPair();
    pair.setAcceptedProposalPair("1.2", "value2");
    member.setAcceptedProposalPair("1:1", pair);

    assertEquals(pair, member.getAcceptedProposalPair());
  }

  @Test
  public void testSetAcceptedProposalPairWithLowerProposalNumber() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);
    AcceptedProposalPair pair1 = new AcceptedProposalPair();
    pair1.setAcceptedProposalPair("1.2", "value2");
    AcceptedProposalPair pair2 = new AcceptedProposalPair();
    pair2.setAcceptedProposalPair("1.1", "value1");
    member.setAcceptedProposalPair("1:1", pair1);
    member.setAcceptedProposalPair("1:2", pair2);

    assertNotEquals("1:1", member.getAcceptedProposalPair().getProposalID());
  }

  @Test
  public void testSetDelayTime() {
    Comms mockComm = mock(Comms.class);
    Server mockServer = mock(Server.class);
    Member member = new Member("1", mockComm, mockServer);
    member.setDelayTime(5000);
    assertEquals(5000, member.getDelayTime());
  }

}
