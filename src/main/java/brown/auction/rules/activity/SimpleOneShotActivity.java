package brown.auction.rules.activity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.auction.marketstate.IMarketState;
import brown.auction.rules.AbsRule;
import brown.auction.rules.IActivityRule;
import brown.communication.bid.IBid;
import brown.communication.bid.IBidBundle;
import brown.communication.bid.library.BidType;
import brown.communication.messages.ITradeMessage;
import brown.platform.item.ICart;

/**
 * carts must be of 1 item of size 1, agents must not have bid before.
 * 
 * @author andrewcoggins
 *
 */
public class SimpleOneShotActivity extends AbsActivity
    implements IActivityRule {

  @Override
  public void isAcceptable(IMarketState state, ITradeMessage aBid,
      List<ITradeMessage> currentBids, ICart items) {
    if (!isWellFormed(aBid, items)) {
      state.setAcceptable(false);
    } else {
      state.setAcceptable(true);
      IBid bid = aBid.getBid();
      for (ITradeMessage currentBid : currentBids) {
        if (currentBid.getAgentID().equals(aBid.getAgentID())) {
          state.setAcceptable(false);
          break;
        }
      }
      IBidBundle bundle = (IBidBundle) bid;
      Map<ICart, Double> carts = bundle.getBids();
      for (ICart cart : carts.keySet()) {
        // all carts have only one item
        if (cart.getItems().size() != 1) {
          state.setAcceptable(false);
          break;
        }
        // all items are size 1
        if (cart.getItems().get(0).getItemCount() != 1) {
          state.setAcceptable(false);
          break;

        }
      }
    }
  }

  @Override
  public void setReserves(IMarketState state, ICart items) {
    // TODO Auto-generated method stub

  }

}
