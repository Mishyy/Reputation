package com.kNoAPP.Reputation.data;


public enum Table {
	
	REP_STATS("ReputationStats", "CREATE TABLE IF NOT EXISTS ReputationStats("
			+ "uuid varchar(36), "
			+ "name varchar(16), "
			+ "rep int)"),
	REP_VOTES("ReputationVotes", "CREATE TABLE IF NOT EXISTS ReputationVotes("
			+ "giver varchar(36), "
			+ "receiver varchar(36), "
			+ "type varchar(4))");
	
	private String name;
	private String setup;
	
	private Table(String name, String setup) {
		this.name = name;
		this.setup = setup;
	}

	public String getName() {
		return name;
	}
	
	public String getSetup() {
		return setup;
	}
}
