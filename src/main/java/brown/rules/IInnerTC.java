package brown.rules;

import brown.market.marketstate.IMarketState;

/**
 * An inner termination condition manages when a single auction is over. 
 */
public interface IInnerTC {
  
  /**
   * determines the condition for an inner auction to be over. 
   * an example of an inner termined auction is an ascending auction.
   * @param state market internal state.
   */
  public void innerTerminated(IMarketState state);

  /**
   * resets all stored information. 
   */
  public void reset();

}
