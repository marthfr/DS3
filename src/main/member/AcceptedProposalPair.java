package main.member;

public class AcceptedProposalPair {
  private String propasalId;
  private String proposalVal;

  public AcceptedProposalPair() {
    this.propasalId = null;
    this.proposalVal = null;
  }

  public AcceptedProposalPair getAcceptedProposalPair() {
    return this;
  }

  public void setAcceptedProposalPair(String propasalId, String proposalVal) {
    this.propasalId = propasalId;
    this.proposalVal = proposalVal;
  }

  public String getProposalValue() {
    return this.proposalVal;
  }
    public String getProposalID() {
    return this.propasalId;
  }

  public boolean isNull() {
    return this.propasalId != null && this.proposalVal != null;
  }
}

