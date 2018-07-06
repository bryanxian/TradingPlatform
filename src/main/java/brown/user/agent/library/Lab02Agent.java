package brown.user.agent.library;

import brown.logging.library.Logging;
import brown.mechanism.channel.library.SealedBidChannel;
import brown.platform.messages.library.GameReportMessage;
import brown.platform.messages.library.SimpleSealedReportMessage;
import brown.system.setup.library.SSSPSetup;

/**
 * Agent that bids in the lab 2 game- just submits valuation.
 * @author andrew
 *
 */
public class Lab02Agent extends AbsLab02Agent {

	public Lab02Agent(String host, int port) {
		super(host, port, new SSSPSetup());
	}

	@Override
	public void onSimpleSealed(SealedBidChannel channel) {		
		// this just bids your valuation
		this.submitBid(channel, this.getValuation());
	}
		
  @Override
  public void onGameReport(GameReportMessage gameReport) {
    Logging.log("Game report received");
    SimpleSealedReportMessage report = (SimpleSealedReportMessage) gameReport;
    report.getWinner(); // get who won last game
    report.getNumPlayers(); //  get the number of players last round
  } 
	
	public static void main(String[] args) {
		new Lab02Agent("localhost", 2121);
    new Lab02Agent("localhost", 2121);		
    new Lab02Agent("localhost", 2121);
    new Lab02Agent("localhost", 2121);  
    new Lab02Agent("localhost", 2121);
    new Lab02Agent("localhost", 2121);     
		while (true) {}
	}

}