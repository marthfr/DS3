# DISTRIBUTED SYSTEMS ASSIGNMENT 3 - Paxos Based Adelaide Suburbs Council Election 
## Martin Huang - a1798069

## Assignment Brief
This year, the Adelaide Suburbs Council is holding elections for the council president. Any of the nine members can become the council president. Members have varied responsiveness and preferences:

- `Member M1` – has wanted to be council president for a very long time. M1 is very chatty over social media and responds to emails/texts/calls almost instantly. It is as if M1 has an in-brain connection with their mobile phone!

- `Member M2` – has also wanted to be council president for a very long time, except their very long time is longer than everybody else's. M2 lives in the Adelaide Hills and thus their internet connection is really poor, almost non-existent. Responses to emails come in very late, and sometimes only to one of the emails in the email thread, so it is unclear whether M2 has read/understood them all. However, M2 sometimes likes to work at Sheoak Café. When that happens, their responses are instant and M2 replies to all emails.

- `Member M3 ` – has also wanted to be council president. M3 is not as responsive as M1, nor as late as M2, however sometimes emails completely do not get to M3. The other councillors suspect that it’s because sometimes M3 goes camping in the Coorong, completely disconnected from the world.

- `Members M4-M9` have no particular ambitions about council presidency and no particular preferences or animosities, so they will try to vote fairly. Their jobs keep them fairly busy and as such their response times  will vary.

On voting day, a council member will nominate a presidential candidate, and a majority vote is necessary for election.

The goal was to develop a Paxos-based election system that is fault tolerent and can manage different communication delays and disruptions. Socket based communication was recommended and was used.

## The Paxos Protocol

Paxos is a consensus algorithm designed to achieve agreement within distributed systems, even in the face of partial system failures. The protocol operates in distinct phases, ensuring that nodes in a distributed system agree upon a single piece of data or value.

## Overview of System

1. Initiating Proposal : A proposer forms a unique proposal ID and queries acceptors on whether a higher ID proposal exists. If not, they propose a value.

2. Consensus: Consensus is reached when the majority of acceptors commit to the proposer's proposal.

## Detailed Stages
#### Stage 1: Proposer (PREPARE) & Acceptor (PROMISE)
- Proposers issue a unique, ascending ID for the proposal, dispatching a `PREPARE` request to acceptors.
- Acceptors assess the `PREPARE` request against known IDs.
  - An ID not exceeding known IDs may be dismissed or met with a `REJECT`.
  - A new highest ID prompts a `PROMISE` not to entertain smaller IDs. If a proposal was previously accepted, its ID and value are included in the `PROMISE`.

#### Stage 2a: Proposer (PROPOSE)
- When a majority of `PROMISE` responses are collected, the proposer:
  - Must adopt an accepted value if provided by any acceptor.
  - Can propose a new value otherwise.
- An `ACCEPT` request is then circulated with the selected value and ID.

#### Stage 2b: Acceptor (ACCEPT)
- Acceptors judge the proposal by its ID:
  - The largest ID seen results in acceptance and an `ACCEPTED` broadcast to learners.
  - Otherwise, the proposal may be ignored or declined.
Consensus hinges on the value of the proposal, not the ID itself.

In essence, Paxos ensures unique proposals and steers the system to agree on a proposal's value, enabling reliable application or recording of that value.

## Member Class Overview

### Functions
1. Identification & Interaction: Distinct memberId for each Member and communication with peers.
2. Proposal Tracking: Generation and record-keeping of proposal IDs and the highest observed proposal.
3. Vote Processing: Using VoteServer to address and retain accepted proposals.
4. Simulating Delays: Introducing delays reflective of real-world or test conditions.
5. Offline Simulation: Members can simulate network issues or failures.
6. Consensus Messaging: Handles diverse messages related to achieving consensus.

## Server Class Overview

### Functions
1. **Achieving Consensus**: Implements Paxos to orchestrate voting.
2. **Communication**: Manages PREPARE, `PROMISE`, `ACCEPT`, and `ACCEPTED` messages.
3. **Member Oversight**: Maintains a roster of Member nodes participating in voting.
4. **Time-Related Handling**: Manages timeouts for proposal deliberation and acceptance.
5. **Electing a Leader**: Establishes the council president based on majority agreement.

## Testing
### PaxosTest
1. `testConcurrentVotingProposals` The primary test for concurrency within the system. It creates two separate threads (`Thread1` and `Thread2`) are initiated, representing a member sending a prepare request. After both threads complete execution, the test asserts that the president selected is "1". This test demonstrates the Paxos algorithm's ability to handle concurrent proposals and resolve them correctly.

2. `testImmediateResponse`
Testing how the Paxos algorithm handles immediate responses from members when selecting a president. Similar to the previous test, a VoteServer is set up along with members. Here, three threads (`member1Thread`, `member2Thread`, and `member3Thread`) are used, each starting a prepare request from different members. The test asserts that member "3" is elected as president, verifying that the system can manage immediate and multiple responses in the election process.

3. `testM2orM3GoOffline`
Testing when one of the members (member 3 in this case) goes offline during the election process. Calling to `forceOffline()` for member 3. Despite member 3 initiating a prepare request, the test validates that member "1" becomes the president. This scenario tests the handling member failures or network issues.

### VoteServerTest
Includes various tests to validate the functionalities of the VoteServer class. Covering member management (`testSetMembers`, `testAddMember`), communication setup (`testSetCommunication`), broadcasting messages to all members (`testBroadcast`), and comparing proposal numbers (`testCompareProposalNumbers`). Additional tests ensure correct initial conditions like testGetPresidentInitiallyNull, and message handling (`testHandleMessagePrepare`, `testHandleMessagePromise`). This tests robustness and correctness of the `VoteServer` class's responsibilities in the Paxos algorithm.

### MemberTest
Testing the `Member` class functionalities. It includes tests for basic operations: retrieving a member ID (`testGetMemberId`), generating proposal numbers (`testGenerateProposalNumber`), and setting/getting the highest seen proposal number (`testSetAndGetHighestSeenProposalNumber`). It also tests critical functionalities related to the Paxos protocol, such as sending prepare requests (`testSendPrepareRequest`), managing response delays (`testDelayTimeInitialization`), and handling member states (online/offline) in sending responses (`testSendRejectWhenOffline`, `testSendPromiseWhenOnline`). The ability of members to set and respect proposal pairs is also checked (`testSetAcceptedProposalPair`, `testSetAcceptedProposalPairWithLowerProposalNumber`), along with the customization of delay times (`testSetDelayTime`). These tests collectively ensure the Member class accurately represents and behaves as a Paxos protocol participant.

## Run
### Compiling
To compile the main application, run:

```bash
make
```
Will compile all java files into class files. Resulting output will be located in the bin folder.

### Running Tests
To compile and run the tests:

```bash
make test
```
This will compile both the main and test classes and subsequently run the tests using JUnit. Output will be shown in the terminal and could potentially run for a while as the algorithm runs. 

If you want the fastest display of this program, run ```make test```


### Refresh slate
```bash
make clean
```
Remove all class files to allow for clean recompilation with `make clean`
